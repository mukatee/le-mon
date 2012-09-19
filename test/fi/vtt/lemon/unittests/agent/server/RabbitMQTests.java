/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.unittests.agent.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import fi.vtt.lemon.probe.ServerClient;
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
//    ServerSocket socket = new ServerSocket(5672);
//    socket.accept();

    ConnectionFactory factory = new ConnectionFactory();
//    factory.setHost("localhost");
    factory.setHost("::1");
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
    ServerClient client = new ServerClient("::1");
    System.out.println("client created");
    client.measurement("MURI", 1, "Hi");
    System.out.println("msg delivered");
    Thread.sleep(5000);
  }
}
