/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external;


import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probe.measurement.MeasurementThreadFactory;
import fi.vtt.lemon.server.Value;
import osmo.common.log.Logger;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Provides an interface to post data to the client.
 * 
 * @author Teemu Kanstren
 */
public class RestClient {
  private final static Logger log = new Logger(RestClient.class);
  private final ScheduledThreadPoolExecutor executor;
  private final String url;

  public RestClient() {
    int threadPoolSize = Config.getInt(RabbitConst.THREAD_POOL_SIZE);
    executor = new ScheduledThreadPoolExecutor(threadPoolSize, new MeasurementThreadFactory());
    url = Config.getString(RESTConst.CLIENT_URL, "http://localhost:11112/client");
    log.debug("Initializing REST plugin with endpoint "+url);
    log.debug("REST plugin initialized");
  }

  /**
   * Creates a task to send measurement data to the client.
   * Creates a task object and adds that to the task pool for the thread pool executor to execute.
   * 
   * @param value The measurement value to provide.
   */
  public void measurement(Value value) {
    log.debug("Scheduling measurement post task..");
    PostToClientTask task = new PostToClientTask(url, value);
    executor.execute(task);
  }
}

