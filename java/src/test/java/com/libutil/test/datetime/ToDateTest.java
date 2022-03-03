package com.libutil.test.datetime;

import java.text.ParseException;
import java.util.Date;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class ToDateTest {

  public static void main(String args[]) {
    String in;
    String exp;

    try {
      in = "2022-01-01";
      exp = in;
      test1(in, exp);
    } catch (Exception e) {
      Log.i("[NG] " + e.toString());
    }

    try {
      in = "2022-12-31";
      exp = in;
      test1(in, exp);
    } catch (Exception e) {
      Log.i("[NG] " + e.toString());
    }

    try {
      in = "2022-01-32";
      exp = in;
      test1(in, exp);
    } catch (Exception e) {
      Log.i("[OK] " + e.toString());
    }

    try {
      in = "2022-13-01";
      exp = in;
      test1(in, exp);
    } catch (Exception e) {
      Log.i("[OK] " + e.toString());
    }

    try {
      in = "2022-02-29";
      exp = in;
      test1(in, exp);
    } catch (Exception e) {
      Log.i("[OK] " + e.toString());
    }

    try {
      in = "";
      exp = in;
      test1(in, exp);
    } catch (Exception e) {
      Log.i("[OK] " + e.toString());
    }

    try {
      in = null;
      exp = in;
      test1(in, exp);
    } catch (Exception e) {
      Log.i("[OK] " + e.toString());
    }

    // -----

    try {
      in = "2022-01-01";
      exp = in;
      test2(in, exp);
    } catch (Exception e) {
      Log.i("[NG] " + e.toString());
    }

    try {
      in = "2022-12-31";
      exp = in;
      test2(in, exp);
    } catch (Exception e) {
      Log.i("[NG] " + e.toString());
    }

    try {
      in = "2022-01-32";
      exp = "2022-02-01";
      test2(in, exp);
    } catch (Exception e) {
      Log.i("[NG] " + e.toString());
    }

    try {
      in = "2022-13-01";
      exp = "2023-01-01";
      test2(in, exp);
    } catch (Exception e) {
      Log.i("[OK] " + e.toString());
    }

    try {
      in = "2022-02-29";
      exp = "2022-03-01";
      test2(in, exp);
    } catch (Exception e) {
      Log.i("[OK] " + e.toString());
    }

    try {
      in = "";
      exp = in;
      test2(in, exp);
    } catch (Exception e) {
      Log.i("[OK] " + e.toString());
    }

    try {
      in = null;
      exp = in;
      test2(in, exp);
    } catch (Exception e) {
      Log.i("[OK] " + e.toString());
    }

  }

  private static void test1(String in, String exp) throws ParseException {
    Date date = DateTime.toDate(in, "yyyy-MM-dd");
    String out = DateTime.toString(date, "yyyy-MM-dd");
    String ret = "NG";
    if (exp.equals(out)) {
      ret = "OK";
    }
    Log.i("[" + ret + "] IN=" + in + " EXP=" + exp + " OUT=" + out);
  }

  private static void test2(String in, String exp) throws ParseException {
    Date date = DateTime.toDate(in, "yyyy-MM-dd", true);
    String out = DateTime.toString(date, "yyyy-MM-dd");
    String ret = "NG";
    if (exp.equals(out)) {
      ret = "OK";
    }
    Log.i("[" + ret + "] IN=" + in + " EXP=" + exp + " OUT=" + out);
  }

}
