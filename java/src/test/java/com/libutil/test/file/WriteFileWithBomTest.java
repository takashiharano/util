package com.libutil.test.file;

import java.io.IOException;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class WriteFileWithBomTest {

  public static void main(String args[]) {
    writeFileTest();
  }

  private static void writeFileTest() {
    String dir = "C:/tmp/";

    String path = dir + "file1.txt";
    String str = "abc\nあいうえお\n华语";
    try {
      FileUtil.writeWithBom(path, str);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Log.d("done");
  }

}
