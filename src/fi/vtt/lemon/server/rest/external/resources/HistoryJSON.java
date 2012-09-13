package fi.vtt.lemon.server.rest.external.resources;

import fi.vtt.lemon.server.rest.external.RestPlugin;
import fi.vtt.lemon.server.shared.datamodel.Value;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static fi.vtt.lemon.server.rest.RESTConst.*;

/** @author Teemu Kanstren */
@Path(PATH_HISTORY)
public class HistoryJSON {
  private static Logger log = new Logger(HistoryJSON.class);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response requestHistory(@HeaderParam("authorization") String authHeader, JSONObject req) {
    RestPlugin restPlugin = RestPlugin.getInstance();
    log.debug("History request");
    JSONArray root = new JSONArray();

    if (restPlugin.isAlive(authHeader)) {
      // read measurements history from database according to requested time interval
      // and push it to the client
      try {
        long start = req.getLong(START_TIME);
        long end = req.getLong(END_TIME);
        Collection<Long> bmIds = new ArrayList<>();
        JSONArray array = req.getJSONArray(BM_LIST);
        List<Value> measurements = restPlugin.getHistory(start, end, bmIds);
        // conversion to json
        for (Value value : measurements) {
          JSONObject obj = new JSONObject();
          root.put(obj);
          obj.put(BM_ID, value.getBm().getBmId());
          obj.put(TIME, value.getTime().getTime());
          obj.put(VALUE, value.valueString());
        }
      } catch (JSONException e) {
        log.error("Error while creating JSON for measurement history", e);
        return Response.serverError().build();
      }
    } else {
      log.debug("User does not have an authenticated session.");
      return Response.serverError().build();
    }

    return Response.ok(root.toString(), MediaType.APPLICATION_JSON).build();
  }
}
