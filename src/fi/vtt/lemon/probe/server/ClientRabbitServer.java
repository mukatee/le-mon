/*
 * Copyright (c) 2013 VTT
 */

package fi.vtt.lemon.probe.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probe.Probe;
import fi.vtt.lemon.server.internal.EventProcessor;
import fi.vtt.lemon.server.internal.MeasurementProcessor;
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
public class ClientRabbitServer implements Runnable {
  private final static Logger log = new Logger(ClientRabbitServer.class);
  /** Allows receiving messages from RabbitMQ. */
  private QueueingConsumer consumer;
  /** The thread pool for processing received messages. */
  private ScheduledThreadPoolExecutor executor;
  private final Probe probe;
  private boolean shouldRun = true;

  public ClientRabbitServer(Probe probe) {
    this.probe = probe;
  }

  /**
   * The main method for the thread that reads RabbitMQ queue and creates processors for received messages using
   * the thread pool.
   */
  @Override
  public void run() {
    while (shouldRun) {
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
        case MSG_ADD_MEASURE:
          System.out.println("Measurement add received:"+json);
          task = new AddMeasureProcessor(probe, json);
          break;
        case MSG_REMOVE_MEASURE:
          System.out.println("Measurement delete received:"+json);
          task = new RemoveMeasureProcessor(probe, json);
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
    log.debug("Starting client rabbit");
    //setup the thread pool
    executor = new ScheduledThreadPoolExecutor(Config.getInt(RabbitConst.THREAD_POOL_SIZE, 5));

    //start up RabbitMQ connection
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(Config.getString(RabbitConst.BROKER_ADDRESS, "::1"));
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(ALL_PROBES_QUEUE, false, false, false, null);

    consumer = new QueueingConsumer(channel);
    channel.basicConsume(ALL_PROBES_QUEUE, true, consumer);
    //start the thread that receives messages (this class)
    Thread thread = new Thread(this);
    thread.start();
    log.debug("Client rabbit started");
  }
  
  public void stop() {
    shouldRun = false;
  }
}
