/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

public class ProbeDesc {
  private final long probeId;
  private final long bmId;
  private final String name;
  private final boolean disabled;
  
  public ProbeDesc(long probeId, long bmId, String name, boolean disabled) {
    super();
    this.probeId = probeId;
    this.bmId = bmId;
    this.name = name;
    this.disabled = disabled;
  }

  public long getProbeId() {
    return probeId;
  }

  public long getBmId() {
    return bmId;
  }

  public String getName() {
    return name;
  }

  public boolean isDisabled() {
    return disabled;
  }
}
