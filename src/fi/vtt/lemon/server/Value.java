/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server;

import java.text.DateFormat;
import java.util.Date;

/**
 * Data value for a measurement.
 *
 * @author Teemu Kanstren
 */
public class Value {
  /** The ID for the measurenet. */
  private String measureURI;
  /** Precision of the probe that performed the measure. */
  private int precision;
  /** The value of the measurement. */
  private String value;
  /** The time of the measurement. */
  private Date time;

  /**
   * Once upon time, this was used by Persistence engine to sort the results in various ways.. 
   */
  public enum SortKey {
    PRECISION, MEASUREURI, VALUE, TIME
  }

  public Value(String measureURI, int precision, String value, long time) {
    this.measureURI = measureURI;
    this.precision = precision;
    this.value = value;
    this.time = new Date(time);
  }
  
  public String getMeasureURI() {
    return measureURI;
  }

  public int getPrecision() {
    return precision;
  }

  public Date getTime() {
    return time;
  }

  public String getTimeFormatted() {
    return DateFormat.getDateTimeInstance().format(time);
  }

  @Override
  public String toString() {
    return measureURI + ":" + value;
  }

  public String valueString() {
    return "" + value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Value value1 = (Value) o;

    if (precision != value1.precision) return false;
    if (measureURI != null ? !measureURI.equals(value1.measureURI) : value1.measureURI != null) return false;
    if (time != null ? !time.equals(value1.time) : value1.time != null) return false;
    if (value != null ? !value.equals(value1.value) : value1.value != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = measureURI != null ? measureURI.hashCode() : 0;
    result = 31 * result + precision;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    result = 31 * result + (time != null ? time.hashCode() : 0);
    return result;
  }
}
