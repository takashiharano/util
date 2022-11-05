package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.TestUtil;

public class ParseDoubleTest {

  public static void main(String args[]) {
    test("-1.5", -1.5);
    test("0", 0);
    test("1.5", 1.5);
    test("a", 0);

    test2("-1.5", -1.5);
    test2("0", 0);
    test2("1.5", 1.5);
    test2("a", -1.5);
  }

  private static void test(String input, double expected) {
    double v = StrUtil.parseDouble(input);
    TestUtil.assertEquals(expected, v);
  }

  private static void test2(String input, double expected) {
    double defaultValue = -1.5f;
    double v = StrUtil.parseDouble(input, defaultValue);
    TestUtil.assertEquals(expected, v);
  }

}
