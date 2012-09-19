/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

import java.util.Comparator;

public class BMComparator implements Comparator<BMDesc> {
  private final String sortKey;
  private final boolean ascending;

  public BMComparator(String sortKey, boolean ascending) {
    this.sortKey = sortKey;
    this.ascending = ascending;
  }

  public int compare(BMDesc a1, BMDesc a2) {
    int result = 0;
    if (sortKey.equals("bmId")) {
      result = (int)(a1.getBmId()-a2.getBmId());
    }
    if (sortKey.equals("deviceId")) {
      result = (int)(a1.getDeviceId()-a2.getDeviceId());
    }
    if (sortKey.equals("clazz")) {
      result = a1.getClazz().compareTo(a2.getClazz());
    }
    if (sortKey.equals("name")) {
      result = a1.getName().compareTo(a2.getName());
    }
    if (sortKey.equals("description")) {
      result = a1.getDescription().compareTo(a2.getDescription());
    }
    if (!ascending) {
      result = 0 - result;
    }
    return result;
  }
}
