/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probe;

import java.util.Properties;

/**
 * Interface for requesting measurements from actual probes.
 *
 * @author Teemu Kanstren
 */
public interface Probe {
  /** Should give the probe precision. */
  public int getPrecision();
  /** What measure does it give? */
  public String getMeasureURI();
  /** Called by the le-mon probe-agent when a measurement is needed. */
  public String measure();
}
