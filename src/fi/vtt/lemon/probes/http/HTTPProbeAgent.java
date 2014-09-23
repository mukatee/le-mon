package fi.vtt.lemon.probes.http;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.MsgConst;
import fi.vtt.lemon.probe.ProbeServer;
import fi.vtt.lemon.probe.tasks.BMSender;
import fi.vtt.lemon.probe.tasks.RegistrationSender;
import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.MessagePooler;
import fi.vtt.lemon.server.Registry;
import fi.vtt.lemon.server.data.ProbeDescription;
import fi.vtt.lemon.server.rest.RESTConst;
import fi.vtt.lemon.server.rest.probe.BMValue;
import fi.vtt.lemon.server.tasks.MeasurementProcessor;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import osmo.common.TestUtils;
import osmo.common.log.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static fi.vtt.lemon.MsgConst.*;

/**
 * Grabs a base measure from a HTTP request posted at the address http://host/port/uri.
 * The base measure information is given as configuration for this class, and the content is the body of the http request.
 * A new class is to be instantiated for different probes.
 * The address is the hostname+port+"http-probe". For example, http://localhost:11111/http-probe
 *
 * @author Teemu Kanstren
 */
public class HTTPProbeAgent extends HttpServlet {
  private final static Logger log = new Logger(HTTPProbeAgent.class);
  private final String measureURI;
  private final int precision;

  public HTTPProbeAgent(String measureURI, int precision) {
    this.measureURI = measureURI;
    this.precision = precision;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  private void showPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    System.out.println("processing msg...");
    // Read from request
    StringBuilder buffer = new StringBuilder();
    BufferedReader reader = req.getReader();
    String line;
    while ((line = reader.readLine()) != null) {
      buffer.append(line);
    }
    String data = buffer.toString();

    log.debug("Measurement data received:"+data);
    System.out.println("got data:"+data);

    MessagePooler pooler = ProbeServer.getPooler();

    log.debug("Received BM '"+measureURI+"' from '"+req.getRemoteAddr()+" with value:"+data);
    pooler.schedule(new BMSender(measureURI, precision, data));
    PrintWriter out = resp.getWriter();
    //write back some silly response to allow testing this agent through the browser or other tools
    out.println("hello:"+measureURI+" -- "+data);
    out.close();
  }

  /**
   * Start it up here, sets up the filters etc.
   * 
   * @param args The command line parameters.
   * @throws Exception IF there is an error.
   */
  public static void main(String[] args) throws Exception {
    int port = Config.getInt(MsgConst.HTTP_PORT, 22222);
    Server server = new Server(port);
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");


    String httpConfig = TestUtils.readFile("http_probes.json", "UTF8");
    JSONArray array = new JSONArray(httpConfig);
    int length = array.length();
    MessagePooler pooler = ProbeServer.getPooler();
    for (int i = 0 ; i < length ; i++) {
      JSONObject probej = array.getJSONObject(i);
      String url = probej.getString("url");
      String ip = probej.getString("ip");
      String measureURI = probej.getString("measure_uri");
      int precision = probej.getInt("precision");
      pooler.schedule(new RegistrationSender(measureURI, precision));
      context.addServlet(new ServletHolder(new HTTPProbeAgent(measureURI, precision)), "/" + url);
    }

    server.setHandler(context);
    server.start();

  }
}
