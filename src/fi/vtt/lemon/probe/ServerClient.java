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
 * Handles messaging with the le-mon server-agent.
 *
 * @author Teemu Kanstren
 */
public class ServerClient {
  private final static Logger log = new Logger(ServerClient.class);
  /** Communication through RabbitMQ. */
  private final Channel channel;

  /**
   * Constructor..
   * 
   * @param url The server address..
   */
  public ServerClient(String url) {
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
   * Provides a measurement to the lemon server-agent.
   * 
   * @param measureURI The measure ID.
   * @param precision The probe precision.
   * @param value The measurement value.
   * @return True if no problem in trying to send the data..
   */
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

   /**
   * Provides an event to the lemon server-agent.
   *
   * @param type Event type (define your own types).
   * @param source Source of event (whatever floats your boat).
   * @param msg The message of the event (give details, etc.)
   * @return True if no problem in trying to send the data..
   */
  public boolean event(String type, String source, String msg) {
    JSONObject json = new JSONObject();
    try {
      json.put(MSGTYPE, MSG_EVENT);
      json.put(PARAM_TIME, System.currentTimeMillis());
      json.put(PARAM_EVENT_TYPE, type);
      json.put(PARAM_EVENT_SOURCE, source);
      json.put(PARAM_EVENT_MSG, msg);
      channel.basicPublish("", SERVER_QUEUE, null, json.toString().getBytes("UTF8"));
      return true;
    } catch (Exception e) {
      log.error("Error while sending msg to server", e);
      return false;
    }
  }
}
