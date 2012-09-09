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

package fi.vtt.lemon.mfw.unittests.agent.probe;

import fi.vtt.lemon.common.Const;
import fi.vtt.lemon.common.KnowledgeSource;
import fi.vtt.lemon.probe.plugins.measurement.MeasurementProvider;
import fi.vtt.lemon.probe.shared.BaseMeasure;
import fi.vtt.lemon.probe.shared.MeasurementRequest;
import fi.vtt.lemon.probe.shared.MeasurementResponse;
import fi.vtt.lemon.probe.shared.ProbeInformation;
import fi.vtt.lemon.probes.tester.TestProbe;
import fi.vtt.lemon.mfw.unittests.system.TestServerAgent;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Teemu Kanstren
 */
public class MeasurementRequestTests {
  private static final String targetName1 = "target1";
  private static final String targetType1 = "targetType1";
  private static final String targetName2 = "target2";
  private static final String targetType2 = "targetType2";
  private static final String bmName1 = "bmName1";
  private static final String bmClass1 = "bmClass1";
  private static final String bmName2 = "bmName2";
  private static final String bmClass2 = "bmClass2";
  private static String measureURI1 = null;
  private static String measureURI2 = null;

  @BeforeClass
  public static void setup() {
    measureURI1 = Const.createMeasureURI(targetType1, targetName1, bmClass1, bmName1);
    measureURI2 = Const.createMeasureURI(targetType2, targetName2, bmClass2, bmName2);
  }

  /**
   * Requests a single measure from a measurementproviderthread and checks that the correct measure
   * is queried from the given probe and posted to the blackboard.
   *
   * @throws Exception
   */
  @Test
  public void requestSingleMeasure() throws Exception {
  }

  /**
   * This test requests for sampling of measurements every 100 milliseconds, and checks the correct amount is received in 400 millis.
   */
  @Test
  public void requestSamplingMeasures() throws Exception {
  }

  /**
   * Makes a request through an actual blackboard, uses actual bundle activators to tie it all together.
   * Checks that correct type of responses are received for requests.
   *
   * @throws Exception
   */
  @Test
  public void requestThroughBlackboard() throws Exception {
  }
}
