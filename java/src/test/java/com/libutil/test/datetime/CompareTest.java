package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class CompareTest {

  public static void main(String args[]) {
    test1();
    Log.i("----------");
    test2();
    Log.i("----------");
    test3();
    Log.i("----------");
    test4();
  }

  public static void test1() {
    DateTime dt0 = new DateTime("2021-07-10 12:00:00.000");
    DateTime dt1 = new DateTime("2021-07-10 12:00:00.001");
    Log.i(dt0.compare(dt1));
    Log.i(dt0.compare(dt0));
    Log.i(dt1.compare(dt0));
  }

  public static void test2() {
    Log.i(DateTime.compareDateTime("2021-07-10 12:00:00.000", "2021-07-10 12:00:00.001"));
    Log.i(DateTime.compareDateTime("2021-07-10 12:00:00.000", "2021-07-10 12:00:00.000"));
    Log.i(DateTime.compareDateTime("2021-07-10 12:00:00.001", "2021-07-10 12:00:00.00"));
  }

  public static void test3() {
    DateTime dt0 = new DateTime("2021-07-10 12:00:00.000");
    DateTime dt1 = new DateTime("2021-07-11 12:00:00.000");
    Log.i(dt0.compare(dt1));
    Log.i(dt0.compare(dt0));
    Log.i(dt1.compare(dt0));
  }

  public static void test4() {
    DateTime dt0 = new DateTime("2021-07-10");
    DateTime dt1 = new DateTime("2021-07-11");
    Log.i(dt0.compare(dt1));
    Log.i(dt0.compare(dt0));
    Log.i(dt1.compare(dt0));
  }

}
