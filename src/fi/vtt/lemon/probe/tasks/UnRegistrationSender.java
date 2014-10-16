package fi.vtt.lemon.probe.tasks;

import fi.vtt.lemon.probe.Probe;
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
public class UnRegistrationSender implements Runnable {
  private final static Logger log = new Logger(UnRegistrationSender.class);
  private final String measureURI;
  private final int precision;

  public UnRegistrationSender(Probe probe) {
    this.measureURI = probe.getMeasureURI();
    this.precision = probe.getPrecision();
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    try {
      JSONObject json = new JSONObject();
      json.put(MSGTYPE, MSG_UNREGISTER);
      json.put(PARAM_TIME, System.currentTimeMillis());
      json.put(PARAM_MEASURE_URI, measureURI);
      json.put(PARAM_PROBE_URL, ProbeServer.getProbeAddress());

      String server = ProbeServer.getServerAgentAddress();
      RestClient.sendPost(server + RESTConst.PATH_UNREGISTER, json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
