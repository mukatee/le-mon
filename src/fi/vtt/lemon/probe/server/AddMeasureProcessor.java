/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probe.server;

import fi.vtt.lemon.probe.Probe;
import fi.vtt.lemon.server.LemonServer;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static fi.vtt.lemon.RabbitConst.*;

/**
 * Defines a task for the adding a new measurement to the probe.
 * 
 * @author Teemu Kanstren 
 */
public class AddMeasureProcessor implements Runnable {
  private final static Logger log = new Logger(AddMeasureProcessor.class);
  /** The message data received. */
  private final JSONObject json;
  private final Probe probe;

  public AddMeasureProcessor(Probe probe, JSONObject json) {
    this.probe = probe;
    this.json = json;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    String config = null;
    try {
      long time = json.getLong(PARAM_TIME);
      config = json.getString(PARAM_CONFIG);
      probe.addMeasure(config);
      log.debug("Measurent removed from probe:"+config);
    } catch (Exception e) {
      log.error("Failed to add measure:"+config, e);
    }
  }
}
