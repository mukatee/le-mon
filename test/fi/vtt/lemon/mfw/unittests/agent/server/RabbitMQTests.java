package fi.vtt.lemon.mfw.unittests.agent.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import fi.vtt.lemon.probe.plugins.xmlrpc.ServerClient;
import fi.vtt.lemon.server.internal.InternalServer;
import org.junit.Test;

/** @author Teemu Kanstren */
public class RabbitMQTests {
  @Test
  public void sendMsg() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare("QUEUE_NAME", false, false, false, null);
    String message = "Hello World!";
    channel.basicPublish("", "QUEUE_NAME", null, message.getBytes());
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }

  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare("QUEUE_NAME", false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
  }

  @Test
  public void endToEnd() throws Exception {
    System.out.println("starting");
    InternalServer server = new InternalServer();
    System.out.println("server created");
    server.start();
    System.out.println("server started");
    ServerClient client = new ServerClient("localhost");
    System.out.println("client created");
    client.measurement("MURI", 1, "Hi");
    System.out.println("msg delivered");
    Thread.sleep(5000);
  }
}
