package com.libutil.test.base64s;

import com.libutil.Base64s;
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
    test((byte[]) null, null, null);
    test((String) null, null, null);
    test((String) null, "", null);
    test("", null, "");
    test("abc", null, "YWJj");
    test("", "", "");
    test("", "x", "");
    test("abc", "", "YWJj");
    test("abc", "x", "GRobAA==");
    test("abc", "xyz", "GRsZAA==");
    test("abc", "xyz1", "GRsZzgE=");
    test("a", "A2345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234#", "IM3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvc/g==");
  }

  private static void encodeTestJa() {
    test("あいう", "", "44GC44GE44GG");
    test("あいう", "x", "m/n6m/n8m/n+AA==");
    test("あいう", "xyz", "m/j4m/j+m/j8AA==");
    test("あいう", "xyz123456a", "m/j40rO317SwngE=");
  }

  private static void encodeBytesTest() {
    byte[] b = { (byte) 0x61, (byte) 0x62, (byte) 0x63 };
    test(b, "", "YWJj");
    test(b, "x", "GRobAA==");
    test(b, "xyz", "GRsZAA==");
    test(b, "xyz1", "GRsZzgE=");
  }

  private static void test(String s, String key, String expected) {
    String r = Base64s.encode(s, key);
    TestUtil.assertEquals(key, expected, r);
  }

  private static void test(byte[] b, String key, String expected) {
    String r = Base64s.encode(b, key);
    TestUtil.assertEquals(key, expected, r);
  }

}
