/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probe.measurement;

import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.Probe;
import osmo.common.log.Logger;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static fi.vtt.lemon.RabbitConst.*;

/**
 * Keeps track of measurement tasks and if one exceeds the given timeout threshold, cancels the task and
 * removes the subscription. Also produces an error event to describe the scenario.
 *
 * @author Teemu Kanstren
 */
public class WatchDog implements Runnable {
  private final static Logger log = new Logger(WatchDog.class);
  private final Map<Probe, WatchedTask> subscriptions;
  private final int timeout;
  private final ScheduledExecutorService executor;
  private final ServerClient server;

  //timeout in seconds, has to be multiplied by 1000 since comparisons are made in milliseconds
  public WatchDog(ServerClient server, Map<Probe, WatchedTask> subscriptions, int timeout) {
    this.server = server;
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
    for (Map.Entry<Probe, WatchedTask> entry : subscriptions.entrySet()) {
      WatchedTask task = entry.getValue();
      log.debug("Running time:"+task.getRunningTime());
      if (task.getRunningTime() > timeout) {
        log.debug("Canceled measure task due to timeout (probe failure?):" + task);
        task.cancel();
        Probe probe = entry.getKey();
        subscriptions.remove(probe);
        server.event(EVENT_PROBE_HANGS, task.getMeasureURI(), "Probe has become non-responsive:"+probe);
      }
    }
  }
}
