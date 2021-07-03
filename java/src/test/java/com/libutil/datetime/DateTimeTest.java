package com.libutil.datetime;

import com.libutil.DateTime;
import com.libutil.Log;

public class DateTimeTest {

  public static void main(String args[]) {
    Log.d("test1 ----------");
    DateTime datetime = new DateTime();
    test(datetime);

    Log.d("test2 ----------");
    datetime.setTimeZone("PST");
    test(datetime);

    Log.d("test3 ----------");
    datetime = new DateTime(2019, 12, 05, 23, 30);
    test(datetime);

    Log.d("test4 ----------");
    datetime = new DateTime(2019, 12, 05, 23, 30, "PST");
    test(datetime);

    Log.d("test5 ----------");
    datetime = new DateTime();
    test(datetime);

    Log.d("test6 ----------");
    datetime = new DateTime(1594092896789L);
    test(datetime);

    Log.d("test7 ----------");
    String src = "20210703T123456.789+0900";
    datetime = new DateTime(src);
    test(datetime);

    Log.d("test8 ----------");
    src = "20210703T123456.789-0800";
    datetime = new DateTime(src);
    test(datetime);

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
