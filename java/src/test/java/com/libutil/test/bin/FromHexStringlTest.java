package com.libutil.test.bin;

import com.libutil.BinUtil;
import com.libutil.test.Log;

public class FromHexStringlTest {

  public static void main(String args[]) {
    Log.d(BinUtil.fromHexString("  01  02    03 04 FF "));
    Log.d(BinUtil.fromHexString("01 FFA"));
    Log.d(BinUtil.fromHexString("1 2 3 A0"));
    Log.d(BinUtil.fromHexString("123A0"));
  }

}
