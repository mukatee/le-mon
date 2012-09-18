package fi.vtt.lemon.server.internal;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import static fi.vtt.lemon.server.external.RESTConst.*;

/** @author Teemu Kanstren */

@Path("/bm/{bmid}")
public class BaseMeasureJSON {
  private static Logger log = new Logger(BaseMeasureJSON.class);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public void setBMResult(@PathParam("bmid") String bmId, JSONObject req) {
    log.debug("-----------BMValue received");
    log.debug("-----------bmId: " + bmId);
    try {
      log.debug("-----------time: " + req.get(TIME));
      log.debug("-----------value: " + req.get(VALUE));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
