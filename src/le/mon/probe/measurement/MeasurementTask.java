package le.mon.probe.measurement;

import le.mon.MsgConst;
import le.mon.probe.Probe;
import le.mon.probe.ProbeServer;
import le.mon.probe.tasks.BMSender;
import le.mon.probe.tasks.EventSender;
import le.mon.server.MessagePooler;
import osmo.common.log.Logger;

/**
 * A task for performing a single measurement.
 *
 * @author Teemu Kanstren
 */
public class MeasurementTask implements Runnable {
  private final static Logger log = new Logger(MeasurementTask.class);
  /** The probe that provides the measurement. */
  private final Probe probe;
  /** Identifies the measurement. */
  private final String measureURI;
  /** When the task was started, allows checking if it is hanging or not. */
  private long startTime;
  /** Has it finished? */
  private boolean running = false;

  public MeasurementTask(Probe probe) {
    this.probe = probe;
    this.measureURI = probe.getMeasureURI();
  }

  public String getMeasureURI() {
    return measureURI;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  public void run() {
    log.debug("Calling measure on:" + probe);
    startTime = System.currentTimeMillis();
    running = true;
    String measure = null;
    try {
      measure = probe.measure();
    } catch (Exception e) {
      log.error("Error while performing measurement for " + measureURI, e);
    }
    running = false;
    log.debug("Received measure:" + measure + " from:" + measureURI);

    MessagePooler pooler = ProbeServer.getPooler();
    if (measure == null) {
      pooler.schedule(new EventSender(MsgConst.EVENT_NO_VALUE_FOR_BM, measureURI, "No valid measure available."));
      return;
    }
    pooler.schedule(new BMSender(measureURI, measure));
  }

  /**
   * Calculates how long the task has been running.
   *
   * @return Time the task has been running, in milliseconds.
   */
  public long getRunningTime() {
    log.debug("Running:" + running);
    if (!running) {
      return 0;
    }
    long now = System.currentTimeMillis();
    return now - startTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public boolean isRunning() {
    return running;
  }

  public Probe getProbe() {
    return probe;
  }
}
