package fi.vtt.lemon.server.external.resources;

import fi.vtt.lemon.server.Registry;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static fi.vtt.lemon.server.external.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_AVAILABILITY)
public class AvailabilityJSON {
  private static Logger log = new Logger(AvailabilityJSON.class);

  @GET
  public Response getBMAvailability() {
    List<String> bms = Registry.getRegistry().getAvailableBM();
    JSONArray root = new JSONArray();
    if (!bms.isEmpty()) {
      for (String measureURI : bms) {
        JSONObject bm = new JSONObject();
        try {
          bm.put(MEASURE_URI, measureURI);
        } catch (JSONException e) {
          log.error("Failed to create list of available BM", e);
          return Response.serverError().build();
        }
        root.put(bm);
      }
    }

    return Response.ok(root.toString(), MediaType.APPLICATION_JSON).build();
  }
}
