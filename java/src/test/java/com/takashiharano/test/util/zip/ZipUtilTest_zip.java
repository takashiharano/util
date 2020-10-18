package com.takashiharano.test.util.zip;

import com.takashiharano.util.Log;
import com.takashiharano.util.zip.ZipUtil;

public class ZipUtilTest_zip {

  public static void main(String args[]) {
    Log.d("ZIPping...");
    ZipUtil.zip("C:/test1", "C:/test/test1.zip");
    Log.d("OK");

    Log.d("ZIPping...");
    ZipUtil.zip("C:/test1", "C:/test/test2.zip", true);
    Log.d("OK");
  }

}
