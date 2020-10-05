package com.takashiharano.test.util;

import com.takashiharano.util.Log;
import com.takashiharano.util.StrBuilder;

public class StrBuilder_appendlnTest {

  public static void main(String args[]) {
    StrBuilder tb;

    Log.d("---");
    tb = new StrBuilder();
    tb.appendln("");
    Log.d(tb.toString());

    Log.d("---");
    tb = new StrBuilder();
    tb.appendln("abc");
    tb.appendln("123");
    Log.d(tb.toString());
  }

}
