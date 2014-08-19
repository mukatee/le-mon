/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external.resources;

import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.Persistence;
import fi.vtt.lemon.server.Registry;
import fi.vtt.lemon.server.Value;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static fi.vtt.lemon.server.external.RESTConst.*;

/**
 * Provides a REST+JSON resource to ask for given measurement history.
 * Currently not properly implemented. TODO: implement it properly. 
 *
 * @author Teemu Kanstren 
 */
@Path(PATH_HISTORY)
public class HistoryJSON {
  private static Logger log = new Logger(HistoryJSON.class);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response requestHistory(@HeaderParam("authorization") String authHeader, JSONObject req) {
    Registry registry = LemonServer.getRegistry();
    log.debug("History request");
    JSONArray root = new JSONArray();

    if (registry.check(authHeader)) {
      // read measurements history from database according to requested time interval
      // and push it to the client
      try {
        long start = req.getLong(START_TIME);
        long end = req.getLong(END_TIME);
        Collection<String> bmIds = new ArrayList<>();
        JSONArray array = req.getJSONArray(BM_LIST);
        int length = array.length();
        for (int i = 0 ; i < length ; i++) {
          JSONObject jom = array.getJSONObject(i);
          String id = jom.getString(MEASURE_URI);
          bmIds.add(id);
        }
        List<Value> measurements = LemonServer.getHistory(start, end, bmIds);
        // conversion to json
        for (Value value : measurements) {
          JSONObject obj = new JSONObject();
          root.put(obj);
          obj.put(MEASURE_URI, value.getMeasureURI());
          obj.put(TIME, value.getTime().getTime());
          obj.put(VALUE, value.valueString());
          array.put(obj);
        }
      } catch (JSONException e) {
        log.error("Error while creating JSON for measurement history", e);
        return Response.serverError().build();
        //TODO: better error messages
//        return Response.status(Response.Status.BAD_REQUEST).build();
      }
    } else {
      log.debug("User does not have an authenticated session.");
      return Response.serverError().build();
    }

    return Response.ok(root.toString(), MediaType.APPLICATION_JSON).build();
  }
}
