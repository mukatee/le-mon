package fi.vtt.lemon.probe.measurement;

import fi.vtt.lemon.probe.Probe;
import fi.vtt.lemon.probe.ProbeServer;
import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.tasks.EventSender;
import fi.vtt.lemon.server.MessagePooler;
import osmo.common.log.Logger;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static fi.vtt.lemon.MsgConst.*;

/**
 * Keeps track of measurement tasks and if one exceeds the given timeout threshold, cancels the task.
 * Also produces an error event to describe the scenario.
 *
 * @author Teemu Kanstren
 */
public class WatchDog implements Runnable {
  private final static Logger log = new Logger(WatchDog.class);
  /** The set of tasks to be watched. */
  private final Map<Probe, WatchedTask> tasks;
  /** Timeout for canceling a task (in milliseconds). */
  private final int timeout;
  /** The thread pool executor. */
  private final ScheduledExecutorService executor;
  /** The connection to the le-mon servere. */
  private final ServerClient server;

  /**
   * Creates a dog for watching all the probes and their tasks..
   * 
   * @param server Connection to the le-mon server to provide events.
   * @param tasks The tasks to be watched, may change during runtime.
   * @param timeout The timeout (in seconds) until failure is assumed.
   */
  public WatchDog(ServerClient server, Map<Probe, WatchedTask> tasks, int timeout) {
    this.server = server;
    this.tasks = tasks;
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

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  public void run() {
    log.debug("Set:" + tasks.entrySet());
    //we assume the hashmap we have is thread safe (e.g. concurrenthashmap) so we just iterate it
    for (Map.Entry<Probe, WatchedTask> entry : tasks.entrySet()) {
      WatchedTask task = entry.getValue();
      log.debug("Running time:"+task.getRunningTime());
      if (task.getRunningTime() > timeout) {
        log.debug("Canceled measure task due to timeout (probe failure?):" + task);
        task.cancel();
        Probe probe = entry.getKey();
        tasks.remove(probe);
        MessagePooler pooler = ProbeServer.getPooler();
        pooler.schedule(new EventSender(EVENT_PROBE_HANGS, task.getMeasureURI(), "Probe has become non-responsive:" + probe));
      }
    }
  }
}
