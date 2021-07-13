package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class ExistsTest {

  public static void main(String args[]) {
    test1();
    Log.i("----------");
    test2();
  }

  private static void test1() {
    String path = "C:/test/200.txt";
    Log.i(path + ": " + FileUtil.exists(path));

    path = "C:/test/404.txt";
    Log.i(path + ": " + FileUtil.exists(path));

    path = "";
    Log.i(path + ": " + FileUtil.exists(path));

    path = null;
    Log.i(path + ": " + FileUtil.exists(path));
  }

  private static void test2() {
    String path = "C:/test/200.txt";
    Log.i(path + ": " + FileUtil.notExist(path));

    path = "C:/test/404.txt";
    Log.i(path + ": " + FileUtil.notExist(path));

    path = "";
    Log.i(path + ": " + FileUtil.notExist(path));

    path = null;
    Log.i(path + ": " + FileUtil.notExist(path));
  }

}
