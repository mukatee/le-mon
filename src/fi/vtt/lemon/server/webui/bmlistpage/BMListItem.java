/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.bmlistpage;

/**
 * @author Teemu Kanstren
 */
public class BMListItem {
  private final String measureURI;
  private final String value;

  public BMListItem(String measureURI, String value) {
    this.measureURI = measureURI;
    if (value != null && value.length() > 102) {
      value = value.substring(0, 100);
    }
    this.value = value;
  }

  public String getMeasureURI() {
    return measureURI;
  }

  public String getValue() {
    return value;
  }
}
