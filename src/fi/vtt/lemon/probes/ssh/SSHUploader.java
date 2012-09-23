/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probes.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

import java.io.IOException;

/** 
 * Simple class for uploading something over SSH (SCP).
 * Mainly useful if you have a server where you want to drop something without installing bunch of stuff but have
 * an SSH server running already.. Not to mention testing your SSH configuration and code works.
 * 
 * @author Teemu Kanstren 
 */
public class SSHUploader {
  /**
   * This is where the magic happens.
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    /* Create a connection instance */
    Connection conn = new Connection("192.168.60.128");

    /* Now connect */
    conn.connect();

    /* Authenticate.
     * If you get an IOException saying something like
     * "Authentication method password not supported by the server at this stage."
     * then please check the FAQ.
     */
    String username = "hii";
    String password = "haa";
    String filename = "java\\distro\\distro.zip";
    boolean authenticated = conn.authenticateWithPassword(username, password);

    if (authenticated == false)
      throw new IOException("Authentication failed.");

    SCPClient client = conn.createSCPClient();
    client.put(filename, ".");
    conn.close();
  }
}
