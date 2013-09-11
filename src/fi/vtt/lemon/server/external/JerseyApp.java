/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.server.external.resources.AddMeasureJSON;
import fi.vtt.lemon.server.external.resources.AvailabilityJSON;
import fi.vtt.lemon.server.external.resources.FrameworkInfoJSON;
import fi.vtt.lemon.server.external.resources.HistoryJSON;
import fi.vtt.lemon.server.external.resources.RemoveMeasureJSON;
import fi.vtt.lemon.server.external.resources.ShutdownJSON;
import fi.vtt.lemon.server.external.resources.SubscribeJSON;
import fi.vtt.lemon.server.external.resources.UnsubscribeJSON;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/** 
 * Main starting point for a Jersey REST application.
 * 
 * @author Teemu Kanstren 
 */
public class JerseyApp extends Application {
  /**
   * Defines the resources that are accessible through the REST interface.
   * 
   * @return The set of resource for Jersey.
   */
  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new HashSet<>();
    resources.add(ShutdownJSON.class);
    resources.add(HistoryJSON.class);
    resources.add(SubscribeJSON.class);
    resources.add(UnsubscribeJSON.class);
    resources.add(FrameworkInfoJSON.class);
    resources.add(AvailabilityJSON.class);
    resources.add(AddMeasureJSON.class);
    resources.add(RemoveMeasureJSON.class);
    return resources;
  }

  /**
   * Starts up Jersey and makes the web-services (REST) available.
   * Starts Jetty internally and binds Jersey as a servlet to provide the interface.
   * 
   * @throws Exception
   */
  public void start() throws Exception {
    int port = Config.getInt(RESTConst.REST_SERVER_PORT, 11111);
    Server server = new Server(port);
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