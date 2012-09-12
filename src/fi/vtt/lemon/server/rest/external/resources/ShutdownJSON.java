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

import osmo.common.log.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static fi.vtt.lemon.server.rest.RESTConst.*;

/**
 * @author Teemu Kanstren
 */
@Path(PATH_SHUTDOWN)
public class ShutdownJSON {
  private final static Logger log = new Logger(ShutdownJSON.class);

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String shutdown() {
    log.debug("SHUTDOWN initiated via REST service");
    System.exit(0);
    return "OK, shutting down. (this should never show since system should have exited already..)";
  }
}
