package fi.vtt.lemon.rest;

import fi.vtt.lemon.server.data.Value;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static fi.vtt.lemon.server.external.RESTConst.*;

/** @author Teemu Kanstren */
public class FakeClient extends HttpServlet {
  private final static Logger log = new Logger(FakeClient.class);
  private static List<Value> values = new ArrayList<>();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  private void showPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    BufferedReader reader = req.getReader();
    String line = "";
    StringBuilder sb = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      sb.append(line);
    }
    System.out.println("received:" + sb);

    try {
      JSONObject json = new JSONObject(sb.toString());
      String measureURI = json.getString(MEASURE_URI);
      long time = json.getLong(TIME);
      String data = json.getString(VALUE);
      int precision = json.getInt(PRECISION);
      Value value = new Value(measureURI, precision, data, new Date(time));
      values.add(value);
    } catch (JSONException e) {
      log.error("Failed to parse measure JSON", e);
    }
  }


  public static List<Value> getValues() {
    return values;
  }
}
