package com.libutil.test.time;

import com.libutil.Time;
import com.libutil.test.Log;

public class TimeToStringTest {

  public static void main(String args[]) {
    long[] testData = { 0, 1, 100, 1234, 60000, 3600000, 86400000, 171954123, 1719541230, -1719541230 };
    for (int i = 0; i < testData.length; i++) {
      test(testData[i]);
    }

    Log.i("-- static ----------");
    for (int i = 0; i < testData.length; i++) {
      test2(testData[i]);
    }
  }

  public static void test(long t) {
    Time time = new Time(t);
    Log.i(t + " = " + time.toString("HH:mm:ss.SSS"));
    Log.i(t + " = " + time.toString("HR:mm:ss.SSS"));
    Log.i(t + " = " + time.toString("Dd HH24:mm:ss.SSS"));
    Log.i("");
  }

  public static void test2(long t) {
    Log.i(t + " = " + Time.toString(t, "HH:mm:ss.SSS"));
    Log.i(t + " = " + Time.toString(t, "HR:mm:ss.SSS"));
    Log.i(t + " = " + Time.toString(t, "Dd HH24:mm:ss.SSS"));
    Log.i("");
  }

}
