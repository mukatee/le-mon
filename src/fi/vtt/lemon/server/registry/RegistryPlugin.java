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

package fi.vtt.lemon.server.registry;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.server.PersistencePlugin;
import fi.vtt.lemon.server.shared.datamodel.BMDescription;
import fi.vtt.lemon.server.shared.datamodel.MeasurementSubscription;
import fi.vtt.lemon.server.shared.datamodel.ProbeDescription;
import fi.vtt.lemon.server.shared.datamodel.TargetDescription;
import osmo.common.log.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Teemu Kanstren
 */
public class RegistryPlugin implements Runnable {
  private final static Logger log = new Logger(RegistryPlugin.class);
  //key = measureURI
  private final Map<String, BMDescription> availableBM = new HashMap<String, BMDescription>();
  //key = long bmid provided by sac
  private final Map<Long, BMDescription> bmIds = new HashMap<Long, BMDescription>();
  //key = targetType+targetName
  private final Map<String, TargetDescription> targets = new HashMap<String, TargetDescription>();
  //key = probeId, value = ProbeDescription
  private final Map<Long, ProbeDescription> probes = new HashMap<Long, ProbeDescription>();
  private int nextDmId = 1;
  //maximum delay for receiving keep-alive messages before a probe-agent is reported as lost
  private int maxDelay = DEFAULT_DELAY;
  //how much time to wait between checking the keep-alive status of probe-agents
  private int delayIncrement = DEFAULT_INCREMENT;
  //default keep-alive threshold if nothing is configured
  private static final int DEFAULT_DELAY = 10 * 1000; //10 seconds
  //default keep-alive wait time if nothing is configured
  private static final int DEFAULT_INCREMENT = 1000; //1 second
  //should the registry keep running its threads or shut down?
  private boolean shouldRun = true;
  //access to the persistent state
  private PersistencePlugin persistence = null;
  private SubscriptionRegistry subscriptionRegistry;

  public RegistryPlugin(int maxDelay, int delayIncrement) {
    if (maxDelay > 0) {
      //assume that if one is set, both are set
      this.maxDelay = maxDelay;
      this.delayIncrement = delayIncrement;
    } else {
        Properties props = Config.get();
        this.maxDelay = Integer.parseInt(props.getProperty(RabbitConst.MAX_KEEPALIVE_DELAY));
    }
    //start a thread to monitor keep-alive messages
    Thread t = new Thread(this);
    t.setDaemon(true);
    t.start();

    subscriptionRegistry = new SubscriptionRegistry();
  }

  public void stop() {
    shouldRun = false;
  }

  public void setPersistence(PersistencePlugin persistence) {
    this.persistence = persistence;
  }

  //get the BMDescription for the given measureURI (measure identifier)
  public BMDescription descriptionFor(String measureURI) {
    return availableBM.get(measureURI);
  }

  //provides a list of all registered probes
  public List<ProbeDescription> getProbes() {
    //create copy of current state to avoid breaking on concurrent access etc.
    List<ProbeDescription> result = new ArrayList<ProbeDescription>(probes.size());
    result.addAll(probes.values());
    return result;
  }

  //get current list of registered probes
  public synchronized List<BMDescription> getAvailableBM() {
    Collection<BMDescription> values = availableBM.values();
    //create copy of current state to avoid breaking on concurrent access etc.
    List<BMDescription> result = new ArrayList<BMDescription>(values.size());
    result.addAll(values);
    return result;
  }

  //list of all active targets (with probes registered for them)
  public Collection<TargetDescription> getTargets() {
    //create copy of current state to avoid breaking on concurrent access etc.
    List<TargetDescription> targets = new ArrayList<TargetDescription>();
    targets.addAll(this.targets.values());
    return targets;
  }

  //gives probe description for given probeid
  public ProbeDescription getProbeFor(long probeId) {
    return probes.get(probeId);
  }

  //give the best available probe for given BM (with highest precision)
  public ProbeDescription getProbeForBM(long bmId) {
    BMDescription bm = bmIds.get(bmId);
    String measureURI = bm.getMeasureURI();
    ProbeDescription result = null;
    for (ProbeDescription probe : probes.values()) {
      if (!probe.getMeasureURI().equals(measureURI)) {
        continue;
      }
      if (result != null) {
        if (result.getPrecision() < probe.getPrecision()) {
          result = probe;
        }
      } else {
        result = probe;
      }
    }
    return result;
  }

  //parse target type from a measureURI
  public String parseTargetType(String measureURI) {
    int[] bounds = getTargetTypeBounds(measureURI);
    return measureURI.substring(bounds[0], bounds[1]);
  }

  //get start and end index of target type inside given measureURI
  private int[] getTargetTypeBounds(String measureURI) {
    int si = measureURI.indexOf("//");
    if (si <= 0) {
      throw new IllegalArgumentException("Invalid measure URI:" + measureURI);
    }
    si += 2;
    int ei = measureURI.indexOf("/", si);
    if (si <= 0) {
      throw new IllegalArgumentException("Invalid measure URI:" + measureURI);
    }
    return new int[]{si, ei};
  }

  //parse target name from given measureURI
  public String parseTargetName(String measureURI) {
    int[] bounds = getTargetTypeBounds(measureURI);
    //end of type + "/"
    int si = bounds[1] + 1;
    int ei = measureURI.indexOf("/", si);
    if (si <= 0) {
      throw new IllegalArgumentException("Invalid measure URI:" + measureURI);
    }
    return measureURI.substring(si, ei);
  }

  //when a probe sends a keep-alive message, this is invoked
  public boolean processKeepAlive(long probeId) {
    ProbeDescription probe = probes.get(probeId);
    if (probe != null) {
      probe.resetDelay();
      return true;
    }
    return false;
  }

  /**
   * Called when a registration is received from a probe-agent.
   *
   * @param properties A set of key-value pairs describing the probe. For key and their description see.. what? :)
   * @return The probe identifier for the registered probe.
   */
  public synchronized long registerProbe(Map<String, String> properties) {
    log.debug("registering probe");
    BMDescription bm = null;
    boolean newTarget = false;
    boolean newBM = false;
    try {
      log.debug("creating target description");
      TargetDescription target = persistence.createTargetDescription(properties);
      if (!targets.containsValue(target)) {
        newTarget = true;
      }
      targets.put(target.getTargetType() + target.getTargetName(), target);
      log.debug("creating bm description");
      bm = persistence.createBMDescription(properties);
    } catch (Exception e) {
      log.error("Failed to create target/bm for probe description.", e);
      return RabbitConst.ERROR_CODE_ILLEGAL_ARGUMENTS_FOR_PROBE;
    }
    if (!bmIds.containsKey(bm.getBmId())) {
      newBM = true;
    }
    availableBM.put(bm.getMeasureURI(), bm);
    bmIds.put(bm.getBmId(), bm);

    //we use the database to manage the probe information
    log.debug("creating probe description");
    ProbeDescription desc = persistence.createProbeDescription(properties);
    //we keep a mapping from id to desc to ease access in other functions
    probes.put(desc.getProbeId(), desc);

    return desc.getProbeId();
  }

  //this is the thread that runs checking when a keep-alive message is received and if the delay for a probe has
  //surpassed the given threshold. if one gets lots, the set of available BM is updated accordingly.
  public void run() {
    while (shouldRun) {
      try {
        Thread.sleep(delayIncrement);
      } catch (InterruptedException e) {
        log.error("sleep interrupted", e);
      }
      synchronized (this) {
        for (Iterator<Map.Entry<Long, ProbeDescription>> i = probes.entrySet().iterator(); i.hasNext(); ) {
          Map.Entry<Long, ProbeDescription> entry = i.next();
          ProbeDescription probe = entry.getValue();
          probe.increaseDelay(delayIncrement);
//          log.debug("Increasing delay for "+probe+" by "+delayIncrement);
          if (probe.getDelay() >= maxDelay) {
            //TODO: also check all BM if we need to remove one, etc.
            log.debug("Lease terminated for:" + probe);
            i.remove();

            //check if there are any BM for the target (device)            
            boolean found = false;
            boolean targetDisabled = false;
            TargetDescription target = probe.getBm().getTarget();
            for (Map.Entry<Long, BMDescription> bmEntry : bmIds.entrySet()) {
              TargetDescription targetDesc = bmEntry.getValue().getTarget();
              if (target == targetDesc) {
                found = true;
                break;
              }
            }
            if (!found) {
              //there are no BM for the target so it can be removed
              log.debug("removing Target:" + target);
              String key = target.getTargetType() + target.getTargetName();
              targets.remove(key);
              targetDisabled = true;
            }

            // here can be several probes for the same BM so we can only remove the BM if no other probe is present
            //note that above we removed this probe from the list of probes so it is no longer found by getForBM()
            boolean bmDisabled = false;
            if (getProbeForBM(probe.getBm().getBmId()) == null) {
              String measureURI = probe.getMeasureURI();
              BMDescription bmDesc = availableBM.remove(measureURI);
              log.debug("removind BM:" + bmDesc);
              bmIds.remove(bmDesc.getBmId());
              bmDisabled = true;
            }
          }
        }
      }
    }
  }

  public void setProbeDisabled(ProbeDescription probe) {
    log.debug("Disabling Probe:" + probe);
    probes.remove(probe.getProbeId());

    //check if there are any BM for the target (device)            
    boolean found = false;
    boolean targetDisabled = false;
    TargetDescription target = probe.getBm().getTarget();
    for (Map.Entry<Long, BMDescription> bmEntry : bmIds.entrySet()) {
      TargetDescription targetDesc = bmEntry.getValue().getTarget();
      if (target == targetDesc) {
        found = true;
        break;
      }
    }
    if (!found) {
      //there are no BM for the target so it can be removed
      log.debug("removing Target:" + target);
      String key = target.getTargetType() + target.getTargetName();
      targets.remove(key);
      targetDisabled = true;
    }

    // here can be several probes for the same BM so we can only remove the BM if no other probe is present
    //note that above we removed this probe from the list of probes so it is no longer found by getForBM()
    boolean bmDisabled = false;
    if (getProbeForBM(probe.getBm().getBmId()) == null) {
      String measureURI = probe.getMeasureURI();
      BMDescription bmDesc = availableBM.remove(measureURI);
      log.debug("removind BM:" + bmDesc);
      bmIds.remove(bmDesc.getBmId());
      bmDisabled = true;
    }
  }

  public ProbeDescription getProbeForSubscription(long subscriptionId) {
    try {
      long probeId = subscriptionRegistry.getSubscription(subscriptionId).getProbeId();
      return getProbeFor(probeId);
    } catch (Exception e) {
      log.error("Failed to get probe for subscription", e);
      return null;
    }
  }

  public long addSubscription(long sacId, BMDescription bm, long frequency, long probeId) {
    String msg = "New measurement subscription URI:" + bm.getMeasureURI() + " F:" + frequency + " PID:" + probeId;
    //TODO: Nämä uusix
//    bb.process(new ServerEvent(System.currentTimeMillis(), EventType.NEW_SUBSCRIPTION, "SAC " + sacId, msg));
    return subscriptionRegistry.addSubscription(sacId, bm, frequency, probeId);
  }

  public long getSacIdForSubscription(long subscriptionId) {
    return subscriptionRegistry.getSacIdForSubscriptionId(subscriptionId);
  }

  public long getFrequencyForSubscription(long subscriptionId) {
    return subscriptionRegistry.getFrequencyForSubscriptionId(subscriptionId);
  }

  public void removeSubscription(long sacId, long subscriptionId) {
    String msg = "Removed measurement subscription id:" + subscriptionId;
//    bb.process(new ServerEvent(System.currentTimeMillis(), EventType.DELETE_SUBSCRIPTION, "SAC " + sacId, msg));
    subscriptionRegistry.removeSubscription(subscriptionId);
  }

  public long getIdForSubscription(long sacId, long bmId) {
    return subscriptionRegistry.getIdForSubscription(sacId, bmId);
  }

  public long addMeasurementRequest(long sacId, BMDescription bm, long probeId) {
    String msg = "New measurement request URI:" + bm.getMeasureURI() + " PID:" + probeId;
//    bb.process(new ServerEvent(System.currentTimeMillis(), EventType.NEW_MEASUREMENT_REQUEST, "SAC " + sacId, msg));
    return subscriptionRegistry.addSubscription(sacId, bm, 0, probeId);
  }
}
