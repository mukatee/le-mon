package fi.vtt.lemon.server.internal;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import static fi.vtt.lemon.RabbitConst.*;

/**
 * Acts as a server for probe-agents to communicate with.
 *
 * @author Teemu Kanstren
 */
public class InternalServer implements Runnable {
  private final static Logger log = new Logger(InternalServer.class);
  private QueueingConsumer consumer;
  private ScheduledThreadPoolExecutor executor;

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

  public void start() throws Exception {
    int THREAD_POOL_SIZE = 5;
    executor = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("::1");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(SERVER_QUEUE, false, false, false, null);

    consumer = new QueueingConsumer(channel);
    channel.basicConsume(SERVER_QUEUE, true, consumer);
    Thread thread = new Thread(this);
    thread.start();
  }
}
