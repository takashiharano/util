package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class NowTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.i(DateTime.now());
    Log.i(DateTime.now("yyyyMMdd'T'HHmmss.SSSXX"));
    Log.i(DateTime.now("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    Log.i(DateTime.now("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    Log.i(DateTime.now("yyyyMMdd"));
    Log.i(DateTime.now("yyyy-MM-dd"));
    Log.i(DateTime.now("yyyy/MM/dd"));
    Log.i(DateTime.now("yyyy.MM.dd"));
  }

}
