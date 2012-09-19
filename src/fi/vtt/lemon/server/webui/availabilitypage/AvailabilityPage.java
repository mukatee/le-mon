/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

import fi.vtt.lemon.server.webui.WebUIPlugin;
import fi.vtt.lemon.server.webui.mfwclient.Availability;
import fi.vtt.lemon.server.webui.mfwclient.BM;
import fi.vtt.lemon.server.webui.mfwclient.Device;
import fi.vtt.lemon.server.webui.mfwclient.Probe;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.Model;
import osmo.common.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityPage extends WebPage {
  private final static Logger log = new Logger(AvailabilityPage.class);
  private WebUIPlugin webUI;
  private List<BMDesc> bms;
  private List<ProbeDesc> probes;
  private List<DeviceDesc> devices;

  public AvailabilityPage() {
    webUI = WebUIPlugin.getInstance();
    add(new StyleSheetReference("listpageCSS", getClass(), "style.css"));
    
    Availability availability = webUI.getAvailability();
    
    devices = new ArrayList<DeviceDesc>();
    if (availability != null) {
      DeviceDesc deviceDesc;
      for (Device device : availability.getDevice()) {
        deviceDesc = new DeviceDesc(device.getId(), device.getName(), device.getType(), device.isDisabled());
        devices.add(deviceDesc);
      }
    }
    
    createDeviceList(devices);
    
    bms = new ArrayList<BMDesc>();
    if (availability != null) {
      BMDesc bmDesc;
      for (BM bm : availability.getBM()) {
        if (!bm.getDeviceId().isEmpty()) {
          bmDesc = new BMDesc(bm.getId(), bm.getDeviceId().get(0), bm.getClazz(), bm.getName(), bm.getDescription(), bm.isDisabled());
        } else {
          bmDesc = new BMDesc(bm.getId(), 0, bm.getClazz(), bm.getName(), bm.getDescription(), bm.isDisabled());
        }
        bms.add(bmDesc);
      }
    }

    createBMList(bms);
    
    probes = new ArrayList<ProbeDesc>();
    if (availability != null) {
      ProbeDesc probeDesc;
      for (Probe probe : availability.getProbe()) {        
        if (!probe.getBmId().isEmpty()) {
          probeDesc = new ProbeDesc(probe.getId(), probe.getBmId().get(0), probe.getName(), probe.isDisabled());
        } else {
          probeDesc = new ProbeDesc(probe.getId(), 0, probe.getName(), probe.isDisabled());
        }
        probes.add(probeDesc);
      }
    }
    
    createProbeList(probes);

  }
  
  public void createDeviceList(List<DeviceDesc> devices) {
    List<IColumn<?>> columns = new ArrayList<IColumn<?>>();

    columns.add(new PropertyColumn(new Model<String>("deviceId"), "deviceId", "deviceId") {
      @Override
      public String getCssClass() {
        return "numeric";
      }
    });

    columns.add(new PropertyColumn(new Model<String>("name"), "name", "name"));
    columns.add(new PropertyColumn(new Model<String>("type"), "type", "type"));

    DefaultDataTable deviceTable = new DefaultDataTable("devicetable", columns, new DeviceListDataProvider(devices), 8);
    add(deviceTable);
  }

  public void createBMList(List<BMDesc> bms) {
    List<IColumn<?>> columns = new ArrayList<IColumn<?>>();

    columns.add(new PropertyColumn(new Model<String>("bmId"), "bmId", "bmId") {
      @Override
      public String getCssClass() {
        return "numeric";
      }
    });
    columns.add(new PropertyColumn(new Model<String>("deviceId"), "deviceId", "deviceId") {
      @Override
      public String getCssClass() {
        return "numeric";
      }
    });

    columns.add(new PropertyColumn(new Model<String>("clazz"), "clazz", "clazz"));
    columns.add(new PropertyColumn(new Model<String>("name"), "name", "name"));
    columns.add(new PropertyColumn(new Model<String>("description"), "description", "description"));

    DefaultDataTable bmTable = new DefaultDataTable("bmtable", columns, new BMListDataProvider(bms), 8);
    add(bmTable);
  }
  
  public void createProbeList(List<ProbeDesc> Probes) {
    List<IColumn<?>> columns = new ArrayList<IColumn<?>>();

    columns.add(new PropertyColumn(new Model<String>("probeId"), "probeId", "probeId") {
      @Override
      public String getCssClass() {
        return "numeric";
      }
    });
    columns.add(new PropertyColumn(new Model<String>("bmId"), "bmId", "bmId") {
      @Override
      public String getCssClass() {
        return "numeric";
      }
    });
    columns.add(new PropertyColumn(new Model<String>("name"), "name", "name"));

    DefaultDataTable probeTable = new DefaultDataTable("probetable", columns, new ProbeListDataProvider(Probes), 8);
    add(probeTable);
  }

}
