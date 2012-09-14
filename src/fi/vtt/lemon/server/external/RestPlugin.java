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

package fi.vtt.lemon.server.external;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fi.vtt.lemon.server.PersistencePlugin;

import fi.vtt.lemon.server.registry.RegistryPlugin;
import fi.vtt.lemon.server.external.resources.Session;
import fi.vtt.lemon.server.Value;
import fi.vtt.lemon.server.Value.SortKey;
import osmo.common.log.Logger;

/** @author Teemu Kanstren */
public class RestPlugin {
  private final static Logger log = new Logger(RestPlugin.class);
  private static RestPlugin restPlugin = null;
  //for accessing runtime state
  private RegistryPlugin registry;
  //provides bundle to database
  private PersistencePlugin persistence;
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
  public List<String> getAvailableBMList() {
    return registry.getAvailableBM();
  }

  public void subscribeBaseMeasure(String measureURI) {
    registry.addSubscription(measureURI);
  }

  //removes an existing subscription
  public void unSubscribeToBM(String measureURI) {
    log.debug("unSubscribeToBM:"+measureURI);
    registry.removeSubscription(measureURI);
  }

  public List<Value> getHistory(long start, long end, Collection<Long> bmIds) {
    Long[] bmids = new Long[bmIds.size()];
    bmIds.toArray(bmids);
    List<Value> data = persistence.getValues(start, end, bmids, SortKey.TIME, true);
    return data;
  }

  public boolean check(String authHeader) {
    return true;
  }
}
