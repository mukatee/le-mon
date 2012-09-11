package fi.vtt.lemon.server;

import fi.vtt.lemon.server.shared.ServerAgent;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.List;
import java.util.Map;

/**
 * Acts as a server for probe-agents to communicate with.
 *
 * @author Teemu Kanstren
 */
public class InternalServer extends ServerResource {
  public static void main(String[] args) throws Exception {
    // Create the HTTP server and listen on port 8182
    new Server(Protocol.HTTP, 8182, InternalServer.class).start();
  }

  @Get
  public String toString() {
    return "hello, world";
  }
  public boolean measurement(long time, String measureURI, int precision, String value, long subscriptionId) {
    return false;
  }

  public void event(long time, String type, String source, String message, long subscriptionId) {
  }

  public long register(Map<String, String> properties) {
    return 0;
  }

  public boolean keepAlive(long probeId) {
    return false;
  }

  public void unregister(long probeId) {
  }

  public void checkSubscriptions(long probeId, List<Long> subscriptionIds) {
  }

  public boolean BMReport(long time, String measureURI, String value, long subscriptionId, boolean matchReference, String reference) {
    return false;
  }
}
