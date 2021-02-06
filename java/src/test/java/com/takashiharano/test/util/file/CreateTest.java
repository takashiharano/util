package com.takashiharano.test.util.file;

import com.takashiharano.util.FileUtil;
import com.takashiharano.util.Log;

public class CreateTest {

  public static void main(String args[]) {
    test1();
    test2a();
    test2b();
  }

  private static void test1() {
    String path = "C:/test/a.txt";
    boolean created = FileUtil.create(path);
    Log.d("OK: " + path + ": " + created);
  }

  private static void test2a() {
    String path = "C:/test/404/a.txt";
    boolean created = FileUtil.create(path);
    Log.d("OK: " + path + ": " + created);
  }

  private static void test2b() {
    String path = "C:/test/404/a.txt";
    boolean created = FileUtil.create(path, true);
    Log.d("OK: " + path + ": " + created);
  }

}
