package fi.vtt.lemon.server.external.rest;

import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.Registry;
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

import static fi.vtt.lemon.server.external.RESTConst.*;


/**
 * @author Teemu Kanstren
 */
public class Subscribe extends HttpServlet {
  private static Logger log = new Logger(Subscribe.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  private void showPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    log.debug("SubcribeToBM request received, " + req);
    String auth = req.getParameter("auth");
    String msg = req.getParameter("msg");

    Registry registry = LemonServer.getRegistry();
    JSONArray root = new JSONArray();

    if (registry.check(auth)) {
      try {
        JSONObject json = new JSONObject(msg);
        registry.addSubscription(json.getString(MEASURE_URI));
      } catch (JSONException e) {
        log.error("Error while creating JSON for measurement history", e);
        return;
      }
    } else {
      log.debug("User does not have an authenticated session.");
      return;
    }
    PrintWriter out = resp.getWriter();
    out.write(root.toString());
    return;
  }
}