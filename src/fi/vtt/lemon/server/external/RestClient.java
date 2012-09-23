/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import fi.vtt.lemon.Config;
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
    String url = Config.getString(RabbitConst.CONFIGURATION_FILENAME, "http://localhost:11112/client");
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

