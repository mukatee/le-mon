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

package fi.vtt.lemon.server.rest.resources;

import osmo.common.log.Logger;
import fi.vtt.lemon.server.rest.RestPlugin;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;


@Path("/client/bmsubscription/{bmid}")
public class SubscribeBaseMeasureResource {
  private final static Logger log = new Logger(SubscribeBaseMeasureResource.class);
  private RestPlugin restPlugin;

  @POST
  @Consumes(MediaType.APPLICATION_XML)
  public void subscribeToBM(@HeaderParam("authorization") String authHeader, @PathParam("bmid") String bmId, SubscriptionRequest request) {
    log.debug("SubcribeToBM request received, bmId: " + bmId + ", interval: " + request.getInterval());
    restPlugin = RestPlugin.getInstance();

    if ( restPlugin.isAlive( authHeader ) )
    {
      restPlugin.subscribeBaseMeasure(authHeader, Long.parseLong(bmId), request.getInterval());
    }
    else
    {
      throw new WebApplicationException( Status.BAD_REQUEST );
    }
  }

  @DELETE
  public void unsubscribeToBM(@HeaderParam("authorization") String authHeader, @PathParam("bmid") String bmId) {
    log.debug("UnsubcribeToBM request received, bmId: " + bmId);
    restPlugin = RestPlugin.getInstance();
    
    if ( restPlugin.isAlive( authHeader ) )
    {
      restPlugin.unsubscribeBaseMeasure(authHeader, Long.parseLong(bmId));
    }
    else
    {
      throw new WebApplicationException( Status.BAD_REQUEST );
    }
  }
}   