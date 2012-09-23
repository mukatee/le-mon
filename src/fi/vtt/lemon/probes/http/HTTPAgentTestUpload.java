/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probes.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/** @author Teemu Kanstren */
public class HTTPAgentTestUpload {
  //for self-testing
  public static void main(String[] args) throws Exception {
    // URL of CGI-Bin script.
    URL target = new URL("http://localhost:8081/mfw/bm/os_version");
    HttpURLConnection url = (HttpURLConnection) target.openConnection();
    // Let the run-time system (RTS) know that we want input.
    url.setDoInput(true);
    // Let the RTS know that we want to do output.
    url.setDoOutput(true);
    // No caching, we want the real thing.
    url.setUseCaches(false);
    // Specify the content type.
    url.setRequestProperty("Content-Type", "text/plain");
    // Send POST output.
    url.setRequestMethod("POST");
    DataOutputStream printout = new DataOutputStream(url.getOutputStream());
    String content = "A value has been observed.";
    printout.writeBytes(content);
    printout.flush();
    printout.close();
    // Get response data.
    BufferedReader br = new BufferedReader(new InputStreamReader(url.getInputStream()));
    String str;
    while (null != ((str = br.readLine()))) {
      System.out.println(str);
    }
    br.close();
  }
}
