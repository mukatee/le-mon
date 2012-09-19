/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.unittests.agent.server;

import fi.vtt.lemon.server.Registry;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Teemu Kanstren
 */
public class RegistryTests {
  @Test
  public void testTargetParsingFromMeasureURI() {
    String measureURI = "MFW://target_type/target_name/bm_class/bm_name";
    Registry registry = new Registry();
    assertEquals("target_type", registry.parseTargetType(measureURI));
    assertEquals("target_name", registry.parseTargetName(measureURI));
  }
}
