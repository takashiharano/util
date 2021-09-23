package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class IsBlankTest {

  public static void main(String args[]) {
    Log.i("isBlank() ---");
    Log.i(StrUtil.isBlank(null));
    Log.i(StrUtil.isBlank(""));
    Log.i(StrUtil.isBlank(" "));
    Log.i(StrUtil.isBlank("\t"));
    Log.i(StrUtil.isBlank("\n"));
    Log.i(StrUtil.isBlank("a bc"));
    Log.i(StrUtil.isBlank(" abc "));
    Log.i(StrUtil.isBlank("abc"));

    Log.i("isNotBlank() ---");
    Log.i(StrUtil.isNotBlank(null));
    Log.i(StrUtil.isNotBlank(""));
    Log.i(StrUtil.isNotBlank(" "));
    Log.i(StrUtil.isNotBlank("\t"));
    Log.i(StrUtil.isNotBlank("\n"));
    Log.i(StrUtil.isNotBlank("a bc"));
    Log.i(StrUtil.isNotBlank(" abc "));
    Log.i(StrUtil.isNotBlank("abc"));
  }

}
