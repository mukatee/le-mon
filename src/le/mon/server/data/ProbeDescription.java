package le.mon.server.data;

/**
 * Describes a measurement probe.
 *
 * @author Teemu Kanstren
 */
public class ProbeDescription {
  /** The address where we can send messages to this probe. */
  private final String url;
  /** Measurement identifier. */
  private final String measureURI;
  /** Is the probe available, reporting keep-alive. */
  private boolean available = true;
  /** Time in milliseconds when we last heard about this probe (keep-alive). */
  private long lastHeard = 0;

  public ProbeDescription(String url, String measureURI) {
    this.url = url;
    this.measureURI = measureURI;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public long getLastHeard() {
    return lastHeard;
  }

  public void setLastHeard(long lastHeard) {
    this.lastHeard = lastHeard;
  }

  public String getUrl() {
    return url;
  }

  public String getMeasureURI() {
    return measureURI;
  }

  @Override
  public String toString() {
    return "ProbeDescription{" +
            "url='" + url + '\'' +
            ", measureURI='" + measureURI + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ProbeDescription that = (ProbeDescription) o;

    if (measureURI != null ? !measureURI.equals(that.measureURI) : that.measureURI != null) return false;
    if (url != null ? !url.equals(that.url) : that.url != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = url != null ? url.hashCode() : 0;
    result = 31 * result + (measureURI != null ? measureURI.hashCode() : 0);
    return result;
  }
}
