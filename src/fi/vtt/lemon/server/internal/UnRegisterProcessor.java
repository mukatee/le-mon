package fi.vtt.lemon.server.internal;

import fi.vtt.lemon.server.LemonServer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static fi.vtt.lemon.RabbitConst.*;

/**
 * @author Teemu Kanstren
 */
public class UnRegisterProcessor implements Runnable {
  private final static Logger log = new Logger(MeasurementProcessor.class);
  /** The event data as received from the measurement infrastructure. */
  private final JSONObject json;

  /**
   * Constructor.
   *
   * @param json What was received from the measurement infrastructure.
   */
  public UnRegisterProcessor(JSONObject json) {
    this.json = json;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    try {
      String measureURI = json.getString(PARAM_MEASURE_URI);
      LemonServer.unregister(measureURI);
    } catch (JSONException e) {
      log.error("Failed to parse event data from JSON", e);
    }
  }
}
