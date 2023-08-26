package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class GetMidnightTimestampTest {

  public static void main(String args[]) {
    test1();

    Log.i("+0900L");
    test0900();

    Log.i("+0800");
    test0800();

    Log.i("String");
    testS();
  }

  // for JST
  private static void test1() {
    long[][] testData = { { 1642604400000L, 1642604400000L }, { 1642604400001L, 1642604400000L }, { 1642681157943L, 1642604400000L }, { 1642690799999L, 1642604400000L }, { 1642690800000L, 1642690800000L } };
    for (int i = 0; i < testData.length; i++) {
      long[] data = testData[i];
      long data_in = data[0];
      long data_expected = data[1];

      long out = DateTime.getMidnightTimestamp(data_in);
      String ret;
      if (out == data_expected) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void test0900() {
    long[][] testData = { { 1642604400000L, 1642604400000L }, { 1642604400001L, 1642604400000L }, { 1642681157943L, 1642604400000L }, { 1642690799999L, 1642604400000L }, { 1642690800000L, 1642690800000L } };
    for (int i = 0; i < testData.length; i++) {
      long[] data = testData[i];
      long data_in = data[0];
      long data_expected = data[1];

      long out = DateTime.getMidnightTimestamp(data_in, "+0900");
      String ret;
      if (out == data_expected) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void test0800() {
    long[][] testData = { { 1642604400000L, 1642521600000L }, { 1642604400001L, 1642521600000L }, { 1642681157943L, 1642608000000L }, { 1642694399999L, 1642608000000L }, { 1642694400000L, 1642694400000L } };
    for (int i = 0; i < testData.length; i++) {
      long[] data = testData[i];
      long data_in = data[0];
      long data_expected = data[1];

      long out = DateTime.getMidnightTimestamp(data_in, "+0800");
      String ret;
      if (out == data_expected) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void testS() {
    String[][] testData = { { "2022-01-20 00:00:00.000 +09:00", "1642604400000" }, { "2022-01-20 00:00:00.001 +09:00", "1642604400000" }, { "2022-01-20 21:19:17.943 +09:00", "1642604400000" }, { "2022-01-20 23:59:59.999 +09:00", "1642604400000" }, { "2022-01-21 00:00:00.000 +09:00", "1642690800000" }, { "2022-01-19 23:00:00.000 +08:00", "1642521600000" }, { "2022-01-19 23:00:00.001 +08:00", "1642521600000" }, { "2022-01-20 20:19:17.943 +08:00", "1642608000000" }, { "2022-01-20 23:59:59.999 +08:00", "1642608000000" }, { "2022-01-21 00:00:00.000 +08:00", "1642694400000" } };
    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in = data[0];
      String data_expected = data[1];

      long out = DateTime.getMidnightTimestamp(data_in);
      String ret;
      if ((out + "").equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

}
