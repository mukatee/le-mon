/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.lemon.server.rest.external;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fi.vtt.lemon.server.PersistencePlugin;

import fi.vtt.lemon.server.registry.RegistryPlugin;
import fi.vtt.lemon.server.rest.external.resources.Session;
import fi.vtt.lemon.server.shared.datamodel.BMDescription;
import fi.vtt.lemon.server.shared.datamodel.ProbeDescription;
import fi.vtt.lemon.server.shared.datamodel.TargetDescription;
import fi.vtt.lemon.server.shared.datamodel.Value;
import fi.vtt.lemon.server.shared.datamodel.Value.SortKey;
import osmo.common.log.Logger;

/** @author Teemu Kanstren */
public class RestPlugin {
  private final static Logger log = new Logger(RestPlugin.class);
  private static RestPlugin restPlugin = null;
  //for accessing runtime state
  private RegistryPlugin registry;
  //provides bundle to database
  private PersistencePlugin persistence;
  //(sac)Id for measurement subscriptions
  private long restId = -2;
  // registered clients
  private Map<String, Session> sessions;
  // client subsciptions
  private ClientSubscriptionRegistry subs;

  public static RestPlugin getInstance() {
    return restPlugin;
  }

  public RestPlugin() {
    restPlugin = this;

    sessions = new LinkedHashMap<>();
    subs = new ClientSubscriptionRegistry();
  }

  public void setRegistry(RegistryPlugin registry) {
    this.registry = registry;
  }

  //Gives a list of available BM.
  public List<BMDescription> getAvailableBMList() {
    return registry.getAvailableBM();
  }

  public List<ProbeDescription> getProbes() {
    return registry.getProbes();
  }

  public ProbeDescription getProbeForBM(long bmId) {
    return registry.getProbeForBM(bmId);
  }

  public Collection<TargetDescription> getTargets() {
    return registry.getTargets();
  }

  //Requests for a given measurement to be provided
  public boolean requestBM(long bmId) {
    log.debug("requestBM");
    ProbeDescription probe = registry.getProbeForBM(bmId);
    long subscriptionId = registry.addMeasurementRequest(restId, probe.getBm(), probe.getProbeId());
    return true;
  }

  public void requestBaseMeasure(String base64auth, long bmId) {
    Session session = sessions.get(extractAuthentication(base64auth));
    log.debug("Client (" + session.getName() + ") is requesting base measure, id=" + bmId);
    ProbeDescription probe = registry.getProbeForBM(bmId);
    long subscriptionId = registry.addMeasurementRequest(restId, probe.getBm(), probe.getProbeId());
  }

  //Requests for a given measurement to be provided
  public void subscribeToBM(long bmId, long interval) {
    log.debug("subscribeToBM");
    ProbeDescription probe = registry.getProbeForBM(bmId);
    long subscriptionId = registry.addSubscription(restId, probe.getBm(), interval, probe.getProbeId());
  }

  public void subscribeBaseMeasure(String authHeader, long id, long interval) {
    ProbeDescription probe = registry.getProbeForBM(id);

    long subscriptionId = registry.addSubscription(restId, probe.getBm(), interval, probe.getProbeId());

    Session client = sessions.get(extractAuthentication(authHeader));
    subs.add(subscriptionId, client);
  }

  //removes an existing subscription
  public void unSubscribeToBM(long bmId) {
    log.debug("unSubscribeToBM");
    long subscriptionId = registry.getIdForSubscription(restId, bmId);
    registry.removeSubscription(restId, subscriptionId);
  }

  public void unsubscribeBaseMeasure(String authHeader, long id) {
    long subscriptionId = registry.getIdForSubscription(restId, id);
    registry.removeSubscription(restId, subscriptionId);

    Session client = sessions.get(extractAuthentication(authHeader));
    subs.remove(subscriptionId, client);
  }

  public String getName() {
    return RestPlugin.class.getName();
  }

  public String registerClient(String base64auth, String name, String endpoint) {
    log.debug("Registering client: " + name + ", " + endpoint);
    String authentication = extractAuthentication(base64auth);

    // if client has already registered, all subscriptions should be released
    if (sessions.containsKey(authentication)) {
      sessions.remove(authentication);
    }

    // create new session for the client
    UUID id = UUID.randomUUID();
    Session session = new Session(id, name, endpoint);

    sessions.put(authentication, session);

    return session.getId().toString();
  }

  public boolean isAlive(String base64auth) {
    return sessions.containsKey(extractAuthentication(base64auth));
  }

  public boolean isAuthorized(String base64auth) {
    String authentication = extractAuthentication(base64auth);
    boolean authorized = false;

    if (authentication.length() > 0) {
      // now we are ready to check authentication string
      String decoded = "";
//      String decoded = Base64.base64Decode(authentication);
      log.debug("Decoded authentication string: " + decoded);
      String[] userpass = decoded.split(":");

      log.debug("User credentials: [" + userpass[0] + ":" + userpass[1] + "]");

      String pass = "password";
      String user = "username";

      if (user.equals(userpass[0]) && pass.equals(userpass[1])) {
        authorized = true;
      }
    }

    return authorized;
  }

  // helper method for splitting authorization header
  private String extractAuthentication(String auth) {
    String result = "";

    if (auth != null) {
      String[] items = auth.split(" ");

      if (items.length == 2 && "Basic".equals(items[0])) {
        result = items[1];
      }
    }

    return result;
  }

  public String getFrameworkInfo() {
    return getName();
  }

  public List<Value> getHistory(long start, long end, Collection<Long> bmIds) {
    Long[] bmids = new Long[bmIds.size()];
    bmIds.toArray(bmids);
    List<Value> data = persistence.getValues(start, end, bmids, SortKey.TIME, true);
    return data;
  }
}
