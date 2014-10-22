package fi.vtt.lemon.server.registry;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.MsgConst;
import fi.vtt.lemon.probe.measurement.DaemonThreadFactory;
import fi.vtt.lemon.server.data.ProbeDescription;
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
    this.timeout = timeout*1000;
    this.registry = registry;
    //schedule the watchdog task starting one second from now, and to run with one second delay in between
    executor.scheduleWithFixedDelay(this, 1, 1, TimeUnit.SECONDS);
    log.debug("PA WatchDog started with timeout " + timeout);
  }

  @Override
  public void run() {
    List<ProbeDescription> probes = registry.getProbes();
    long now = System.currentTimeMillis();
    long thresholdMissing = now-timeout*1000;
    long thresholdGone = now-timeout*1000;
    Collection<ProbeDescription> lost = new ArrayList<>();
    Collection<ProbeDescription> missing = new ArrayList<>();
    for (ProbeDescription probe : probes) {
      if (!probe.isAvailable()) {
        if (probe.getLastHeard() > thresholdGone) {
          lost.add(probe);
        }
      } else {
        if (probe.getLastHeard() > thresholdMissing) {
          probe.setAvailable(false);
          missing.add(probe);
        }
      }
    }
    for (ProbeDescription probe : missing) {
      registry.missing(probe);
    }
    for (ProbeDescription probe : lost) {
      registry.lost(probe);
    }
  }

  public void shutdown() {
    executor.shutdown();
  }
}