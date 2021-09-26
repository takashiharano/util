package com.libutil.test.bin;

import com.libutil.BinUtil;
import com.libutil.test.Log;

public class GetSequentialBytesTest {

  public static void main(String args[]) {
    getTestBytesTest();
  }

  private static void getTestBytesTest() {
    byte[] b;
    b = BinUtil.getSequentialBytes(1);
    Log.d(b);

    b = BinUtil.getSequentialBytes(2);
    Log.d(b);

    b = BinUtil.getSequentialBytes(10);
    Log.d(b);

    b = BinUtil.getSequentialBytes(1, "!", "#");
    Log.d(b);

    b = BinUtil.getSequentialBytes(2, "!", "#");
    Log.d(b);

    b = BinUtil.getSequentialBytes(3, "!", "#");
    Log.d(b);

    b = BinUtil.getSequentialBytes(10, "!", "#");
    Log.d(b);

    b = BinUtil.getSequentialBytes(12, "!", "#");
    Log.d(b);

    b = BinUtil.getSequentialBytes(16, "!", "#", 5);
    Log.d(b);
  }

}
