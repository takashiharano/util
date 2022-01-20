package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class NormalizeTimeZoneTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    String[][] testData = { { "-0800", "-0800" }, { "-08:00", "-0800" }, { "Z", "+0000" }, { "+0", "+0000" },
        { "-0000", "+0000" }, { "+0900", "+0900" }, { "+09:00", "+0900" }, { "+9", "+0900" }, { "+900", "+0900" },
        { "+9.5", "+0930" }, { "+10", "+1000" }, { "+12.75", "+1245" } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in = data[0];
      String data_expected = data[1];

      String out = DateTime.normalizeTimeZone(data_in);
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
