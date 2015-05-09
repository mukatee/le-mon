package le.mon.server.tasks;

import le.mon.MsgConst;
import le.mon.server.LemonServer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

/**
 * @author Teemu Kanstren
 */
public class KeepAliveProcessor implements Runnable {
  private final static Logger log = new Logger(KeepAliveProcessor.class);
  /** The event data as received from the measurement infrastructure. */
  private final JSONObject json;

  /**
   * Constructor.
   *
   * @param json What was received from the measurement infrastructure.
   */
  public KeepAliveProcessor(JSONObject json) {
    this.json = json;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    try {
      String measureURI = json.getString(MsgConst.PARAM_MEASURE_URI);
      LemonServer.keepAlive(measureURI);
    } catch (JSONException e) {
      log.error("Failed to parse event data from JSON", e);
    }
  }
}
