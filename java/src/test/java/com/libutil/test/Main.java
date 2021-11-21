package com.libutil.test;

public class Main {

  public static void main(String args[]) {
    test();
    test();
    test();
  }

  private static void test() {
    Log.d("test start");
    long start = Log.t("Start");

    Log.t("End", start);
    Log.d("test end");
  }

}
