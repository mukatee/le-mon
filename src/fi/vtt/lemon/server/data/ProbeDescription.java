package fi.vtt.lemon.server.data;

/**
 * @author Teemu Kanstren
 */
public class ProbeDescription {
  private final String url;
  private final String measureURI;
  private final int precision;

  public ProbeDescription(String url, String measureURI, int precision) {
    this.url = url;
    this.measureURI = measureURI;
    this.precision = precision;
  }

  public String getUrl() {
    return url;
  }

  public String getMeasureURI() {
    return measureURI;
  }

  public int getPrecision() {
    return precision;
  }

  @Override
  public String toString() {
    return "ProbeDescription{" +
            "url='" + url + '\'' +
            ", measureURI='" + measureURI + '\'' +
            ", precision=" + precision +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ProbeDescription that = (ProbeDescription) o;

    if (precision != that.precision) return false;
    if (measureURI != null ? !measureURI.equals(that.measureURI) : that.measureURI != null) return false;
    if (url != null ? !url.equals(that.url) : that.url != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = url != null ? url.hashCode() : 0;
    result = 31 * result + (measureURI != null ? measureURI.hashCode() : 0);
    result = 31 * result + precision;
    return result;
  }
}
