package le.mon;

import osmo.common.log.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Provides access to the le-mon configuration defined in the le-mon(dot)properties file.
 *
 * @author Teemu Kanstren
 */
public class Config {
  private final static Logger log = new Logger(Config.class);
  /** Represents the underlying le-mon properties file. (named le-mon.properties) */
  private static Properties props = new Properties();

  static {
    try {
      props.load(new FileInputStream("le-mon.properties"));
    } catch (IOException e) {
      System.out.println("Unable to load configuration (le-mon.properties)");
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Get a configuration property parsed as an integer number.
   * This is a getter for an optional configuration value that has a default value if the property is not present.
   *
   * @param property The name of the property (key in file).
   * @param defaultValue Give this value if no valid value is found.
   * @return The integer as parsed or the default value if no valid value found.
   */
  public static int getInt(String property, int defaultValue) {
    try {
      return Integer.parseInt(props.getProperty(property));
    } catch (NumberFormatException e) {
      log.error("No value found for property "+property+", using default of "+defaultValue, e);
      return defaultValue;
    }
  }

  /**
   * Get a configuration property parsed as an integer number.
   * This is a getter for a required configuration value that throws an exception if no valid value is found.
   *
   * @param property The name of the property (key in file).
   * @return The integer as parsed, or if no valid value is found throws a RuntimeException.
   */
  public static int getInt(String property) {
    int value = getInt(property, Integer.MIN_VALUE);
    if (value == Integer.MIN_VALUE) {
      throw new RuntimeException("Required configuration parameter '"+property+"' not found. Aborting.'");
    }
    return value;
  }

  /**
   * Get a configuration property as a String object.
   * This is a getter for an optional configuration value that has a default value if the property is not present.
   *
   * @param property The name of the property (key in file).
   * @param defaultValue Give this value if no valid value is found.
   * @return The String value as defined or the default value if no valid value found.
   */
  public static String getString(String property, String defaultValue) {
    return props.getProperty(property, defaultValue);
  }

  /**
   * Get a configuration property as a String object.
   * This is a getter for a required configuration value that throws an exception if no valid value is found.
   *
   * @param property The name of the property (key in file).
   * @return The String value as defined, or if no valid value is found throws a RuntimeException.
   */
  public static String getString(String property) {
    String value = props.getProperty(property);
    if (value == null) {
      throw new RuntimeException("Required configuration parameter '"+property+"' not found. Aborting.'");
    }
    return value;
  }
}
