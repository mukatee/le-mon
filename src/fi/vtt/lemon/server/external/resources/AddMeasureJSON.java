/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external.resources;

import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.Registry;
import fi.vtt.lemon.server.internal.ServerToProbe;
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
import static fi.vtt.lemon.RabbitConst.*;

/**
 * Provides a REST+JSON resource for the client to add measurement to probe. 
 *
 * @author Teemu Kanstren 
 */
@Path(PATH_ADD_MEASURE)
public class AddMeasureJSON {
  private final static Logger log = new Logger(AddMeasureJSON.class);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addMeasure(@HeaderParam("authorization") String authHeader, JSONObject req) {
    log.debug("Add measure request received, " + req);
    ServerToProbe stp = LemonServer.getProbeClient();
    Registry registry = LemonServer.getRegistry();

    if (registry.check(authHeader)) {
      try {
        stp.addMeasurement(req.getString(MEASURE_URI), req.getString(PARAM_CONFIG));
      } catch (JSONException e) {
        log.error("Failed to parse subscribe JSON", e);
        return Response.serverError().build();
      }
    } else {
      return Response.serverError().build();
    }
    return Response.ok(MediaType.APPLICATION_JSON).build();
  }

}
