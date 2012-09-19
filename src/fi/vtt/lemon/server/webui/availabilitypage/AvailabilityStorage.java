/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

//import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AvailabilityStorage {
  
  private final Vector<DeviceDesc> devices = new Vector<DeviceDesc>();
  private final Vector<BMDesc> bms = new Vector<BMDesc>();
  private final Vector<ProbeDesc> probes = new Vector<ProbeDesc>();
  
  public synchronized void addDevice(DeviceDesc device) {
    devices.add(device);
  }
  
  public synchronized void addBM(BMDesc bm) {
    bms.add(bm);
  }
  
  public synchronized void addProbe(ProbeDesc probe) {
    probes.add(probe);
  }

  public synchronized List<DeviceDesc> getDevices() {
    return devices;
  }

  public synchronized List<BMDesc> getBms() {
    return bms;
  }

  public synchronized List<ProbeDesc> getProbes() {
    return probes;
  }
  
}
