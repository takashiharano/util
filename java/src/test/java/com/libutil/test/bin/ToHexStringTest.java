package com.libutil.test.bin;

import com.libutil.BinUtil;
import com.libutil.test.Log;

public class ToHexStringTest {

  public static void main(String args[]) {
    dumpBytesTest0();
  }

  private static void dumpBytesTest0() {
    byte[] b = { 0, 97, 98, 99, 127, (byte) 0x1A, (byte) 0xA1, (byte) 0xfe, (byte) 0xff, (byte) 0xA1, (byte) 0xfe,
        (byte) 0xff, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8 };
    String d = null;
    d = BinUtil.toHexString(b);
    Log.d(d);
  }

}
