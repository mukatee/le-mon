/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.bmlistpage;

import java.util.Comparator;

/**
 * @author Teemu Kanstren
 */
public class BMComparator implements Comparator<BMListItem> {
  private final String sortKey;
  private final boolean ascending;

  public BMComparator(String sortKey, boolean ascending) {
    this.sortKey = sortKey;
    this.ascending = ascending;
  }

  public int compare(BMListItem a1, BMListItem a2) {
    int result = 0;
    if (sortKey.equals("measureURI")) {
      result = a1.getMeasureURI().compareTo(a2.getMeasureURI());
    }
    if (sortKey.equals("value")) {
      result = a1.getValue().compareTo(a2.getValue());
    }
    if (!ascending) {
      result = 0 - result;
    }
    return result;
  }
}
