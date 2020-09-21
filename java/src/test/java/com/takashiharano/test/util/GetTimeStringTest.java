package com.takashiharano.test.util;

import com.takashiharano.util.Log;
import com.takashiharano.util.Util;

public class GetTimeStringTest {

  public static void main(String args[]) {
    long[] testData = { 0, 1, 10, 100, 1000, 1001, 1010, 1100, 1234, 59001, 59010, 59100, 59999, 60000, 60001, 60010,
        60100, 3600000, 86400000, 86400001, 171959000, 171959001, 171959010, 171959123 };
    for (int i = 0; i < testData.length; i++) {
      long data = testData[i];
      Log.d(Util.getTimeString(data));
    }

    Log.d("---");

    for (int i = 0; i < testData.length; i++) {
      long data = testData[i];
      Log.d(Util.getTimeString(data, false, true));
    }

    Log.d("---");

    for (int i = 0; i < testData.length; i++) {
      long data = testData[i];
      Log.d(Util.getTimeString(data, true, true));
    }

  }

}
