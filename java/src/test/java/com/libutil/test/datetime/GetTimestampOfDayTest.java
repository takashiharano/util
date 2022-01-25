package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class GetTimestampOfDayTest {

  public static void main(String args[]) {
    test("00:00");
    test("09:00");
    test("12:00");
    test("00:00:00");
    test("00:00:00.000");
    test("00:00:00.001");
    test("00:00:01");
    test("00:01:00");

    Log.i("+0800");
    test("00:00+0800");
    test("09:00+0800");
    test("12:00+0800");
    test("00:00:00+0800");
    test("00:00:00.000+0800");
    test("00:00:00.001+0800");
    test("00:00:01+0800");
    test("00:01:00+0800");

    Log.i("Z");
    test("00:00Z");
    test("09:00Z");
    test("12:00Z");
    test("00:00:00Z");
    test("00:00:00.000Z");
    test("00:00:00.001Z");
    test("00:00:01Z");
    test("00:01:00Z");

    Log.i("-1");
    test1("00:00", -1);
    test1("09:00", -1);
    test1("12:00", -1);
    test1("00:00:00", -1);
    test1("00:00:00.000", -1);
    test1("00:00:00.001", -1);
    test1("00:00:01", -1);
    test1("00:01:00", -1);
  }

  public static void test(String time) {
    Log.i(DateTime.getTimestampOfDay(time));
  }

  public static void test1(String time, int offset) {
    Log.i(DateTime.getTimestampOfDay(time, offset));
  }

}
