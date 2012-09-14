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

package fi.vtt.lemon.server.webui.eventlistpage;

import java.text.DateFormat;
import java.util.Date;

/**
 * Describes an event, e.g. alert for exceeded threshold or a lost probe.
 *
 * @author Teemu Kanstren
 */
public class ServerEvent {
  //the information message of the event, natural language for human consumer
  private String message;
  //time when the event was observed
  private Date time;
  private String type;
  private String source;

  public enum SortKey {
    MESSAGE, TIME
  }

  public ServerEvent(long time, String type, String source, String message) {
    this.source = source;
    this.message = message;
    this.time = new Date(time);
    this.type = type;
    this.source = source;
  }

  public String getMessage() {
    return message;
  }

  public long getTime() {
    return time.getTime();
  }

  public String getSource() {
    return source;
  }

  public String getTimeString() {
    return DateFormat.getDateTimeInstance().format(time);
  }
}
