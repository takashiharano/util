package com.libutil.test.str;

import com.libutil.TextBuffer;
import com.libutil.test.Log;

public class TextBuffer_appendlnTest {

  public static void main(String args[]) {
    TextBuffer tb;

    Log.d("---");
    tb = new TextBuffer();
    tb.appendln("");
    Log.d(tb.toString());

    Log.d("---");
    tb = new TextBuffer();
    tb.appendln("abc");
    tb.appendln("123");
    Log.d(tb.toString());

    Log.d("---");
    tb = new TextBuffer();
    tb.appendln("abc");
    tb.appendln();
    tb.appendln("123");
    Log.d(tb.toString());
  }

}
