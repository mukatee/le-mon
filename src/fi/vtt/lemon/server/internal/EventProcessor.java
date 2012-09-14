package fi.vtt.lemon.server.internal;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static fi.vtt.lemon.RabbitConst.*;

/** @author Teemu Kanstren */
public class EventProcessor implements Runnable {
  private final static Logger log = new Logger(MeasurementProcessor.class);
  private final JSONObject json;

  public EventProcessor(JSONObject json) {
    this.json = json;
  }

  @Override
  public void run() {
    try {
      long time = json.getLong(PARAM_TIME);
      String type = json.getString(PARAM_EVENT_TYPE);
      String source = json.getString(PARAM_EVENT_SOURCE);
      String msg = json.getString(PARAM_EVENT_MSG);
    } catch (JSONException e) {
      log.error("Failed to parse event data from:"+json, e);
      return;
    }
  }
}
