package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class IsTrueTest {

  public static void main(String args[]) {
    Log.i("---------- false ----------");
    test(null, false);
    test("", false);
    test("0", false);
    test("false", false);
    test("False", false);
    test("FALSE", false);

    test(" ", false);
    test("  ", false);
    test(" 0 ", false);
    test(" false ", false);
    test(" False ", false);
    test(" FALSE ", false);

    Log.i("---------- true ----------");
    test("1", true);
    test("2", true);
    test("true", true);
    test("True", true);
    test("TRUE", true);
    test("A", true);

    test(" 1 ", true);
    test(" 2 ", true);
    test(" true ", true);
    test(" True ", true);
    test(" TRUE ", true);
    test(" A ", true);
  }

  private static void test(String s, boolean exp) {
    String st = "NG";
    boolean got = StrUtil.isTrue(s);
    if (got == exp) {
      st = "OK";
    }
    String ret = "[" + st + "] " + s + " exp=" + exp + " git=" + got;
    Log.i(ret);
  }

}
