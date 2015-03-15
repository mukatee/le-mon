package le.mon.probe.tasks;

import le.mon.probe.ProbeServer;
import le.mon.server.rest.RESTConst;
import le.mon.server.rest.RestClient;
import le.mon.MsgConst;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static le.mon.MsgConst.*;

/**
 * Defines a task for the internal server to process measurements received from the measurement infrastructure.
 * Processed by a worker thread pool in the server.
 *
 * @author Teemu Kanstren
 */
public class RegistrationSender implements Runnable {
  private final static Logger log = new Logger(RegistrationSender.class);
  private final String measureURI;

  public RegistrationSender(String measureURI) {
    this.measureURI = measureURI;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    try {
      JSONObject json = new JSONObject();
      json.put(MsgConst.MSGTYPE, MsgConst.MSG_REGISTER);
      json.put(MsgConst.PARAM_TIME, System.currentTimeMillis());
      json.put(MsgConst.PARAM_MEASURE_URI, measureURI);
      json.put(MsgConst.PARAM_PROBE_URL, ProbeServer.getProbeAddress());

      String server = ProbeServer.getServerAgentAddress();
      RestClient.sendPost(server + RESTConst.PATH_REGISTER, json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
