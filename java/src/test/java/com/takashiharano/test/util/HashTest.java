package com.takashiharano.test.util;

import com.takashiharano.util.FileUtil;
import com.takashiharano.util.Hash;
import com.takashiharano.util.Log;

public class HashTest {

  public static void main(String args[]) {
    fromString();
    fromFile();
  }

  private static void fromString() {
    String str = "abc";
    Log.d("hash=" + Hash.md5(str));
    Log.d("hash=" + Hash.sha1(str));
    Log.d("hash=" + Hash.sha256(str));
    Log.d("hash=" + Hash.sha512(str));
  }

  private static void fromFile() {
    String path = "C:/test/a.txt";
    byte[] b = FileUtil.readFile(path);
    Log.d("hash=" + Hash.md5(b));
    Log.d("hash=" + Hash.sha1(b));
    Log.d("hash=" + Hash.sha256(b));
    Log.d("hash=" + Hash.sha512(b));
  }

}
