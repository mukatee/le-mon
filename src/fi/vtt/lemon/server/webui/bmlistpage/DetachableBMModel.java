/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.bmlistpage;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author Teemu Kanstren
 */
public class DetachableBMModel extends LoadableDetachableModel<BMListItem> {
  private final BMListItem bm;

  public DetachableBMModel(BMListItem bm) {
    this.bm = bm;
  }

  @Override
  protected BMListItem load() {
    return bm;
  }
}