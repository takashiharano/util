package com.libutil.util;

import com.libutil.Log;
import com.libutil.StrUtil;

public class StrUtilIsEmptyTest {

  public static void main(String args[]) {
    Log.d(StrUtil.isEmpty(null));
    Log.d(StrUtil.isEmpty(""));
    Log.d(StrUtil.isEmpty(" "));
    Log.d(StrUtil.isEmpty("\n"));
    Log.d(StrUtil.isEmpty(" abc "));
    Log.d(StrUtil.isEmpty("abc"));
    Log.d("---");
    Log.d(StrUtil.isEmpty(null, true));
    Log.d(StrUtil.isEmpty("", true));
    Log.d(StrUtil.isEmpty(" ", true));
    Log.d(StrUtil.isEmpty("\n", true));
    Log.d(StrUtil.isEmpty(" abc ", true));
    Log.d(StrUtil.isEmpty("abc", true));
  }

}
