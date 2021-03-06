package le.mon.server.rest.client;

import le.mon.server.LemonServer;
import le.mon.server.registry.Registry;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static le.mon.server.rest.RESTConst.*;


/**
 * @author Teemu Kanstren
 */
public class Unsubscribe extends HttpServlet {
  private static Logger log = new Logger(Unsubscribe.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  private void showPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    log.debug("Unsubcribe request received, " + req);

    // Read from request
    StringBuilder buffer = new StringBuilder();
    BufferedReader reader = req.getReader();
    String line;
    while ((line = reader.readLine()) != null) {
      buffer.append(line);
    }
    String data = buffer.toString();

    log.debug("Latest request:"+data);

    Registry registry = LemonServer.getRegistry();
    JSONArray root = new JSONArray();

//    if (registry.check(auth)) {
      try {
        JSONObject json = new JSONObject(data);
        registry.removeSubscription(json.getString(MEASURE_URI));
      } catch (JSONException e) {
        log.error("Error while creating JSON for unsubscribe request", e);
        return;
      }
//    } else {
//      log.debug("User does not have an authenticated session.");
//      return;
//    }
    PrintWriter out = resp.getWriter();
    out.write(root.toString());
    return;
  }
}