package com.libutil.util;

import com.libutil.Log;
import com.libutil.StrBuilder;

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
