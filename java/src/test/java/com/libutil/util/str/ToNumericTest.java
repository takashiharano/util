package com.libutil.util.str;

import com.libutil.Log;
import com.libutil.StrUtil;

public class ToNumericTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.i(StrUtil.toBoolean("true"));
    Log.i(StrUtil.toBoolean("TRUE"));
    Log.i(StrUtil.toBoolean("True"));
    Log.i(StrUtil.toBoolean("false"));
    Log.i(StrUtil.toBoolean("FALSE"));
    Log.i(StrUtil.toBoolean("False"));
    Log.i(StrUtil.toBoolean(""));
    Log.i(StrUtil.toBoolean("a"));
    Log.i(StrUtil.toBoolean(null));

    Log.i(StrUtil.toFloat("1.5"));
    Log.i(StrUtil.toFloat(""));
    Log.i(StrUtil.toFloat("a"));
    Log.i(StrUtil.toFloat(null));
    Log.i(StrUtil.toFloat(null, 2.1f));

    Log.i(StrUtil.toDouble("1.5"));
    Log.i(StrUtil.toDouble(""));
    Log.i(StrUtil.toDouble("a"));
    Log.i(StrUtil.toDouble(null, 2.1));

    Log.i(StrUtil.toInteger("1"));
    Log.i(StrUtil.toInteger(""));
    Log.i(StrUtil.toInteger("a"));
    Log.i(StrUtil.toInteger(null, -1));

    Log.i(StrUtil.toLong("1"));
    Log.i(StrUtil.toLong(""));
    Log.i(StrUtil.toLong("a"));
    Log.i(StrUtil.toLong(null, -1L));
  }

}
