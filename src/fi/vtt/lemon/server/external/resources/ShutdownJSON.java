/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external.resources;

import osmo.common.log.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static fi.vtt.lemon.server.external.RESTConst.*;

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
