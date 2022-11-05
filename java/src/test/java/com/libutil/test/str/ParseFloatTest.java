package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.TestUtil;

public class ParseFloatTest {

  public static void main(String args[]) {
    test("-1.5", -1.5f);
    test("0", 0);
    test("1.5", 1.5f);
    test("a", 0);

    test2("-1.5", -1.5f);
    test2("0", 0);
    test2("1.5", 1.5f);
    test2("a", -1.5f);
  }

  private static void test(String input, float expected) {
    float v = StrUtil.parseFloat(input);
    TestUtil.assertEquals(expected, v);
  }

  private static void test2(String input, float expected) {
    float defaultValue = -1.5f;
    float v = StrUtil.parseFloat(input, defaultValue);
    TestUtil.assertEquals(expected, v);
  }

}
