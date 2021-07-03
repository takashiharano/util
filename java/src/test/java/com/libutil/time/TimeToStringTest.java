package com.libutil.time;

import com.libutil.Log;
import com.libutil.Time;

public class TimeToStringTest {

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
    Time time = new Time(t);
    Log.i(t + " = " + time.toString("HR:mm:ss.SSS"));
    Log.i(t + " = " + time.toString("DdHH:mm:ss.SSS"));
    Log.i("");
  }

}
