/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

import java.util.Comparator;

public class ProbeComparator implements Comparator<ProbeDesc> {
  private final String sortKey;
  private final boolean ascending;

  public ProbeComparator(String sortKey, boolean ascending) {
    this.sortKey = sortKey;
    this.ascending = ascending;
  }

  public int compare(ProbeDesc a1, ProbeDesc a2) {
    int result = 0;
    if (sortKey.equals("probeId")) {
      result = (int)(a1.getProbeId()-a2.getProbeId());
    }
    if (sortKey.equals("bmId")) {
      result = (int)(a1.getBmId()-a2.getBmId());
    }
    if (sortKey.equals("name")) {
      result = a1.getName().compareTo(a2.getName());
    }    
    if (!ascending) {
      result = 0 - result;
    }
    return result;
  }
}
