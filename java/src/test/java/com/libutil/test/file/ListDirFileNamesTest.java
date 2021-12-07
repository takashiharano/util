package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class ListDirFileNamesTest {

  public static void main(String args[]) {
    Log.d("Dirs & Files ----------");
    String[] files = FileUtil.listDirFileNames("C:/test/");
    for (int i = 0; i < files.length; i++) {
      Log.d(i + ": " + files[i]);
    }
  }

}
