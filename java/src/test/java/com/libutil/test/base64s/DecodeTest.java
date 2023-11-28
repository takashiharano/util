package com.libutil.test.base64s;

import com.libutil.Base64s;
import com.libutil.TestUtil;
import com.libutil.test.Log;

public class DecodeTest {

  public static void main(String args[]) {
    decodeTest();
    Log.i("----");
    decodeTestJa();
    Log.i("----");
    decodeBytesTest();
  }

  private static void decodeTest() {
    test(null, "", (String) null);
    test(null, null, (String) null);
    test("", null, "");
    test("YWJj", null, "abc");
    test("", "", "");
    test("", "x", "");
    test("YWJj", "", "abc");
    test("GRobAA==", "x", "abc");
    test("GRsZAA==", "xyz", "abc");
    test("GRsZzgE=", "xyz1", "abc");
    test("IM3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvc/g==", "A2345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234#", "a");
    test("XX==", "x", "");
  }

  private static void decodeTestJa() {
    test("44GC44GE44GG", "", "あいう");
    test("m/n6m/n8m/n+AA==", "x", "あいう");
    test("m/j4m/j+m/j8AA==", "xyz", "あいう");
    test("m/j40rO317SwngE=", "xyz123456a", "あいう");
  }

  private static void decodeBytesTest() {
    byte[] exp = { (byte) 0x61, (byte) 0x62, (byte) 0x63 };
    test("YWJj", "", exp);
    test("GRobAA==", "x", exp);
    test("GRsZAA==", "xyz", exp);
    test("GRsZzgE=", "xyz1", exp);
  }

  private static void test(String s, String k, String expected) {
    String r = Base64s.decodeString(s, k);
    TestUtil.assertEquals(k, expected, r);
  }

  private static void test(String s, String k, byte[] expected) {
    byte[] r = Base64s.decode(s, k);
    Log.i("k=" + k);
    for (int i = 0; i < r.length; i++) {
      Log.i("EXP=" + expected[i] + " : ACTUAL=" + r[i]);
    }
  }

}
