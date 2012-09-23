/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.internal;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import static fi.vtt.lemon.RabbitConst.*;

/**
 * Acts as a server for probe-agents to communicate with.
 * Internal refers to communication within the le-mon measurement infrastructure.
 * External would be reference to communication with external elements such as the client.
 *
 * @author Teemu Kanstren
 */
public class InternalServer implements Runnable {
  private final static Logger log = new Logger(InternalServer.class);
  /** Allows receiving messages from RabbitMQ. */
  private QueueingConsumer consumer;
  /** The thread pool for processing received messages. */
  private ScheduledThreadPoolExecutor executor;

  /**
   * The main method for the thread that reads RabbitMQ queue and creates processors for received messages using
   * the thread pool.
   */
  @Override
  public void run() {
    while (true) {
      String msg = null;
      JSONObject json = null;
      try {
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        String body = new String(delivery.getBody());
        json = new JSONObject(body);
        log.debug(" [x] Received '" + body + "'");
        msg = (String) json.get(MSGTYPE);
      } catch (Exception e) {
        log.error("Error while getting message from RabbitMQ. Sleeping 5s.", e);
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e1) {
        }
      }
      Runnable task = null;
      switch (msg) {
        case MSG_MEASUREMENT:
          System.out.println("Measurement received:"+json);
          task = new MeasurementProcessor(json);
          break;
        case MSG_EVENT:
          System.out.println("Event received:"+json);
          task = new EventProcessor(json);
          break;
        default:
          log.debug("Invalid message received:"+json);
          return;
      }
      executor.execute(task);
    }
  }

  /**
   * Binds to the associated RabbitMQ message queue, initiates a thread pool for processing received messages,
   * and starts the queue listener as a thread itself.
   * 
   * @throws Exception
   */
  public void start() throws Exception {
    //setup the thread pool
    executor = new ScheduledThreadPoolExecutor(Config.getInt(RabbitConst.THREAD_POOL_SIZE, 5));

    //start up RabbitMQ connection
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(Config.getString(RabbitConst.BROKER_ADDRESS, "::1"));
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(SERVER_QUEUE, false, false, false, null);

    consumer = new QueueingConsumer(channel);
    channel.basicConsume(SERVER_QUEUE, true, consumer);
    //start the thread that receives messages (this class)
    Thread thread = new Thread(this);
    thread.start();
  }
}
