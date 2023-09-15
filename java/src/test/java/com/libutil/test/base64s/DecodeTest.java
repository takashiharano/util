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
    test("", "", "");
    test("", "x", "");
    test("YWJj", "", "abc");
    test("ABkaGw==", "x", "abc");
    test("ABkbGQ==", "xyz", "abc");
    test("ARkbGc4=", "xyz1", "abc");
    test("/iDNzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczL3A==", "A2345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234#", "a");
  }

  private static void decodeTestJa() {
    test("44GC44GE44GG", "", "あいう");
    test("AJv5+pv5/Jv5/g==", "x", "あいう");
    test("AJv4+Jv4/pv4/A==", "xyz", "あいう");
    test("AZv4+NKzt9e0sJ4=", "xyz123456a", "あいう");
  }

  private static void decodeBytesTest() {
    byte[] exp = { (byte) 0x61, (byte) 0x62, (byte) 0x63 };
    test("YWJj", "", exp);
    test("ABkaGw==", "x", exp);
    test("ABkbGQ==", "xyz", exp);
    test("AxkbGc7NzA==", "xyz123", exp);
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
