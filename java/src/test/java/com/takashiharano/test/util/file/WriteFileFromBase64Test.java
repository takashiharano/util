package com.takashiharano.test.util.file;

import java.io.IOException;

import com.takashiharano.util.FileUtil;
import com.takashiharano.util.Log;

public class WriteFileFromBase64Test {

  public static void main(String args[]) {
    test1();
  }

  private static void test1() {
    String base64 = "YWJj";
    String path = "C:/test/a.txt";
    try {
      FileUtil.writeFileFromBase64(path, base64);
      Log.d("OK");
    } catch (IOException e) {
      Log.d("ERROR: " + e);
    }
  }

}
