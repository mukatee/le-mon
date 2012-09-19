/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui;

import fi.vtt.lemon.server.webui.frameset.BodyFrame;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.file.Folder;

/**
 * The main Application used by Wicket to create the web ui.
 *
 * @author Teemu Kanstren
 */
public class WebUIWicketApplication extends WebApplication {
  private Folder uploadFolder = null;

  public Class<? extends Page> getHomePage() {
    return BodyFrame.class;
//    return ProbeListPage.class;
  }

  @Override
  public Session newSession(Request request, Response response) {
    return new WebUISession(request);
  }

  @Override
  protected void init() {
    super.init();
    getResourceSettings().setThrowExceptionOnMissingResource(false);

    uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads");
    // Ensure folder exists
    uploadFolder.mkdirs();

//    mountBookmarkablePage("/multi", MultiUploadPage.class);
//    mountBookmarkablePage("/single", UploadPage.class);
  }

  public Folder getUploadFolder() {
    return uploadFolder;
  }
}
