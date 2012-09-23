/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probe.measurement;

import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.Probe;
import osmo.common.log.Logger;

import static fi.vtt.lemon.RabbitConst.*;

/** 
 * A task for performing a single measurement.
 * 
 * @author Teemu Kanstren 
 */
public class MeasurementTask implements Runnable {
  private final static Logger log = new Logger(MeasurementProvider.class);
  /** The probe that provides the measurement. */
  private final Probe probe;
  /** Identifies the measurement. */
  private final String measureURI;
  /** Provides a connection to the le-mon server. */
  private final ServerClient server;
  /** When the task was started, allows checking if it is hanging or not. */
  private long startTime;
  /** Has it finished? */
  private boolean running = false;

  public MeasurementTask(ServerClient server, Probe probe) {
    this.probe = probe;
    this.measureURI = probe.getMeasureURI();
    this.server = server;
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
    int precision = probe.getPrecision();

    if (measure == null) {
      server.event(EVENT_NO_VALUE_FOR_BM, measureURI, "No valid measure available.");
      return;
    }    
    server.measurement(measureURI, precision, measure);
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
