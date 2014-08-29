package fi.vtt.lemon.server.external;

import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author Teemu Kanstren
 */
public class RestClient2 {
  private final static Logger log = new Logger(RestClient2.class);
  private final String base;

  public RestClient2(String base) {
    this.base = base;
  }

  public String post(String to) {
    return sendPost(base+to);
  }

  public String post(String to, JSONObject data) {
    return sendPost(base+to, data);
  }

  public static String sendPost(String to) {
    return sendPost(to, new JSONObject());
  }

  public static String sendPost(String to, JSONObject data) {
    return sendPost(to, "msg=", data);
  }
  
  public static String sendPost(String to, String prefix, JSONObject data) {
    HttpURLConnection conn = null;

    try {
      conn = (HttpURLConnection) new URL(to).openConnection();
      conn.setRequestMethod("POST");
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    conn.setRequestProperty("User-Agent", "le-mon");
    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

    conn.setDoOutput(true);
    try (OutputStream out = conn.getOutputStream()) {
      DataOutputStream wr = new DataOutputStream(out);
      String msg = prefix+data;
      wr.writeBytes(msg);
      wr.flush();
      wr.close();

      int responseCode = conn.getResponseCode();
      log.debug("\nSending 'POST' request to URL : " + to);
      log.debug("Post data : " + data.toString());
      log.debug("Response Code : " + responseCode);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    StringBuilder response = new StringBuilder();
    try (InputStream in = conn.getInputStream()) {
      BufferedReader bin = new BufferedReader(new InputStreamReader(in));
      String line = "";
      while ((line = bin.readLine()) != null) {
        response.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    //print result
    System.out.println("response:"+response.toString());
    return response.toString();
  }
}
