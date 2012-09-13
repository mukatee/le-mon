/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.lemon.server.shared.datamodel;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Data value for a measurement.
 * Annotations are for persistence with JPA (DB storage).
 *
 * @author Teemu Kanstren
 */
public class Value {
  private BMDescription bm;
  //we have a separate precision here to tell the exact precision of this value when it was measured..
  private int precision;
  //the actual value, only strings are supported now
  private String value;
  //time of measurement
  private Date time;
  private boolean error;

  public enum SortKey {
    PRECISION, MEASUREURI, VALUE, TIME
  }

  public Value(BMDescription bm, int precision, String value, long time, boolean error) {
    this.bm = bm;
    this.precision = precision;
    this.value = value;
    this.time = new Date(time);
    this.error = error;
  }
  
  public String getMeasureURI() {
    return bm.getMeasureURI();
  }

  public BMDescription getBm() {
    return bm;
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
    return bm.getMeasureURI() + ":" + value;
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
    if (bm != null ? !bm.equals(value1.bm) : value1.bm != null) return false;
    if (time != null ? !time.equals(value1.time) : value1.time != null) return false;
    if (value != null ? !value.equals(value1.value) : value1.value != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = bm != null ? bm.hashCode() : 0;
    result = 31 * result + precision;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    result = 31 * result + (time != null ? time.hashCode() : 0);
    return result;
  }
}
