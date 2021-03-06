package le.mon.server.rest.client;

import le.mon.server.LemonServer;
import le.mon.server.registry.Registry;
import le.mon.server.data.Value;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static le.mon.server.rest.RESTConst.*;


/**
 * @author Teemu Kanstren
 */
public class History extends HttpServlet {
  private static Logger log = new Logger(History.class);

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

    Registry registry = LemonServer.getRegistry();
    log.debug("History request");
    JSONArray root = new JSONArray();

//    if (registry.check(auth)) {
      // read measurements history from database according to requested time interval
      // and push it to the client
      try {
        JSONObject json = new JSONObject(data);
        long start = json.getLong(START_TIME);
        long end = json.getLong(END_TIME);
        Collection<String> bmIds = new ArrayList<>();
        JSONArray array = json.getJSONArray(BM_LIST);
        int length = array.length();
        for (int i = 0 ; i < length ; i++) {
          JSONObject jom = array.getJSONObject(i);
          String id = jom.getString(MEASURE_URI);
          bmIds.add(id);
        }
        List<Value> measurements = LemonServer.getHistory(start, end, bmIds);
        // conversion to json
        for (Value value : measurements) {
          JSONObject obj = new JSONObject();
          root.put(obj);
          obj.put(MEASURE_URI, value.getMeasureURI());
          obj.put(TIME, value.getTime().getTime());
          obj.put(VALUE, value.valueString());
          array.put(obj);
        }
      } catch (JSONException e) {
        log.error("Error while creating JSON for measurement history", e);
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