package fi.vtt.lemon.server.rest.external.resources;

import fi.vtt.lemon.server.rest.external.RestPlugin;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fi.vtt.lemon.server.rest.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_BASE_MEASURE)
public class BaseMeasureJSON {
  private final static Logger log = new Logger(BaseMeasureJSON.class);

  //TODO: Change this to return the latest value
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  public Response requestBM(@HeaderParam("authorization") String authHeader, @PathParam(BM_ID) String bmId) {
    RestPlugin restPlugin = RestPlugin.getInstance();
    JSONObject json = new JSONObject();
    if (restPlugin.isAlive(authHeader)) {
      log.debug("BM request received, bmId: " + bmId);
      restPlugin.requestBaseMeasure(authHeader, Long.parseLong(bmId));
      try {
        json.put(VALUE, "TBD");
      } catch (JSONException e) {
        log.error("Error while creating response value", e);
        return Response.serverError().build();
      }
    } else {
      return Response.serverError().build();
    }
    return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
  }
}
