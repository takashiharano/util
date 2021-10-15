package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class ToStringTest {

  public static void main(String args[]) {
    staticTest();
    instanceTest();
  }

  public static void staticTest() {
    String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    String in = "20210101";
    String exp = "2021-01-01T00:00:00.000";
    test(in, exp, format);

    in = "2021/01/01";
    exp = "2021-01-01T00:00:00.000";
    test(in, exp, format);

    in = "2021/01/01 0:00:00";
    exp = "2021-01-01T00:00:00.000";
    test(in, exp, format);

    in = "2021-01-02T12:34:56.789";
    exp = "2021-01-02T12:34:56.789";
    test(in, exp, format);

    in = "20210101123456789";
    exp = "2021-01-01T12:34:56.789";
    test(in, exp, format);

    format = "yyyy-MM-dd";

    in = "20210101";
    exp = "2021-01-01";
    test(in, exp, format);

    in = "20210101";
    exp = "2021-01-01";
    test(in, exp, format);

    long ts = 1625110496789L;
    exp = "2021-07-01T12:34:56.789"; // JST
    format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    testByTimestamp(ts, exp, format);
  }

  private static void test(String in, String exp, String format) {
    String out = DateTime.toString(in, format);
    String ret = "NG";
    if (exp.equals(out)) {
      ret = "OK";
    }
    Log.i("[" + ret + "] IN=" + in + " EXP=" + exp + " OUT=" + out);
  }

  private static void testByTimestamp(long in, String exp, String format) {
    String out = DateTime.toString(in, format);
    String ret = "NG";
    if (exp.equals(out)) {
      ret = "OK";
    }
    Log.i("[" + ret + "] IN=" + in + " EXP=" + exp + " OUT=" + out);
  }

  public static void instanceTest() {
    DateTime datetime = new DateTime();
    Log.i(datetime.toString());
    Log.i(datetime.toString("yyyyMMdd'T'HHmmssSSSXX"));
  }

}
