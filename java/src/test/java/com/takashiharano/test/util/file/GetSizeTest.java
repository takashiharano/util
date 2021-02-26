package com.takashiharano.test.util.file;

import com.takashiharano.util.FileUtil;
import com.takashiharano.util.Log;

public class GetSizeTest {

  public static void main(String args[]) {
    test1();
    test2();
    test3();
  }

  private static void test1() {
    String path = "C:/test/a.txt";
    long size = FileUtil.getSize(path);
    Log.d("OK: " + path + ": " + size);
  }

  private static void test2() {
    String path = "C:/test/404.txt";
    long size = FileUtil.getSize(path);
    Log.d("OK: " + path + ": " + size);
  }

  private static void test3() {
    String path = "C:/test/";
    long size = FileUtil.getSize(path);
    Log.d("OK: " + path + ": " + size);
  }

}