/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server;

import osmo.common.log.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author Teemu Kanstren
 */
public class Registry {
  private final static Logger log = new Logger(Registry.class);
  //key = measureURI
  private final Collection<String> availableBM = new HashSet<>();
  private Collection<String> subscriptionRegistry = new HashSet<>();

  public void addBM(String measureURI) {
    availableBM.add(measureURI);
  }
  
  //get current list of registered probes
  public synchronized List<String> getAvailableBM() {
    List<String> result = new ArrayList<>();
    result.addAll(availableBM);
    return result;
  }

  //parse target type from a measureURI
  public String parseTargetType(String measureURI) {
    int[] bounds = getTargetTypeBounds(measureURI);
    return measureURI.substring(bounds[0], bounds[1]);
  }

  //get start and end index of target type inside given measureURI
  private int[] getTargetTypeBounds(String measureURI) {
    int si = measureURI.indexOf("//");
    if (si <= 0) {
      throw new IllegalArgumentException("Invalid measure URI:" + measureURI);
    }
    si += 2;
    int ei = measureURI.indexOf("/", si);
    if (si <= 0) {
      throw new IllegalArgumentException("Invalid measure URI:" + measureURI);
    }
    return new int[]{si, ei};
  }

  //parse target name from given measureURI
  public String parseTargetName(String measureURI) {
    int[] bounds = getTargetTypeBounds(measureURI);
    //end of type + "/"
    int si = bounds[1] + 1;
    int ei = measureURI.indexOf("/", si);
    if (si <= 0) {
      throw new IllegalArgumentException("Invalid measure URI:" + measureURI);
    }
    return measureURI.substring(si, ei);
  }

  public void addSubscription(String measureURI) {
    String msg = "New measurement subscription URI:" + measureURI;
    //TODO: Nämä uusix
//    bb.process(new ServerEvent(System.currentTimeMillis(), EventType.NEW_SUBSCRIPTION, "SAC " + sacId, msg));
    subscriptionRegistry.add(measureURI);
  }

  public void removeSubscription(String measureURI) {
    String msg = "Removed measurement subscription id:" + measureURI;
//    bb.process(new ServerEvent(System.currentTimeMillis(), EventType.DELETE_SUBSCRIPTION, "SAC " + sacId, msg));
    subscriptionRegistry.remove(measureURI);
  }

  public boolean check(String authHeader) {
    return true;
  }
}
