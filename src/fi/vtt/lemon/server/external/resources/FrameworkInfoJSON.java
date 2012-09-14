package fi.vtt.lemon.server.external.resources;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fi.vtt.lemon.server.external.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_FRAMEWORK_INFO)
public class FrameworkInfoJSON {
  private final static Logger log = new Logger(FrameworkInfoJSON.class);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response requestFrameworkInfo() {
    JSONObject json = new JSONObject();
    try {
      json.put(INFO, "LE-MON v0.1");
    } catch (JSONException e) {
      log.error("Error while creating response value", e);
      return Response.serverError().build();
    }
    return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
  }

}
