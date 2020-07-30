package com.takashiharano.test.util;

import java.util.ArrayList;

import com.takashiharano.util.Log;

public class LogTest {

  public static void main(String args[]) {
    Log.x("X-Debug");
    Log.d("Debug");
    Log.i("Information");
    Log.w("Warning");
    Log.e("Error");
    Log.f("Fatal Error");
    stackTest();
    stackTest1();
    stackTest10();

    byte[] b = { (byte) 0x0, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6, (byte) 0x7,
        (byte) 0x8, (byte) 0x9, (byte) 0xa };
    Log.d(b);

    String[] strArray = { "abc", "123", "xyz" };
    Log.d(strArray);

    ArrayList<String> strList = new ArrayList<>();
    strList.add("abc");
    strList.add("123");
    strList.add("xyz");
    Log.d(strList);

    logETest();
    printStackTest();

    dateTimeFormatTest();
    moduleNameTest();
    outputLeveltTest();
  }

  private static void stackTest() {
    Log.stack();
  }

  private static void stackTest1() {
    Log.stack(1);
  }

  private static void stackTest10() {
    Log.stack(10);
  }

  private static void logETest() {
    Exception e3 = new Exception("C");
    Exception e2 = new Exception("B", e3);
    Exception e1 = new Exception("A", e2);
    Log.e("---------");
    Log.e("log.e test");
    Log.e(e1);
  }

  private static void printStackTest() {
    Exception e3 = new Exception("C");
    Exception e2 = new Exception("B", e3);
    Exception e1 = new Exception("A", e2);
    Log.e("---------");
    Log.e("Stack Test", e1);
  }

  private static void dateTimeFormatTest() {
    Log.setDateTimeFormat("yyyyMMdd'T'HHmmss.SSSXX");
    Log.d("test");
  }

  private static void moduleNameTest() {
    Log.setModuleName("ModuleA");
    Log.d("test");
  }

  private static void outputLeveltTest() {
    Log.i("--- LogLevel 4 ---");
    Log.setOutputLevel(4);
    Log.d("4: DEBUG");
    Log.i("4: INFO");
    Log.w("4: WARN");
    Log.e("4: ERROR");

    Log.i("--- LogLevel DEBUG ---");
    Log.setOutputLevel(Log.LogLevel.DEBUG);
    Log.d("DEBUG: DEBUG");
    Log.i("DEBUG: INFO");
    Log.w("DEBUG: WARN");
    Log.e("DEBUG: ERROR");
  }

}
