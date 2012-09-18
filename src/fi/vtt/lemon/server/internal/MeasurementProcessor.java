package fi.vtt.lemon.server.internal;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static fi.vtt.lemon.RabbitConst.*;

/** @author Teemu Kanstren */
public class MeasurementProcessor implements Runnable {
  private final static Logger log = new Logger(MeasurementProcessor.class);
  private final JSONObject json;

  public MeasurementProcessor(JSONObject json) {
    this.json = json;
  }

  @Override
  public void run() {
    try {
      long time = json.getLong(PARAM_TIME);
      String measureURI = json.getString(PARAM_MEASURE_URI);
      int precision = json.getInt(PARAM_PRECISION);
      String value = json.getString(PARAM_VALUE);
    } catch (JSONException e) {
      log.error("Failed to parse measurement data from:"+json, e);
      return;
    }
  }
}
