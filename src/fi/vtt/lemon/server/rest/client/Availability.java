package fi.vtt.lemon.server.rest.client;

import fi.vtt.lemon.server.LemonServer;
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

import static fi.vtt.lemon.server.rest.RESTConst.*;


/**
 * @author Teemu Kanstren
 */
public class Availability extends HttpServlet {
  private static Logger log = new Logger(Availability.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  private void showPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    List<String> bms = LemonServer.getRegistry().getAvailableBM();
    log.debug("requested bm list:"+ bms.size());
    System.out.println("requested bm list:"+ bms.size());
    
    JSONObject root = new JSONObject();
    JSONArray array = new JSONArray();
    if (!bms.isEmpty()) {
      for (String measureURI : bms) {
        JSONObject bm = new JSONObject();
        try {
          bm.put(MEASURE_URI, measureURI);
        } catch (JSONException e) {
          log.error("Failed to create list of available BM", e);
          return;
        }
        array.put(bm);
      }
      try {
        root.put(AVAILABILITY_ARRAY, array);
      } catch (JSONException e) {
        log.error("Failed to build JSON", e);
        return;
      }

      PrintWriter out = resp.getWriter();
      out.write(root.toString());
    }
  }
}