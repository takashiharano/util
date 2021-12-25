package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class RenameTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.d(FileUtil.rename("C:/test/file1.txt", "file2.txt"));
    // Log.d(FileUtil.rename("C:/test/file1.txt", "C:/test/file2.txt"));
    // Log.d(FileUtil.rename("C:/test/file1.txt", "C:/test/a/file2.txt"));
    Log.d("OK");
  }

}
