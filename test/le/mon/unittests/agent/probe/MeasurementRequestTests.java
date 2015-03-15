package le.mon.unittests.agent.probe;

import le.mon.MsgConst;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
    measureURI1 = MsgConst.createMeasureURI(targetType1, targetName1, bmClass1, bmName1);
    measureURI2 = MsgConst.createMeasureURI(targetType2, targetName2, bmClass2, bmName2);
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
