package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class DeleteTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.d(FileUtil.delete("C:/test/del.txt"));
    Log.d(FileUtil.delete("C:/test/NotFound.txt"));
    Log.d("OK");
  }

}
