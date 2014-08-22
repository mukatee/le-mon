package fi.vtt.lemon.server.webui;

import fi.vtt.lemon.server.webui.pages.StaticPageServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author Teemu Kanstren
 */
public class JettyStarter {
  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    context.addServlet(new ServletHolder(new StaticPageServlet()),"/availability");

    server.start();
    server.join();
  }
}
