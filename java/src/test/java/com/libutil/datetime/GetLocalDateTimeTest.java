package com.libutil.datetime;

import com.libutil.DateTime;
import com.libutil.Log;

public class GetLocalDateTimeTest {

  public static void main(String args[]) {
    DateTime datetime;
    Log.i("DateTime(2021, 07, 04, 12, 34, 56, 789, \"PST\") ----------");
    datetime = new DateTime(2021, 07, 04, 12, 34, 56, 789, "-0800");
    test(datetime);

    datetime = datetime.getLocalDateTime();
    test(datetime);

    datetime = datetime.getLocalDateTime("Z");
    test(datetime);

    datetime = datetime.getLocalDateTime("GMT+0800");
    test(datetime);

    Log.i("------------------------------------------------------------");
    datetime = new DateTime(2021, 07, 04, 12, 34, 56, 789);
    test(datetime);

    datetime = datetime.getLocalDateTime();
    test(datetime);

    datetime = datetime.getLocalDateTime("GMT-0800");
    test(datetime);

    datetime = datetime.getLocalDateTime("Z");
    test(datetime);

    datetime = datetime.getLocalDateTime("GMT+0800");
    test(datetime);
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
    Log.i("timestamp = " + datetime.getTimeStamp());
    Log.i("");
  }

}
