package fi.vtt.lemon.probe.rest;

import fi.vtt.lemon.probe.Probe;
import fi.vtt.lemon.probe.ProbeServer;
import fi.vtt.lemon.probe.tasks.AddMeasureProcessor;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    System.out.println("received:"+msg);

    log.debug("Add measure request received "+msg);

    if (ProbeServer.check(auth)) {
      try {
        JSONObject json = new JSONObject(msg);
        String measureURI = json.getString(MEASURE_URI);
        Probe probe = ProbeServer.probeFor(measureURI);
        Runnable task = new AddMeasureProcessor(probe, json);
        ProbeServer.getPooler().schedule(task);
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