package com.libutil.test.zip;

import com.libutil.test.Log;
import com.libutil.zip.ZipUtil;

public class ZipUtilTest_zip_1file {

  public static void main(String args[]) {
    Log.d("ZIPping...");
    ZipUtil.zip("C:/test/a.txt", "C:/tmp/zip1.zip");
    Log.d("OK");
  }

}
