package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.TestUtil;

public class ParseIntTest {

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

  private static void test(String input, int expected) {
    int v = StrUtil.parseInt(input);
    TestUtil.assertEquals(expected, v);
  }

  private static void test2(String input, int expected) {
    int defaultValue = -1;
    int v = StrUtil.parseInt(input, defaultValue);
    TestUtil.assertEquals(expected, v);
  }

}
