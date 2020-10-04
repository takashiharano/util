package com.takashiharano.test.util;

import com.takashiharano.util.Log;
import com.takashiharano.util.TextBuilder;

public class TextBuilder_substrTest {

  public static void main(String args[]) {
    TextBuilder tb;

    Log.d("---");
    tb = new TextBuilder();
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
