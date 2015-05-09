package le.mon.probe;

import le.mon.Config;
import le.mon.probe.rest.AddMeasure;
import le.mon.probe.rest.RemoveMeasure;
import le.mon.server.rest.RESTConst;
import le.mon.server.webui.pages.StaticPageServlet;
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

    context.addServlet(new ServletHolder(new StaticPageServlet("probe-info.html")), "/pages/info");

    context.addServlet(new ServletHolder(new AddMeasure()), RESTConst.PATH_ADD_MEASURE);
    context.addServlet(new ServletHolder(new RemoveMeasure()), RESTConst.PATH_REMOVE_MEASURE);

    server.start();
  }
}
