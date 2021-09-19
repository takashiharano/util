package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class IsEmptyTest {

  public static void main(String args[]) {
    Log.i(StrUtil.isEmpty(null));
    Log.i(StrUtil.isEmpty(""));
    Log.i(StrUtil.isEmpty(" "));
    Log.i(StrUtil.isEmpty("\t"));
    Log.i(StrUtil.isEmpty("\n"));
    Log.i(StrUtil.isEmpty("a bc"));
    Log.i(StrUtil.isEmpty(" abc "));
    Log.i(StrUtil.isEmpty("abc"));
  }

}
