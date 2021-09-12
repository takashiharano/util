package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class EqualsTest {

  public static void main(String args[]) {
    Log.i("equals() ----");
    test("abc", "abc");
    test(null, null);
    test("abc", "Abc");
    test("abc", null);
    test(null, "abc");
    test("abc", "xyz");

    Log.i("equalsIgnoreCase() ----");
    testIgnoreCase("abc", "abc");
    testIgnoreCase(null, null);
    testIgnoreCase("abc", "Abc");
    testIgnoreCase("abc", null);
    testIgnoreCase(null, "abc");
    testIgnoreCase("abc", "xyz");
  }

  private static void test(String s1, String s2) {
    Log.i("s1=" + s1 + " s2=" + s2 + " : " + StrUtil.equals(s1, s2));
  }

  private static void testIgnoreCase(String s1, String s2) {
    Log.i("s1=" + s1 + " s2=" + s2 + " : " + StrUtil.equalsIgnoreCase(s1, s2));
  }

}
