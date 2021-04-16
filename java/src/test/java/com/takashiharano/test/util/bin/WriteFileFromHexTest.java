package com.takashiharano.test.util.bin;

import java.io.IOException;

import com.takashiharano.util.BinUtil;
import com.takashiharano.util.Log;

public class WriteFileFromHexTest {

  public static void main(String args[]) {
    String hex = "41 42 43 00 0A 31 32 33 FF";
    String path = "C:/test/hex.txt";
    try {
      BinUtil.writeFileFromHex(path, hex);
      Log.d("OK");
    } catch (IOException e) {
      Log.e(e);
    }
  }

}
