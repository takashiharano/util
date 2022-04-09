package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class GetCurrentDateTimeTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.i(DateTime.getCurrentDateTime());
    Log.i(DateTime.getCurrentDateTime("yyyyMMdd'T'HHmmss.SSSXX"));
    Log.i(DateTime.getCurrentDateTime("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    Log.i(DateTime.getCurrentDateTime("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    Log.i(DateTime.getCurrentDateTime("yyyyMMdd"));
    Log.i(DateTime.getCurrentDateTime("yyyy-MM-dd"));
    Log.i(DateTime.getCurrentDateTime("yyyy/MM/dd"));
    Log.i(DateTime.getCurrentDateTime("yyyy.MM.dd"));
  }

}
