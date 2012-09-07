/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.noen.mfw.bundle.server.webui;

import fi.vtt.noen.mfw.bundle.common.DataObject;
import fi.vtt.noen.mfw.bundle.server.ServerPlugin;
import osmo.common.log.Logger;
import fi.vtt.noen.mfw.bundle.server.registry.RegistryPlugin;
import fi.vtt.noen.mfw.bundle.server.webui.bmreportspage.BMReportsStorage;
import fi.vtt.noen.mfw.bundle.server.webui.bmresultspage.BMResult;
import fi.vtt.noen.mfw.bundle.server.webui.bmresultspage.BMResultsStorage;
import fi.vtt.noen.mfw.bundle.server.webui.derivedmeasurepage.DMMonitorPlugin;
import fi.vtt.noen.mfw.bundle.server.webui.mfwclient.Availability;
import fi.vtt.noen.mfw.bundle.server.webui.mfwclient.MFW;
import fi.vtt.noen.mfw.bundle.server.webui.mfwclient.MFWClient;
import fi.vtt.noen.mfw.bundle.server.webui.mfwclient.ProbeParameter;
import fi.vtt.noen.mfw.bundle.server.webui.mfwclient.ProbeParameters;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.BMReport;

import fi.vtt.noen.mfw.bundle.server.shared.datamodel.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides a browser-based UI to see and manage information about the MFW operation. Uses the OSGI HTTP service
 * as the web-server.
 *
 * @author Teemu Kanstren
 */
public class WebUIPlugin {
  private final static Logger log = new Logger(WebUIPlugin.class);
  //service itself..
  private static WebUIPlugin webui = null;
  //stores dm values and provides access to them on the DM page
  private DMMonitorPlugin dmMonitor = null;
  //  private AlertLabel alert = null;
  private boolean initialized = false;
  private BMResultsStorage bmresults;
  private MFWClient mfwClient;
  private ServerPlugin server;
  private BMReportsStorage bmreports;
  private Map<String, String> latestValues = new HashMap<String, String>();

  public void setServer(ServerPlugin server) {
    this.server = server;
  }

  public void setRegistry(RegistryPlugin registry) {
//    this.registry = registry;
  }

  public static WebUIPlugin getInstance() {
    return webui;
  }

  public WebUIPlugin() {
    webui = this;
    bmresults = new BMResultsStorage();
    bmreports = new BMReportsStorage();
  }

  public Set getCommands() {
    return createCommandSet(BMReport.class, Value.class);
  }

  public void process(DataObject data) {
    if (data instanceof BMReport) {
      BMReport bmReport = (BMReport) data;
      log.debug("received bm report:" + bmReport);
      bmreports.addBMReport(bmReport);
    }
    if (data instanceof Value) {
      Value value = (Value) data;
      String uri = value.getMeasureURI();
      latestValues.put(uri, value.getString());
    }
  }

  public void terminate() {
    //here we should put any code needed to remove something that was done in the "init" method.
    //this happens when the httpservice is de-registered and later re-registered (new init() call)
    initialized = false;
  }

  public void init() {
    if (initialized) {
      return;
    }
    Hashtable<String, String> props = new Hashtable<String, String>();
    props.put("applicationClassName", WebUIWicketApplication.class.getName());
    //TODO: initialize wicket
    service.registerFilter(new WebUIWicketFilter(), "/.*", props, 0, null);
    initialized = true;
    log.debug("wicket started");
  }

  public DMMonitorPlugin getDMMonitor() {
    return dmMonitor;
  }

  public void setDMMonitor(DMMonitorPlugin dmMonitor) {
    this.dmMonitor = dmMonitor;
  }

  public List<BMResult> getBMResults() {
    return bmresults.getBmResults();
  }

  public void addBMResult(BMResult bm) {
    bmresults.addBMResult(bm);
  }

  public List<ProbeParameter> getProbeParameters(long probeId) {
    List<ProbeParameter> params = new ArrayList<ProbeParameter>();

    ProbeParameters parameters = mfwClient.getProbeParamaters(probeId);

    if (parameters != null) {
      for (ProbeParameter parameter : parameters.getProbeParameter()) {
        params.add(parameter);
      }
    }

    return params;
  }

  public void setProbeParameter(long probeId, String paramName, String paramValue) {
    mfwClient.setProbeParamaters(probeId, paramName, paramValue);

  }

  public Availability getAvailability() {
    Availability availability = mfwClient.getAvailability();
    return availability;
  }

  public void registerMFWClient(MFWClient mfwClient2) {
    mfwClient = mfwClient2;

  }

  public void subscribeToBM(long bmId, long deviceId, long frequency) {
    if (frequency == 0) {
      mfwClient.requestBM(bmId, deviceId);
    } else if (frequency > 0) {
      mfwClient.subscribeToBM(bmId, deviceId, frequency);
    }
  }

  public void unsubscribeToBM(long bmId, long deviceId) {
    mfwClient.unsubscribeToBM(bmId, deviceId);
  }

  public Map<String, String> getLatestValues() {
    return latestValues;
  }

  public MFW getMFWInformation() {
    MFW mfwInfo = mfwClient.getMFW();
    return mfwInfo;
  }

  public void setReference(BMReport bmReport) {
    server.setReference(bmReport.getSubscriptionId(), bmReport.getCurrentValue());
  }

  public List<BMReport> getBMReports() {
    return bmreports.getBmReports();
  }

  public void addBMReport(BMReport bmReport) {
    bmreports.addBMReport(bmReport);
  }

  public void clearBMReports() {
    bmreports = new BMReportsStorage();
  }
}
