package le.mon.server.tasks;

import le.mon.MsgConst;
import le.mon.server.LemonServer;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

/**
 * Defines a task for the internal server to process measurements received from the measurement infrastructure.
 * Processed by a worker thread pool in the server.
 *
 * @author Teemu Kanstren
 */
public class MeasurementProcessor implements Runnable {
  private final static Logger log = new Logger(MeasurementProcessor.class);
  /** The message data received. */
  private final JSONObject json;

  public MeasurementProcessor(JSONObject json) {
    this.json = json;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    try {
      long time = json.getLong(MsgConst.PARAM_TIME);
      String measureURI = json.getString(MsgConst.PARAM_MEASURE_URI);
      String value = json.getString(MsgConst.PARAM_VALUE);
      LemonServer.measurement(measureURI, time, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
