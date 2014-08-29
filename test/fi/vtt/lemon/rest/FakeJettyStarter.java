package fi.vtt.lemon.rest;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.server.external.RESTConst;
import fi.vtt.lemon.server.external.rest.AddMeasure;
import fi.vtt.lemon.server.external.rest.Availability;
import fi.vtt.lemon.server.external.rest.FWInfo;
import fi.vtt.lemon.server.external.rest.History;
import fi.vtt.lemon.server.external.rest.RemoveMeasure;
import fi.vtt.lemon.server.external.rest.Shutdown;
import fi.vtt.lemon.server.external.rest.Subscribe;
import fi.vtt.lemon.server.external.rest.Unsubscribe;
import fi.vtt.lemon.server.webui.pages.StaticPageServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author Teemu Kanstren
 */
public class FakeJettyStarter {
  public static void main(String[] args) throws Exception {
    new FakeJettyStarter().start();
  }
    
  public void start() throws Exception {
    int port = Config.getInt(RESTConst.REST_CLIENT_PORT, 11113);
    Server server = new Server(port);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    context.addServlet(new ServletHolder(new FakeClient()), "/client/value");

    server.start();
  }
}
