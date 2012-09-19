/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.historypage;

import fi.vtt.lemon.server.Value;

import java.util.Comparator;

/**
 * @author Teemu Kanstren
 */
public class ValueComparator implements Comparator<Value> {
  private final String sortKey;
  private final boolean ascending;

  public ValueComparator(String sortKey, boolean ascending) {
    this.sortKey = sortKey;
    this.ascending = ascending;
  }

  public int compare(Value v1, Value v2) {
    int result = 0;
    if (sortKey.equals("measureURI")) {
      result = v1.getMeasureURI().compareTo(v2.getMeasureURI());
    }
    if (sortKey.equals("precision")) {
      result = v1.getPrecision() - (v2.getPrecision());
    }
    if (sortKey.equals("value")) {
      result = v1.valueString().compareTo(v2.valueString());
    }
    if (!ascending) {
      result = 0 - result;
    }
    return result;
  }

}