package le.mon.probes.tester;

import java.util.Random;

/**
 * Test probe to provide test data to the server-agent.
 *
 * @author Teemu Kanstren
 */
public class TestProbe3 extends TestProbe {
  private Random random;

  public TestProbe3() {
    super("MFW://Communication protocol/Bob3/Encryption key length/Bobby3", 1);
    random = new Random();
  }

  public String measure() {
    return Integer.toString( random.nextInt( 100 ) + 1 );
  }
}