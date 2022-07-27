package com.libutil.test.hash;

import com.libutil.HashUtil;
import com.libutil.test.Log;

public class GetHashStringTest {

  public static void main(String args[]) {
    Log.d(HashUtil.getHashString("", "SHA-1"));
    Log.d(HashUtil.getHashString("", "SHA-256"));
    Log.d(HashUtil.getHashString("", "SHA-512"));
  }

}
