/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probe.measurement;

import fi.vtt.lemon.probe.Probe;
import osmo.common.log.Logger;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Teemu Kanstren
 */
public class WatchedTask {
  private final static Logger log = new Logger(WatchedTask.class);
  private final Future future;
  private final MeasurementTask task;
  private final Map<Probe, WatchedTask> subscriptions;

  public WatchedTask(Future future, MeasurementTask task, Map<Probe, WatchedTask> subscriptions) {
    this.future = future;
    this.task = task;
    this.subscriptions = subscriptions;
  }

  public long getRunningTime() {
    return task.getRunningTime();
  }

  public void cancel() {
    future.cancel(true);
    subscriptions.remove(task.getProbe());
  }

  public String getMeasureURI() {
    return task.getMeasureURI();
  }
}
