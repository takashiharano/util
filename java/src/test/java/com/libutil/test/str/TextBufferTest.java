package com.libutil.test.str;

import com.libutil.TextBuffer;
import com.libutil.test.Log;

public class TextBufferTest {

  public static void main(String args[]) {
    appendTest();
    substrTest();
  }

  public static void appendTest() {
    char[] chs = { 'c', 'd' };
    StringBuilder sb1 = new StringBuilder("sb");
    TextBuffer tb1 = new TextBuffer("tb");

    Log.d("---");

    TextBuffer tb = new TextBuffer();

    tb.append("a").append('b').append(chs).append(1).append(2L).append(3.4f).append(5.6).append(true).append(sb1).append(tb1).newLine().appendln("xyz");

    tb.appendln("a").appendln('b').appendln(chs).appendln(1).appendln(2L).appendln(3.4f).appendln(5.6).appendln(true).appendln(sb1).appendln(tb1).newLine().appendln("xyz");

    Log.d(tb.toString());
  }

  public static void substrTest() {
    TextBuffer tb;

    Log.d("---");
    tb = new TextBuffer();
    tb.append("0123456789");
    Log.d(tb.substr(0, 3));
    Log.d(tb.substr(0, 0));
    Log.d(tb.substr(-1, -1));
    Log.d(tb.substr(3, -2));
    Log.d(tb.substr(3, 15));

    Log.d("---");
    Log.d(tb.substr(0));
    Log.d(tb.substr(1));
    Log.d(tb.substr(10));
    Log.d(tb.substr(-1));
    Log.d(tb.substr(-2));
    Log.d(tb.substr(-10));
    Log.d(tb.substr(-11));
  }

}
