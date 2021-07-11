package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class FormatTimeTest {

  public static void main(String args[]) {
    test(0);
    test(1);
    test(100);
    test(1000);
    test(1234);
    test(60000);
    test(3600000);
    test(86400000);
    test(171954123);
  }

  public static void test(long t) {
    Log.i(t + " = " + DateTime.formatTime(t, "DdHH:mm:ss.SSS"));
  }

}
