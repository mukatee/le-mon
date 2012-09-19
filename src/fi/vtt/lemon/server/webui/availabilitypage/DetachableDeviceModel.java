/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

import org.apache.wicket.model.LoadableDetachableModel;

public class DetachableDeviceModel extends LoadableDetachableModel<DeviceDesc> {
  private final DeviceDesc deviceDesc;

  public DetachableDeviceModel(DeviceDesc deviceDesc) {
    this.deviceDesc = deviceDesc;
  }

  @Override
  protected DeviceDesc load() {
    return deviceDesc;
  }
}