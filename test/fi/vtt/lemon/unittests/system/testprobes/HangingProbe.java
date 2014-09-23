package fi.vtt.lemon.unittests.system.testprobes;

import fi.vtt.lemon.probe.Probe;

/**
 * @author Teemu Kanstren
 */
public class HangingProbe implements Probe {
  @Override
  public int getPrecision() {
    return 0;
  }

  @Override
  public String getMeasureURI() {
    return "This-one-hangs";
  }

  public String measure() {
    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {
    }
    return null;
  }

  @Override
  public void addMeasure(String config) {
  }

  @Override
  public void removeMeasure(String config) {
  }
}
