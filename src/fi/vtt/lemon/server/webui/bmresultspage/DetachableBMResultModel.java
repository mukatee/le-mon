/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.bmresultspage;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author Teemu Kanstren
 */
public class DetachableBMResultModel extends LoadableDetachableModel<BMResult> {
  private final BMResult bmResult;

  public DetachableBMResultModel(BMResult bmResult) {
    this.bmResult = bmResult;
  }

  @Override
  protected BMResult load() {
    return bmResult;
  }
}