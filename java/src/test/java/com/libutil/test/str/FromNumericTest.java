package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class FromNumericTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.i(StrUtil.fromBoolean(true));
    Log.i(StrUtil.fromBoolean(false));
    Log.i(StrUtil.fromFloat(1.5f));
    Log.i(StrUtil.fromDouble(1.5));
    Log.i(StrUtil.fromInteger(1));
    Log.i(StrUtil.fromLong(1L));
  }

}
