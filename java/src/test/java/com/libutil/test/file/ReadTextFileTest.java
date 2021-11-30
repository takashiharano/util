package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class ReadTextFileTest {

  public static void main(String args[]) {
    String path = "C:/test/a.txt";
    String str = FileUtil.readText(path);
    Log.d(str);
  }

}
