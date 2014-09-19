package fi.vtt.lemon.server.rest.client;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static fi.vtt.lemon.server.rest.RESTConst.*;


/**
 * @author Teemu Kanstren
 */
public class FWInfo extends HttpServlet {
  private static Logger log = new Logger(FWInfo.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  private void showPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    JSONObject json = new JSONObject();
    try {
      json.put(INFO, "LE-MON v0.1");
    } catch (JSONException e) {
      log.error("Error while creating response value", e);
      return;
    }
    PrintWriter out = resp.getWriter();
    out.write(json.toString());
    return;
  }
}