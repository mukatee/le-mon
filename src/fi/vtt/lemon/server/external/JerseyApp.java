package fi.vtt.lemon.server.external;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import fi.vtt.lemon.server.external.resources.AvailabilityJSON;
import fi.vtt.lemon.server.external.resources.FrameworkInfoJSON;
import fi.vtt.lemon.server.external.resources.HistoryJSON;
import fi.vtt.lemon.server.external.resources.ShutdownJSON;
import fi.vtt.lemon.server.external.resources.SubscriptionJSON;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.ws.rs.core.Application;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/** @author Teemu Kanstren */
public class JerseyApp extends Application {
  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new HashSet<>();
    resources.add(ShutdownJSON.class);
    resources.add(HistoryJSON.class);
    resources.add(SubscriptionJSON.class);
    resources.add(FrameworkInfoJSON.class);
    resources.add(AvailabilityJSON.class);
    return resources;
  }

  public void start() throws Exception {
    InetSocketAddress addr = new InetSocketAddress("0:0:0:0", 54640);
    Server server = new Server(addr);
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