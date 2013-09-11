/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.server.external.JerseyApp;
import fi.vtt.lemon.server.external.RestClient;
import fi.vtt.lemon.server.internal.InternalServer;
import fi.vtt.lemon.server.internal.ServerToProbe;

import java.util.Collection;
import java.util.List;

/** 
 * Starts up the le-mon server-agent. This is the part that provides a REST interface for the clients interested
 * in getting measurement data from probes. It is also the part that interfaces with all the probes to collect
 * the measurements they provide (through RabbitMQ).
 * 
 * @author Teemu Kanstren 
 */
public class LemonServer {
  /** For maintaining list of connected probes, available measures, etc. */
  private static Registry registry;
  /** For persisting measurement results etc. Currently not implemented. */
  private static Persistence persistence;
  /** For making callbacks to the client, that is to provide the measurement results that are subscribed to, when available. */
  private static RestClient client;
  private static ServerToProbe probeClient;

  /**
   * Global access to the registry for different server elements. 
   * Global variables are great, just in case you disagree, the excuse is the following.
   * Various objects are created externally such as Jersey request processing objects, which require this type of
   * global access point to the server state.
   * 
   * @return The registry.
   */
  public synchronized static Registry getRegistry() {
    if (registry == null) {
      registry = new Registry();
    }
    return registry;
  }

  /**
   * Startup.
   * 
   * @param args Command line parameters.
   * @throws Exception If errors..
   */
  public static void main(String[] args) throws Exception {
    registry = new Registry();
    persistence = new Persistence();
    client = new RestClient();
    probeClient = new ServerToProbe(Config.getString(RabbitConst.BROKER_ADDRESS, "::1"));
    JerseyApp jersey = new JerseyApp();
    jersey.start();
    InternalServer internal = new InternalServer();
    internal.start();
  }

  /**
   * When a measurement is received.
   * 
   * @param measureURI The identifier of the measurement.
   * @param time The time of the measurement.
   * @param precision The precision of probe that performed the measure.
   * @param value The measurement value.
   */
  public static void measurement(String measureURI, long time, int precision, String value) {
    registry.addBM(measureURI);
    Value v = new Value(measureURI, precision, value, time);
    if (registry.isSubscribed(measureURI)) {
      client.measurement(v);
    }
    persistence.store(v);
  }

  /**
   * To provide access to measurement history where interesting. Currently not implemented.
   * 
   * @param start Start time for requested history.
   * @param end End time for requested history.
   * @param bmIds The list of measurements that are requested (history).
   * @return The list of measurements matching the given criteria.
   */
  public static List<Value> getHistory(long start, long end, Collection<Long> bmIds) {
    return null;
  }

  public static ServerToProbe getProbeClient() {
    return probeClient;
  }
}
