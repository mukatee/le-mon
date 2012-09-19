/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.bmresultspage;

import java.util.Comparator;

/**
 * @author Teemu Kanstren
 */
public class ValueComparator implements Comparator<BMResult> {
  private final String sortKey;
  private final boolean ascending;

  public ValueComparator(String sortKey, boolean ascending) {
    this.sortKey = sortKey;
    this.ascending = ascending;
  }

  public int compare(BMResult result1, BMResult result2) {
    int result = 0;
    if (sortKey.equals("bm_id")) {
      result = (int) (result1.getBm_id()- result2.getBm_id());
    }
    if (sortKey.equals("device_id")) {
      result = (int) (result1.getDevice_id() - result2.getDevice_id());
    }
    if (sortKey.equals("value")) {
      result = result1.getValue().compareTo(result2.getValue());
    }
    if (sortKey.equals("time")) {
      result = result1.getTime().compare(result2.getTime());
    }
    if (!ascending) {
      result = 0 - result;
    }
    return result;
  }

}