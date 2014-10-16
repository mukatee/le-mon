package fi.vtt.lemon.probe.tasks;

import fi.vtt.lemon.probe.ProbeServer;
import fi.vtt.lemon.server.rest.RESTConst;
import fi.vtt.lemon.server.rest.RestClient;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static fi.vtt.lemon.MsgConst.*;

/**
 * Defines a task for the internal server to process measurements received from the measurement infrastructure.
 * Processed by a worker thread pool in the server.
 * 
 * @author Teemu Kanstren 
 */
public class RegistrationSender implements Runnable {
  private final static Logger log = new Logger(RegistrationSender.class);
  private final String measureURI;
  private final int precision;

  public RegistrationSender(String measureURI, int precision) {
    this.measureURI = measureURI;
    this.precision = precision;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    try {
      JSONObject json = new JSONObject();
      json.put(MSGTYPE, MSG_REGISTER);
      json.put(PARAM_TIME, System.currentTimeMillis());
      json.put(PARAM_MEASURE_URI, measureURI);
      json.put(PARAM_PROBE_URL, ProbeServer.getProbeAddress());

      String server = ProbeServer.getServerAgentAddress();
      RestClient.sendPost(server + RESTConst.PATH_REGISTER, json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
