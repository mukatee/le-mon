package fi.vtt.lemon.server.tasks;

import fi.vtt.lemon.server.LemonServer;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static fi.vtt.lemon.MsgConst.*;

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
      long time = json.getLong(PARAM_TIME);
      String measureURI = json.getString(PARAM_MEASURE_URI);
      int precision = json.getInt(PARAM_PRECISION);
      String value = json.getString(PARAM_VALUE);
      LemonServer.measurement(measureURI, time, precision, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
