package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class ReadAsBase64Test {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    readAsBase64Test("C:/test/a.txt");
    readAsBase64Test("C:/test/0.txt");
    readAsBase64Test("C:/test/NotFound");
  }

  private static void readAsBase64Test(String path) {
    String str = FileUtil.readAsBase64(path);
    Log.d(path + "=\"" + str + "\"");
  }

}
