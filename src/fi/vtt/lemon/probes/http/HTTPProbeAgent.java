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

package fi.vtt.lemon.probes.http;

import fi.vtt.lemon.probe.ServerClient;
import osmo.common.log.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Grabs a base measure from a HTTP request posted at the address http://<address>/{bm-name}.
 * The base measure name is the part in the url and the content is the body of the http request.
 *
 * @author Teemu Kanstren
 */
public class HTTPProbeAgent implements Filter {
  private final static Logger log = new Logger(HTTPProbeAgent.class);
  private final ServerClient server;
  private final String measureURI;
  private final int precision;

  public HTTPProbeAgent(ServerClient server, String measureURI, int precision) {
    this.server = server;
    this.measureURI = measureURI;
    this.precision = precision;
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    resp.setContentType("text/plain");
    String name = req.getRequestURL().toString();
    int index = name.lastIndexOf('/');
    //todo: error handling if < 0 index is received
    //we take the name of the base measure, that which is after the last "/" character
    name = name.substring(index+1);

    // Get response data.
    BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
    String str;
    String content = "";
    //read the body, the base measure content
    while (null != ((str = br.readLine()))) {
      content += str;
    }
    br.close();

    server.measurement(measureURI, precision, content);
    log.debug("Received BM '"+name+"' from '"+req.getRemoteAddr()+" with value:"+content);

    PrintWriter out = resp.getWriter();
    out.println("hello:"+name+" -- "+content);
    out.close();
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {
  }

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
