package fi.vtt.lemon.unittests.agent.server;

import org.eclipse.jetty.server.Server;
import org.testng.annotations.Test;

/** @author Teemu Kanstren */
public class JettyTests {
  @Test
  public void startup() throws Exception {
    Server server = new Server(8080);
    server.start();
    server.join();
  }
}
