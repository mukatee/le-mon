package le.mon.probe;

/**
 * Interface for requesting measurements from actual probes.
 *
 * @author Teemu Kanstren
 */
public interface Probe {
  /** What measure does it give? */
  public String getMeasureURI();

  /** Called by the le-mon probe-agent when a measurement is needed. */
  public String measure();

  public void addMeasure(String config);

  public void removeMeasure(String config);
}
