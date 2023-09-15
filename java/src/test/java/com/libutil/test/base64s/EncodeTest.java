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
    test("", "", "");
    test("", "x", "");
    test("abc", "", "YWJj");
    test("abc", "x", "ABkaGw==");
    test("abc", "xyz", "ABkbGQ==");
    test("abc", "xyz1", "ARkbGc4=");
    test("a", "A2345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234#", "/iDNzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczL3A==");
  }

  private static void encodeTestJa() {
    test("あいう", "", "44GC44GE44GG");
    test("あいう", "x", "AJv5+pv5/Jv5/g==");
    test("あいう", "xyz", "AJv4+Jv4/pv4/A==");
    test("あいう", "xyz123456a", "AZv4+NKzt9e0sJ4=");
  }

  private static void encodeBytesTest() {
    byte[] b = { (byte) 0x61, (byte) 0x62, (byte) 0x63 };
    test(b, "", "YWJj");
    test(b, "x", "ABkaGw==");
    test(b, "xyz", "ABkbGQ==");
    test(b, "xyz1", "ARkbGc4=");
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
