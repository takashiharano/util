package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class FileHashTest {

  public static void main(String args[]) {
    fileHashTest();
  }

  private static void fileHashTest() {
    String path = "C:/test/a.txt";
    Log.d(FileUtil.getHash(path, "SHA-1"));
    Log.d(FileUtil.getHash(path, "SHA-256"));
    Log.d(FileUtil.getHash(path, "SHA-512"));
  }

}
