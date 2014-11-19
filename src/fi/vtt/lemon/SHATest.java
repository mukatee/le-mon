package fi.vtt.lemon;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Teemu Kanstren
 */
public class SHATest {
  public static void main(String[] args) throws Exception {
    String newPcr = extend("2522d914e2729b07b6845598fd4889024cf26bb9", "18e535fd7f252aa966286f138bccee85607c2374");
    System.out.println(newPcr);
  }

  public static String extend(String oldPcr, String hash) throws NoSuchAlgorithmException, IOException {
    MessageDigest sha1 = MessageDigest.getInstance("SHA1");

    byte[] pcrData = hexStringToBytes(oldPcr);
    byte[] hashData = hexStringToBytes(hash);
    assert(pcrData.length == 20);
    assert(hashData.length == 20);
    byte[] data = new byte[40];
    System.arraycopy(pcrData, 0, data, 0, 20);
    System.arraycopy(hashData, 0, data, 20, 20);
    byte[] result = sha1.digest(data);

    StringBuilder sb = new StringBuilder();
    for (byte aResult : result) {
      sb.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
    }

    String newPcr = sb.toString();
    return newPcr;
  }

  public static byte[] hexStringToBytes(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
              + Character.digit(s.charAt(i+1), 16));
    }
    return data;
  }
}
