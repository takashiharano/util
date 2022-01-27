package com.libutil.test.time;

import com.libutil.Time;
import com.libutil.test.Log;

public class ToStringTest {

  public static void main(String args[]) {
    test(-1);
    test(0);
    test(1);
    test(100);
    test(1000);
    test(1234);
    test(60000);
    test(3600000);
    test(86400000);
    test(171954123);
    test(1719541230);
  }

  public static void test(long t) {
    Log.i(t + " = " + Time.toString(t, "HH:mm:ss.SSS"));
    Log.i(t + " = " + Time.toString(t, "HR:mm:ss.SSS"));
    Log.i(t + " = " + Time.toString(t, "Dd H24:mm:ss.SSS"));
    Log.i(t + " = " + Time.toString(t, "THH:mm:ss.SSS"));
    Log.i(t + " = " + Time.toString(t, "T+HH:mm:ss.SSS"));
    Log.i("");
  }

}
