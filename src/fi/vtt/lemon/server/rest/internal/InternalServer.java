package fi.vtt.lemon.server.rest.internal;

import java.util.List;
import java.util.Map;

/**
 * Acts as a server for probe-agents to communicate with.
 *
 * @author Teemu Kanstren
 */
public class InternalServer {

  public boolean measurement(long time, String measureURI, int precision, String value, long subscriptionId) {
    return false;
  }

  public void event(long time, String type, String source, String message, long subscriptionId) {
  }

  public long register(Map<String, String> properties) {
    return 0;
  }

  public boolean keepAlive(long probeId) {
    return false;
  }

  public void unregister(long probeId) {
  }

  public void checkSubscriptions(long probeId, List<Long> subscriptionIds) {
  }

  public boolean BMReport(long time, String measureURI, String value, long subscriptionId, boolean matchReference, String reference) {
    return false;
  }
}
