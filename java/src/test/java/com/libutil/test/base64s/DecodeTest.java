package com.libutil.test.base64s;

import com.libutil.Base64S;
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
    test("YWJj", 0, "abc");
    test("YGNi", 1, "abc");
    test("Y2Bh", 2, "abc");
    test("n5yd", 254, "abc");
    test("np2c", 255, "abc");
    test("YWJj", 256, "abc");
    test("YGNi", 257, "abc");
    test("n5yd", 510, "abc");
    test("np2c", 511, "abc");
    test("YWJj", 512, "abc");
    test("YGNi", 513, "abc");
  }

  private static void decodeTestJa() {
    test("44GC44GE44GG", 0, "あいう");
    test("4oCD4oCF4oCH", 1, "あいう");
    test("4YOA4YOG4YOE", 2, "あいう");
    test("HX98HX96HX94", 254, "あいう");
    test("HH59HH57HH55", 255, "あいう");
    test("44GC44GE44GG", 256, "あいう");
    test("4oCD4oCF4oCH", 257, "あいう");
    test("HX98HX96HX94", 510, "あいう");
    test("HH59HH57HH55", 511, "あいう");
    test("44GC44GE44GG", 512, "あいう");
    test("4oCD4oCF4oCH", 513, "あいう");
  }

  private static void decodeBytesTest() {
    byte[] exp = { (byte) 0x61, (byte) 0x62, (byte) 0x63 };
    test("YWJj", 0, exp);
    test("YGNi", 1, exp);
    test("Y2Bh", 2, exp);
  }

  private static void test(String s, int n, String expected) {
    String r = Base64S.decodeString(s, n);
    TestUtil.assertEquals(n + "", expected, r);
  }

  private static void test(String s, int n, byte[] expected) {
    byte[] r = Base64S.decode(s, n);
    Log.i("n=" + n);
    for (int i = 0; i < r.length; i++) {
      Log.i("EXP=" + expected[i] + " : ACTUAL=" + r[i]);
    }
  }

}
