package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class HasBomTest {

  public static void main(String args[]) {
    HasBomTest tester = new HasBomTest();
    tester.test();
  }

  private void test() {
    test1();
    test2();
    test3();
    test4();
  }

  private void test1() {
    char c = 0xFEFF;
    StringBuilder sb = new StringBuilder();
    sb.append(c);
    sb.append("abc");
    String s = sb.toString();
    Log.dumpString(s);
    Log.i(StrUtil.hasBom(s));
  }

  private void test2() {
    String s = "abc";
    Log.dumpString(s);
    Log.i(StrUtil.hasBom(s));
  }

  private void test3() {
    String s = "";
    Log.dumpString(s);
    Log.i(StrUtil.hasBom(s));
  }

  private void test4() {
    String s = null;
    Log.dumpString(s);
    Log.i(StrUtil.hasBom(s));
  }

}
