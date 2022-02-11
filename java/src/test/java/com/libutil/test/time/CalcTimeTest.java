package com.libutil.test.time;

import com.libutil.Time;
import com.libutil.test.Log;

public class CalcTimeTest {

  public static void main(String args[]) {
    Log.i("addTime() ----------");
    addTest();
    addTest2();

    Log.i("subTime() ----------");
    subTest();
    subTest2();

    Log.i("multiTime() ----------");
    multiTest();
    multiTest2();

    Log.i("divTime() ----------");
    divTest();
    divTest2();
  }

  private static void addTest() {
    String[][] addTestData = { { "12:00", "01:30", "13:30" }, { "12:00", "13:00", "25:00" } };

    for (int i = 0; i < addTestData.length; i++) {
      String[] data = addTestData[i];
      String data_in1 = data[0];
      String data_in2 = data[1];
      String data_expected = data[2];

      Time t = Time.addTime(data_in1, data_in2);
      String out = t.toString("HR:mm");
      String ret;
      if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in1 + "," + data_in2 + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void addTest2() {
    String[][] testData = { { "12:00", "01:30", "13:30 0d" }, { "12:00", "13:00", "01:00 1d" } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in1 = data[0];
      String data_in2 = data[1];
      String data_expected = data[2];

      Time t = Time.addTime(data_in1, data_in2);
      String out = t.toString("HH24:mm Dd");
      String ret;
      if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in1 + "," + data_in2 + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void subTest() {
    String[][] testData = { { "12:00", "01:30", "10:30" }, { "12:00", "13:00", "-01:00" } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in1 = data[0];
      String data_in2 = data[1];
      String data_expected = data[2];

      Time t = Time.subTime(data_in1, data_in2);
      String out = t.toString("HR:mm");
      String ret;
      if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in1 + "," + data_in2 + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void subTest2() {
    String[][] testData = { { "12:00", "01:30", "10:30 0d" }, { "12:00", "13:00", "-01:00 0d" },
        { "12:00", "37:00", "-01:00 1d" } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in1 = data[0];
      String data_in2 = data[1];
      String data_expected = data[2];

      Time t = Time.subTime(data_in1, data_in2);
      String out = t.toString("HH24:mm Dd");
      String ret;
      if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in1 + "," + data_in2 + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void multiTest() {
    String[][] testData = { { "00:15", "3", "00:45" }, { "03:30", "3", "10:30" }, { "12:00", "2", "24:00" } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in1 = data[0];
      String data_in2 = data[1];
      String data_expected = data[2];

      Time t = Time.multiTime(data_in1, Integer.parseInt(data_in2));
      String out = t.toString("HR:mm");
      String ret;
      if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in1 + "," + data_in2 + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void multiTest2() {
    String[][] testData = { { "00:15", "3", "00:45" }, { "03:30", "3", "10:30" }, { "12:00", "2", "1d00:00" } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in1 = data[0];
      String data_in2 = data[1];
      String data_expected = data[2];

      Time t = Time.multiTime(data_in1, Integer.parseInt(data_in2));
      String out = t.toString("HH:mm");
      String ret;
      if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in1 + "," + data_in2 + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void divTest() {
    String[][] testData = { { "00:45", "3", "00:15" }, { "10:30", "3", "03:30" }, { "24:00", "2", "12:00" },
        { "48:00", "2", "24:00" } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in1 = data[0];
      String data_in2 = data[1];
      String data_expected = data[2];

      Time t = Time.divTime(data_in1, Integer.parseInt(data_in2));
      String out = t.toString("HR:mm");
      String ret;
      if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in1 + "," + data_in2 + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void divTest2() {
    String[][] testData = { { "2d00:00", "2", "1d00:00" }, { "72:30", "3", "1d00:10" }, { "1d00:00", "2", "12:00" } };

    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in1 = data[0];
      String data_in2 = data[1];
      String data_expected = data[2];

      Time t = Time.divTime(data_in1, Integer.parseInt(data_in2));
      String out = t.toString("HH:mm");
      String ret;
      if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in1 + "," + data_in2 + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

}
