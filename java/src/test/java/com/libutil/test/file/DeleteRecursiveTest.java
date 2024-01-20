package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class DeleteRecursiveTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.d(FileUtil.delete("C:/test/dir0"));
    Log.d(FileUtil.delete("C:/test/dir0", false));
    Log.d("OK");
  }

}
