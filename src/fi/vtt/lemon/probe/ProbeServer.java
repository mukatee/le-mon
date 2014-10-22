package fi.vtt.lemon.probe;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.server.MessagePooler;
import fi.vtt.lemon.server.rest.RESTConst;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Teemu Kanstren
 */
public class ProbeServer {
  private static final Map<String, Probe> probes = new HashMap<>();
  private static MessagePooler pooler = new MessagePooler();
  
  private ProbeServer() {
  }
  
  public static void addProbe(Probe probe) {
    probes.put(probe.getMeasureURI(), probe);
  }
  
  public static void removeProbe(Probe probe) {
    probes.remove(probe.getMeasureURI());
  }

  public static Probe probeFor(String measureURI) {
    return probes.get(measureURI);
  }

  public static MessagePooler getPooler() {
    return pooler;
  }

  public static boolean check(String auth) {
    return true;
  }
  
  public static void start() throws Exception {
    new JettyStarter().start();
  }
  
  public static String getServerAgentAddress() {
    int serverPort = Config.getInt(RESTConst.REST_SERVER_SERVER_PORT, 11112);
    return Config.getString(RESTConst.REST_SERVER_SERVER_URL, "http://[::1]:"+serverPort);
  }

  public static String getProbeAddress() {
    int serverPort = Config.getInt(RESTConst.REST_PROBE_SERVER_PORT, 11112);
    return Config.getString(RESTConst.REST_PROBE_SERVER_URL, "http://[::1]:"+serverPort);
  }

  public static void registerIfNeeded(String measureURI) {
    
  }
}
