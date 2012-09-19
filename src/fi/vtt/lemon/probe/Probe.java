/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probe;

import java.util.Properties;

/**
 * The interface for requesting the actual measurements from actual probes.
 * Called by the probe-agent to satisfy the measurement requests.
 * Sampling is expected to be done by the probe-agent through this interface.
 * The implementation of this interface itself can and needs to implement any functionality
 * to address specific needs set by a probe, such as handling any sampling done by the probe
 * itself, or custom protocols for the probe control.
 *
 * @author Teemu Kanstren
 */
public interface Probe {
  public int getPrecision();
  public String getMeasureURI();
  /** Called by the MFW components when a measurement is needed. */
  public String measure();
}
