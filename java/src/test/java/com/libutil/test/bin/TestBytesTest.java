package com.libutil.test.bin;

import com.libutil.BinUtil;
import com.libutil.test.Log;

public class TestBytesTest {

  public static void main(String args[]) {
    getTestBytesTest();
  }

  private static void getTestBytesTest() {
    byte[] b;
    b = BinUtil.getTestBytes(1);
    Log.d(b);

    b = BinUtil.getTestBytes(2);
    Log.d(b);

    b = BinUtil.getTestBytes(10);
    Log.d(b);

    b = BinUtil.getTestBytes(1, "!", "#");
    Log.d(b);

    b = BinUtil.getTestBytes(2, "!", "#");
    Log.d(b);

    b = BinUtil.getTestBytes(3, "!", "#");
    Log.d(b);

    b = BinUtil.getTestBytes(10, "!", "#");
    Log.d(b);

    b = BinUtil.getTestBytes(12, "!", "#");
    Log.d(b);

    b = BinUtil.getTestBytes(16, "!", "#", 5);
    Log.d(b);
  }

}
