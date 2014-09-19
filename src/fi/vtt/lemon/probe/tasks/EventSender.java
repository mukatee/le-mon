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
public class EventSender implements Runnable {
  private final static Logger log = new Logger(EventSender.class);
  private final String type;
  private final String source;
  private final String msg;

  public EventSender(String type, String source, String msg) {
    this.type = type;
    this.source = source;
    this.msg = msg;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    try {
      JSONObject json = new JSONObject();
      json.put(MSGTYPE, MSG_EVENT);
      json.put(PARAM_TIME, System.currentTimeMillis());
      json.put(PARAM_EVENT_TYPE, type);
      json.put(PARAM_EVENT_SOURCE, source);
      json.put(PARAM_EVENT_MSG, msg);
      String server = ProbeServer.getServerAgentAddress();
      RestClient2.sendPost(server+ RESTConst.PATH_EVENT, json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
