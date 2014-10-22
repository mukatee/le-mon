package fi.vtt.lemon.probe.tasks;

import fi.vtt.lemon.probe.ProbeServer;
import fi.vtt.lemon.server.rest.RESTConst;
import fi.vtt.lemon.server.rest.RestClient;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static fi.vtt.lemon.MsgConst.*;

/**
 * @author Teemu Kanstren
 */
public class KeepAliveTask implements Runnable {
  private final static Logger log = new Logger(KeepAliveTask.class);
  private final String measureURI;

  public KeepAliveTask(String measureURI) {
    this.measureURI = measureURI;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    try {
      JSONObject json = new JSONObject();
      json.put(MSGTYPE, MSG_KEEP_ALIVE);
      json.put(PARAM_TIME, System.currentTimeMillis());
      json.put(PARAM_MEASURE_URI, measureURI);
      json.put(PARAM_PROBE_URL, ProbeServer.getProbeAddress());

      String server = ProbeServer.getServerAgentAddress();
      RestClient.sendPost(server + RESTConst.PATH_KEEP_ALIVE, json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
