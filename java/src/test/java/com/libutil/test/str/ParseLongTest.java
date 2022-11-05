package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.TestUtil;

public class ParseLongTest {

  public static void main(String args[]) {
    test("-1", -1);
    test("0", 0);
    test("1", 1);
    test("a", 0);

    test2("-1", -1);
    test2("0", 0);
    test2("1", 1);
    test2("a", -1);
  }

  private static void test(String input, long expected) {
    long v = StrUtil.parseLong(input);
    TestUtil.assertEquals(expected, v);
  }

  private static void test2(String input, long expected) {
    long defaultValue = -1;
    long v = StrUtil.parseLong(input, defaultValue);
    TestUtil.assertEquals(expected, v);
  }

}
