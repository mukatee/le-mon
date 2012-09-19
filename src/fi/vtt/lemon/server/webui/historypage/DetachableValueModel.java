/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.historypage;

import fi.vtt.lemon.server.Value;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author Teemu Kanstren
 */
public class DetachableValueModel extends LoadableDetachableModel<Value> {
  private transient final Value value;

  public DetachableValueModel(Value value) {
    this.value = value;
  }

  @Override
  protected Value load() {
    return value;
  }
}