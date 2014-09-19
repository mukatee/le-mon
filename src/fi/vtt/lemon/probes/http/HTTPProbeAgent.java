/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probes.http;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.MsgConst;
import fi.vtt.lemon.probe.ProbeServer;
import fi.vtt.lemon.probe.tasks.BMSender;
import fi.vtt.lemon.server.MessagePooler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import osmo.common.log.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Grabs a base measure from a HTTP request posted at the address http://host/port/uri.
 * The base measure information is given as configuration for this class, and the content is the body of the http request.
 * A new class is to be instantiated for different probes.
 * The address is the hostname+post+"http-probe". For example, http://localhost:11111/http-probe
 *
 * @author Teemu Kanstren
 */
public class HTTPProbeAgent implements Filter {
  private final static Logger log = new Logger(HTTPProbeAgent.class);
  /** The ID for the measure provided. */
  private final String measureURI;
  /** Precision of the probe providing the measure. */
  private final int precision;

  /**
   * Constructor.
   * 
   * @param measureURI The measure ID for the probe posting the data.
   * @param precision The precision of the probe providing the measures.
   */
  public HTTPProbeAgent(String measureURI, int precision) {
    this.measureURI = measureURI;
    this.precision = precision;
  }

  /**
   * The servlet filter method for capturing the posted data.
   * 
   * @param servletRequest The HTTP request.
   * @param resp Provides access to the HTTP response.
   * @param chain Chain of filters from the Servlet framework.
   * @throws IOException Sure.
   * @throws ServletException If there is a problem.
   */
  public void doFilter(ServletRequest servletRequest, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    resp.setContentType("text/plain");

    // Get response data.
    BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
    String str;
    String content = "";
    //read the body, the base measure content
    while (null != ((str = br.readLine()))) {
      content += str;
    }
    br.close();

    MessagePooler pooler = ProbeServer.getPooler();
    pooler.schedule(new BMSender(measureURI, precision, content));
    log.debug("Received BM '"+measureURI+"' from '"+req.getRemoteAddr()+" with value:"+content);

    PrintWriter out = resp.getWriter();
    //write back some silly response to allow testing this agent through the browser or other tools
    out.println("hello:"+measureURI+" -- "+content);
    out.close();
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {
  }

  /**
   * Start it up here, sets up the filters etc.
   * 
   * @param args The command line parameters.
   * @throws Exception IF there is an error.
   */
  public static void main(String[] args) throws Exception {
    int port = Config.getInt(MsgConst.HTTP_PORT, 11111);
    Server server = new Server(port);
    //define what should be the HTTP root of our application
    ServletContextHandler handler = new ServletContextHandler();
    handler.setContextPath("/");
    //Wrap the servlet so we can deploy in programmatically with Jetty
//    ServletHolder holder = new ServletHolder(new ServletContainer());
    //and install the servlet on the HTTP root under this path
//    handler.addServlet(holder, Config.getString(RabbitConst.HTTP_URI, "/http-probe/"));
    server.setHandler(handler);
    server.start();
  }
}
