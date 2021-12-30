package com.libutil.test.bin;

import com.libutil.BinUtil;
import com.libutil.test.Log;

public class FromBinStringlTest {

  public static void main(String args[]) {
    Log.d(BinUtil.fromBinString("01000001 01000010 01000011"));
    Log.d(BinUtil.fromBinString(" 01000001  01000010 01000011 "));
    Log.d(BinUtil.fromBinString("010000010100001001000011"));
    Log.d(BinUtil.fromBinString("01000001\n01000010\r\n01000011"));
  }

}
