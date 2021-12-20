package com.libutil.test.bin;

import java.io.IOException;

import com.libutil.BinUtil;
import com.libutil.FileUtil;
import com.libutil.test.Log;

public class ToHBinStringTest {

  public static void main(String args[]) {
    dumpBytesTest();
    dumpBytesTestFromFile();
    dumpBytesTestFromFileLimit();
  }

  private static void dumpBytesTest() {
    byte[] b = { 0, 97, 98, 99, 127, (byte) 0x1A, (byte) 0xA1, (byte) 0xfe, (byte) 0xff, (byte) 0xA1, (byte) 0xfe,
        (byte) 0xff, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8 };
    String d = null;
    d = BinUtil.toBinString(b);
    Log.d(d);
  }

  private static void dumpBytesTestFromFile() {
    byte[] b = FileUtil.read("C:/test/a.jpg");
    String d = BinUtil.toBinString(b);
    try {
      FileUtil.write("C:/tmp/a.txt", d);
      Log.d("OK");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void dumpBytesTestFromFileLimit() {
    byte[] b = FileUtil.read("C:/test/a.jpg");
    String d = BinUtil.toBinString(b, 32);
    try {
      FileUtil.write("C:/tmp/b.txt", d);
      Log.d("OK");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
