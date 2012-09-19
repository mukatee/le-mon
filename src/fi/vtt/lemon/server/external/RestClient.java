/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probe.measurement.MeasurementThreadFactory;
import fi.vtt.lemon.server.Value;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.core.MediaType;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static fi.vtt.lemon.server.external.RESTConst.*;

/**
 * @author Teemu Kanstren
 */
public class RestClient {
  private final static Logger log = new Logger(RestClient.class);
  private int threadPoolSize = 5;
  private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadPoolSize, new MeasurementThreadFactory());
  private WebResource wr;

  public RestClient() {
    String url = null;
    try {
      InputStream in = new FileInputStream(RabbitConst.CONFIGURATION_FILENAME);
      Properties props = new Properties();
      props.load(in);
      url = props.getProperty(RabbitConst.REST_CLIENT_ENDPOINT_URL);
      if (url == null) {
        throw new IllegalArgumentException("No URL defined for REST client endpoint. Unable to start REST plugin.");
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to read configuration file '" + RabbitConst.CONFIGURATION_FILENAME + "'", e);
    }
    Client client = Client.create();
    wr = client.resource(url);
    log.debug("Initializing REST plugin with endpoint "+url);
    log.debug("REST plugin initialized");
  }

  public void measurement(Value value) {
    log.debug("Scheduling measurement post task..");
    PostToClientTask task = new PostToClientTask(wr, value);
    executor.execute(task);
  }
}

