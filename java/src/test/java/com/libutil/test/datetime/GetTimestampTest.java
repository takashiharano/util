package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class GetTimestampTest {

  public static void main(String args[]) {
    String in;
    long exp;

    // Local Time
    in = "20210703T123456.789";
    exp = 1625283296789L;
    test(in, exp);

    in = "2021-07-03 12:34:56.789";
    exp = 1625283296789L;
    test(in, exp);

    in = "20210703123456789";
    exp = 1625283296789L;
    test(in, exp);

    // +0900 ------------------------------
    in = "20210703T123456.789+0900";
    exp = 1625283296789L;
    test(in, exp);

    in = "2021-07-03 12:34:56.789 +09:00";
    exp = 1625283296789L;
    test(in, exp);

    // +0800 ------------------------------
    in = "20210703T123456.789+0800";
    exp = 1625286896789L;
    test(in, exp);

    in = "2021-07-03 12:34:56.789 +08:00";
    exp = 1625286896789L;
    test(in, exp);

    // +0000 ------------------------------
    in = "20210703T123456.789+0000";
    exp = 1625315696789L;
    test(in, exp);

    in = "2021-07-03 12:34:56.789+00:00";
    exp = 1625315696789L;
    test(in, exp);

    // Z ------------------------------
    in = "20210703T123456.789Z";
    exp = 1625315696789L;
    test(in, exp);

    in = "2021-07-03 12:34:56.789Z";
    exp = 1625315696789L;
    test(in, exp);

    // -0800 ------------------------------
    in = "20210703T123456.789+0800";
    exp = 1625286896789L;
    test(in, exp);

    in = "2021-07-03 12:34:56.789 -08:00";
    exp = 1625344496789L;
    test(in, exp);
  }

  private static void test(String in, long exp) {
    long timestamp = DateTime.getTimestamp(in);
    String ret = "NG";
    if (timestamp == exp) {
      ret = "OK";
    }
    Log.i("[" + ret + "] IN=" + in + " EXP=" + exp + " OUT=" + timestamp);
  }

}
