package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.StrUtil;
import com.libutil.test.Log;

public class SplitDatetimeAndTimezoneTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    String[][] testData = { { "20200920T123456.789+09:00", "20200920T123456.789", "+09:00" },
        { "2020-09-20T12:34:56.789+09:00", "2020-09-20T12:34:56.789", "+09:00" },
        { "2020-09-20T12:34:56.789-09:00", "2020-09-20T12:34:56.789", "-09:00" },
        { "2020-09-20T12:34:56.789Z", "2020-09-20T12:34:56.789", "Z" },
        { "2020-09-20T12:34:56.789", "2020-09-20T12:34:56.789", null },
        { "12:34:56.789+09:00", "12:34:56.789", "+09:00" }, { "12:34+09:00", "12:34", "+09:00" },
        { "12:34:56.789-09:00", "12:34:56.789", "-09:00" }, { "12:34-09:00", "12:34", "-09:00" },
        { "1234-0900", "1234", "-0900" }, { "1234+0900", "1234", "+0900" }, { "T1234-0900", "T1234", "-0900" },
        { "T1234+0900", "T1234", "+0900" }, { "12:34:56.789+0900", "12:34:56.789", "+0900" },
        { "12:34+0900", "12:34", "+0900" }, { "12:34:56.789-0900", "12:34:56.789", "-0900" },
        { "12:34-0900", "12:34", "-0900" }, { "2022-01-24", "2022-01-24", null } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in = data[0];
      String data_expected0 = data[1];
      String data_expected1 = data[2];

      String[] out = DateTime.splitDateTimeAndTimezone(data_in);
      String ret;
      if (StrUtil.equals(out[0], data_expected0) && StrUtil.equals(out[1], data_expected1)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in + " " + "EXP0=" + data_expected0 + " OUT0=" + out[0] + " " + "EXP1="
          + data_expected1 + " OUT1=" + out[1]);
    }
  }

}
