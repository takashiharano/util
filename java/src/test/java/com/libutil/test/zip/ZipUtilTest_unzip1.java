package com.libutil.test.zip;

import com.libutil.test.Log;
import com.libutil.zip.ZipUtil;

public class ZipUtilTest_unzip1 {

  public static void main(String args[]) {
    Log.d("UNZIPping...");
    ZipUtil.unzip("C:/test/test.zip", "a.txt", "C:/tmp");
    Log.d("OK");
  }

}
