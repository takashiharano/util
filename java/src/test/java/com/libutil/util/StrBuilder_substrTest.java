package com.libutil.util;

import com.libutil.Log;
import com.libutil.StrBuilder;

public class StrBuilder_substrTest {

  public static void main(String args[]) {
    StrBuilder tb;

    Log.d("---");
    tb = new StrBuilder();
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
