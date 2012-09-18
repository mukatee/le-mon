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

package fi.vtt.lemon.probe.shared;

import fi.vtt.lemon.RabbitConst;

import java.util.Map;
import java.util.Properties;

/**
 * A base class for extending to create specific probes to the users needs. The term probe-agent here is a bit confusing,
 * This class (or its children) are instantiated for each probe configuration found in the configuration file. Thus this
 * practically describes a single "probe" entity, which is controlled by a generic "probe-agent".
 *
 * @author Teemu Kanstren
 */
public abstract class BaseProbeAgent implements Probe {
  protected ProbeInformation pi;

  public void init(Properties properties) {
    String targetName = properties.getProperty(RabbitConst.PROBE_TARGET_NAME);
    String targetType = properties.getProperty(RabbitConst.PROBE_TARGET_TYPE);
    String bmClass = properties.getProperty(RabbitConst.PROBE_BM_CLASS);
    String bmName = properties.getProperty(RabbitConst.PROBE_BM_NAME);
    String bmDescription = properties.getProperty(RabbitConst.PROBE_BM_DESCRIPTION);
    String probeName = properties.getProperty(RabbitConst.PROBE_NAME);
    int precision = Integer.parseInt(properties.getProperty(RabbitConst.PROBE_PRECISION));
    String xmlRpcUrl = properties.getProperty(RabbitConst.SERVER_URL);
    pi = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
  }

  public ProbeInformation getInformation() {
    return pi;
  }

  public void setBaseConfigurationParameters(Map<String, String> configuration) {
    String targetName = configuration.get(RabbitConst.PROBE_TARGET_NAME);
    String targetType = configuration.get(RabbitConst.PROBE_TARGET_TYPE);
    String bmClass = configuration.get(RabbitConst.PROBE_BM_CLASS);
    String bmName = configuration.get(RabbitConst.PROBE_BM_NAME);
    String bmDescription = configuration.get(RabbitConst.PROBE_BM_DESCRIPTION);
    String probeName = configuration.get(RabbitConst.PROBE_NAME);
    int precision = Integer.parseInt(configuration.get(RabbitConst.PROBE_PRECISION));
    String xmlRpcUrl = configuration.get(RabbitConst.SERVER_URL);
    pi = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
  }

}
