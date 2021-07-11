package com.libutil.test.bin;

import java.io.IOException;

import com.libutil.BinUtil;
import com.libutil.test.Log;

public class WriteFileFromHexStringTest {

  public static void main(String args[]) {
    String hex = "41 42 43 00 0A 31 32 33 FF";
    String path = "C:/test/hex.txt";
    try {
      BinUtil.writeFileFromHexString(path, hex);
      Log.d("OK");
    } catch (IOException e) {
      Log.e(e);
    }
  }

}
