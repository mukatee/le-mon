package le.mon.probe.tasks;

import le.mon.probe.ProbeServer;
import le.mon.server.rest.RESTConst;
import le.mon.server.rest.RestClient;
import le.mon.MsgConst;
import le.mon.probe.ProbeServer;
import le.mon.server.rest.RestClient;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static le.mon.MsgConst.*;

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
      json.put(MsgConst.MSGTYPE, MsgConst.MSG_EVENT);
      json.put(MsgConst.PARAM_TIME, System.currentTimeMillis());
      json.put(MsgConst.PARAM_EVENT_TYPE, type);
      json.put(MsgConst.PARAM_EVENT_SOURCE, source);
      json.put(MsgConst.PARAM_EVENT_MSG, msg);
      String server = ProbeServer.getServerAgentAddress();
      RestClient.sendPost(server + RESTConst.PATH_EVENT, json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
