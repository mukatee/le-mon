/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon;

import osmo.common.log.Logger;

import java.util.Properties;

/** @author Teemu Kanstren */
public class Config {
  private final static Logger log = new Logger(Config.class);
  private static Properties props = new Properties();
  
  public static int getInt(String property, int defaultValue) {
    try {
      return Integer.parseInt(props.getProperty(property));
    } catch (NumberFormatException e) {
      log.error("No value found for property "+property+", using default of "+defaultValue, e);
      return defaultValue;
    }
  }

  public static int getInt(String property) {
    int value = getInt(property, Integer.MIN_VALUE);
    if (value == Integer.MIN_VALUE) {
      throw new RuntimeException("Required configuration parameter '"+property+"' not found. Aborting.'");
    }
    return value;
  }

  public static String getString(String property, String defaultValue) {
    return props.getProperty(property, defaultValue);
  }

  public static String getString(String property) {
    String value = props.getProperty(property);
    if (value == null) {
      throw new RuntimeException("Required configuration parameter '"+property+"' not found. Aborting.'");
    }
    return value;
  }
}
