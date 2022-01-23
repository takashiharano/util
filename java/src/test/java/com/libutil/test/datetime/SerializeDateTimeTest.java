package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class SerializeDateTimeTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    String[][] testData = { { "20200920", "20200920000000000" }, { "2020-09-20", "20200920000000000" },
        { "2020/09/20", "20200920000000000" }, { "20200920 1234", "20200920123400000" },
        { "2020-09-20 12:34", "20200920123400000" }, { "2020/09/20 12:34", "20200920123400000" },
        { "20200920T1234", "20200920123400000" }, { "20200920T123456.789", "20200920123456789" },
        { "20200920T123456.789", "20200920123456789" }, { "2020-09-20 12:34:56.789", "20200920123456789" },
        { "2020-09-20 12:34:56.789", "20200920123456789" }, { "2020/09/03 12:34:56.789", "20200903123456789" },
        { "2020/09/03 12:34:56.789", "20200903123456789" }, { "2020/9/3 12:34:56.789", "20200903123456789" },
        { "2020/9/3 12:34:56.789", "20200903123456789" }, { "20200903123456789", "20200903123456789" },
        { "2020-09-20T12:34:56.789+09:00", "20200920123456789+0900" },
        { "2020-09-20T12:34:56.789Z", "20200920123456789+0000" } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in = data[0];
      String data_expected = data[1];

      String out = DateTime.serializeDateTime(data_in);
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
