package fi.vtt.lemon.server.rest.external.resources;

import fi.vtt.lemon.common.ProbeConfiguration;
import fi.vtt.lemon.server.rest.external.RestPlugin;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static fi.vtt.lemon.server.rest.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_CONFIGURATION)
public class ProbeConfigurationJSON {
  private final static Logger log = new Logger(ProbeConfigurationJSON.class);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProbeConfiguration(@HeaderParam("authorization") String authHeader, @PathParam(PROBE_ID) String probeId) {

    log.debug("probeConfiguration request: " + probeId);
    RestPlugin restPlugin = RestPlugin.getInstance();
    JSONArray root = new JSONArray();

    if (restPlugin.isAlive(authHeader)) {
      // get configuration
      try {
        Collection<ProbeConfiguration> pcs = restPlugin.getProbeConfiguration(Long.parseLong(probeId));
        JSONObject obj = new JSONObject();
        for (ProbeConfiguration p : pcs) {
          obj.put(p.getName(), p.getValue());
        }
      } catch (JSONException e) {
        log.error("Error while creating JSON for probe configuration", e);
        return Response.serverError().build();
      }
    } else {
      log.debug("User does not have an authenticated session.");
      return Response.serverError().build();
    }
    return Response.ok(root.toString(), MediaType.APPLICATION_JSON).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response setProbeConfiguration(@HeaderParam("authorization") String authHeader, @PathParam(PROBE_ID) String probeId, JSONObject req) {
    log.debug("probeConfiguration change request: " + probeId + ", config=" + req);
    RestPlugin restPlugin = RestPlugin.getInstance();

    if (restPlugin.isAlive(authHeader)) {
      Map<String, String> parameters = null;
      try {
        parameters = new HashMap<>();
        JSONArray array = req.getJSONArray(VALUES);
        int count = array.length();
        for (int i = 0 ; i < count ; i++) {
          JSONObject obj = array.getJSONObject(i);
          parameters.put(obj.getString(NAME), obj.getString(VALUE));
        }
      } catch (JSONException e) {
        log.error("Error while creating JSON for probe configuration", e);
        return Response.serverError().build();
      }

      boolean success = restPlugin.setProbeConfiguration(Long.parseLong(probeId), parameters);

      if (!success) {
        log.debug("Failed to set probe configuration");
        return Response.serverError().build();
      }
    } else {
      log.debug("User does not have an authenticated session.");
      return Response.serverError().build();
    }
    return Response.ok(MediaType.APPLICATION_JSON).build();
  }
}
