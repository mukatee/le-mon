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

package fi.vtt.lemon.probe.plugins.measurement;

import fi.vtt.lemon.probe.plugins.xmlrpc.ServerClient;
import fi.vtt.lemon.probe.shared.Probe;
import fi.vtt.lemon.probe.shared.ProbeInformation;
import osmo.common.log.Logger;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Teemu Kanstren
 */
public class WatchedTask {
  private final static Logger log = new Logger(WatchedTask.class);
  private final Future future;
  private final MeasurementTask task;
  private final Map<Probe, WatchedTask> subscriptions;

  public WatchedTask(Future future, MeasurementTask task, Map<Probe, WatchedTask> subscriptions) {
    this.future = future;
    this.task = task;
    this.subscriptions = subscriptions;
  }

  public long getRunningTime() {
    return task.getRunningTime();
  }

  public void cancel() {
    future.cancel(true);
    subscriptions.remove(task.getProbe());
  }

  public ProbeInformation getProbeInfo() {
    return task.getProbeInfo();
  }

  public MeasurementTask getMeasurementTask() {
    return task;
  }
}
