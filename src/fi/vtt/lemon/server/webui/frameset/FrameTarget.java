/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.frameset;

import org.apache.wicket.IClusterable;
import org.apache.wicket.Page;


/**
 * The right hand side of the bottom frame.
 * base borrowed from wicket examples.
 */
public final class FrameTarget implements IClusterable {
  private static final long serialVersionUID = 1L;

  private Class<? extends Page> frameClass;

  public FrameTarget() {
  }

  public <C extends Page> FrameTarget(Class<C> frameClass) {
    this.frameClass = frameClass;
  }

  public Class<? extends Page> getFrameClass() {
    return frameClass;
  }

  public <C extends Page> void setFrameClass(Class<C> frameClass) {
    this.frameClass = frameClass;
  }
}