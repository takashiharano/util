package com.libutil.test.time;

import com.libutil.Time;
import com.libutil.test.Log;

public class ParseMillisTest {

  public static void main(String args[]) {
    String[] testData = { "00:00", "00:00:00", "00:00:00.000", "00:00:00.001", "00:00:01", "00:01", "01:00",
        "01:00:00.001", "01:00:01", "01:00:30", "01:00:30.123", "0100", "010030", "010030.123", "010030.1", "010030.01",
        "010030.001", "+01:00", "-01:00", "1d23:45:56.789", "-1d23:45:56.789" };

    Log.d("---");
    for (int i = 0; i < testData.length; i++) {
      String data = testData[i];
      Log.d(Time.parseMillis(data));
    }

  }

}
