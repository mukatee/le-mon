package fi.vtt.lemon.server.rest.external.resources;

import fi.vtt.lemon.server.rest.external.RestPlugin;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fi.vtt.lemon.server.rest.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_FRAMEWORK_INFO)
public class FrameworkInfoJSON {
  private final static Logger log = new Logger(FrameworkInfoJSON.class);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response requestFrameworkInfo() {
    RestPlugin restPlugin = RestPlugin.getInstance();
    JSONObject json = new JSONObject();
    try {
      json.put(INFO, restPlugin.getFrameworkInfo());
    } catch (JSONException e) {
      log.error("Error while creating response value", e);
      return Response.serverError().build();
    }
    return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
  }

}
