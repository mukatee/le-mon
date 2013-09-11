/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probe.measurement;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.Probe;
import osmo.common.log.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Manages measurement provisioning for pull based probes, such as the SSH probe.
 * Intended to be shared for several probes.
 *
 * @author Teemu Kanstren
 */
public class MeasurementProvider {
  private final static Logger log = new Logger(MeasurementProvider.class);
  /** Executes measurement tasks using a thread pool. */
  private ScheduledThreadPoolExecutor executor = null;
  /** Currently running tasks for each probe. */
  private Map<Probe, WatchedTask> tasks = new ConcurrentHashMap<>();
  /** A separate task that checks running measurement tasks and cancels ones that are taking too long (assumes the task is hanging). */
  private WatchDog watchDog = null;
  /** For communicating with the le-mon server. */
  private final ServerClient server;

  public MeasurementProvider(ServerClient server) throws Exception {
    this.server = server;
  }

  /**
   * Stops the measurement process (thread pool executor, watchdog).
   */
  public void stop() {
    executor.shutdown();
    watchDog.shutdown();
  }

  /**
   * Start measuring the given probe with given configuration (from file).
   * 
   * @param probe The probe to start measuring.
   */
  public synchronized void startMeasuring(Probe probe) {
    int threadPoolSize = Config.getInt(RabbitConst.THREAD_POOL_SIZE, 5);
    int taskTimeOut = Config.getInt(RabbitConst.TASK_TIMEOUT, 5);
    if (executor == null) {
      executor = new ScheduledThreadPoolExecutor(threadPoolSize, new MeasurementThreadFactory());
      watchDog = new WatchDog(server, tasks, taskTimeOut);
    }
    MeasurementTask task = new MeasurementTask(server, probe);
    Future future = null;
    int interval = Config.getInt(RabbitConst.MEASURE_INTERVAL, 1);
    executor.scheduleAtFixedRate(task, 0, interval, TimeUnit.SECONDS);
    WatchedTask watchMe = new WatchedTask(future, task, tasks);
    //TODO: the set of tasks for a probe should be a collection, otherwise we will overwrite old ones if several 
    //measurements are started for a probe in a short time..
    tasks.put(probe, watchMe);
    log.debug("measuring probe:" + probe);
  }
}
