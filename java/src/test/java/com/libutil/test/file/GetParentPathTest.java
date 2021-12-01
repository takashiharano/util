package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class GetParentPathTest {

  public static void main(String args[]) {
    String parent = FileUtil.getParentPath("C:/test/a/b/c/x.txt");
    Log.d(parent);
  }

}
