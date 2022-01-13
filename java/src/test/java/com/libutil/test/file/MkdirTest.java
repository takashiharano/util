package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class MkdirTest {

  public static void main(String args[]) {
    boolean created = FileUtil.mkdir("C:/tmp/a/b/c");
    Log.d(created);
  }

}
