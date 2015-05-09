package le.mon.server.registry;

import le.mon.probe.measurement.DaemonThreadFactory;
import le.mon.server.data.ProbeDescription;
import osmo.common.log.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Teemu Kanstren
 */
public class ProbeAgentWatchDog implements Runnable {
  private final static Logger log = new Logger(ProbeAgentWatchDog.class);
  /** The thread pool executor. */
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());
  /** Timeout for reporting a probe as lost (in milliseconds). */
  private final int timeout;
  private final Registry registry;

  public ProbeAgentWatchDog(int timeout, Registry registry) {
    //multiply by 1000 to turn seconds into milliseconds
    this.timeout = timeout * 1000;
    this.registry = registry;
  }

  public void start() {
    //schedule the watchdog task starting one second from now, and to run with one second delay in between
    executor.scheduleWithFixedDelay(this, 1, 1, TimeUnit.SECONDS);
    log.debug("PA WatchDog started with timeout " + timeout);
  }

  @Override
  public void run() {
    log.debug("Running watchdog");
    List<ProbeDescription> probes = registry.getProbes();
    long thresholdMissing = timeout;
    long thresholdGone = timeout + thresholdMissing;
    Collection<ProbeDescription> lost = new ArrayList<>();
    Collection<ProbeDescription> missing = new ArrayList<>();
    for (ProbeDescription probe : probes) {
      long diff = System.currentTimeMillis() - probe.getLastHeard();
      if (!probe.isAvailable()) {
        if (diff > thresholdGone) {
          lost.add(probe);
        }
      } else {
        if (diff > thresholdMissing) {
          probe.setAvailable(false);
          missing.add(probe);
        }
      }
    }
    if (missing.size() > 0) log.info("probes missing:" + missing);
    for (ProbeDescription probe : missing) {
      registry.missing(probe);
    }
    if (lost.size() > 0) log.info("lost probes:" + lost);
    for (ProbeDescription probe : lost) {
      registry.lost(probe);
    }
  }

  public void shutdown() {
    executor.shutdown();
  }
}
