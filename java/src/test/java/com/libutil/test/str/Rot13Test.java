package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.TestUtil;
import com.libutil.test.Log;

public class Rot13Test {

  public static void main(String args[]) {
    test1();
    test2();
    test3();
    test4();
  }

  private static void test1() {
    Log.i("Test1");
    TestUtil.assertEquals("NOPQRSTUVWXYZABCDEFGHIJKLM", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    TestUtil.assertEquals("nopqrstuvwxyzabcdefghijklm", StrUtil.rot13("abcdefghijklmnopqrstuvwxyz"));
    TestUtil.assertEquals("1234567890", StrUtil.rot13("1234567890"));

    TestUtil.assertEquals("NOPQRSTUVWXYZABCDEFGHIJKLMnopqrstuvwxyzabcdefghijklm1234567890", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"));

    TestUtil.assertEquals("n\r\no", StrUtil.rot13("a\r\nb"));
    TestUtil.assertEquals("\t", StrUtil.rot13("\t"));
    TestUtil.assertEquals("", StrUtil.rot13(""));
    TestUtil.assertEquals("あ", StrUtil.rot13("あ"));
    TestUtil.assertEquals("nあ", StrUtil.rot13("aあ"));
    TestUtil.assertEquals(null, StrUtil.rot13(null));
  }

  private static void test2() {
    Log.i("Test2");
    TestUtil.assertEquals("BCDEFGHIJKLMNOPQRSTUVWXYZA", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1));
    TestUtil.assertEquals("bcdefghijklmnopqrstuvwxyza", StrUtil.rot13("abcdefghijklmnopqrstuvwxyz", 1));
    TestUtil.assertEquals("1234567890", StrUtil.rot13("1234567890", 1));

    TestUtil.assertEquals("BCDEFGHIJKLMNOPQRSTUVWXYZAbcdefghijklmnopqrstuvwxyza1234567890", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890", 1));

    TestUtil.assertEquals("b\r\nc", StrUtil.rot13("a\r\nb", 1));
    TestUtil.assertEquals("\t", StrUtil.rot13("\t", 1));
    TestUtil.assertEquals("", StrUtil.rot13("", 1));
    TestUtil.assertEquals("あ", StrUtil.rot13("あ", 1));
    TestUtil.assertEquals("bあ", StrUtil.rot13("aあ", 1));
    TestUtil.assertEquals(null, StrUtil.rot13(null, 1));
  }

  private static void test3() {
    Log.i("Test3");
    TestUtil.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", StrUtil.rot13("BCDEFGHIJKLMNOPQRSTUVWXYZA", -1));
    TestUtil.assertEquals("abcdefghijklmnopqrstuvwxyz", StrUtil.rot13("bcdefghijklmnopqrstuvwxyza", -1));
    TestUtil.assertEquals("1234567890", StrUtil.rot13("1234567890", -1));

    TestUtil.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890", StrUtil.rot13("BCDEFGHIJKLMNOPQRSTUVWXYZAbcdefghijklmnopqrstuvwxyza1234567890", -1));

    TestUtil.assertEquals("a\r\nb", StrUtil.rot13("b\r\nc", -1));
    TestUtil.assertEquals("\t", StrUtil.rot13("\t", -1));
    TestUtil.assertEquals("", StrUtil.rot13("", -1));
    TestUtil.assertEquals("あ", StrUtil.rot13("あ", -1));
    TestUtil.assertEquals("aあ", StrUtil.rot13("bあ", -1));
    TestUtil.assertEquals(null, StrUtil.rot13(null, -1));
  }

  private static void test4() {
    Log.i("Test4");
    TestUtil.assertEquals("ZABCDEFGHIJKLMNOPQRSTUVWXY", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", -27));
    TestUtil.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", -26));
    TestUtil.assertEquals("BCDEFGHIJKLMNOPQRSTUVWXYZA", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", -25));
    TestUtil.assertEquals("ZABCDEFGHIJKLMNOPQRSTUVWXY", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", -1));
    TestUtil.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 0));
    TestUtil.assertEquals("BCDEFGHIJKLMNOPQRSTUVWXYZA", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1));
    TestUtil.assertEquals("CDEFGHIJKLMNOPQRSTUVWXYZAB", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 2));
    TestUtil.assertEquals("NOPQRSTUVWXYZABCDEFGHIJKLM", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 13));
    TestUtil.assertEquals("OPQRSTUVWXYZABCDEFGHIJKLMN", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 14));
    TestUtil.assertEquals("ZABCDEFGHIJKLMNOPQRSTUVWXY", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 25));
    TestUtil.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 26));
    TestUtil.assertEquals("BCDEFGHIJKLMNOPQRSTUVWXYZA", StrUtil.rot13("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 27));
  }

}
