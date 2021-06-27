package com.libutil.util;

import com.libutil.Log;
import com.libutil.RandomUtil;

public class RandomTest {

  public static void main(String args[]) {
    Log.i("int       = " + RandomUtil.getInt());
    Log.i("int 10    = " + RandomUtil.getInt(10));
    Log.i("int 1-6   = " + RandomUtil.getInt(1, 6));
    Log.i("int 10-20 = " + RandomUtil.getInt(10, 20));
    Log.i("double    = " + RandomUtil.getDouble());
    Log.i("boolean   = " + RandomUtil.getBoolean());
    Log.i("String    = " + RandomUtil.getString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", 8));
    Log.i("String    = " + RandomUtil.getString("あいうえお", 8));
  }

}
