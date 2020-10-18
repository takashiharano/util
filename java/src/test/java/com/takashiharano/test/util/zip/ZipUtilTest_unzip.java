package com.takashiharano.test.util.zip;

import com.takashiharano.util.Log;
import com.takashiharano.util.zip.ZipUtil;

public class ZipUtilTest_unzip {

  public static void main(String args[]) {
    Log.d("UNZIPping...");
    ZipUtil.unzip("C:/test/test.zip", "C:/tmp");
    Log.d("OK");
  }

}
