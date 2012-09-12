package fi.vtt.lemon.server.rest.external.resources;

import fi.vtt.lemon.server.rest.external.RestPlugin;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fi.vtt.lemon.server.rest.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_SUBSCRIPTION)
public class SubscriptionJSON {
  private final static Logger log = new Logger(SubscriptionJSON.class);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response subscribeToBM(@HeaderParam("authorization") String authHeader, @PathParam("bmid") String bmId, JSONObject req) {
    log.debug("SubcribeToBM request received, bmId: " + bmId + ", " + req);
    RestPlugin restPlugin = RestPlugin.getInstance();

    if (restPlugin.isAlive(authHeader)) {
      try {
        restPlugin.subscribeBaseMeasure(authHeader, Long.parseLong(bmId), req.getLong(INTERVAL));
      } catch (JSONException e) {
        log.error("Failed to create list of available BM", e);
        return Response.serverError().build();
      }
    } else {
      return Response.serverError().build();
    }
    return Response.ok(MediaType.APPLICATION_JSON).build();
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  public Response unsubscribeToBM(@HeaderParam("authorization") String authHeader, @PathParam("bmid") String bmId) {
    log.debug("UnsubcribeToBM request received, bmId: " + bmId);
    RestPlugin restPlugin = RestPlugin.getInstance();
    if (restPlugin.isAlive(authHeader)) {
      restPlugin.unsubscribeBaseMeasure(authHeader, Long.parseLong(bmId));
    } else {
      return Response.serverError().build();
    }
    return Response.ok(MediaType.APPLICATION_JSON).build();
  }
}
