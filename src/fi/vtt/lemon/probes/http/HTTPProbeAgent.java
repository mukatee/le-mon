/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probes.http;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probe.ServerClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import osmo.common.log.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Grabs a base measure from a HTTP request posted at the address http://host/port/uri.
 * The base measure information is given as configuration for this class, and the content is the body of the http request.
 * Done properly, this should read a list of such elements from a configuration file.
 *
 * @author Teemu Kanstren
 */
public class HTTPProbeAgent implements Filter {
  private final static Logger log = new Logger(HTTPProbeAgent.class);
  private final ServerClient server;
  private final String measureURI;
  private final int precision;

  public HTTPProbeAgent(String measureURI, int precision) {
    this.server = new ServerClient(Config.getString(RabbitConst.BROKER_ADDRESS, "::1"));
    this.measureURI = measureURI;
    this.precision = precision;
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    resp.setContentType("text/plain");
    String name = req.getRequestURL().toString();
    int index = name.lastIndexOf('/');
    //todo: error handling if < 0 index is received
    //we take the name of the base measure, that which is after the last "/" character
    name = name.substring(index+1);

    // Get response data.
    BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
    String str;
    String content = "";
    //read the body, the base measure content
    while (null != ((str = br.readLine()))) {
      content += str;
    }
    br.close();

    server.measurement(measureURI, precision, content);
    log.debug("Received BM '"+name+"' from '"+req.getRemoteAddr()+" with value:"+content);

    PrintWriter out = resp.getWriter();
    out.println("hello:"+name+" -- "+content);
    out.close();
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {
  }

  public static void main(String[] args) throws Exception {
    int port = Config.getInt(RabbitConst.HTTP_PORT, 11111);
    Server server = new Server(port);
    //define what should be the HTTP root of our application
    ServletContextHandler handler = new ServletContextHandler();
    handler.setContextPath("/");
    //Wrap the servlet so we can deploy in programmatically with Jetty
    ServletHolder holder = new ServletHolder(new ServletContainer());
    //and install the servlet on the HTTP root under this path
    handler.addServlet(holder, Config.getString(RabbitConst.HTTP_URI, "/http-probe/"));
    server.setHandler(handler);
    server.start();
  }
}
