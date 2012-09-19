/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.eventlistpage;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author Teemu Kanstren
 */
public class DetachableEventModel extends LoadableDetachableModel<ServerEvent> {
  private final ServerEvent event;

  public DetachableEventModel(ServerEvent event) {
    this.event = event;
  }

  @Override
  protected ServerEvent load() {
    return event;
  }
}