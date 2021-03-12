package com.takashiharano.test.util.bin;

import com.takashiharano.util.BinUtil;
import com.takashiharano.util.Log;

public class Bytes2HexTest {

  public static void main(String args[]) {
    dumpBytesTest0();
  }

  private static void dumpBytesTest0() {
    byte[] b = { 0, 97, 98, 99, 127, (byte) 0x1A, (byte) 0xA1, (byte) 0xfe, (byte) 0xff, (byte) 0xA1, (byte) 0xfe,
        (byte) 0xff, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8 };
    String d = null;
    d = BinUtil.bytes2hex(b);
    Log.d(d);
  }

}
