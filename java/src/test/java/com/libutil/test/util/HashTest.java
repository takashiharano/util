package com.libutil.test.util;

import com.libutil.FileUtil;
import com.libutil.HashUtil;
import com.libutil.test.Log;

public class HashTest {

  public static void main(String args[]) {
    fromString();
    fromFile();
  }

  private static void fromString() {
    String str = "abc";
    Log.d("hash=" + HashUtil.getHashString(str, "MD5"));
    Log.d("hash=" + HashUtil.getHashString(str, "SHA-1"));
    Log.d("hash=" + HashUtil.getHashString(str, "SHA-256"));
    Log.d("hash=" + HashUtil.getHashString(str, "SHA-512"));

    // java.security.NoSuchAlgorithmException: x MessageDigest not available
    // Log.d("hash=" + HashUtil.getHashString(str, "x"));

    // NullPointerException
    // Log.d("hash=" + HashUtil.getHashString((String) null, "SHA-256"));
  }

  private static void fromFile() {
    String path = "C:/test/a.txt";
    byte[] b = FileUtil.read(path);
    Log.d("hash=" + HashUtil.getHashString(b, "MD5"));
    Log.d("hash=" + HashUtil.getHashString(b, "SHA-1"));
    Log.d("hash=" + HashUtil.getHashString(b, "SHA-256"));
    Log.d("hash=" + HashUtil.getHashString(b, "SHA-512"));

    // java.security.NoSuchAlgorithmException: x MessageDigest not available
    // Log.d("hash=" + HashUtil.getHashString(b, "x"));
  }

}
