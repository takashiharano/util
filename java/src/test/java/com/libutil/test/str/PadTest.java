package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class PadTest {

  public static void main(String args[]) {
    Log.i("leftPad() ----");
    leftPadTest("abc", " ", -1);
    leftPadTest("abc", " ", 0);
    leftPadTest("abc", " ", 2);
    leftPadTest("abc", " ", 3);
    leftPadTest("abc", " ", 4);
    leftPadTest("abc", " ", 5);

    Log.i("leftPad(align=true) ----");
    // leftPadTest2("abc", " ", -1); // StringIndexOutOfBoundsException
    leftPadTest2("abc", " ", 0);
    leftPadTest2("abc", " ", 2);
    leftPadTest2("abc", " ", 3);
    leftPadTest2("abc", " ", 4);
    leftPadTest2("abc", " ", 5);

    Log.i("rightPad() ----");
    rightPadTest("abc", " ", -1);
    rightPadTest("abc", " ", 0);
    rightPadTest("abc", " ", 2);
    rightPadTest("abc", " ", 3);
    rightPadTest("abc", " ", 4);
    rightPadTest("abc", " ", 5);

    Log.i("rightPad(align=true) ----");
    // rightPadTest2("abc", " ", -1); // StringIndexOutOfBoundsException
    rightPadTest2("abc", " ", 0);
    rightPadTest2("abc", " ", 2);
    rightPadTest2("abc", " ", 3);
    rightPadTest2("abc", " ", 4);
    rightPadTest2("abc", " ", 5);
  }

  private static void leftPadTest(String s1, String s2, int len) {
    Log.i("\"" + StrUtil.leftPad(s1, s2, len) + "\"");
  }

  private static void leftPadTest2(String s1, String s2, int len) {
    Log.i("\"" + StrUtil.leftPad(s1, s2, len, true) + "\"");
  }

  private static void rightPadTest(String s1, String s2, int len) {
    Log.i("\"" + StrUtil.rightPad(s1, s2, len) + "\"");
  }

  private static void rightPadTest2(String s1, String s2, int len) {
    Log.i("\"" + StrUtil.rightPad(s1, s2, len, true) + "\"");
  }

}
