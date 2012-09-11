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

import fi.vtt.lemon.common.EventType;
import fi.vtt.lemon.probe.shared.ProbeEvent;
import osmo.common.log.Logger;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Keeps track of measurement tasks and if one exceeds the given timeout threshold, cancels the task and
 * removes the subscription. Also produces an error event to describe the scenario.
 *
 * @author Teemu Kanstren
 */
public class WatchDog implements Runnable {
  private final static Logger log = new Logger(WatchDog.class);
  private final Map<Long, WatchedTask> subscriptions;
  private final int timeout;
  private final ScheduledExecutorService executor;

  //timeout in seconds, has to be multiplied by 1000 since comparisons are made in milliseconds
  public WatchDog(Map<Long, WatchedTask> subscriptions, int timeout) {
    this.subscriptions = subscriptions;
    //multiply by 1000 to turn seconds into milliseconds
    this.timeout = timeout*1000;
    //create a thread pool of size one, allowing scheduling, using daemon threads
    executor = Executors.newScheduledThreadPool(1, new MeasurementThreadFactory());
    //schedule the watchdog task starting one second from now, and to run with one second delay in between
    executor.scheduleWithFixedDelay(this, 1, 1, TimeUnit.SECONDS);
    log.debug("WatchDog started with timeout "+timeout);
  }

  public void shutdown() {
    executor.shutdown();
  }

  public void run() {
    log.debug("Set:"+subscriptions.entrySet());
    //we assume the hashmap we have is thread safe (e.g. concurrenthashmap) so we just iterate it
    for (Map.Entry<Long, WatchedTask> entry : subscriptions.entrySet()) {
      WatchedTask task = entry.getValue();
      //start by checking one-time tasks for completion
      task.checkState();
      log.debug("Running time:"+task.getRunningTime());
      if (task.getRunningTime() > timeout) {
        log.debug("Canceled measure task due to timeout (probe failure?):" + task);
        task.cancel();
        long subscriptionId = entry.getKey();
        subscriptions.remove(subscriptionId);
        ProbeEvent event = new ProbeEvent(task.getServerAgent(), EventType.PROBE_HUNG, task.getProbeInfo().getMeasureURI(), "Probe has become non-responsive.", subscriptionId);
//        bb.process(event);
      }
    }
  }
}