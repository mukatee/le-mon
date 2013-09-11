/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.internal;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import java.io.IOException;

import static fi.vtt.lemon.RabbitConst.*;

/**
 * Handles messaging from le-mon server to probes.
 *
 * @author Teemu Kanstren
 */
public class ServerToProbe {
  private final static Logger log = new Logger(ServerToProbe.class);
  /** Communication through RabbitMQ. */
  private final Channel channel;

  /**
   * Constructor..
   * 
   * @param url The server address..
   */
  public ServerToProbe(String url) {
    //set up RabbitMQ
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(url);
    try {
      Connection connection = factory.newConnection();
      channel = connection.createChannel();
      channel.queueDeclare(SERVER_QUEUE, false, false, false, null);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to initialize connection to RabbitMQ server with url:"+url, e);
    }
  }

  /**
   * Adds a measurement for a probe.
   * 
   * @param measureURI The measure ID.
   * @param config The measurement config.
   * @return True if no problem in trying to send the data..
   */
  public boolean addMeasurement(String measureURI, String config) {
    JSONObject json = new JSONObject();
    try {
      json.put(MSGTYPE, MSG_ADD_MEASURE);
      json.put(PARAM_TIME, System.currentTimeMillis());
      json.put(PARAM_MEASURE_URI, measureURI);
      json.put(PARAM_CONFIG, config);
      //TODO: this should be changed to only msg the relevant probe and not all
      channel.basicPublish("", ALL_PROBES_QUEUE, null, json.toString().getBytes("UTF8"));
      return true;
    } catch (Exception e) {
      log.error("Error while sending msg to server", e);
      return false;
    }
  }

  /**
   * Removes a measurement for a probe.
   *
   * @param measureURI The measure ID.
   * @param config The measurement config.
   * @return True if no problem in trying to send the data..
   */
  public boolean removeMeasurement(String measureURI, String config) {
    JSONObject json = new JSONObject();
    try {
      json.put(MSGTYPE, MSG_REMOVE_MEASURE);
      json.put(PARAM_TIME, System.currentTimeMillis());
      json.put(PARAM_MEASURE_URI, measureURI);
      json.put(PARAM_CONFIG, config);
      //TODO: this should be changed to only msg the relevant probe and not all
      channel.basicPublish("", ALL_PROBES_QUEUE, null, json.toString().getBytes("UTF8"));
      return true;
    } catch (Exception e) {
      log.error("Error while sending msg to server", e);
      return false;
    }
  }
}
