package le.mon.unittests;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Teemu Kanstren
 */
public class PortManager {
  private static int next = 10000;

  public static int next() {
    return next++;
  }

  public static URL testUrlFor(int port) {
    try {
      return new URL(testUrlStringFor(port));
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  public static String testUrlStringFor(int port) {
    return "http://localhost:"+port+"/xmlrpc";
  }
}
