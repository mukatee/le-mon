package fi.vtt.lemon.server;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.server.rest.RESTConst;
import fi.vtt.lemon.server.rest.client.*;
import fi.vtt.lemon.server.rest.probe.BMValue;
import fi.vtt.lemon.server.rest.probe.Event;
import fi.vtt.lemon.server.rest.probe.Register;
import fi.vtt.lemon.server.rest.probe.UnRegister;
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
    int port = Config.getInt(RESTConst.REST_SERVER_SERVER_PORT, 11111);
    Server server = new Server(port);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    context.addServlet(new ServletHolder(new StaticPageServlet()),"/pages/availability");
    context.addServlet(new ServletHolder(new StaticPageServlet()),"/pages/info");
    context.addServlet(new ServletHolder(new StaticPageServlet()),"/pages/latest");
    context.addServlet(new ServletHolder(new StaticPageServlet()),"/pages/probes");

    context.addServlet(new ServletHolder(new AddMeasure()), RESTConst.PATH_ADD_MEASURE);
    context.addServlet(new ServletHolder(new Availability()), RESTConst.PATH_AVAILABILITY);
    context.addServlet(new ServletHolder(new FWInfo()), RESTConst.PATH_FRAMEWORK_INFO);
    context.addServlet(new ServletHolder(new History()), RESTConst.PATH_HISTORY);
    context.addServlet(new ServletHolder(new RemoveMeasure()), RESTConst.PATH_REMOVE_MEASURE);
    context.addServlet(new ServletHolder(new Shutdown()), RESTConst.PATH_SHUTDOWN);
    context.addServlet(new ServletHolder(new Subscribe()), RESTConst.PATH_SUBSCRIBE);
    context.addServlet(new ServletHolder(new Unsubscribe()), RESTConst.PATH_UNSUBSCRIBE);

    context.addServlet(new ServletHolder(new BMValue()), RESTConst.PATH_BM_RESULT);
    context.addServlet(new ServletHolder(new Event()), RESTConst.PATH_EVENT);
    context.addServlet(new ServletHolder(new Register()), RESTConst.PATH_REGISTER);
    context.addServlet(new ServletHolder(new UnRegister()), RESTConst.PATH_UNREGISTER);

    server.start();
  }
}
