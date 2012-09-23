/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probe.measurement;

import osmo.common.log.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Creates threads for the thread pool. 
 * Delegates to Executors.detaultThreadFactory with the difference that all threads created are defined as daemon threads.
 *
 * @author Teemu Kanstren
 */
public class MeasurementThreadFactory implements ThreadFactory {
  private final static Logger log = new Logger(MeasurementThreadFactory.class);
  private final ThreadFactory delegate = Executors.defaultThreadFactory();

  public static void main(String[] args) throws Exception {
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new MeasurementThreadFactory());
    executor.scheduleAtFixedRate(new Runnable() {
      public void run() {
        System.out.println("hello");
      }
    }, 0, 100, TimeUnit.MILLISECONDS);
    Thread.sleep(500);
  }

  public Thread newThread(Runnable r) {
    log.debug("new thread created");
    Thread t = delegate.newThread(r);
    t.setDaemon(true);
    return t;
  }
}
