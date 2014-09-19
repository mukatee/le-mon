package fi.vtt.lemon.rest;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.server.rest.RESTConst;
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
    int port = Config.getInt(RESTConst.REST_CLIENT_PORT, 11114);
    Server server = new Server(port);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    context.addServlet(new ServletHolder(new FakeClient()), "/client/value");

    server.start();
  }
}
