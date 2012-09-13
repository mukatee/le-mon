package fi.vtt.lemon.server.rest.external;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import fi.vtt.lemon.server.rest.external.resources.AvailabilityJSON;
import fi.vtt.lemon.server.rest.external.resources.BaseMeasureJSON;
import fi.vtt.lemon.server.rest.external.resources.FrameworkInfoJSON;
import fi.vtt.lemon.server.rest.external.resources.HistoryJSON;
import fi.vtt.lemon.server.rest.external.resources.ProbeConfigurationJSON;
import fi.vtt.lemon.server.rest.external.resources.ProbesJSON;
import fi.vtt.lemon.server.rest.external.resources.RegisterJSON;
import fi.vtt.lemon.server.rest.external.resources.ShutdownJSON;
import fi.vtt.lemon.server.rest.external.resources.SubscriptionJSON;
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
    resources.add(RegisterJSON.class);
    resources.add(ProbesJSON.class);
    resources.add(BaseMeasureJSON.class);
    resources.add(HistoryJSON.class);
    resources.add(SubscriptionJSON.class);
    resources.add(ProbeConfigurationJSON.class);
    resources.add(FrameworkInfoJSON.class);
    resources.add(AvailabilityJSON.class);
    return resources;
  }

  public void start() throws Exception {
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
    server.join();
  }
}