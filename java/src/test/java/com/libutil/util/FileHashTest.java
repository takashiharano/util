package com.libutil.util;

import com.libutil.Log;
import com.libutil.Util;

public class FileHashTest {

  public static void main(String args[]) {
    fileHashTest();
  }

  private static void fileHashTest() {
    String path = "C:/test/a.txt";
    Log.d(Util.getFileHash(path, "SHA-1"));
    Log.d(Util.getFileHash(path, "SHA-256"));
    Log.d(Util.getFileHash(path, "SHA-512"));
  }

}
