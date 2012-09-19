/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.unittests.agent.server;

import fi.vtt.lemon.server.external.JerseyApp;
import org.junit.Test;

/** @author Teemu Kanstren */
public class JerseyTests {
  @Test
  public void runJersey() throws Exception {
    JerseyApp app = new JerseyApp();
    app.start();
  }
}
