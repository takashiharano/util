package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class DiffDaysTest {

  public static void main(String args[]) {
    Log.i("long, long ---");
    test1();

    Log.i("long, long, true ---");
    test2();

    Log.i("long, long, +0800 ---");
    test3();

    Log.i("String, String");
    testS();
  }

  private static void test1() {
    long[][] testData = { { 1642258799000L, 1642258800000L, 1 }, { 1642258800000L, 1642258799000L, -1 },
        { 1642258800000L, 1642258800000L, 0 } };

    for (int i = 0; i < testData.length; i++) {
      long[] data = testData[i];
      long data_in1 = data[0];
      long data_in2 = data[1];
      long data_expected = data[2];

      int out = DateTime.diffDays(data_in1, data_in2);
      String ret;
      if (out == data_expected) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN1=" + data_in1 + "IN2=" + data_in2 + " EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void test2() {
    long[][] testData = { { 1642258799000L, 1642258800000L, 1 }, { 1642258800000L, 1642258799000L, 1 },
        { 1642258800000L, 1642258800000L, 0 } };

    for (int i = 0; i < testData.length; i++) {
      long[] data = testData[i];
      long data_in1 = data[0];
      long data_in2 = data[1];
      long data_expected = data[2];

      int out = DateTime.diffDays(data_in1, data_in2, true);
      String ret;
      if (out == data_expected) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN1=" + data_in1 + "IN2=" + data_in2 + " EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void test3() {
    long[][] testData = { { 1642258799000L, 1642258800000L, 0 }, { 1642258800000L, 1642258799000L, 0 },
        { 1642262400000L, 1642262400000L, 0 } };

    for (int i = 0; i < testData.length; i++) {
      long[] data = testData[i];
      long data_in1 = data[0];
      long data_in2 = data[1];
      long data_expected = data[2];

      int out = DateTime.diffDays(data_in1, data_in2, "+0800");
      String ret;
      if (out == data_expected) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN1=" + data_in1 + "IN2=" + data_in2 + " EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void testS() {
    String[][] testData = { { "2022-01-15 23:59:59.000 +09:00", "2022-01-16 00:00:00.000 +09:00" },
        { "2022-01-16 00:00:00.000 +09:00", "2022-01-15 23:59:59.000 +09:00" },
        { "2022-01-16 00:00:00.000 +09:00", "2022-01-16 00:00:00.000 +09:00" },
        { "2022-01-15 23:59:59.000 +08:00", "2022-01-16 00:00:00.000 +08:00" },
        { "2022-01-16 00:00:00.000 +08:00", "2022-01-15 23:59:59.000 +08:00" },
        { "2022-01-16 00:00:00.000 +08:00", "2022-01-16 00:00:00.000 +08:00" } };

    int[] exp = { 1, -1, 0, 1, -1, 0 };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in1 = data[0];
      String data_in2 = data[1];
      int data_expected = exp[i];

      int out = DateTime.diffDays(data_in1, data_in2);
      String ret;
      if (out == data_expected) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN1=" + data_in1 + "IN2=" + data_in2 + " EXP=" + data_expected + " OUT=" + out);
    }
  }

}
