package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class ToSingleSpaceTest {

  public static void main(String args[]) {
    test(null);
    test("");
    test("abc");
    test("a b");
    test("a  b");
    test("a b c");
    test("a  b c");
    test("a   b");
    test(" a b ");
    test(" a  b ");
    test("  a  b  ");
    test("a\tb");
    test("a\t\tb");
    test("a \tb");
    test("a  \tb");
    test("a　b");
    test("a　　b");
    test("a 　b");
    test("a  　b");
  }

  public static void test(String s) {
    String r = StrUtil.toSingleSpace(s);
    String ret = null;
    if (r != null) {
      ret = "\"" + r + "\"";
    }
    Log.i(ret);
  }

}
