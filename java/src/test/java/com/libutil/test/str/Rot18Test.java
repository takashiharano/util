package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.TestUtil;
import com.libutil.test.Log;

public class Rot18Test {

  public static void main(String args[]) {
    test1();
    test2();
    test3();
  }

  private static void test1() {
    Log.i("Test1");
    TestUtil.assertEquals("NOPQRSTUVWXYZABCDEFGHIJKLMnopqrstuvwxyzabcdefghijklm6789012345", StrUtil.rot18("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"));
    TestUtil.assertEquals(null, StrUtil.rot18(null));
  }

  private static void test2() {
    Log.i("Test2");
    TestUtil.assertEquals("BCDEFGHIJKLMNOPQRSTUVWXYZAbcdefghijklmnopqrstuvwxyza2345678901", StrUtil.rot18("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890", 1));
    TestUtil.assertEquals(null, StrUtil.rot18(null, 1));
  }

  private static void test3() {
    Log.i("Test3");
    TestUtil.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890", StrUtil.rot18("BCDEFGHIJKLMNOPQRSTUVWXYZAbcdefghijklmnopqrstuvwxyza2345678901", -1));
  }

}
