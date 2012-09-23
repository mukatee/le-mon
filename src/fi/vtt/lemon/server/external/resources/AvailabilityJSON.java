/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external.resources;

import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.Registry;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static fi.vtt.lemon.server.external.RESTConst.*;

/**
 * Provides a REST+JSON resource to list the set of available measurements. 
 * 
 * @author Teemu Kanstren 
 */
@Path(PATH_AVAILABILITY)
public class AvailabilityJSON {
  private static Logger log = new Logger(AvailabilityJSON.class);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBMAvailability() {
    List<String> bms = LemonServer.getRegistry().getAvailableBM();
    JSONObject root = new JSONObject();
    JSONArray array = new JSONArray();
    if (!bms.isEmpty()) {
      for (String measureURI : bms) {
        JSONObject bm = new JSONObject();
        try {
          bm.put(MEASURE_URI, measureURI);
        } catch (JSONException e) {
          log.error("Failed to create list of available BM", e);
          return Response.serverError().build();
        }
        array.put(bm);
      }
      try {
        root.put(AVAILABILITY_ARRAY, array);
      } catch (JSONException e) {
        log.error("Failed to build JSON", e);
        return Response.serverError().build();
      }
    }

    return Response.ok(root.toString(), MediaType.APPLICATION_JSON).build();
  }
}
