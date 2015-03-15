package le.mon.probes.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import le.mon.probe.Probe;
import le.mon.probe.ProbeServer;
import le.mon.probe.ServerClient;
import le.mon.probe.measurement.MeasurementProvider;
import le.mon.probe.tasks.RegistrationSender;
import le.mon.server.MessagePooler;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.TestUtils;
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
 * For example, script = "measure.sh", command = "bash" the following is the process.
 * Login using username and password.
 * Upload the script file using SCP (from local working directory to remote working directory).
 * Execute the script by making an SSH terminal connection and sending "bash measure.sh" command.
 * Capture the system.out print.
 * Return the captured data as base measure.
 *
 * @author Teemu Kanstren
 */
public class SSHProbeAgent implements Probe {
  private final static Logger log = new Logger(SSHProbeAgent.class);
  private final static Map<String, Connection> connections = new HashMap<>();
  /** Identifier for measurement. */
  private final String measureURI;
  /** Precision of the probe providing the measure. */
  private final int precision;
  /** Address for the target where the SSH connection is made. */
  private final String target;
  /** Filename of the measurement script. Should be relative to the working directory. */
  private String filename = null;
  /** Username to login to the SSH server. */
  private String username = null;
  /** Password to login to the SSH server. */
  private String password = null;
  /** Shell command to execute the script. */
  private String command = null;
  /** Possible errors from the script (system.err). */
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

  /**
   * Performs the measurement, invoked by MeasurementProcessor created task.
   *
   * @return The measurement result.
   */
  public String measure() {
    try {
      String result = executeScript();
      log.debug("measurement result:" + result);
      return result;
    } catch (Exception e) {
      throw new RuntimeException("Failed to perform measure for " + measureURI + ", ", e);
    }
  }

  /**
   * Uploads the file, executes the command, captures the output as a result.
   *
   * @return The remote system system.out as provided by SSH.
   * @throws Exception If there is an error..
   */
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

  /**
   * Reads output from given input stream as much as is given.
   * TODO: Check for some buffer size limit and stop if it goes over.
   *
   * @param in The inputstream to read.
   * @return The output as read from the inputstream.
   * @throws IOException If there is an error.
   */
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

  @Override
  public void addMeasure(String config) {
    log.debug("should add measurement but NOT IMPLEMENTED:"+config);
  }

  @Override
  public void removeMeasure(String config) {
    log.debug("should remove measurement but NOT IMPLEMENTED:"+config);
  }

  /**
   * Starts up the SSH Probe agent, reads configuration from file, connects to server, starts measuring.
   * If anything goes wrong, fails.
   * TODO: add some resilience.
   *
   * @param args Command line parameters. Unused.
   * @throws Exception If there is an error.
   */
  public static void main(String[] args) throws Exception {
    String httpConfig = TestUtils.readFile("ssh_probes.json", "UTF8");
    JSONArray array = new JSONArray(httpConfig);
    int length = array.length();
    MessagePooler pooler = ProbeServer.getPooler();
    for (int i = 0 ; i < length ; i++) {
      JSONObject probej = array.getJSONObject(i);
      String filename = probej.getString("file");
      String command = probej.getString("command");
      String target = probej.getString("target");
      String username = probej.getString("username");
      String password = probej.getString("password");
      String measureURI = probej.getString("measure_uri");
      int precision = probej.getInt("precision");
      pooler.schedule(new RegistrationSender(measureURI));
      String url = ProbeServer.getServerAgentAddress();
      MeasurementProvider mp = new MeasurementProvider();
      mp.startMeasuring(new SSHProbeAgent(measureURI, precision, target, filename, username, password, command));
    }
  }
}
