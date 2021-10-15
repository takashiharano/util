package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class CompareTest {

  public static void main(String args[]) {
    Log.i("1----------");
    test1();

    Log.i("2----------");
    test2();

    Log.i("3----------");
    test3();

    Log.i("4----------");
    test4();
  }

  public static void test1() {
    DateTime dt0 = new DateTime("2021-07-10 12:00:00.000");
    DateTime dt1 = new DateTime("2021-07-10 12:00:00.001");
    Log.i(dt0.compareTo(dt1));
    Log.i(dt0.compareTo(dt0));
    Log.i(dt1.compareTo(dt0));
  }

  public static void test2() {
    DateTime dt0 = new DateTime("2021-07-10 12:00:00.000");
    DateTime dt1 = new DateTime("2021-07-11 12:00:00.000");
    Log.i(dt0.compareTo(dt1));
    Log.i(dt0.compareTo(dt0));
    Log.i(dt1.compareTo(dt0));
  }

  public static void test3() {
    DateTime dt0 = new DateTime("2021-07-10");
    DateTime dt1 = new DateTime("2021-07-11");
    Log.i(dt0.compareTo(dt1));
    Log.i(dt0.compareTo(dt0));
    Log.i(dt1.compareTo(dt0));
  }

  public static void test4() {
    Log.i(DateTime.compare("2021-07-10 12:00:00.000", "2021-07-10 12:00:00.001"));
    Log.i(DateTime.compare("2021-07-10 12:00:00.000", "2021-07-10 12:00:00.000"));
    Log.i(DateTime.compare("2021-07-10 12:00:00.001", "2021-07-10 12:00:00.00"));
    Log.i("---");
    Log.i(DateTime.compare("2021-01-01 12:00:00.000", "2021-07-10 12:00:00.001"));
    Log.i(DateTime.compare("2021-10-01 12:00:00.000", "2021-07-10 12:00:00.000"));
  }

}
