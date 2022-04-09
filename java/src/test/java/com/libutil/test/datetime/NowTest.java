package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class NowTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.i(DateTime.now());
  }

}
