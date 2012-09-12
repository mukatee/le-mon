package fi.vtt.lemon.server.rest.external.resources;

import fi.vtt.lemon.server.rest.external.RestPlugin;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

import static fi.vtt.lemon.server.rest.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_REGISTER)
public class RegisterJSON {
  private final static Logger log = new Logger(RegisterJSON.class);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response registerClient(@HeaderParam("authorization") String authHeader, JSONObject req) {
    log.debug("registering client " + req);
    RestPlugin restPlugin = RestPlugin.getInstance();

    JSONObject json = new JSONObject();

    if (restPlugin.isAuthorized(authHeader)) {
      try {
        String name = req.getString(NAME);
        String endpoint = req.getString(ENDPOINT);
        if (name != null && endpoint != null) {
          String id = restPlugin.registerClient(authHeader, name, endpoint);
          log.debug("Session: " + id);
          json.put("id", id);
        } else {
          return Response.serverError().build();
        }
      } catch (JSONException e) {
        log.error("Failed to register client", e);
        return Response.serverError().build();
      }
    } else {
      return Response.serverError().build();
    }
    return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
  }
}
