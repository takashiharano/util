package com.libutil.test.zip;

import com.libutil.test.Log;
import com.libutil.zip.ZipUtil;

public class ZipUtilTest_zip_files {

  public static void main(String args[]) {
    Log.d("ZIPping...");
    String[] files = { "C:/test/a.txt", "C:/test/b.txt", "C:/test/dir1" };
    ZipUtil.zip(files, "C:/tmp/zip_files.zip");
    Log.d("OK");
  }

}
