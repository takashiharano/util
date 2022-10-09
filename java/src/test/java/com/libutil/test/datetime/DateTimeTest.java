package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class DateTimeTest {

  public static void main(String args[]) {
    Log.i("DateTime() ----------");
    DateTime datetime = new DateTime();
    test(datetime);

    Log.i("setTimeZone(\"PST\") ----------");
    datetime.setTimeZone("PST");
    test(datetime);

    Log.i("DateTime(2019, 12, 05, 23, 30) ----------");
    datetime = new DateTime(2019, 12, 05, 23, 30);
    test(datetime);

    Log.i("DateTime(2019, 12, 05, 23, 30, \"PST\") ----------");
    datetime = new DateTime(2019, 12, 05, 23, 30, "PST");
    test(datetime);

    Log.i("DateTime(1594092896789L) ----------");
    datetime = new DateTime(1594092896789L);
    test(datetime);

    Log.i("DateTime(1594092896.789) ----------");
    datetime = new DateTime(1594092896.789);
    test(datetime);

    Log.i("DateTime(1594092896.789123) ----------");
    datetime = new DateTime(1594092896.789123);
    test(datetime);

    Log.i("DateTime(\"20210703T123456.789+0900\") ----------");
    String src = "20210703T123456.789+0900";
    datetime = new DateTime(src);
    test(datetime);

    Log.i("DateTime(\"20210703T123456.789-0800\")");
    src = "20210703T123456.789-0800";
    datetime = new DateTime(src);
    test(datetime);

    Log.i("--------------------------------------------------");
    try {
      Log.i("DateTime(\"\")");
      src = "";
      datetime = new DateTime(src);
      test(datetime);
    } catch (Exception e) {
      Log.e(e);
    }

    Log.i("--------------------------------------------------");
    try {
      Log.i("DateTime(\"aaa\")");
      src = "aaa";
      datetime = new DateTime(src);
      test(datetime);
    } catch (Exception e) {
      Log.e(e);
    }

    Log.i("--------------------------------------------------");
    try {
      Log.i("DateTime(null)");
      src = null;
      datetime = new DateTime(src);
      test(datetime);
    } catch (Exception e) {
      Log.e(e);
    }
  }

  private static void test(DateTime datetime) {
    String str = datetime.toString();
    Log.i(str);
    Log.i("Year       = " + datetime.getYear());
    Log.i("Month      = " + datetime.getMonth());
    Log.i("Day        = " + datetime.getDay());
    Log.i("Hour       = " + datetime.getHour());
    Log.i("Minute     = " + datetime.getMinute());
    Log.i("Second     = " + datetime.getSecond());
    Log.i("Millisecond= " + datetime.getMillisecond());
    Log.i("TimeZoneId = " + datetime.getTimeZoneId());
    Log.i("timestamp = " + datetime.getTimestamp());
    Log.i("");
  }

}
