package com.libutil.datetime;

import com.libutil.DateTime;
import com.libutil.Log;

public class DateTimeTest {

  public static void main(String args[]) {
    Log.d("DateTime() ----------");
    DateTime datetime = new DateTime();
    test(datetime);

    Log.d("setTimeZone(\"PST\") ----------");
    datetime.setTimeZone("PST");
    test(datetime);

    Log.d("DateTime(2019, 12, 05, 23, 30) ----------");
    datetime = new DateTime(2019, 12, 05, 23, 30);
    test(datetime);

    Log.d("DateTime(2019, 12, 05, 23, 30, \"PST\") ----------");
    datetime = new DateTime(2019, 12, 05, 23, 30, "PST");
    test(datetime);

    Log.d("DateTime(1594092896789L) ----------");
    datetime = new DateTime(1594092896789L);
    test(datetime);

    Log.d("DateTime(\"20210703T123456.789+0900\") ----------");
    String src = "20210703T123456.789+0900";
    datetime = new DateTime(src);
    test(datetime);

    Log.d("DateTime(\"20210703T123456.789-0800\")");
    src = "20210703T123456.789-0800";
    datetime = new DateTime(src);
    test(datetime);

    try {
      Log.d("DateTime(\"\")");
      src = "";
      datetime = new DateTime(src);
      test(datetime);
    } catch (Exception e) {
      Log.e(e);
    }

    try {
      Log.d("DateTime(\"aaa\")");
      src = "aaa";
      datetime = new DateTime(src);
      test(datetime);
    } catch (Exception e) {
      Log.e(e);
    }

    try {
      Log.d("DateTime(null)");
      src = null;
      datetime = new DateTime(src);
      test(datetime);
    } catch (Exception e) {
      Log.e(e);
    }

    formatTimeTest();
  }

  private static void test(DateTime datetime) {
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
    Log.d("timestamp = " + datetime.getTimeStamp());
    Log.d("");
  }

  private static void formatTimeTest() {
    long t = 1234;
    Log.d(t + " = " + DateTime.formatTime(t, "HH:mm:ss.SSS"));
  }

}
