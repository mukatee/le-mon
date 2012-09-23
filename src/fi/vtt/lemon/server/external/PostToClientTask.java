package fi.vtt.lemon.server.external;

import com.sun.jersey.api.client.WebResource;
import fi.vtt.lemon.probe.measurement.MeasurementThreadFactory;
import fi.vtt.lemon.server.Value;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.core.MediaType;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static fi.vtt.lemon.server.external.RESTConst.*;

/** 
 * Defines a task for sending a measurement result to the client.
 * Executed by the thread pool executor.
 * 
 * @author Teemu Kanstren 
 */
public class PostToClientTask implements Runnable {
  private final static Logger log = new Logger(PostToClientTask.class);
  /** Access to post data over REST+JSON to the client. */
  private final WebResource wr;
  /** The measurement result to post. */
  private final Value value;

  public PostToClientTask(WebResource wr, Value value) {
    this.wr = wr;
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
      json.put(PRECISION, value.getPrecision());
      json.put(VALUE, value.valueString());
      wr.path(PATH_BM_RESULT).type(MediaType.APPLICATION_JSON).post(json);
    } catch (Exception e) {
      //TODO: self-retry might need to be considered in case of network failure etc..
      log.error("Failed to post value to client", e);
    }
  }
}
