package le.mon.server.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Teemu Kanstren
 */
public class Event {
  private final String message;
  private final String sourceURI;
  private final Date time;
  private final int type;

  private static final Map<Integer, String> types = new HashMap<>();
  public static final int ET_PROBE_ADD = 1;
  public static final int ET_PROBE_REMOVE = 2;
  public static final int ET_PROBE_LOST = 3;
  public static final int ET_PROBE_FOUND = 4;
  public static final int ET_PROBE_MISSING = 5;

  static {
    types.put(ET_PROBE_ADD, "Add Probe");
    types.put(ET_PROBE_REMOVE, "Remove Probe");
    types.put(ET_PROBE_LOST, "Lost Probe");
    types.put(ET_PROBE_FOUND, "Found Probe");
  }

  public Event(String message, String sourceURI, Date time, int type) {
    this.message = message;
    this.sourceURI = sourceURI;
    this.time = time;
    this.type = type;
  }

  public String getMessage() {
    return message;
  }

  public String getSourceURI() {
    return sourceURI;
  }

  public Date getTime() {
    return time;
  }

  public int getType() {
    return type;
  }
}
