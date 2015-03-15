package le.mon.probe.tasks;

import le.mon.probe.ProbeServer;
import le.mon.server.rest.RESTConst;
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
public class BMSender implements Runnable {
  private final static Logger log = new Logger(BMSender.class);
  private final String measureURI;
  private final String value;


  public BMSender(String measureURI, String value) {
    this.measureURI = measureURI;
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
      json.put(PARAM_VALUE, value);

      String server = ProbeServer.getServerAgentAddress();
      ProbeServer.registerIfNeeded(measureURI);
      RestClient.sendPost(server + RESTConst.PATH_BM_RESULT, json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
