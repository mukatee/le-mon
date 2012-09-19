/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

public class BMDesc {
  private final long bmId;
  private final long deviceId;
  private final String clazz;
  private final String name;
  private final String description;
  private final boolean disabled;
  
  public BMDesc(long bmId, long deviceId, String clazz, String name,
      String description, boolean disabled) {
    super();
    this.bmId = bmId;
    this.deviceId = deviceId;
    this.clazz = clazz;
    this.name = name;
    this.description = description;
    this.disabled = disabled;
  }
  
  public long getBmId() {
    return bmId;
  }
  public long getDeviceId() {
    return deviceId;
  }
  public String getClazz() {
    return clazz;
  }
  public String getName() {
    return name;
  }
  public String getDescription() {
    return description;
  }
  public boolean isDisabled() {
    return disabled;
  }
}
