package com.takashiharano.test.util;

import com.takashiharano.util.Log;
import com.takashiharano.util.TextBuilder;

public class TextBuilder_appendlnTest {

  public static void main(String args[]) {
    TextBuilder tb;

    Log.d("---");
    tb = new TextBuilder();
    tb.appendln("");
    Log.d(tb.toString());

    Log.d("---");
    tb = new TextBuilder();
    tb.appendln("abc");
    tb.appendln("123");
    Log.d(tb.toString());
  }

}
