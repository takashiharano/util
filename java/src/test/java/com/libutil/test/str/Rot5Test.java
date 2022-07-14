package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.TestUtil;
import com.libutil.test.Log;

public class Rot5Test {

  public static void main(String args[]) {
    test1();
    test2();
    test3();
    test4();
  }

  private static void test1() {
    Log.i("Test1");
    TestUtil.assertEquals("6789012345", StrUtil.rot5("1234567890"));
    TestUtil.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz6789012345", StrUtil.rot5("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"));

    TestUtil.assertEquals("6\r\n7", StrUtil.rot5("1\r\n2"));
    TestUtil.assertEquals("\t", StrUtil.rot5("\t"));
    TestUtil.assertEquals("", StrUtil.rot5(""));
    TestUtil.assertEquals("あ", StrUtil.rot5("あ"));
    TestUtil.assertEquals("6あ", StrUtil.rot5("1あ"));
    TestUtil.assertEquals(null, StrUtil.rot5(null));
  }

  private static void test2() {
    Log.i("Test2");
    TestUtil.assertEquals("2345678901", StrUtil.rot5("1234567890", 1));

    TestUtil.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz2345678901", StrUtil.rot5("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890", 1));

    TestUtil.assertEquals("2\r\n3", StrUtil.rot5("1\r\n2", 1));
    TestUtil.assertEquals("\t", StrUtil.rot5("\t", 1));
    TestUtil.assertEquals("", StrUtil.rot5("", 1));
    TestUtil.assertEquals("あ", StrUtil.rot5("あ", 1));
    TestUtil.assertEquals("2あ", StrUtil.rot5("1あ", 1));
    TestUtil.assertEquals(null, StrUtil.rot5(null, 1));
  }

  private static void test3() {
    Log.i("Test3");
    TestUtil.assertEquals("1234567890", StrUtil.rot5("2345678901", -1));

    TestUtil.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890", StrUtil.rot5("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz2345678901", -1));

    TestUtil.assertEquals("1\r\n2", StrUtil.rot5("2\r\n3", -1));
    TestUtil.assertEquals("\t", StrUtil.rot5("\t", -1));
    TestUtil.assertEquals("", StrUtil.rot5("", -1));
    TestUtil.assertEquals("あ", StrUtil.rot5("あ", -1));
    TestUtil.assertEquals("1あ", StrUtil.rot5("2あ", -1));
    TestUtil.assertEquals(null, StrUtil.rot5(null, -1));
  }

  private static void test4() {
    Log.i("Test4");
    TestUtil.assertEquals("0123456789", StrUtil.rot5("0123456789", -10));
    TestUtil.assertEquals("1234567890", StrUtil.rot5("0123456789", -9));
    TestUtil.assertEquals("2345678901", StrUtil.rot5("0123456789", -8));
    TestUtil.assertEquals("3456789012", StrUtil.rot5("0123456789", -7));
    TestUtil.assertEquals("4567890123", StrUtil.rot5("0123456789", -6));
    TestUtil.assertEquals("5678901234", StrUtil.rot5("0123456789", -5));
    TestUtil.assertEquals("6789012345", StrUtil.rot5("0123456789", -4));
    TestUtil.assertEquals("7890123456", StrUtil.rot5("0123456789", -3));
    TestUtil.assertEquals("8901234567", StrUtil.rot5("0123456789", -2));
    TestUtil.assertEquals("9012345678", StrUtil.rot5("0123456789", -1));
    TestUtil.assertEquals("0123456789", StrUtil.rot5("0123456789", 0));
    TestUtil.assertEquals("1234567890", StrUtil.rot5("0123456789", 1));
    TestUtil.assertEquals("2345678901", StrUtil.rot5("0123456789", 2));
    TestUtil.assertEquals("3456789012", StrUtil.rot5("0123456789", 3));
    TestUtil.assertEquals("4567890123", StrUtil.rot5("0123456789", 4));
    TestUtil.assertEquals("5678901234", StrUtil.rot5("0123456789", 5));
    TestUtil.assertEquals("6789012345", StrUtil.rot5("0123456789", 6));
    TestUtil.assertEquals("7890123456", StrUtil.rot5("0123456789", 7));
    TestUtil.assertEquals("8901234567", StrUtil.rot5("0123456789", 8));
    TestUtil.assertEquals("9012345678", StrUtil.rot5("0123456789", 9));
    TestUtil.assertEquals("0123456789", StrUtil.rot5("0123456789", 10));
  }

}
