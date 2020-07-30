package com.takashiharano.test;

import com.takashiharano.util.Log;

public class Main {

  public static void main(String args[]) {
    test();
    test();
    test();
  }

  private static void test() {
    Log.d("test start");
    long start = Log.timeStart();

    Log.timeEndMilli(start);
    Log.d("test end");
  }

}
