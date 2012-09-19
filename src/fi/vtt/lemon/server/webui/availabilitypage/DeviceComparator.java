/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

import java.util.Comparator;

public class DeviceComparator implements Comparator<DeviceDesc> {
  private final String sortKey;
  private final boolean ascending;

  public DeviceComparator(String sortKey, boolean ascending) {
    this.sortKey = sortKey;
    this.ascending = ascending;
  }

  public int compare(DeviceDesc a1, DeviceDesc a2) {
    int result = 0;
    if (sortKey.equals("deviceId")) {
      result = (int)(a1.getDeviceId()-a2.getDeviceId());
    }    
    if (sortKey.equals("name")) {
      result = a1.getName().compareTo(a2.getName());
    }
    if (sortKey.equals("type")) {
      result = a1.getType().compareTo(a2.getType());
    }
    if (!ascending) {
      result = 0 - result;
    }
    return result;
  }
}