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

package fi.vtt.lemon.probe.plugins.xmlrpc;

import fi.vtt.lemon.common.Const;
import fi.vtt.lemon.common.DataObject;
import fi.vtt.lemon.common.ProbeConfiguration;
import fi.vtt.lemon.probe.plugins.measurement.MeasurementProvider;
import fi.vtt.lemon.probe.shared.MeasurementRequest;
import fi.vtt.lemon.probe.shared.Probe;
import fi.vtt.lemon.probe.shared.ProbeAgent;
import fi.vtt.lemon.probe.shared.UnsubscriptionRequest;
import fi.vtt.lemon.server.shared.ServerAgent;
import osmo.common.log.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implements the basic functionality of a ProbeAgent.
 *
 * @author Teemu Kanstren
 */
public class ProbeAgentImpl implements ProbeAgent {
  private final static Logger log = new Logger(ProbeAgentImpl.class);
  //is this bundle started?
  private boolean started = false;
  //configuration for this probe-agent
  private final ProbeAgentConfig config;
  //the server-agent that we are communicating with
  private ServerAgent destination;
  //key=probeId, value=probe implementation
  private Map<Long, Probe> probes = new HashMap<Long, Probe>();
  //key=measureURI, value=probe implementation
  private Map<String, Probe> measureURItoProbeMap = new HashMap<String, Probe>();
  //this is a separate thread to register probes in order not to block the OSGI container providing us with events
  private RegistrationThread registrationThread =null;
  private MeasurementProvider measurementProvider;
  private SubscriptionChecker subscriptionChecker;

  public ProbeAgentImpl(ProbeAgentConfig config) throws Exception {
    if (config == null) {
      InputStream configStream = new FileInputStream(Const.CONFIGURATION_FILENAME);
      config = new ProbeAgentConfig(configStream);
    }
    this.config = config;
    subscriptionChecker = new SubscriptionChecker(this, config.getSubscriptionCheckInterval());        
  }

  public void setServerAgent(ServerAgent destination) {
    log.debug("Setting server agent (destination):"+destination);
    this.destination = destination;
    this.config.setDestination(destination);
    if (subscriptionChecker != null) {
      this.subscriptionChecker.setServerAgent(destination);
    } else {
      log.debug("SubscriptionChecker is null, could not set server agent");
    }
  }
  
  public void setMeasurementProvider(MeasurementProvider measurementProvider) {
    this.measurementProvider = measurementProvider;    
    if (subscriptionChecker != null) {
      this.subscriptionChecker.setMeasurementProvider(measurementProvider);
    } else {
      log.debug("SubscriptionChecker is null, could not set measurement provider");
    }
  }

  //start a given probe
  public void startProbe(long probeId) {
    Probe probe = probes.get(probeId);
    probe.startProbe();
  }

  //stop a given probe
  public void stopProbe(long probeId) {
    Probe probe = probes.get(probeId);
    probe.stopProbe();
  }

  public ProbeAgentConfig getConfig() {
    return config;
  }

  //start this probe-agent (from OSGI bundlecontext.start())
  public synchronized void startAgent() {
    if (started) {
      throw new IllegalStateException("Trying to start a ProbeAdapter that is already started - cannot be started more than once.");
    }
    started = true;
    log.debug("Started probe XMLRPC server");
    destination = config.getDestination();
    registrationThread = new RegistrationThread(config, probes, measureURItoProbeMap);
    registrationThread.start();
    
    if (destination != null) {
      this.subscriptionChecker.setServerAgent(destination);
    }
    if (measurementProvider != null) {
      this.subscriptionChecker.setMeasurementProvider(measurementProvider);
    }
  }

  private void registerProbeInformation(Probe probe) {
    for (Probe p : probes.values()) {
      if (p.equals(probe)) {
        //in this case it was already registered and the OSGI container is just spamming us with events..
        return;
      }
    }
    registrationThread.addToBeRegistered(probe);
  }

  //called from bundlecontext.stop()
  public void stopAgent() {
    if (!started) {
      log.debug("Trying to stop a ProbeAdapter that is not started.");
      return;
    }
    registrationThread.stop();
    subscriptionChecker.stop();
  }

  /**
   * Request a onetime measurement.
   */
  public void requestMeasure(String measureURI, long subscriptionId) {
    subscribe(measureURI, -1, subscriptionId);
  }

  /**
   * Request a continuous sampling of a measurement.
   *
   * @param interval  The sampling frequency in milliseconds.
   */
  public void subscribe(String measureURI, long interval, long subscriptionId) {
    //todo: if there are several probes with same measureURI, this must pick the best one to provide the measure
    log.debug("Adding a measurement request with interval " + interval);
    //note to self: auto-unboxing of null values gives strange nullpointers (for primitive types)
    Probe probe = measureURItoProbeMap.get(measureURI);
    MeasurementRequest request = new MeasurementRequest(destination, measureURI, probe, interval, subscriptionId);
//    bb.process(request);
    log.debug("Added a measurement request:" + measureURI + "->" + request);
  }
  
  public void unSubscribe(String measureURI, long subscriptionId) {
//    log.debug("unsubcribed " + measureURI);
    UnsubscriptionRequest ur = new UnsubscriptionRequest(destination, measureURI, subscriptionId);
//    bb.process(ur);
    log.debug("Added unsubscription request. MeasureURI: " +measureURI+ ", subscriptionId: " +subscriptionId);
  }

  /**
   * Set the configuration for the given probe.
   *
   * @param probeId Identifies the probe for which the configuration is to be set.
   * @param configuration The parameter values to be set. How the probe handles them is up to the probe implementation.
   */
  public void setConfiguration(long probeId, Map<String, String> configuration) {
    log.debug("setting configuration for probe: " + probeId);
    Probe probe = probes.get(probeId);
    probe.setConfiguration(configuration);
    if (measurementProvider != null) {
      measurementProvider.setConfiguration(probe, configuration);
    }
  }

  /**
   * A server-agent is requesting the configuration for a specific probe.
   *
   * @param probeId Identifies the probe for which the configuration is requested.
   * @return The configuration of the requested probe.
   */
  public Collection<ProbeConfiguration> getConfigurationParameters(long probeId) {
    log.debug("Getting configuration for probeId: "+probeId);
    log.debug("probes:"+probes);
    Probe probe = probes.get(probeId);
    log.debug("probe:"+probe);
    return probe.getConfigurationParameters();
  }

  //currently we do not use blackboard but just use the OSGI services. we still use the plugin architecture to allow this to change.
  public Set getCommands() {
    return null;
  }

  public void process(DataObject data) {

  }

  /**
   * Gives all the registered probes.
   *
   * @return All the registered probes. key=probeId, value=probe implementation reference
   */
  public Map<Long, Probe> getProbes() {
    log.debug("getProbes() providing list of probes");
    //we make a copy so it wont be messed up on two-sided updates
    Map<Long, Probe> result = new HashMap<Long, Probe>();
    result.putAll(probes);
    return result;
  }
  
  public void setReference(long subscriptionId, String reference) {
    log.debug("setReference");
    measurementProvider.setReference(subscriptionId, reference);
  }

}
