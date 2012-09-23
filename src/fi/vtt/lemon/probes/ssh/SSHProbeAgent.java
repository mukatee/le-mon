/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probes.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probe.Probe;
import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.measurement.MeasurementProvider;
import osmo.common.log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * A probe-agent for performing measurements over the SSH protocol. Connects to the configured IP address,
 * sends a (configured) script file over with SCP to the host, does a login over SSH using the given credentials,
 * and uses the configured shell command to execute the script in question. Provides the system.out output of the
 * script as a base measure.
 *
 * @author Teemu Kanstren
 */
public class SSHProbeAgent implements Probe {
  private final static Logger log = new Logger(SSHProbeAgent.class);
  private final static Map<String, Connection> connections = new HashMap<>();
  private final String measureURI;
  private final int precision;
  private final String target;
  //filename of the script. should be relative to the "working directory"
  private String filename = null;
  private String username = null;
  private String password = null;
  //shell command to execute the script
  private String command = null;
  private String errors = null;

  public SSHProbeAgent(String measureURI, int precision, String target, String filename, String username, String password, String command) {
    this.measureURI = measureURI;
    this.precision = precision;
    this.target = target;
    this.filename = filename;
    this.username = username;
    this.password = password;
    this.command = command;
  }

  public String getMeasureURI() {
    return measureURI;
  }

  public int getPrecision() {
    return precision;
  }

  public String measure() {
    try {
      String result = executeScript();
      log.debug("measurement result:" + result);
      return result;
    } catch (Exception e) {
      throw new RuntimeException("Failed to perform measure for " + measureURI + ", ", e);
    }
  }

  //executes the configured shell script on the configured target
  private String executeScript() throws Exception {
    Connection conn = connections.get(target);
    if (conn == null) {
      conn = new Connection(target);
      log.debug("connecting now to:"+target);
      /* Now connect */
      conn.connect();
      log.debug("connected ok");
      boolean authenticated = conn.authenticateWithPassword(username, password);

      if (!authenticated)
        throw new IOException("Authentication failed.");

      log.debug("authenticated ok");
      connections.put(target, conn);
    }
    log.debug("executing script on target:"+target);

    SCPClient client = conn.createSCPClient();
    client.put(filename, ".");

    log.debug("script file moved to server");
    Session sess = conn.openSession();
    sess.execCommand(command + " " + filename);

    InputStream stdout = new StreamGobbler(sess.getStdout());
    InputStream stderr = new StreamGobbler(sess.getStderr());

    String output = readOutput(stdout);
    errors = readOutput(stderr);

    System.out.println("done reading, errors:"+errors);

    /* Show exit status, if available (otherwise "null") */
//    System.out.println("ExitCode: " + sess.getExitStatus());
    /* Close this session */
    sess.close();
    /* Close the connection */
    //conn.close();
    return output;
  }

  private String readOutput(InputStream in) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    StringBuffer result = new StringBuffer();
    while (true) {
      String line = br.readLine();
      if (line == null)
        break;
      result.append(line);
      result.append("\n");
    }
    return result.toString();
  }

  //for testing
  public static void main(String[] args) throws Exception {
    MeasurementProvider mp = new MeasurementProvider(new ServerClient("::1"), 5, 10);
    mp.setInterval(Config.getInt(RabbitConst.MEASURE_INTERVAL));
    String measureURI = Config.getString(RabbitConst.PARAM_MEASURE_URI);
    int precision = Config.getInt(RabbitConst.PROBE_PRECISION);
    String target = Config.getString(RabbitConst.MEASUREMENT_TARGET);
    String filename = Config.getString(RabbitConst.FILENAME);
    String username = Config.getString(RabbitConst.USERNAME);
    String password = Config.getString(RabbitConst.PASSWORD);
    String command = Config.getString(RabbitConst.COMMAND);
    mp.startMeasuring(new SSHProbeAgent(measureURI, precision, target, filename, username, password, command));
  }
}
