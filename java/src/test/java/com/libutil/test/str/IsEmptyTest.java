package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class IsEmptyTest {

  public static void main(String args[]) {
    Log.i("isEmpty() ---");
    Log.i(StrUtil.isEmpty(null));
    Log.i(StrUtil.isEmpty(""));
    Log.i(StrUtil.isEmpty(" "));
    Log.i(StrUtil.isEmpty("\t"));
    Log.i(StrUtil.isEmpty("\n"));
    Log.i(StrUtil.isEmpty("a bc"));
    Log.i(StrUtil.isEmpty(" abc "));
    Log.i(StrUtil.isEmpty("abc"));

    Log.i("isNotEmpty() ---");
    Log.i(StrUtil.isNotEmpty(null));
    Log.i(StrUtil.isNotEmpty(""));
    Log.i(StrUtil.isNotEmpty(" "));
    Log.i(StrUtil.isNotEmpty("\t"));
    Log.i(StrUtil.isNotEmpty("\n"));
    Log.i(StrUtil.isNotEmpty("a bc"));
    Log.i(StrUtil.isNotEmpty(" abc "));
    Log.i(StrUtil.isNotEmpty("abc"));
  }

}
