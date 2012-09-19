/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

import org.apache.wicket.model.LoadableDetachableModel;

public class DetachableBMModel extends LoadableDetachableModel<BMDesc> {
  private final BMDesc bm;

  public DetachableBMModel(BMDesc bm) {
    this.bm = bm;
  }

  @Override
  protected BMDesc load() {
    return bm;
  }
}