package fi.vtt.lemon.server.rest.client;

import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.MessagePooler;
import fi.vtt.lemon.server.registry.Registry;
import fi.vtt.lemon.server.data.ProbeDescription;
import fi.vtt.lemon.server.rest.RESTConst;
import fi.vtt.lemon.server.rest.RestClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static fi.vtt.lemon.MsgConst.*;
import static fi.vtt.lemon.server.rest.RESTConst.*;


/**
 * @author Teemu Kanstren
 */
public class AddMeasure extends HttpServlet {
  private static Logger log = new Logger(AddMeasure.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  private void showPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String auth = req.getParameter("auth");
    String msg = req.getParameter("msg");

//    System.out.println("received:"+msg);

    log.debug("Add measure request received "+msg);
    Registry registry = LemonServer.getRegistry();
    MessagePooler pooler = LemonServer.getPooler();

    if (registry.check(auth)) {
      try {
        JSONObject json = new JSONObject(msg);
        String measureURI = json.getString(MEASURE_URI);
        String measurementConfig = json.getString(PARAM_CONFIG);
        pooler.schedule(new Runner(measureURI, measurementConfig));
      } catch (JSONException e) {
        log.error("Failed to parse subscribe JSON", e);
        return;
      }
    } else {
      return;
    }
    return;
  }
 
  private class Runner implements Runnable {
    private final String measureURI;
    private final String measurementConfig;

    private Runner(String measureURI, String measurementConfig) {
      this.measureURI = measureURI;
      this.measurementConfig = measurementConfig;
    }

    @Override
    public void run() {
      try {
        JSONObject json = new JSONObject();
        json.put(MSGTYPE, MSG_ADD_MEASURE);
        json.put(PARAM_TIME, System.currentTimeMillis());
        json.put(PARAM_MEASURE_URI, measureURI);
        json.put(PARAM_CONFIG, measurementConfig);
        Registry registry = LemonServer.getRegistry();
        ProbeDescription probe = registry.probeFor(measureURI);
        RestClient.sendPost(probe.getUrl() + RESTConst.PATH_ADD_MEASURE, json);
      } catch (JSONException e) {
        log.error("Failed to send measurement", e);
      }
    }
  }
}