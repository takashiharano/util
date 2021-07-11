package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class IsEmptyTest {

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
