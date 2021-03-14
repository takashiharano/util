package com.takashiharano.test.util.bin;

import com.takashiharano.util.BinUtil;

public class Hex2ByteslTest {

  public static void main(String args[]) {
    BinUtil.hex2bytes("  01  02    03 04 FF ");
    BinUtil.hex2bytes("01 FFA");
    BinUtil.hex2bytes("1 2 3 A0");
    BinUtil.hex2bytes("123A0");
  }

}
