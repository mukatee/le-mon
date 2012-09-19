/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.eventlistpage;

import java.util.Comparator;

/**
 * @author Teemu Kanstren
 */
public class EventComparator implements Comparator<ServerEvent> {
  private final String sortKey;
  private final boolean ascending;

  public EventComparator(String sortKey, boolean ascending) {
    this.sortKey = sortKey;
    this.ascending = ascending;
  }

  public int compare(ServerEvent a1, ServerEvent a2) {
    int result = 0;
    if (sortKey.equals("time")) {
      result = a1.getTimeString().compareTo(a2.getTimeString());
    }
    if (sortKey.equals("message")) {
      result = a1.getMessage().compareTo(a2.getMessage());
    }
    if (!ascending) {
      result = 0 - result;
    }
    return result;
  }

}