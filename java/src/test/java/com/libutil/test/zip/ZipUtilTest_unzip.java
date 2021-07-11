package com.libutil.test.zip;

import com.libutil.test.Log;
import com.libutil.zip.ZipUtil;

public class ZipUtilTest_unzip {

  public static void main(String args[]) {
    Log.d("UNZIPping...");
    ZipUtil.unzip("C:/test/test.zip", "C:/tmp");
    Log.d("OK");
  }

}
