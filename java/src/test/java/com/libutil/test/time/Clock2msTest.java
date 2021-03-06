package com.libutil.test.time;

import com.libutil.Time;
import com.libutil.test.Log;

public class Clock2msTest {

  public static void main(String args[]) {
    String[] testData = { "01:00", "01:00:30", "01:00:30.123", "0100", "010030", "010030.123", "010030.1", "010030.01",
        "010030.001", "+01:00", "-01:00" };

    Log.d("---");
    for (int i = 0; i < testData.length; i++) {
      String data = testData[i];
      Log.d(Time.clock2ms(data));
    }

  }

}
