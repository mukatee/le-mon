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

package fi.vtt.lemon.probe;

import fi.vtt.lemon.RabbitConst;
import osmo.common.log.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Defines a configuration for a probe-bundle.
 *
 * @author Teemu Kanstren
 */
public class ProbeAgentConfig {
  private final static Logger log = new Logger(ProbeAgentConfig.class);
  //the server-agent where the keep-alive messages are sent
  private ServerClient rabbit = null;

  public ProbeAgentConfig(String rabbitURL) {
    init(rabbitURL);
  }

  public ProbeAgentConfig(InputStream in) {
    init(in);
  }

  private void init(String rabbitURL) {
    log.debug("Creating connection to RabbitMQ server at:"+rabbitURL);
    rabbit = new ServerClient(rabbitURL);
  }

  public ServerClient getServerClient() {
    return rabbit;
  }

  /**
   * Reads the initial configuration from the properties file given as the input stream.
   *
   * @param in The data for the properties file.
   */
  private void init(InputStream in) {
    Properties properties = new Properties();
    try {
      properties.load(in);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load properties from given inputstream", e);
    }
    log.debug("Loaded properties:" + properties);
    String destinationUrl = properties.getProperty(RabbitConst.SERVER_URL);
    if (destinationUrl == null) {
      throw new IllegalArgumentException("No server URL defined. Unable to start.");
    } else {
      init(destinationUrl);
    }
  }
}
