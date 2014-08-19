package fi.vtt.lemon.server.webui.jettytest;

import org.apache.wicket.protocol.http.ContextParamWebApplicationFactory;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.http.WicketServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author Teemu Kanstren
 */
public class WicketHello {
  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);

    ServletContextHandler sch = new ServletContextHandler(ServletContextHandler.SESSIONS);
    ServletHolder sh = new ServletHolder(WicketServlet.class);
    sh.setInitParameter(ContextParamWebApplicationFactory.APP_CLASS_PARAM, WicketTestApp.class.getName());
    sh.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
    //sh.setInitParameter("wicket.configuration", "deployment");
    sch.addServlet(sh, "/*");
    server.setHandler(sch);

    server.start();
    server.join();
  }
}
