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
import fi.vtt.lemon.probe.shared.Probe;
import fi.vtt.lemon.probe.shared.ProbeInformation;
import osmo.common.log.Logger;

import java.util.Properties;

/**
 * Base class to create test probes that provide test-data to the server-agent.
 *
 * @author Teemu Kanstren
 */
public class TestProbe implements Probe {
  private final static Logger log = new Logger(TestProbe.class);
  private final String targetName;
  private final String targetType;
  private final String bmClass;
  private final String bmName;
  private final String bmDescription;
  private final String probeDescription;
  private final String result;
  private final int precision;
  private Properties properties;

  public TestProbe(String targetName, String targetType, String bmClass, String bmName, String bmDescription, String probeDescription, int precision) {
    this(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, null, precision);
  }

  public TestProbe(String targetName, String targetType, String bmClass, String bmName, String bmDescription, String probeDescription, String result, int precision) {
    this.targetName = targetName;
    this.targetType = targetType;
    this.bmClass = bmClass;
    this.bmName = bmName;
    this.bmDescription = bmDescription;
    this.probeDescription = probeDescription;
    this.result = result;
    this.precision = precision;
  }

  public void start() throws Exception {
    MeasurementProvider mp = new MeasurementProvider(new ServerClient("::1"), 5, 10);
    mp.setInterval(1);
    mp.startMeasuring(this);
  }

  public ProbeInformation getInformation() {
    return new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);
  }

  public String measure() {
    log.debug("Testprobe provides measure:" + result);
    return result;
  }

  public void init(Properties properties) {
    log.debug("initializing with:" + properties);
    this.properties = properties;
  }

  public Properties getProperties() {
    return properties;
  }

}
