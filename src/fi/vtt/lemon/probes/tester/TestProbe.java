/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probes.tester;

import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.measurement.MeasurementProvider;
import fi.vtt.lemon.probe.Probe;
import fi.vtt.lemon.probe.server.ClientRabbitServer;
import osmo.common.log.Logger;

/**
 * Base class to create test probes that provide test-data to the server-agent.
 *
 * @author Teemu Kanstren
 */
public class TestProbe implements Probe {
  private final static Logger log = new Logger(TestProbe.class);
  private final String result;
  private final String measureURI;
  private final int precision;
  private MeasurementProvider mp;
  private ClientRabbitServer server;

  public TestProbe(String result, String measureURI, int precision) {
    this.result = result;
    this.measureURI = measureURI;
    this.precision = precision;
  }

  public TestProbe(String measureURI, int precision) {
    this.result = null;
    this.measureURI = measureURI;
    this.precision = precision;
  }

  public void start() throws Exception {
    mp = new MeasurementProvider(new ServerClient("::1"));
    mp.startMeasuring(this);
    server = new ClientRabbitServer(this);
    server.start();
  }
  
  public void stop() throws Exception {
    mp.stop();
    server.stop();
  }

  public String getMeasureURI() {
    return measureURI;
  }

  public int getPrecision() {
    return precision;
  }

  public String measure() {
    log.debug("Testprobe provides measure:" + result);
    return result;
  }

  @Override
  public void addMeasure(String config) {
    log.debug("Testprobe received request to add measure:" + config);
  }

  @Override
  public void removeMeasure(String config) {
    log.debug("Testprobe received request to remove measure:" + config);
  }
}
