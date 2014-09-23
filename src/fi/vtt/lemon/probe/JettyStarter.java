package fi.vtt.lemon.probe;

import fi.vtt.lemon.Config;

import fi.vtt.lemon.probe.rest.AddMeasure;
import fi.vtt.lemon.probe.rest.RemoveMeasure;
import fi.vtt.lemon.server.rest.RESTConst;
import fi.vtt.lemon.server.webui.pages.StaticPageServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author Teemu Kanstren
 */
public class JettyStarter {
  public static void main(String[] args) throws Exception {
    new JettyStarter().start();
  }
    
  public void start() throws Exception {
    int port = Config.getInt(RESTConst.REST_PROBE_SERVER_PORT, 11113);
    Server server = new Server(port);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    context.addServlet(new ServletHolder(new StaticPageServlet("probe-info.html")),"/pages/info");

    context.addServlet(new ServletHolder(new AddMeasure()), RESTConst.PATH_ADD_MEASURE);
    context.addServlet(new ServletHolder(new RemoveMeasure()), RESTConst.PATH_REMOVE_MEASURE);

    server.start();
  }
}
