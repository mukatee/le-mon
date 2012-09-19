/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

/**
 * @author Teemu Kanstren
 */
public class WebUISession extends WebSession {
  public WebUISession(Request request) {
    super(request);
  }
}
