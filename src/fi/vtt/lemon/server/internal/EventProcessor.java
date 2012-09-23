/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.internal;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static fi.vtt.lemon.RabbitConst.*;

/** 
 * Defines a task for the internal server to process events received from the measurement infrastructure.
 * Processed by a worker thread pool in the server.
 * Currently does nothing really, but should implement a callback to the client to notify them or to invoke any
 * associated functionality in the server as required (e.g., to remove available measurements).
 * 
 * @author Teemu Kanstren 
 */
public class EventProcessor implements Runnable {
  private final static Logger log = new Logger(MeasurementProcessor.class);
  /** The event data as received from the measurement infrastructure. */
  private final JSONObject json;

  /**
   * Constructor.
   * 
   * @param json What was received from the measurement infrastructure.
   */
  public EventProcessor(JSONObject json) {
    this.json = json;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    try {
      long time = json.getLong(PARAM_TIME);
      String type = json.getString(PARAM_EVENT_TYPE);
      String source = json.getString(PARAM_EVENT_SOURCE);
      String msg = json.getString(PARAM_EVENT_MSG);
    } catch (JSONException e) {
      log.error("Failed to parse event data from JSON", e);
    }
  }
}
