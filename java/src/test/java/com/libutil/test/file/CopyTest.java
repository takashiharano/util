package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class CopyTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    // Log.d(FileUtil.copy("C:/test/file1.txt", "C:/tmp/a"));
    Log.d(FileUtil.copy("C:/test/file1.txt", "C:/tmp/a/"));

    // Log.d(FileUtil.copy("C:/test/file1.txt", "C:/tmp/a/a.txt"));
    // Log.d(FileUtil.copy("C:/test/file1.txt", "C:/tmp/a/a.txt", true));

    // Log.d(FileUtil.copy("C:/test/file1.txt", "C:/tmp/a/b"));
    // Log.d(FileUtil.copy("C:/test/file1.txt", "C:/tmp/a/b/"));

    // Log.d(FileUtil.copy("C:/test/dir1", "C:/tmp/a"));
    // Log.d(FileUtil.copy("C:/test/dir1/", "C:/tmp/a/", true));

    Log.d("OK");
  }

}
