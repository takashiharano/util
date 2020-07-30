package com.takashiharano.test.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.takashiharano.util.DateTime;
import com.takashiharano.util.Log;

public class DateTimeTest {
  public static void main(String args[]) {
    DateTime datetime = new DateTime();
    String str = datetime.toString();
    Log.d(str);

    datetime.setTimeZone("PST");
    str = datetime.toString();
    Log.d(str);
    Log.d(datetime.getTimeZoneId());
    Log.d(datetime.getTimeZoneDisplayName());

    try {
      datetime = new DateTime(2019, 12, 05, 23, 30);
      str = datetime.toString();
      Log.d(str);
      Log.d(datetime.getTimeZoneId());
      Log.d(datetime.getTimeZoneDisplayName());
    } catch (ParseException e) {
      Log.e(e);
    }

    try {
      datetime = new DateTime(2019, 12, 05, 23, 30, "PST");
      str = datetime.toString();
      Log.d(str);
      Log.d(datetime.getTimeZoneId());
      Log.d(datetime.getTimeZoneDisplayName());
    } catch (ParseException e) {
      Log.e(e);
    }

    _test(datetime, "PST");
    _test(datetime, "JST");

    formatTimeTest();
    formatDateTimeTest();

    test1();
    test2();
  }

  static void _test(DateTime datetime, String zoneId) {
    datetime.setTimeZone(zoneId);
    String str = datetime.toString();
    Log.d("");
    Log.d(str);
    Log.d("id  : " + datetime.getTimeZoneId());
    Log.d("name: " + datetime.getTimeZoneDisplayName());
  }

  static void test1() {
    Log.d("test1 ----------");
    DateTime datetime = new DateTime();
    String str = datetime.toString();
    Log.d(str);
    Log.d("Year       = " + datetime.getYear());
    Log.d("Month      = " + datetime.getMonth());
    Log.d("Day        = " + datetime.getDay());
    Log.d("Hour       = " + datetime.getHour());
    Log.d("Minute     = " + datetime.getMinute());
    Log.d("Second     = " + datetime.getSecond());
    Log.d("Millisecond= " + datetime.getMillisecond());
    Log.d("TimeZoneId = " + datetime.getTimeZoneId());
    Log.d("");
  }

  static void test2() {
    Log.d("test1 ----------");
    DateTime datetime = new DateTime(1594092896789L);
    String str = datetime.toString();
    Log.d(str);
    Log.d("Year       = " + datetime.getYear());
    Log.d("Month      = " + datetime.getMonth());
    Log.d("Day        = " + datetime.getDay());
    Log.d("Hour       = " + datetime.getHour());
    Log.d("Minute     = " + datetime.getMinute());
    Log.d("Second     = " + datetime.getSecond());
    Log.d("Millisecond= " + datetime.getMillisecond());
    Log.d("TimeZoneId = " + datetime.getTimeZoneId());
    Log.d("");
  }

  static void formatTimeTest() {
    long t = 1234;
    Log.d(t + " = " + DateTime.formatTime(t, "HH:mm:ss.SSS"));
  }

  static void formatDateTimeTest() {
    long t = 1594092896789L;
    Log.d(t + " = " + DateTime.formatDateTime(t, "yyyy-MM-dd'T'HH:mm:ss.SSS XXX"));
  }

  @Test
  void getYear() {
    DateTime datetime = new DateTime();
    int year = datetime.getYear();
    assertEquals(2019, year);
  }

  @Test
  void getMonth() {
    DateTime datetime = new DateTime();
    int month = datetime.getMonth();
    assertEquals(7, month);
  }

  @Test
  void toStringTest() {
    DateTime datetime = new DateTime();
    String str = datetime.toString();
    Log.d(str);
    // assertEquals("123456", str);
  }

}
