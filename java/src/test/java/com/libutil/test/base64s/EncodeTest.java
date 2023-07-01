package com.libutil.test.base64s;

import com.libutil.Base64S;
import com.libutil.TestUtil;
import com.libutil.test.Log;

public class EncodeTest {

  public static void main(String args[]) {
    encodeTest();
    Log.i("----");
    encodeTestJa();
    Log.i("----");
    encodeBytesTest();
  }

  private static void encodeTest() {
    test("abc", 0, "YWJj");
    test("abc", 1, "YGNi");
    test("abc", 2, "Y2Bh");
    test("abc", 254, "n5yd");
    test("abc", 255, "np2c");
    test("abc", 256, "YWJj");
    test("abc", 257, "YGNi");
    test("abc", 510, "n5yd");
    test("abc", 511, "np2c");
    test("abc", 512, "YWJj");
    test("abc", 513, "YGNi");
  }

  private static void encodeTestJa() {
    test("あいう", 0, "44GC44GE44GG");
    test("あいう", 1, "4oCD4oCF4oCH");
    test("あいう", 2, "4YOA4YOG4YOE");
    test("あいう", 254, "HX98HX96HX94");
    test("あいう", 255, "HH59HH57HH55");
    test("あいう", 256, "44GC44GE44GG");
    test("あいう", 257, "4oCD4oCF4oCH");
    test("あいう", 510, "HX98HX96HX94");
    test("あいう", 511, "HH59HH57HH55");
    test("あいう", 512, "44GC44GE44GG");
    test("あいう", 513, "4oCD4oCF4oCH");
  }

  private static void encodeBytesTest() {
    byte[] b = { (byte) 0x61, (byte) 0x62, (byte) 0x63 };
    test(b, 0, "YWJj");
    test(b, 1, "YGNi");
    test(b, 2, "Y2Bh");
  }

  private static void test(String s, int n, String expected) {
    String r = Base64S.encode(s, n);
    TestUtil.assertEquals(n + "", expected, r);
  }

  private static void test(byte[] b, int n, String expected) {
    String r = Base64S.encode(b, n);
    TestUtil.assertEquals(n + "", expected, r);
  }

}
