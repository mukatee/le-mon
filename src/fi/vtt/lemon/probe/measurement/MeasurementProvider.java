/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.lemon.probe.measurement;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.Probe;
import osmo.common.log.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A thread for providing measurements as requested by the server.
 * New requests are posted from the ProbeAgent implementation class(es).
 * Needs to be running as a separate thread to be able to provide sampling values as requested and
 * to provide measurements as asynchronous operations.
 *
 * @author Teemu Kanstren
 */
public class MeasurementProvider {
  private final static Logger log = new Logger(MeasurementProvider.class);
  //used for synchronization
//  private final Object lock = new Object();
  private ScheduledThreadPoolExecutor executor = null;
  private Map<Probe, WatchedTask> subscriptions = new ConcurrentHashMap<>();
  //default size of the thread pool for performing measurements
  private int threadPoolSize = 5;
  //how long until a measurement task times out if becoming unresponsive
  private int taskTimeOut = 5;
  private WatchDog watchDog = null;
  private final ServerClient server;
  private int interval = 0;

  public MeasurementProvider(ServerClient server, int threadPoolSize, int taskTimeOut) throws Exception {
    this.server = server;
    if (threadPoolSize <= 0) {
      initFromFile();
    } else {
      this.threadPoolSize = threadPoolSize;
      this.taskTimeOut = taskTimeOut;
    }
  }

  public void setInterval(int interval) {
    this.interval = interval;
  }

  private void initFromFile() throws IOException {
    Properties properties = Config.get();
    String threads = properties.getProperty(RabbitConst.THREAD_POOL_SIZE);
    if (threads != null) {
      threadPoolSize = Integer.parseInt(threads);
      log.debug("Initialized thread pool size to "+threadPoolSize+" threads.");
    } else {
      log.debug("No thread pool size defined, using default of "+threadPoolSize+" threads.");
    }
    String time = properties.getProperty(RabbitConst.TASK_TIMEOUT);
    if (time != null) {
      taskTimeOut = Integer.parseInt(time);
      log.debug("Initialized task timeout to "+taskTimeOut+ " seconds");
    } else {
      log.debug("No task timeout specified, using default of "+taskTimeOut+" seconds." );
    }
  }

  /**
   * stops the sampling thread.
   */
  public void stop() {
    executor.shutdown();
    watchDog.shutdown();
  }

  /**
   * Add a new measurement request to be sampled at a given interval.
   */
  public synchronized void startMeasuring(Probe probe) {
    if (executor == null) {
      executor = new ScheduledThreadPoolExecutor(threadPoolSize, new MeasurementThreadFactory());
      watchDog = new WatchDog(server, subscriptions, taskTimeOut);
    }
    MeasurementTask task = new MeasurementTask(server, probe);
    Future future = null;
    executor.scheduleAtFixedRate(task, 0, interval, TimeUnit.SECONDS);
    WatchedTask watchMe = new WatchedTask(future, task, subscriptions);
    subscriptions.put(probe, watchMe);
    log.debug("measuring probe:" + probe);
  }
}
