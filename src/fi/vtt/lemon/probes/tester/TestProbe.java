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

package fi.vtt.lemon.probes.tester;

import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.measurement.MeasurementProvider;
import fi.vtt.lemon.probe.Probe;
import osmo.common.log.Logger;

/**
 * Base class to create test probes that provide test-data to the server-agent.
 *
 * @author Teemu Kanstren
 */
public class TestProbe implements Probe {
  private final static Logger log = new Logger(TestProbe.class);
  private final String result;
  private final String measureURI;
  private final int precision;

  public TestProbe(String result, String measureURI, int precision) {
    this.result = result;
    this.measureURI = measureURI;
    this.precision = precision;
  }

  public TestProbe(String measureURI, int precision) {
    this.result = null;
    this.measureURI = measureURI;
    this.precision = precision;
  }

  public void start() throws Exception {
    MeasurementProvider mp = new MeasurementProvider(new ServerClient("::1"), 5, 10);
    mp.setInterval(1);
    mp.startMeasuring(this);
  }

  public String getMeasureURI() {
    return measureURI;
  }

  public int getPrecision() {
    return precision;
  }

  public String measure() {
    log.debug("Testprobe provides measure:" + result);
    return result;
  }

}
