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

package fi.vtt.lemon.probe.plugins.measurement;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.common.Const;
import fi.vtt.lemon.common.DataObject;
import fi.vtt.lemon.common.ProbeConfiguration;
import fi.vtt.lemon.probe.shared.MeasurementRequest;
import fi.vtt.lemon.probe.shared.Probe;
import fi.vtt.lemon.probe.shared.UnsubscriptionRequest;
import osmo.common.log.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
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
  private Map<Long, WatchedTask> subscriptions = new ConcurrentHashMap<Long, WatchedTask>();
  //default size of the thread pool for performing measurements
  private int threadPoolSize = 5;
  //how long until a measurement task times out if becoming unresponsive
  private int taskTimeOut = 5;
  private WatchDog watchDog = null;

  public MeasurementProvider(int threadPoolSize, int taskTimeOut) throws Exception {
    if (threadPoolSize <= 0) {
      initFromFile();
    } else {
      this.threadPoolSize = threadPoolSize;
      this.taskTimeOut = taskTimeOut;
    }
  }

  private void initFromFile() throws IOException {
    Properties properties = Config.get();
    String threads = properties.getProperty(Const.THREAD_POOL_SIZE);
    if (threads != null) {
      threadPoolSize = Integer.parseInt(threads);
      log.debug("Initialized thread pool size to "+threadPoolSize+" threads.");
    } else {
      log.debug("No thread pool size defined, using default of "+threadPoolSize+" threads.");
    }
    String time = properties.getProperty(Const.TASK_TIMEOUT);
    if (time != null) {
      taskTimeOut = Integer.parseInt(time);
      log.debug("Initialized task timeout to "+taskTimeOut+ " seconds");
    } else {
      log.debug("No task timeout specified, using default of "+taskTimeOut+" seconds." );
    }
  }

  public Map<Long, WatchedTask> getSubscriptions() {
    return subscriptions;
  }

  /*
  public Collection<MeasurementRequest> getRequests() {
    return requests;
  }*/

  public void start() {
    executor = new ScheduledThreadPoolExecutor(threadPoolSize, new MeasurementThreadFactory());
    watchDog = new WatchDog(subscriptions, taskTimeOut);
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
   *
   * @param req The request to be sampled.
   */
  public void addSamplingRequest(MeasurementRequest req) {   
    //if subscriptionId is found remove the old subscription first.
    //this is for changing sampling frequency without sending separate unsubscription request 
    //and not getting multiple subscriptions for the same sac/bm
    if (req.getSubscriptionId() > 0) {
      WatchedTask watchedTask = subscriptions.get(req.getSubscriptionId());
      if (watchedTask != null) {
        watchedTask.cancel();
      }
    }
    MeasurementTask task = new MeasurementTask(req);
    Future future = null;
    //one time requests have interval of -1
    boolean oneTime = (req.getInterval() <= 0);
    if (oneTime) {
      //one-time measure
      log.debug("adding one-time measure");
      future = new FutureTask(task, null);
      executor.execute(task);
    } else {
      log.debug("scheduling recurring measurement with interval of "+req.getInterval()+"ms.");
      future = executor.scheduleAtFixedRate(task, 0, req.getInterval(), TimeUnit.MILLISECONDS);
    }
    WatchedTask watchMe = new WatchedTask(future, task, subscriptions, oneTime, req.getServer());
    subscriptions.put(req.getSubscriptionId(), watchMe);
    log.debug("added request:" + req);
  }

  /**
   * Provides means to remove previous subscriptions.
   *
   * @param subscriptionId Identifying the measurement request to be removed.
   */ 
  public void unsubscribeTo(long subscriptionId) {
    WatchedTask task = subscriptions.get(subscriptionId);
    task.cancel();
  }
  
/*
  public List<Long> getSubscriptionIdsForProbe(Probe probe) {
    List<Long> subscriptionIds = new ArrayList<Long>();
    for (Map.Entry<Long, WatchedTask> entry : subscriptions.entrySet()) {      
      WatchedTask watchedTask = entry.getValue();
      MeasurementTask task = watchedTask.getMeasurementTask();
      if (probe == task.getProbe()) {
        subscriptionIds.add(entry.getKey());
      }
    }
    return subscriptionIds;
  }
*/
  
  public void setReference(long subscriptionId, String reference) {   
    for (Map.Entry<Long, WatchedTask> entry : subscriptions.entrySet()) {      
      WatchedTask watchedTask = entry.getValue();
      MeasurementTask task = watchedTask.getMeasurementTask();
      if (subscriptionId == task.getSubscriptionId()) {
        task.setReference(reference);
        break;
      }
    }
  }
  
  
  public void setConfiguration(Probe probe, Map<String, String> configuration) {
    String mode = configuration.get("mode");
    if (mode != null) {
      for (Map.Entry<Long, WatchedTask> entry : subscriptions.entrySet()) {      
        WatchedTask watchedTask = entry.getValue();
        MeasurementTask task = watchedTask.getMeasurementTask();
        if (probe == task.getProbe()) {
          for (ProbeConfiguration probeConfig : probe.getConfigurationParameters()) {
            if (probeConfig.getName().equals("mode")) {
              if (mode.equals("compare")) {
                task.setCompare(true);
              } else if (mode.equals("normal")){
                task.setCompare(false);
              }
              break;
            }
          }
        }
      }
    }
  }
  
  public void process(DataObject data) {
    log.debug("processing...");
    if (data instanceof UnsubscriptionRequest) {
      UnsubscriptionRequest ur = (UnsubscriptionRequest) data;
      unsubscribeTo(ur.getSubscriptionId());
    } else {
      MeasurementRequest req = (MeasurementRequest) data;
      addSamplingRequest(req);
    }
  }
}