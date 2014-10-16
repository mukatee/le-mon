package fi.vtt.lemon.server.rest;


import fi.vtt.lemon.server.data.Value;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import static fi.vtt.lemon.server.rest.RESTConst.*;

/** 
 * Defines a task for sending a measurement result to the client.
 * Executed by the thread pool executor.
 * 
 * @author Teemu Kanstren 
 */
public class PostToClientTask implements Runnable {
  private final static Logger log = new Logger(PostToClientTask.class);
  /** Client inet address. */
  private final String url;
  /** The measurement result to post. */
  private final Value value;

  public PostToClientTask(String url, Value value) {
    this.url = url;
    this.value = value;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    log.debug("Sending BM results via REST interface");
    JSONObject json = new JSONObject();
    try {
      json.put(MEASURE_URI, value.getMeasureURI());
      json.put(TIME, value.getTime().getTime());
      json.put(VALUE, value.valueString());
      RestClient.sendAppJSON(url + PATH_BM_RESULT, json);
    } catch (Exception e) {
      //TODO: self-retry might need to be considered in case of network failure etc..
      log.error("Failed to post value to client", e);
    }
  }
}
