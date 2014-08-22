package fi.vtt.lemon.server.external.rest;

import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.Registry;
import fi.vtt.lemon.server.internal.ServerToProbe;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static fi.vtt.lemon.server.external.RESTConst.*;
import static fi.vtt.lemon.RabbitConst.*;


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

    System.out.println("received:"+msg);

    log.debug("Add measure request received "+msg);
    ServerToProbe stp = LemonServer.getProbeClient();
    Registry registry = LemonServer.getRegistry();

    if (registry.check(auth)) {
      try {
        JSONObject json = new JSONObject(msg);
        stp.addMeasurement(json.getString(MEASURE_URI), json.getString(PARAM_CONFIG));
      } catch (JSONException e) {
        log.error("Failed to parse subscribe JSON", e);
        return;
      }
    } else {
      return;
    }
    return;
  }
}