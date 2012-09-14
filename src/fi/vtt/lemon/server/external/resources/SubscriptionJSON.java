package fi.vtt.lemon.server.external.resources;

import fi.vtt.lemon.server.external.RestPlugin;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fi.vtt.lemon.server.external.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_SUBSCRIPTION)
public class SubscriptionJSON {
  private final static Logger log = new Logger(SubscriptionJSON.class);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response subscribeToBM(@HeaderParam("authorization") String authHeader, JSONObject req) {
    log.debug("SubcribeToBM request received, " + req);
    RestPlugin restPlugin = RestPlugin.getInstance();

    if (restPlugin.check(authHeader)) {
      try {
        restPlugin.subscribeBaseMeasure(req.getString(MEASURE_URI));
      } catch (JSONException e) {
        log.error("Failed to parse subscribe JSON", e);
        return Response.serverError().build();
      }
    } else {
      return Response.serverError().build();
    }
    return Response.ok(MediaType.APPLICATION_JSON).build();
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  public Response unsubscribeToBM(@HeaderParam("authorization") String authHeader, JSONObject req) {
    log.debug("UnsubcribeToBM request received, " + req);
    RestPlugin restPlugin = RestPlugin.getInstance();
    if (restPlugin.check(authHeader)) {
      try {
        restPlugin.unSubscribeToBM(req.getString(MEASURE_URI));
      } catch (JSONException e) {
        log.error("Failed to parse unsubscribe JSON", e);
        return Response.serverError().build();
      }
    } else {
      return Response.serverError().build();
    }
    return Response.ok(MediaType.APPLICATION_JSON).build();
  }
}
