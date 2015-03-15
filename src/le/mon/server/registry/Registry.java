package le.mon.server.registry;

import le.mon.Config;
import le.mon.MsgConst;
import le.mon.server.data.ProbeDescription;
import le.mon.server.persistence.Persistence;
import le.mon.MsgConst;
import osmo.common.log.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Maintains the runtime state of the server. This includes list of available measures, client subscriptions, etc.
 *
 * @author Teemu Kanstren
 */
public class Registry {
  private final static Logger log = new Logger(Registry.class);
  /** List of available measurement types as identified by their MeasureURI. */
  private final Collection<String> availableBM = new HashSet<>();
  /** The list of measurements the client is subscribing to (id = MeasureURI). */
  private Collection<String> subscriptionRegistry = new HashSet<>();
  /** Maps a measureURI to a specific probe. */
  private Map<String, ProbeDescription> probeMap = new HashMap<>();
  /** Description for all probes registered. */
  private List<ProbeDescription> probes = new ArrayList<>();
  /** For database storage. */
  private final Persistence persistence;
  private final ProbeAgentWatchDog watchDog;

  public Registry(Persistence persistence) {
    int timeout = Config.getInt(MsgConst.PROBE_TIMEOUT, 5);
    this.watchDog = new ProbeAgentWatchDog(timeout, this);
    watchDog.start();
    this.persistence = persistence;
  }

  public List<ProbeDescription> getProbes() {
    return probes;
  }

  /**
   * Adds a measurement type as available.
   * //TODO: log error if registering with same URI several times, send error back to probe too
   */
  public void addProbe(ProbeDescription probe) {
    log.info("Adding probe:"+probe);
    String measureURI = probe.getMeasureURI();
    availableBM.add(measureURI);
    persistence.probeAdded(measureURI);
    probes.add(probe);
    probeMap.put(measureURI, probe);
    keepAlive(measureURI);
  }

  /**
   * Removes a measurement type as available.
   */
  public void removeProbe(ProbeDescription probe) {
    log.info("Removing probe:"+probe);
    availableBM.remove(probe.getMeasureURI());
    persistence.probeRemoved(probe.getMeasureURI());
    probes.remove(probe);
    probeMap.remove(probe.getMeasureURI());
  }

  //missing means we lost connection to a probe, it might still come back later
  public void missing(ProbeDescription probe) {
    log.info("Missing probe:" + probe);
    availableBM.remove(probe.getMeasureURI());
    persistence.probeMissing(probe.getMeasureURI());
  }

  //this means a probe was permanently lost and should be removed from all lists
  public void lost(ProbeDescription probe) {
    log.info("Lost probe:"+probe);
    persistence.probeLost(probe.getMeasureURI());
    probes.remove(probe);
    probeMap.remove(probe.getMeasureURI());
  }

  //this means we found the probe while it was missing but before it was completely lost
  public void found(ProbeDescription probe) {
    log.info("Probe found again:" + probe);
    availableBM.add(probe.getMeasureURI());
    persistence.probeFound(probe.getMeasureURI());
    probe.setAvailable(true);
  }

  public void keepAlive(String measureURI) {
    log.debug("Keep-Alive for:"+measureURI);
    ProbeDescription probe = probeMap.get(measureURI);
    if (probe == null) {
      log.warn("Received keep-alive for non-existing measure URI:"+measureURI);
      return;
    }
    probe.setLastHeard(System.currentTimeMillis());
    if (!probe.isAvailable()) found(probe);
  }

  /**
   * Get current list of available measurement types.
   *
   * @return The list of MeasureURI's...
   */
  public synchronized List<String> getAvailableBM() {
    List<String> result = new ArrayList<>();
    result.addAll(availableBM);
    return result;
  }

  /**
   * Parse target type from a measureURI. Once upon a time the measureURI was assumed to be formed as
   * MFW://target-type/target-name/bm-type/bm/name. This function would parse the target name from it.
   * Nowadays this is not used as the URI is freeform text due to no real advantage observed to parse it
   * in any specific way in the measurement infrastructure. The client can do what they wish with it instead.
   *
   * @param measureURI To parse.
   * @return The target type as parsed from the given data.
   */
  public String parseTargetType(String measureURI) {
    int[] bounds = getTargetTypeBounds(measureURI);
    return measureURI.substring(bounds[0], bounds[1]);
  }

  /**
   * Get the string bounds for the target type when parsing the target type from MeasureURI.
   *
   * @param measureURI To parse.
   * @return The start and end indices.
   */
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

  /**
   * Same as parseTargetType method but for target name.
   *
   * @param measureURI To parse.
   * @return The target name as parsed.
   */
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

  /**
   * Client has provided a new subscription.
   *
   * @param measureURI Data subscribed to.
   */
  public void addSubscription(String measureURI) {
    log.debug("New measurement subscription URI:" + measureURI);
    subscriptionRegistry.add(measureURI);
  }

  /**
   * Removes a subscription for the client.
   *
   * @param measureURI The measure to remove the subscription for.
   */
  public void removeSubscription(String measureURI) {
    log.debug("Removed measurement subscription id:" + measureURI);
    subscriptionRegistry.remove(measureURI);
  }

  /**
   * Checks if there is a subscription for the given measure.
   *
   * @param measureURI The measure ID.
   * @return True if there is.
   */
  public boolean isSubscribed(String measureURI) {
    return subscriptionRegistry.contains(measureURI);
  }

  /**
   * Should check the authentication scheme of the communications.
   * Currently not implemented.
   *
   * @param authHeader The token.
   * @return True if OK.
   */
  public boolean check(String authHeader) {
    return true;
  }

  public boolean isRegistered(String measureURI) {
    return availableBM.contains(measureURI);
  }

  public ProbeDescription probeFor(String measureURI) {
    return probeMap.get(measureURI);
  }
}
