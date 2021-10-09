package com.libutil.test.file;

import java.io.IOException;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class DecodeBase64FileToFileTest {

  public static void main(String args[]) {
    try {
      test();
    } catch (IOException ioe) {
      Log.e(ioe);
    }
  }

  private static void test() throws IOException {
    FileUtil.decodeBase64FileToFile("C:/test/b64image.txt", "C:/tmp/image.jpg");
    FileUtil.decodeBase64FileToFile("C:/test/NotFound.txt", "C:/tmp/notfound.txt");
    Log.d("OK");
  }

}
