package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class XlsColTest {
  public static void main(String args[]) {
    testN2A(0);
    testN2A(1);
    testN2A(2);
    testN2A(3);
    testN2A(26);
    testN2A(27);
    testN2A(28);
    testN2A(16383);
    testN2A(16384); // max
    testN2A(16385);

    testA2N("");
    testA2N("A");
    testA2N("B");
    testA2N("C");
    testA2N("Z");
    testA2N("AA");
    testA2N("AB");
    testA2N("XFC");
    testA2N("XFD"); // max
    testA2N("XFE");

    testA2N("", 1);
    testA2N("A", -2);
    testA2N("A", -1);
    testA2N("A", 0);
    testA2N("A", 1);
    testA2N("A", 2);
    testA2N("B", -2);
    testA2N("B", -1);
    testA2N("B", 0);
    testA2N("B", 1);
    testA2N("B", 2);
  }

  public static void testN2A(int n) {
    Log.i(n + " = " + StrUtil.xlscol(n));
  }

  public static void testA2N(String s) {
    Log.i(s + " = " + StrUtil.xlscol(s));

  }

  public static void testA2N(String s, int offset) {
    Log.i(s + "+" + offset + " = " + StrUtil.xlscol(s, offset));
  }

}
