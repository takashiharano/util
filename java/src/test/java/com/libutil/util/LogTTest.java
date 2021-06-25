package com.libutil.util;

import com.libutil.Log;

public class LogTTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.d("test start");

    long t = Log.t("Start");

    sleep(500);

    t = Log.t("aaa1");
    sleep(500);
    t = Log.t("aaa2", t);

    t = Log.t("bbb1");
    sleep(1000);
    t = Log.t("bbb2", t);

    t = Log.t("ccc", t);

    Log.d("test end");
  }

  private static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
