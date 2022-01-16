package com.libutil.test.log;

import com.libutil.test.Log;

public class LogTTest {

  public static void main(String args[]) {
    test();
    test2();
  }

  private static void test() {
    Log.i("TEST START");
    Log.t("S2 PR-1 LOAD");
    sleep(1000);
    Log.t("S2 LOX LOAD");
    sleep(1000);
    Log.t("STRONGBACK RETRACT");
    sleep(1000);
    Log.t("STARTUP", 86399900);
    sleep(1000);
    Log.t("LIFTOFF");
    sleep(1000);
    Log.t("MAX-Q");
    sleep(1000);
    Log.t("MECO");
    sleep(1000);
    Log.resetT0();
    Log.t("FAIRING");
    sleep(1000);
    Log.t("ENTRY BURN");
    sleep(1000);
    Log.t("LANDING BURN");
    sleep(1000);
    Log.t("TOUCH DOWN & SHUT OFF");
    Log.setT0(60000);
    sleep(1000);
    Log.t("SECO");
    Log.i("TEST END");
  }

  private static void test2() {
    Log.resetT0();
    Log.t("test1");
    Log.t("test2");
    Log.t("test3");
    Log.t("test4");
    Log.t("test5");
    Log.t("test6");
    Log.t("test7");
    Log.t("test8");
    Log.t("test9");
    Log.t("test10");
    Log.t("test11");
    Log.t("test12");
    Log.t("test13");
    Log.t("test14");
    Log.t("test15");
    Log.t("test16");
    Log.t("test17");
    Log.t("test18");
    Log.t("test19");
    Log.t("test20");
  }

  private static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
