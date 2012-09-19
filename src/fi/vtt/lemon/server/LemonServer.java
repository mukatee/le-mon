/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server;

import fi.vtt.lemon.server.external.JerseyApp;
import fi.vtt.lemon.server.internal.InternalServer;

import java.util.Collection;
import java.util.List;

/** @author Teemu Kanstren */
public class LemonServer {
  private static Registry registry;
  private static Persistence persistence;

  public synchronized static Registry getRegistry() {
    if (registry == null) {
      registry = new Registry();
    }
    return registry;
  }

  public static void main(String[] args) throws Exception {
    registry = new Registry();
    persistence = new Persistence();
    JerseyApp jersey = new JerseyApp();
    jersey.start();
    InternalServer internal = new InternalServer();
    internal.start();
  }
  
  public static void measurement(String measureURI, long time, int precision, String value) {
    registry.addBM(measureURI);
    Value v = new Value(measureURI, precision, value, time);
    persistence.store(v);
  }

  public static List<Value> getHistory(long start, long end, Collection<Long> bmIds) {
    return null;
  }
}
