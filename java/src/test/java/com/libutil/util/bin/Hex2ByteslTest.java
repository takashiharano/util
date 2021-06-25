package com.libutil.util.bin;

import com.libutil.BinUtil;
import com.libutil.Log;

public class Hex2ByteslTest {

  public static void main(String args[]) {
    Log.d(BinUtil.hex2bytes("  01  02    03 04 FF "));
    Log.d(BinUtil.hex2bytes("01 FFA"));
    Log.d(BinUtil.hex2bytes("1 2 3 A0"));
    Log.d(BinUtil.hex2bytes("123A0"));
  }

}
