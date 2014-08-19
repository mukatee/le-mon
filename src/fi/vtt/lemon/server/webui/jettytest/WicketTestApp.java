package fi.vtt.lemon.server.webui.jettytest;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * @author Teemu Kanstren
 */
public class WicketTestApp extends WebApplication {
  @Override
  public Class<? extends Page> getHomePage() {
    return WicketTestHomePage.class;
  }
}
