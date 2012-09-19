/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import osmo.common.log.Logger;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DeviceListDataProvider extends SortableDataProvider<DeviceDesc> {
  private final static Logger log = new Logger(DeviceListDataProvider.class);
  private final List<DeviceDesc> devices;

  public DeviceListDataProvider(List<DeviceDesc> devices) {
    this.devices = devices;
    setSort("name", true);
  }

  public Iterator<DeviceDesc> iterator(int i, int i1) {
    SortParam sp = getSort();
    String key = sp.getProperty();
    log.debug("sort key:" + key);
    if (sp.isAscending()) {
      Collections.sort(devices, new DeviceComparator(key, true));
    } else {
      Collections.sort(devices, new DeviceComparator(key, false));
    }
    return devices.subList(i, i + i1).iterator();
  }

  public int size() {
    return devices.size();
  }

  public IModel<DeviceDesc> model(DeviceDesc deviceDesc) {
    return new DetachableDeviceModel(deviceDesc);
  }
}
