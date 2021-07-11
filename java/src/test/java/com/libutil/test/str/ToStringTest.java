package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class ToStringTest {

  public static void main(String args[]) {
    Log.d(StrUtil.toString("ABC"));
    Log.d(StrUtil.toString(""));
    Log.d(StrUtil.toString('A'));
    Log.d(StrUtil.toString(1));
    Log.d(StrUtil.toString(1L));
    Log.d(StrUtil.toString(1.2f));
    Log.d(StrUtil.toString(1.5));
    Log.d(StrUtil.toString(true));
    Log.d(StrUtil.toString(false));
    Log.d(StrUtil.toString(new Object()));
    Log.d(StrUtil.toString(null));
  }

}
