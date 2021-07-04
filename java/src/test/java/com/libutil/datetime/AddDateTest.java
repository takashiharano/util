package com.libutil.datetime;

import com.libutil.DateTime;
import com.libutil.Log;

public class AddDateTest {

  public static void main(String args[]) {
    String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    String in = "20210101";
    String exp = "2021-01-02T00:00:00.000";
    test(in, 1, exp, format);

    in = "20210101123456789";
    exp = "2021-01-02T12:34:56.789";
    test(in, 1, exp, format);

    format = "yyyy-MM-dd";

    in = "20210101";
    exp = "2021-01-02";
    test(in, 1, exp, format);

    in = "20210101";
    exp = "2021-01-03";
    test(in, 2, exp, format);

    in = "20210101";
    exp = "2020-12-31";
    test(in, -1, exp, format);

    in = "20201231";
    exp = "2021-01-01";
    test(in, 1, exp, format);

    in = "99991231";
    exp = "9999-12-30";
    test(in, -1, exp, format);

    in = "99991231";
    exp = "10000-01-01";
    test(in, 1, exp, format);

    in = "2021-01-01";
    exp = "2021-01-02";
    test(in, 1, exp, format);

    in = "2021/01/01";
    exp = "2021-01-02";
    test(in, 1, exp, format);

    try {
      in = "";
      exp = "";
      test(in, 1, exp, format);
    } catch (Exception e) {
      Log.e(e);
    }

    try {
      in = "2021/01";
      exp = "";
      test(in, 1, exp, format);
    } catch (Exception e) {
      Log.e(e);
    }

    try {
      in = "aaa";
      exp = "";
      test(in, 1, exp, format);
    } catch (Exception e) {
      Log.e(e);
    }

    try {
      in = null;
      exp = "";
      test(in, 1, exp, format);
    } catch (Exception e) {
      Log.e(e);
    }
  }

  private static void test(String in, int days, String exp, String format) {
    String out = DateTime.addDate(in, days, format);
    String ret = "NG";
    if (exp.equals(out)) {
      ret = "OK";
    }

    Log.i("[" + ret + "] IN=" + in + " EXP=" + exp + " OUT=" + out);
  }

}
