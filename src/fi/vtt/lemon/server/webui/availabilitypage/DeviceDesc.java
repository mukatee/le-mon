/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

public class DeviceDesc {
  private final long deviceId;
  private final String name;
  private final String type;
  private final boolean disabled;
  
  public DeviceDesc(long deviceId, String name, String type, boolean disabled) {
    super();
    this.deviceId = deviceId;
    this.name = name;
    this.type = type;
    this.disabled = disabled;
  }

  public long getDeviceId() {
    return deviceId;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public boolean isDisabled() {
    return disabled;
  }
}
