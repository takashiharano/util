package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class ListDirNamesTest {

  public static void main(String args[]) {
    listFileNamesTest();
    listDirNamesTest();
  }

  private static void listFileNamesTest() {
    Log.d("Files ----------");
    String[] files = FileUtil.listFileNames("C:/test/");
    for (int i = 0; i < files.length; i++) {
      Log.d(i + ": " + files[i]);
    }
  }

  private static void listDirNamesTest() {
    Log.d("Dirs ----------");
    String[] files = FileUtil.listDirNames("C:/test/");
    for (int i = 0; i < files.length; i++) {
      Log.d(i + ": " + files[i]);
    }
  }

}
