package com.libutil.test.zip;

import com.libutil.test.Log;
import com.libutil.zip.ZipUtil;

public class ZipUtilTest_zip_dir1 {

  public static void main(String args[]) {
    Log.d("ZIPping...");
    ZipUtil.zip("C:/test/dir1", "C:/tmp/zip_dir1.zip");
    Log.d("OK");
  }

}
