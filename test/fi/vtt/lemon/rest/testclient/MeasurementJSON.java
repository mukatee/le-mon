/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.rest.testclient;

import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.Registry;
import fi.vtt.lemon.server.Value;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static fi.vtt.lemon.server.external.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_BM_RESULT)
public class MeasurementJSON {
  private final static Logger log = new Logger(MeasurementJSON.class);
  private static List<Value> values = new ArrayList<>();

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response measurement(@HeaderParam("authorization") String authHeader, JSONObject req) {
    log.debug("Measure received, " + req);
    System.out.println("MMMM:"+req);

    try {
      String measureURI = req.getString(MEASURE_URI);
      long time = req.getLong(TIME);
      String data = req.getString(VALUE);
      int precision = req.getInt(PRECISION);
      Value value = new Value(measureURI, precision, data, time);
      values.add(value);
    } catch (JSONException e) {
      log.error("Failed to parse measure JSON", e);
      return Response.serverError().build();
    }
    return Response.ok(MediaType.APPLICATION_JSON).build();
  }

  public static List<Value> getValues() {
    return values;
  }
}
