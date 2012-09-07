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

package fi.vtt.noen.mfw.bundle.server;

import fi.vtt.noen.mfw.bundle.common.DataObject;
import fi.vtt.noen.mfw.bundle.common.EventType;
import osmo.common.log.Logger;
import fi.vtt.noen.mfw.bundle.server.registry.RegistryPlugin;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.BMDescription;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.ProbeDescription;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.ProbeDisabled;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.ProbeRegistered;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.ServerEvent;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.Value;

import java.util.Set;

/**
 * @author Teemu Kanstren
 */
public class EventPlugin {
  private final static Logger log = new Logger(EventPlugin.class);
  //for accessing runtime state
  private RegistryPlugin registry;

  public EventPlugin() {
    super(log);
  }

  public void stop() {
  }

  public void setRegistry(RegistryPlugin registry) {
    this.registry = registry;
  }

  @Override
  public Set<Class> getCommands() {
    return createCommandSet(ServerEvent.class, ProbeRegistered.class, ProbeDisabled.class);
  }

  @Override
  public void process(DataObject data) {
    if (data instanceof ServerEvent) {
      ServerEvent event = (ServerEvent) data;
      if (event.type.equals(EventType.PROBE_HUNG)) {
        log.debug("Probe hung event received");
        BMDescription bm = registry.descriptionFor(event.getSource());
        Value value = new Value(bm, 1, event.getMessage(), event.getTime(), event.getSubscriptionId(), true);
        bb.process(value);
        log.debug("BM created");
        ProbeDescription probe = registry.getProbeForSubscription(event.getSubscriptionId());
        registry.setProbeDisabled(probe);
      }
    }
    if (data instanceof ProbeRegistered) {
      ProbeRegistered probe = (ProbeRegistered) data;
      bb.process(new ServerEvent(System.currentTimeMillis(), EventType.PROBE_REGISTERED, "Probe:"+probe.getProbeDescription().getProbeName(), "Registered for BM:"+probe.getProbeDescription().getMeasureURI()));
    }
    if (data instanceof ProbeDisabled) {
      ProbeDisabled probe = (ProbeDisabled) data;
      ServerEvent event = new ServerEvent(System.currentTimeMillis(), EventType.PROBE_LOST, "Probe:"+probe.getProbeDescription().getProbeName(), "Probe is not sending keep-alive and is now removed from active list. (BM:"+probe.getProbeDescription().getMeasureURI()+")");
      bb.process(event);
    }
  }

}
