/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.rest.testclient;

import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.Registry;
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

import static fi.vtt.lemon.server.external.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_BM_RESULT)
public class MeasurementJSON {
  private final static Logger log = new Logger(MeasurementJSON.class);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response measurement(@HeaderParam("authorization") String authHeader, JSONObject req) {
    log.debug("Measure received, " + req);
    System.out.println("MMMM:"+req);

    try {
      req.getString(MEASURE_URI);
      req.getString(TIME);
      req.getString(VALUE);
    } catch (JSONException e) {
      log.error("Failed to parse measure JSON", e);
      return Response.serverError().build();
    }
    return Response.ok(MediaType.APPLICATION_JSON).build();
  }

}
