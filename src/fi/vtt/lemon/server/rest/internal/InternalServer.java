package fi.vtt.lemon.server.rest.internal;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import java.util.List;
import java.util.Map;

import static fi.vtt.lemon.RabbitConst.*;

/**
 * Acts as a server for probe-agents to communicate with.
 *
 * @author Teemu Kanstren
 */
public class InternalServer {
  private final static Logger log = new Logger(InternalServer.class);

  public void start() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(SERVER_QUEUE, false, false, false, null);

    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(SERVER_QUEUE, true, consumer);

    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
      String body = new String(delivery.getBody());
      log.debug(" [x] Received '" + body + "'");
      JSONObject json = new JSONObject(body);
      String msg = json.getString(MSGTYPE);
      switch (msg) {
        case MSG_MEASUREMENT:
          break;
        case MSG_EVENT:
          break;
      }
    }
  }

  public boolean measurement(long time, String measureURI, int precision, String value, long subscriptionId) {
    return false;
  }

  public void event(long time, String type, String source, String message, long subscriptionId) {
  }

  public long register(Map<String, String> properties) {
    return 0;
  }

  public boolean keepAlive(long probeId) {
    return false;
  }

  public void unregister(long probeId) {
  }

  public void checkSubscriptions(long probeId, List<Long> subscriptionIds) {
  }

  public boolean BMReport(long time, String measureURI, String value, long subscriptionId, boolean matchReference, String reference) {
    return false;
  }
}
