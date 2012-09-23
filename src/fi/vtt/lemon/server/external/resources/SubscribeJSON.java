/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external.resources;

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

/**
 * Provides a REST+JSON resource for the client to subscribe to a measurement. 
 *
 * @author Teemu Kanstren 
 */
@Path(PATH_SUBSCRIBE)
public class SubscribeJSON {
  private final static Logger log = new Logger(SubscribeJSON.class);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response subscribeToBM(@HeaderParam("authorization") String authHeader, JSONObject req) {
    log.debug("SubcribeToBM request received, " + req);
    Registry registry = LemonServer.getRegistry();

    if (registry.check(authHeader)) {
      try {
        registry.addSubscription(req.getString(MEASURE_URI));
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
