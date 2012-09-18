/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.lemon.probe.measurement;

import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.shared.Probe;
import fi.vtt.lemon.probe.shared.ProbeInformation;
import osmo.common.log.Logger;

import static fi.vtt.lemon.RabbitConst.*;

/** @author Teemu Kanstren */
public class MeasurementTask implements Runnable {
  private final static Logger log = new Logger(MeasurementProvider.class);
  private final Probe probe;
  private final String measureURI;
  private final ServerClient server;
  private long startTime;
  private boolean running = false;

  public MeasurementTask(ServerClient server, Probe probe) {
    this.probe = probe;
    this.measureURI = probe.getInformation().getMeasureURI();
    this.server = server;
  }

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
    int precision = probe.getInformation().getPrecision();

    if (measure == null) {
      server.event(EVENT_NO_VALUE_FOR_BM, measureURI, "No valid measure available.");
      return;
    }    
    server.measurement(measureURI, precision, measure);
  }

  public ProbeInformation getProbeInfo() {
    return probe.getInformation();
  }

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
