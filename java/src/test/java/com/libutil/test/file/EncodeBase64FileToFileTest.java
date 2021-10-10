package com.libutil.test.file;

import java.io.IOException;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class EncodeBase64FileToFileTest {

  public static void main(String args[]) {
    try {
      test();
    } catch (IOException ioe) {
      Log.e(ioe);
    }
  }

  private static void test() throws IOException {
    FileUtil.encodeBase64FileToFile("C:/test/image.jpg", "C:/tmp/b64_newline.txt");
    FileUtil.encodeBase64FileToFile("C:/test/image.jpg", "C:/tmp/b64.txt", 0);
    FileUtil.encodeBase64FileToFile("C:/test/NotFound.txt", "C:/tmp/b64_not_found.txt");
    Log.d("OK");
  }

}
