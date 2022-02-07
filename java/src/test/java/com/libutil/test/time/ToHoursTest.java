package com.libutil.test.time;

import com.libutil.Time;
import com.libutil.test.Log;

public class ToHoursTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    String[][] testData = { { "00:00", "0.0" }, { "00:15", "0.25" }, { "00:30", "0.5" }, { "00:45", "0.75" },
        { "01:00", "1.0" }, { "01:15", "1.25" }, { "01:30", "1.5" }, { "01:45", "1.75" }, { "10:00", "10.0" },
        { "10:15", "10.25" }, { "10:30", "10.5" }, { "10:45", "10.75" }, { "123:00", "123.0" }, { "123:15", "123.25" },
        { "123:30", "123.5" }, { "123:45", "123.75" }, { "0000", "0.0" }, { "0015", "0.25" }, { "0030", "0.5" },
        { "0045", "0.75" }, { "0100", "1.0" }, { "0115", "1.25" }, { "0130", "1.5" }, { "0145", "1.75" },
        { "1000", "10.0" }, { "1015", "10.25" }, { "1030", "10.5" }, { "1045", "10.75" }, { "12300", "123.0" },
        { "12315", "123.25" }, { "12330", "123.5" }, { "12345", "123.75" } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in = data[0];
      String data_expected = data[1];

      float h = Time.toHours(data_in);
      String out = Float.toString(h);
      String ret;
      if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

}
