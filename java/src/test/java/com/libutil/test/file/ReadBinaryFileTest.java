package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class ReadBinaryFileTest {

  public static void main(String args[]) {
    String path = "C:/test/a.txt";
    byte[] b = FileUtil.read(path);
    Log.d(b);
  }

}
