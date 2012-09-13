/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.lemon.server.rest.external.resources;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;
import fi.vtt.lemon.server.rest.external.RestPlugin;
import fi.vtt.lemon.server.shared.datamodel.BMDescription;
import fi.vtt.lemon.server.shared.datamodel.ProbeDescription;
import fi.vtt.lemon.server.shared.datamodel.TargetDescription;

import static fi.vtt.lemon.server.rest.RESTConst.*;

@Path(PATH_PROBE_INFO)
public class ProbesJSON {
  private final static Logger log = new Logger(ProbesJSON.class);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProbes(@HeaderParam("authorization") String authHeader) {
    RestPlugin restPlugin = RestPlugin.getInstance();
    log.debug("get probes, auth: " + authHeader);

    JSONObject root = new JSONObject();

    if (restPlugin.isAlive(authHeader)) {
      JSONArray targets = new JSONArray();
      JSONArray bms = new JSONArray();
      JSONArray probes = new JSONArray();

      Collection<TargetDescription> tds = restPlugin.getTargets();
      List<BMDescription> bmds = restPlugin.getAvailableBMList();
      List<ProbeDescription> pds = restPlugin.getProbes();

      try {
        for (TargetDescription td : tds) {
          JSONObject target = new JSONObject();
          target.put(NAME, td.getTargetName());
          target.put(TYPE, td.getTargetType());
          targets.put(target);
        }

        for (BMDescription bmd : bmds) {
          JSONObject bm = new JSONObject();
          bm.put(BM_ID, bmd.getBmId());
          bm.put(BM_NAME, bmd.getBmName());
          bm.put(BM_CLASS, bmd.getBmClass());
          bm.put(BM_DESC, bmd.getBmDescription());
          bm.put(DATA_TYPE, bmd.getDataType().name());
          bms.put(bm);
        }

        for (ProbeDescription pd : pds) {
          JSONObject probe = new JSONObject();
          probe.put(PROBE_ID, pd.getProbeId());
          probe.put(NAME, pd.getProbeName());
          probe.put(BM_ID, pd.getBm().getBmId());
          probes.put(probe);
        }
        root.put(TARGET_LIST, targets);
        root.put(BM_LIST, bms);
        root.put(PROBE_LIST, probes);
      } catch (JSONException e) {
        log.error("Failed to create list of available BM", e);
        return Response.serverError().build();
      }
    } else {
      //TODO: change default path, reduce complexity, log these failures..
      return Response.serverError().build();
    }

    return Response.ok(root.toString(), MediaType.APPLICATION_JSON).build();
  }

}   