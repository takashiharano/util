package com.libutil.test.bin;

import com.libutil.BinUtil;
import com.libutil.test.Log;

public class HexDumpTest {

  public static void main(String args[]) {
    str2hexTest(1, 0);
    str2hexTest(16, 8);

    Log.d("1----");
    dumpBytesTest(1, 0);
    dumpBytesTest(16, 8);

    Log.d("2----");
    dumpBytesTest(30, 8);
    dumpBytesTest(31, 8);
    dumpBytesTest(32, 8);
    dumpBytesTest(33, 8);

    Log.d("3----");
    dumpBytesTest(63, 8);
    dumpBytesTest(64, 8);
    dumpBytesTest(65, 8);

    Log.d("4----");
    dumpBytesTest(30, 16);
    dumpBytesTest(31, 16);
    dumpBytesTest(32, 16);

    Log.d("5----");
    dumpBytesTest(63, 16);
    dumpBytesTest(64, 16);
    dumpBytesTest(65, 16);

    Log.d("6----");
    dumpBytesTest(255, 64);
    dumpBytesTest(256, 64);
    dumpBytesTest(257, 64);

    dumpBytesTest(512, 0);
    // dumpBytesTest0();

    Log.d("7----");
    dumpBytesTest(255, 64, 3);
    dumpBytesTest(256, 64, 3);
    dumpBytesTest(257, 64, 3);

    dumpBytesTest0();
  }

  private static void str2hexTest(int len, int limit) {
    byte[] b = new byte[len];
    for (int i = 0; i < len; i++) {
      b[i] = (byte) i;
    }
    String d = BinUtil.toHexString(b, limit);
    Log.d("");
    Log.d(d);
  }

  private static void dumpBytesTest(int len, int limit) {
    dumpBytesTest(len, limit, 16);
  }

  private static void dumpBytesTest(int len, int limit, int lastRows) {
    byte[] b = new byte[len];
    for (int i = 0; i < len; i++) {
      b[i] = (byte) i;
    }
    String d = BinUtil.dumpHex(b, limit, lastRows);
    Log.d("");
    Log.d("\n" + d);
  }

  private static void dumpBytesTest0() {
    byte[] b = { 0, 97, 98, 99, 127, (byte) 0x1A, (byte) 0xA1, (byte) 0xfe, (byte) 0xff, (byte) 0xA1, (byte) 0xfe,
        (byte) 0xff, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8 };
    String d = null;
    long t1 = System.currentTimeMillis();
    for (int i = 0; i < 1000000; i++) {
      d = BinUtil.dumpHex(b);
    }
    long t2 = System.currentTimeMillis();
    Log.d(d);
    Log.d("elapsed=" + (t2 - t1));

    t1 = System.currentTimeMillis();
    for (int i = 0; i < 100000; i++) {
      d = BinUtil.dumpHex(b);
    }
    t2 = System.currentTimeMillis();
    Log.d(d);
    Log.d("elapsed=" + (t2 - t1));

    t1 = System.currentTimeMillis();
    for (int i = 0; i < 100000; i++) {
      d = BinUtil.dumpHex(b, 2);
    }
    t2 = System.currentTimeMillis();
    Log.d(d);
    Log.d("elapsed=" + (t2 - t1));

    t1 = System.currentTimeMillis();
    for (int i = 0; i < 100000; i++) {
      d = BinUtil.dumpHex(b, 4);
    }
    t2 = System.currentTimeMillis();
    Log.d(d);
    Log.d("elapsed=" + (t2 - t1));

    d = BinUtil.toHexString(b, 3, 4, 0);
    Log.d("toHex(b, 4)");
    Log.d(d);

    Log.d("");
    Log.d("Log.d()");
    Log.d(b);

    Log.d("");
    String[] hex = BinUtil.toHexStringArray(b);
    for (int i = 0; i < hex.length; i++) {
      Log.d(i + "=" + hex[i]);
    }
  }

}
