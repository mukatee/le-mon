package fi.vtt.lemon.server;

import fi.vtt.lemon.server.data.ProbeDescription;
import fi.vtt.lemon.server.persistence.Persistence;
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
  private Map<String, ProbeDescription> probeMap = new HashMap<>();
  private List<ProbeDescription> probes = new ArrayList<>();
  private final Persistence persistence;

  public Registry(Persistence persistence) {
    this.persistence = persistence;
  }

  /**
   * Adds a measurement type as available.
   */
  public void addProbe(ProbeDescription probe) {
    log.info("Adding probe:"+probe);
    //TODO: add some watchdog to drop available if nothing received in time interval, or do keep-alive messages
    availableBM.add(probe.getMeasureURI());
    persistence.bmAdded(probe.getMeasureURI());
    probes.add(probe);
    //TODO: precision should be checked before override
    probeMap.put(probe.getMeasureURI(), probe);
  }

  /**
   * Removes a measurement type as available.
   */
  public void removeProbe(ProbeDescription probe) {
    log.info("Removing probe:"+probe);
    //TODO: check if another probe still exist for the URI
    availableBM.remove(probe.getMeasureURI());
    //TODO: rename event to "probe removed" not "bm removed"
    persistence.bmRemoved(probe.getMeasureURI());
    probes.remove(probe);
    //TODO: check if another probe exists..
    probeMap.remove(probe.getMeasureURI());
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
   * @param measureURI
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
