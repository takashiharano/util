package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class GetCurrentStringTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.i(DateTime.getCurrentDateTime());
    Log.i(DateTime.getCurrentString("yyyyMMdd'T'HHmmss.SSSXX"));
    Log.i(DateTime.getCurrentString("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    Log.i(DateTime.getCurrentString("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    Log.i(DateTime.getCurrentString("yyyyMMdd"));
    Log.i(DateTime.getCurrentString("yyyy-MM-dd"));
    Log.i(DateTime.getCurrentString("yyyy/MM/dd"));
    Log.i(DateTime.getCurrentString("yyyy.MM.dd"));
  }

}
