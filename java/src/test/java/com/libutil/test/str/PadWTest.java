package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class PadWTest {

  public static void main(String args[]) {
    Log.i("leftPad() ----");
    leftPadWTest("aあ", " ", -1);
    leftPadWTest("aあ", " ", 0);
    leftPadWTest("aあ", " ", 1);
    leftPadWTest("aあ", " ", 2);
    leftPadWTest("aあ", " ", 3);
    leftPadWTest("aあ", " ", 4);
    leftPadWTest("aあ", " ", 5);

    Log.i("leftPad(align=true) ----");
    // leftPadTest2("aあ", " ", -1); // StringIndexOutOfBoundsException
    leftPadWTest2("aあ", " ", 0);
    leftPadWTest2("aあ", " ", 1);
    leftPadWTest2("aあ", " ", 2);
    leftPadWTest2("aあ", " ", 3);
    leftPadWTest2("aあ", " ", 4);
    leftPadWTest2("aあ", " ", 5);

    Log.i("rightPad() ----");
    rightPadWTest("aあ", " ", -1);
    rightPadWTest("aあ", " ", 0);
    rightPadWTest("aあ", " ", 1);
    rightPadWTest("aあ", " ", 2);
    rightPadWTest("aあ", " ", 3);
    rightPadWTest("aあ", " ", 4);
    rightPadWTest("aあ", " ", 5);

    Log.i("rightPad(align=true) ----");
    // rightPadTest2("aあ", " ", -1); // StringIndexOutOfBoundsException
    rightPadWTest2("aあ", " ", 0);
    rightPadWTest2("aあ", " ", 1);
    rightPadWTest2("aあ", " ", 2);
    rightPadWTest2("aあ", " ", 3);
    rightPadWTest2("aあ", " ", 4);
    rightPadWTest2("aあ", " ", 5);
  }

  private static void leftPadWTest(String s1, String s2, int len) {
    Log.i(len + ": \"" + StrUtil.leftPadW(s1, s2, len) + "\"");
  }

  private static void leftPadWTest2(String s1, String s2, int len) {
    Log.i(len + ": \"" + StrUtil.leftPadW(s1, s2, len, true) + "\"");
  }

  private static void rightPadWTest(String s1, String s2, int len) {
    Log.i(len + ": \"" + StrUtil.rightPadW(s1, s2, len) + "\"");
  }

  private static void rightPadWTest2(String s1, String s2, int len) {
    Log.i(len + ": \"" + StrUtil.rightPadW(s1, s2, len, true) + "\"");
  }

}
