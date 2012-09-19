/*
 * Copyright (c) 2012 VTT
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
