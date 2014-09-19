/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probe.tasks;

import fi.vtt.lemon.probe.ProbeServer;
import fi.vtt.lemon.server.rest.RESTConst;
import fi.vtt.lemon.server.rest.RestClient2;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static fi.vtt.lemon.MsgConst.*;

/**
 * Defines a task for the internal server to process measurements received from the measurement infrastructure.
 * Processed by a worker thread pool in the server.
 * 
 * @author Teemu Kanstren 
 */
public class BMSender implements Runnable {
  private final static Logger log = new Logger(BMSender.class);
  private final String measureURI;
  private final int precision;
  private final String value;

  public BMSender(String measureURI, int precision, String value) {
    this.measureURI = measureURI;
    this.precision = precision;
    this.value = value;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    try {
      JSONObject json = new JSONObject();
      json.put(MSGTYPE, MSG_MEASUREMENT);
      json.put(PARAM_TIME, System.currentTimeMillis());
      json.put(PARAM_MEASURE_URI, measureURI);
      json.put(PARAM_PRECISION, precision);
      json.put(PARAM_VALUE, value);

      String server = ProbeServer.getServerAgentAddress();
      RestClient2.sendPost(server+ RESTConst.PATH_BM_RESULT, json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
