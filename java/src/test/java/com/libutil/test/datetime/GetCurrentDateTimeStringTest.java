package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class GetCurrentDateTimeStringTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.i(DateTime.getCurrentDateTime());
    Log.i(DateTime.getCurrentDateTimeString("yyyyMMdd'T'HHmmss.SSSXX"));
    Log.i(DateTime.getCurrentDateTimeString("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    Log.i(DateTime.getCurrentDateTimeString("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    Log.i(DateTime.getCurrentDateTimeString("yyyyMMdd"));
    Log.i(DateTime.getCurrentDateTimeString("yyyy-MM-dd"));
    Log.i(DateTime.getCurrentDateTimeString("yyyy/MM/dd"));
    Log.i(DateTime.getCurrentDateTimeString("yyyy.MM.dd"));
  }

}
