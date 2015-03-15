package le.mon.probe.measurement;

import le.mon.Config;
import le.mon.MsgConst;
import le.mon.probe.Probe;
import le.mon.probe.ProbeServer;
import le.mon.probe.tasks.KeepAliveTask;
import le.mon.probe.tasks.RegistrationSender;
import le.mon.probe.tasks.UnRegistrationSender;
import le.mon.server.MessagePooler;
import le.mon.MsgConst;
import le.mon.probe.Probe;
import le.mon.probe.tasks.KeepAliveTask;
import le.mon.probe.tasks.RegistrationSender;
import le.mon.probe.tasks.UnRegistrationSender;
import osmo.common.log.Logger;

import java.util.Collection;
import java.util.HashSet;
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
  private ProbeWatchDog watchDog = null;
  private Collection<Probe> probes = new HashSet<>();

  /**
   * Stops the measurement process (thread pool executor, watchdog).
   */
  public void stop() {
    MessagePooler pooler = ProbeServer.getPooler();
    for (Probe probe : probes) {
      pooler.schedule(new UnRegistrationSender(probe));
      log.info("Unregistering probe:"+probe.getMeasureURI());
    }
    executor.shutdown();
    watchDog.shutdown();
  }

  /**
   * Start measuring the given probe with given configuration (from file).
   *
   * @param probe The probe to start measuring.
   */
  public synchronized void startMeasuring(Probe probe) {
    probes.add(probe);
    MessagePooler pooler = ProbeServer.getPooler();
    pooler.schedule(new RegistrationSender(probe.getMeasureURI()));
    int threadPoolSize = Config.getInt(MsgConst.THREAD_POOL_SIZE, 5);
    int taskTimeOut = Config.getInt(MsgConst.TASK_TIMEOUT, 5);
    if (executor == null) {
      executor = new ScheduledThreadPoolExecutor(threadPoolSize, new DaemonThreadFactory());
      watchDog = new ProbeWatchDog(tasks, taskTimeOut);
    }
    MeasurementTask task = new MeasurementTask(probe);
    int interval = Config.getInt(MsgConst.MEASURE_INTERVAL, 1);
    Future future = executor.scheduleAtFixedRate(task, 0, interval, TimeUnit.SECONDS);
    WatchedTask watchMe = new WatchedTask(future, task, tasks);
    //TODO: the set of tasks for a probe should be a collection, otherwise we will overwrite old ones if several measurements are started for a probe in a short time..
    tasks.put(probe, watchMe);
    log.debug("measuring probe:" + probe);
    KeepAliveTask kat = new KeepAliveTask(probe.getMeasureURI());
    Future katFuture = executor.scheduleAtFixedRate(kat, 0, 5, TimeUnit.SECONDS);
  }
}
