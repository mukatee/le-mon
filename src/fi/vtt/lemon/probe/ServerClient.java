/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import java.io.IOException;

import static fi.vtt.lemon.RabbitConst.*;

/**
 * Handles messaging with the server.
 *
 * @author Teemu Kanstren
 */
public class ServerClient {
  private final static Logger log = new Logger(ServerClient.class);
  private final Channel channel;

  public ServerClient(String url) {
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

  public boolean measurement(String measureURI, int precision, String value) {
    JSONObject json = new JSONObject();
    try {
      json.put(MSGTYPE, MSG_MEASUREMENT);
      json.put(PARAM_TIME, System.currentTimeMillis());
      json.put(PARAM_MEASURE_URI, measureURI);
      json.put(PARAM_PRECISION, precision);
      json.put(PARAM_VALUE, value);
      channel.basicPublish("", SERVER_QUEUE, null, json.toString().getBytes("UTF8"));
      return true;
    } catch (Exception e) {
      log.error("Error while sending msg to server", e);
      return false;
    }
  }

  public void event(String type, String source, String msg) {
    JSONObject json = new JSONObject();
    try {
      json.put(MSGTYPE, MSG_EVENT);
      json.put(PARAM_TIME, System.currentTimeMillis());
      json.put(PARAM_EVENT_TYPE, type);
      json.put(PARAM_EVENT_SOURCE, source);
      json.put(PARAM_EVENT_MSG, msg);
      channel.basicPublish("", SERVER_QUEUE, null, json.toString().getBytes("UTF8"));
    } catch (Exception e) {
      log.error("Error while sending msg to server", e);
    }
  }
}
