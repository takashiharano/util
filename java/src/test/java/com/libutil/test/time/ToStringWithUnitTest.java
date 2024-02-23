package com.libutil.test.time;

import com.libutil.Time;
import com.libutil.test.Log;

public class ToStringWithUnitTest {

  public static void main(String args[]) {
    long[] testData = { 0, 1, 10, 100, 1000, 1001, 1010, 1100, 1234, 59001, 59010, 59100, 59999, 60000, 60001, 60010, 60100, 3600000, 86400000, 86400001, 171959000, 171959001, 171959010, 171959123, -1, -1000, -60000 };

    Log.d("---");
    for (int i = 0; i < testData.length; i++) {
      long data = testData[i];
      Log.d(Time.toStringWithUnit(data));
    }

    Log.d("---");
    for (int i = 0; i < testData.length; i++) {
      long data = testData[i];
      Log.d(Time.toStringWithUnit(data, 0));
    }

    Log.d("---");
    for (int i = 0; i < testData.length; i++) {
      long data = testData[i];
      Log.d(Time.toStringWithUnit(data, 1));
    }

    Log.d("---");
    for (int i = 0; i < testData.length; i++) {
      long data = testData[i];
      Log.d(Time.toStringWithUnit(data, 2));
    }
  }

}
