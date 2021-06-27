package com.libutil.util;

import com.libutil.FileUtil;
import com.libutil.HashUtil;
import com.libutil.Log;

public class HashTest {

  public static void main(String args[]) {
    fromString();
    fromFile();
  }

  private static void fromString() {
    String str = "abc";
    Log.d("hash=" + HashUtil.md5(str));
    Log.d("hash=" + HashUtil.sha1(str));
    Log.d("hash=" + HashUtil.sha256(str));
    Log.d("hash=" + HashUtil.sha512(str));

    // NullPointerException at md.digest(input);
    // Log.d("hash=" + HashUtil.sha256((String) null));
  }

  private static void fromFile() {
    String path = "C:/test/a.txt";
    byte[] b = FileUtil.read(path);
    Log.d("hash=" + HashUtil.md5(b));
    Log.d("hash=" + HashUtil.sha1(b));
    Log.d("hash=" + HashUtil.sha256(b));
    Log.d("hash=" + HashUtil.sha512(b));
  }

}
