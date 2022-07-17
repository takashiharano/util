package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class ExistsTest {

  public static void main(String args[]) {
    test1();
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

}
