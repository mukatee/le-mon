package fi.vtt.lemon.server.rest.client;

import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.data.Value;
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
import java.util.List;

import static fi.vtt.lemon.server.rest.RESTConst.*;


/**
 * @author Teemu Kanstren
 */
public class Latest extends HttpServlet {
  private static Logger log = new Logger(Latest.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  private void showPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Read from request
    StringBuilder buffer = new StringBuilder();
    BufferedReader reader = req.getReader();
    String line;
    while ((line = reader.readLine()) != null) {
      buffer.append(line);
    }
    String data = buffer.toString();

    log.debug("Latest request:"+data);
    JSONArray root = new JSONArray();

    try {
      JSONObject json = new JSONObject(data);
      int count = json.getInt(COUNT);
      List<Value> measurements = LemonServer.getLatest(count);
      for (Value value : measurements) {
        JSONObject obj = new JSONObject();
        root.put(obj);
        obj.put(VALUE, value.valueString());
        obj.put(MEASURE_URI, value.getMeasureURI());
        obj.put(TIME, value.getTimeFormatted());
      }
    } catch (JSONException e) {
      log.error("Failed to build JSON", e);
      return;
    }
    PrintWriter out = resp.getWriter();
    out.write(root.toString());
  }
}