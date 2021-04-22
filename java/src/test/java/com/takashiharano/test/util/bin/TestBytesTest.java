package com.takashiharano.test.util.bin;

import com.takashiharano.util.BinUtil;
import com.takashiharano.util.Log;

public class TestBytesTest {

  public static void main(String args[]) {
    getTestBytesTest();
  }

  private static void getTestBytesTest() {
    byte[] b;
    b = BinUtil.getTestBytes(1, -1, -1);
    Log.d(b);

    b = BinUtil.getTestBytes(2, -1, -1);
    Log.d(b);

    b = BinUtil.getTestBytes(10, -1, -1);
    Log.d(b);

    b = BinUtil.getTestBytes(1, 0x21, 0x23);
    Log.d(b);

    b = BinUtil.getTestBytes(2, 0x21, 0x23);
    Log.d(b);

    b = BinUtil.getTestBytes(3, 0x21, 0x23);
    Log.d(b);

    b = BinUtil.getTestBytes(10, 0x21, 0x23);
    Log.d(b);

    b = BinUtil.getTestBytes(12, 0x21, 0x23);
    Log.d(b);
  }

}
