package le.mon.server;

import le.mon.Config;
import le.mon.server.data.ProbeDescription;
import le.mon.server.data.Value;
import le.mon.server.persistence.Persistence;
import le.mon.server.registry.Registry;
import le.mon.server.rest.PostToClientTask;
import le.mon.server.rest.RESTConst;
import le.mon.Config;
import le.mon.server.data.ProbeDescription;
import le.mon.server.data.Value;
import le.mon.server.registry.Registry;
import le.mon.server.rest.PostToClientTask;
import osmo.common.log.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Starts up the le-mon server-agent. This is the part that provides a REST interface for the clients interested
 * in getting measurement data from probes. It is also the part that interfaces with all the probes to collect
 * the measurements they provide (through RabbitMQ).
 *
 * @author Teemu Kanstren
 */
public class LemonServer {
  private final static Logger log = new Logger(LemonServer.class);
  /** For maintaining list of connected probes, available measures, etc. */
  private static Registry registry;
  /** For persisting measurement results etc. Currently not implemented. */
  private static Persistence persistence;
  /** For sending messages in a separate thread, not blocking the current thread. */
  private static MessagePooler pooler;
  /** Address of the client (e.g., MVS) where we can send messages to. URL/IP+port. */
  private static String client;

  /**
   * Global access to the registry for different server elements.
   * Global variables are great, just in case you disagree, the excuse is the following.
   * Various objects are created externally such as REST request processing objects, which require this type of
   * global access point to the server state.
   *
   * @return The registry.
   */
  public synchronized static Registry getRegistry() {
    return registry;
  }

  /**
   * Startup.
   *
   * @param args Command line parameters.
   * @throws Exception If errors..
   */
  public static void main(String[] args) throws Exception {
    persistence = new Persistence();
    registry = new Registry(persistence);
    client = Config.getString(RESTConst.REST_CLIENT_URL, "http://localhost:11114/client");
    pooler = new MessagePooler();
    JettyStarter start = new JettyStarter();
    start.start();
  }

  public static void reset() {
    persistence = new Persistence();
    registry = new Registry(persistence);
  }

  public static void register(String url, String measureURI) {
    registry.addProbe(new ProbeDescription(url, measureURI));
  }

  public static void unregister(String url, String measureURI) {
    registry.removeProbe(new ProbeDescription(url, measureURI));
  }

  /**
   * When a measurement is received.
   *
   * @param measureURI The identifier of the measurement.
   * @param time The time of the measurement.
   * @param value The measurement value.
   */
  public static void measurement(String measureURI, long time, String value) {
    if (!registry.isRegistered(measureURI)) {
      log.warn("Trying to provide measurement for unregistered probe:"+measureURI);
      return;
    }
    Value v = new Value(measureURI, value, new Date(time));
    if (registry.isSubscribed(measureURI)) {
      PostToClientTask task = new PostToClientTask(client, v);
      pooler.schedule(task);
    }
    persistence.store(v);
  }

  public static String getClient() {
    return client;
  }

  /**
   * To provide access to measurement history where interesting. Currently not implemented.
   *
   * @param start Start time for requested history.
   * @param end End time for requested history.
   * @param bmIds The list of measurements that are requested (history).
   * @return The list of measurements matching the given criteria.
   */
  public static List<Value> getHistory(long start, long end, Collection<String> bmIds) {
    return persistence.getValues(bmIds);
  }

  public static MessagePooler getPooler() {
    return pooler;
  }

  public static void setPooler(MessagePooler pooler) {
    LemonServer.pooler = pooler;
  }

  public static List<Value> getLatest(int count) {
    return persistence.getLatest(count);
  }

  public static void keepAlive(String measureURI) {
    registry.keepAlive(measureURI);
  }
}
