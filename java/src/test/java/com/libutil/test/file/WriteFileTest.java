package com.libutil.test.file;

import java.io.IOException;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class WriteFileTest {

  public static void main(String args[]) {
    writeFileTest();
    writeFileTestSJIS();
  }

  private static void writeFileTest() {
    String dir = "C:/tmp/";

    String path = dir + "file1.txt";
    String str = "abc\nあいうえお\n华语";
    try {
      FileUtil.write(path, str);
    } catch (IOException e) {
      e.printStackTrace();
    }

    path = dir + "file2.txt";
    str = "abc\nあいうえお";
    try {
      FileUtil.write(path, str);
    } catch (IOException e) {
      e.printStackTrace();
    }

    path = dir + "file3.txt";
    byte[] bytes = { 97, 98, 99 };
    try {
      FileUtil.write(path, bytes);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Log.d("done");
  }

  private static void writeFileTestSJIS() {
    String dir = "C:/tmp/";

    String path = dir + "file_sjis.txt";
    String str = "abc\nあいうえお\n华语";
    try {
      FileUtil.write(path, str, "Shift_JIS");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
