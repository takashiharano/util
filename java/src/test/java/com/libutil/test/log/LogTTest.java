package com.libutil.test.log;

import com.libutil.test.Log;

public class LogTTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.i("Test start");
    Log.t("S2 PR-1 LOAD");
    sleep(1000);
    Log.t("S2 LOX LOAD");
    sleep(1000);
    Log.t("STRONGBACK RETRACT");
    sleep(1000);
    Log.t("STARTUP");
    sleep(1000);
    Log.t("LIFTOFF");
    sleep(1000);
    Log.t("MAX-Q");
    sleep(1000);
    Log.t("MECO");
    sleep(1000);
    Log.resetT();
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
    Log.i("Test end");
  }

  private static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
