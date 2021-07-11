package com.libutil.test.zip;

import com.libutil.test.Log;
import com.libutil.zip.ZipUtil;

public class ZipUtilTest_zip {

  public static void main(String args[]) {
    Log.d("ZIPping...");
    ZipUtil.zip("C:/test1", "C:/tmp/test1.zip");
    Log.d("OK");

    Log.d("ZIPping...");
    ZipUtil.zip("C:/test1", "C:/tmp/test1-junk.zip", true);
    Log.d("OK");
  }

}
