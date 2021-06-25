package com.libutil.util.file;

import java.io.IOException;

import com.libutil.FileUtil;
import com.libutil.Log;

public class WriteFromBase64Test {

  public static void main(String args[]) {
    test1();
  }

  private static void test1() {
    String base64 = "YWJj";
    String path = "C:/test/a.txt";
    try {
      FileUtil.writeFromBase64(path, base64);
      Log.d("OK");
    } catch (IOException e) {
      Log.d("ERROR: " + e);
    }
  }

}
