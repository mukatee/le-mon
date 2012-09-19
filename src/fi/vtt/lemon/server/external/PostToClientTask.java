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

/** @author Teemu Kanstren */
public class PostToClientTask implements Runnable {
  private final static Logger log = new Logger(PostToClientTask.class);
  private final WebResource wr;
  private final Value value;

  public PostToClientTask(WebResource wr, Value value) {
    this.wr = wr;
    this.value = value;
  }

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
    } catch (JSONException e) {
      //TODO: self-retry might need to be considered in case of network failure etc..
      log.error("Failed to post value to client", e);
    }
  }
}
