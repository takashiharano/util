package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class MoveTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.d(FileUtil.move("C:/test/file1.txt", "C:/test/a"));
    // Log.d(FileUtil.move("C:/test/file1.txt", "C:/test/a/"));
    // Log.d(FileUtil.move("C:/test/file1.txt", "C:/test/a/a.txt"));
    // Log.d(FileUtil.move("C:/test/file1.txt", "C:/test/a/a.txt", true));
    Log.d("OK");
  }

}
