package com.libutil.test.file;

import java.io.IOException;

import com.libutil.BinUtil;
import com.libutil.FileUtil;
import com.libutil.test.Log;

public class WriteFileTestBig {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    String dir = "C:/tmp/";

    String path = dir + "file_big.txt";
    Log.t("1");
    byte[] b = BinUtil.getTestBytes(100 * 1024 * 1024, "!", "#", 1000);
    Log.t("2");
    try {
      Log.t("3", 0);
      FileUtil.write(path, b);
      Log.t("4");
    } catch (IOException e) {
      e.printStackTrace();
    }

    Log.d("done");
  }

}
