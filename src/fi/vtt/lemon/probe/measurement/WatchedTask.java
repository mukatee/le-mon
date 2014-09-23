package fi.vtt.lemon.probe.measurement;

import fi.vtt.lemon.probe.Probe;
import osmo.common.log.Logger;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Represents a task that is being monitored by the WatchDog.
 * 
 * @author Teemu Kanstren
 */
public class WatchedTask {
  private final static Logger log = new Logger(WatchedTask.class);
  /** An interface to the executing task in the Java concurrency framework, allowing its cancellation. */
  private final Future future;
  /** The actual measurement task being executed. */
  private final MeasurementTask task;
  /** The set of watched tasks. */
  private final Map<Probe, WatchedTask> tasks;

  public WatchedTask(Future future, MeasurementTask task, Map<Probe, WatchedTask> tasks) {
    this.future = future;
    this.task = task;
    this.tasks = tasks;
  }

  public long getRunningTime() {
    return task.getRunningTime();
  }

  /**
   * Cancels this task..
   * TODO: multiple tasks can exist for probe.. Should be a collection.
   */
  public void cancel() {
    future.cancel(true);
    tasks.remove(task.getProbe());
  }

  public String getMeasureURI() {
    return task.getMeasureURI();
  }
}
