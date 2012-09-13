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

package fi.vtt.lemon.probe.plugins.xmlrpc;

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
  //port where the RabbitMQ will listen to the incoming messages
  private int port = -1;
  //default interval between sending the keep-alive messages
  private static final int DEFAULT_KEEPALIVE_INTERVAL = 1000;
  //default interval between trying to reconnect to server
  private static final int DEFAULT_RETRY_DELAY = 5000;
  //actual keep alive interval for this thread
  private int keepAliveInterval;
  //the server-agent where the keep-alive messages are sent
  private ServerClient rabbit = null;
  //time to wait between retrying a failed connection to a server-agent
  private int retryDelay;

  public ProbeAgentConfig(int port, String destinationUrl, int keepAliveInterval, int retryDelay) {
    init(port, destinationUrl, keepAliveInterval, retryDelay);
  }

  public ProbeAgentConfig(InputStream in) {
    init(in);
  }

  private void init(int port, String destinationUrl, int keepAliveInterval, int retryDelay) {
    log.debug("Creating connection to RabbitMQ server at:"+destinationUrl);
    ServerClient server = new ServerClient(destinationUrl);
    init(port, server, keepAliveInterval, retryDelay);
  }

  private void init(int port, ServerClient server, int keepAliveInterval, int retryDelay) {
    log.debug("initializing: RabbitMQ client, port = " + port);
    //TODO: make this port useful
    this.port = port;
    this.keepAliveInterval = keepAliveInterval;
    this.rabbit = server;
    this.retryDelay = retryDelay;
  }

  public ServerClient getServerClient() {
    return rabbit;
  }

  public void setServerClient(ServerClient rabbit) {
    this.rabbit = rabbit;
  }

  public int getKeepAliveInterval() {
    return keepAliveInterval;
  }

  public int getRetryDelay() {
    return retryDelay;
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
    String reportIntervalValue = properties.getProperty(RabbitConst.MEASURE_INTERVAL);
    int reportInterval = DEFAULT_KEEPALIVE_INTERVAL;
    if (reportIntervalValue != null) {
      try {
        reportInterval = Integer.parseInt(reportIntervalValue);
      } catch (NumberFormatException e) {
        log.error("Failed to read report interval from config, got:" + reportIntervalValue + ". Using defaults.", e);
      }
    }
    String retryDelayValue = properties.getProperty(RabbitConst.RETRY_DELAY);
    int retryDelay = DEFAULT_RETRY_DELAY;
    if (retryDelayValue != null) {
      try {
        retryDelay = Integer.parseInt(retryDelayValue);
      } catch (NumberFormatException e) {
        log.error("Failed to read retry delay from config, got:"+retryDelayValue+". Using defaults.", e);
      }
    }
    if (destinationUrl == null) {
      throw new IllegalArgumentException("No server URL defined. Unable to start.");
    } else {
      init(port, destinationUrl, reportInterval, retryDelay);
    }
  }
}
