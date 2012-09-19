/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import fi.vtt.lemon.server.external.resources.AvailabilityJSON;
import fi.vtt.lemon.server.external.resources.FrameworkInfoJSON;
import fi.vtt.lemon.server.external.resources.HistoryJSON;
import fi.vtt.lemon.server.external.resources.ShutdownJSON;
import fi.vtt.lemon.server.external.resources.SubscribeJSON;
import fi.vtt.lemon.server.external.resources.UnsubscribeJSON;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/** @author Teemu Kanstren */
public class JerseyApp extends Application {
  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new HashSet<>();
    resources.add(ShutdownJSON.class);
    resources.add(HistoryJSON.class);
    resources.add(SubscribeJSON.class);
    resources.add(UnsubscribeJSON.class);
    resources.add(FrameworkInfoJSON.class);
    resources.add(AvailabilityJSON.class);
    return resources;
  }

  public void start() throws Exception {
//    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

//    serverSocketChannel.socket().bind(new InetSocketAddress(9999));
//    serverSocketChannel.accept();
    Server server = new Server(11111);
    //define what should be the HTTP root of our application
    ServletContextHandler handler = new ServletContextHandler();
    handler.setContextPath("/");
    //Wrap the servlet so we can deploy in programmatically with Jetty
    ServletHolder holder = new ServletHolder(new ServletContainer());
    //tell Jersey where to find the REST application configuration
    holder.setInitParameter("javax.ws.rs.Application", JerseyApp.class.getName());
    //and install the servlet on the HTTP root under this path
    handler.addServlet(holder, "/rest/*");
    server.setHandler(handler);
    server.start();
  }
}