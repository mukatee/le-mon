/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.bmresultspage;

import javax.xml.datatype.XMLGregorianCalendar;

public class BMResult {
  private final long bm_id;
  private final long device_id;
  private final String value;
  private final XMLGregorianCalendar time;
  private final boolean error;
  
  public BMResult(long bmId, long deviceId, String value,
      XMLGregorianCalendar time, boolean error) {
    super();
    this.bm_id = bmId;
    this.device_id = deviceId;
    this.value = value;
    this.time = time;
    this.error = error;
  }

  public long getBm_id() {
    return bm_id;
  }

  public long getDevice_id() {
    return device_id;
  }

  public String getValue() {
    return value;
  }

  public XMLGregorianCalendar getTime() {
    return time;
  }

  public boolean isError() {
    return error;
  }
}
