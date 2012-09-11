package fi.vtt.lemon.server.rest.external.resources;

import fi.vtt.lemon.server.rest.external.RestPlugin;
import fi.vtt.lemon.server.shared.datamodel.BMDescription;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static fi.vtt.lemon.server.rest.RESTConst.*;

/** @author Teemu Kanstren */
@Path("/client/availability")
public class AvailabilityJSON {
  private static Logger log = new Logger(AvailabilityJSON.class);

  @GET
  public Response getBMAvailability() {
    List<BMDescription> bms = RestPlugin.getInstance().getAvailableBMList();
    JSONArray root = new JSONArray();
    if (!bms.isEmpty()) {
      for (BMDescription bmDesc : bms) {
        JSONObject bm = new JSONObject();
        try {
          //TODO: all strings to Const.java
          bm.put(BM_ID, bmDesc.getBmId());
          bm.put(BM_CLASS, bmDesc.getBmClass());
          bm.put(BM_NAME, bmDesc.getBmName());
          bm.put(BM_DESC, bmDesc.getBmDescription());
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
