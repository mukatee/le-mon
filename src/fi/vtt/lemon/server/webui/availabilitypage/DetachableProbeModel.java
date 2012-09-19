/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

import org.apache.wicket.model.LoadableDetachableModel;

public class DetachableProbeModel extends LoadableDetachableModel<ProbeDesc> {
  private final ProbeDesc probe;

  public DetachableProbeModel(ProbeDesc probe) {
    this.probe = probe;
  }

  @Override
  protected ProbeDesc load() {
    return probe;
  }
}
