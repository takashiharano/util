package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class CapitalizeTest {

  public static void main(String args[]) {
    Log.i(StrUtil.capitalize(null));
    Log.i(StrUtil.capitalize(""));
    Log.i(StrUtil.capitalize(" "));
    Log.i(StrUtil.capitalize("\t"));
    Log.i(StrUtil.capitalize("\n"));
    Log.i(StrUtil.capitalize("a bc"));
    Log.i(StrUtil.capitalize(" abc "));
    Log.i(StrUtil.capitalize("abc"));
    Log.i(StrUtil.capitalize("ABC"));
    Log.i(StrUtil.capitalize("Abc"));
    Log.i(StrUtil.capitalize("aBC"));
    Log.i(StrUtil.capitalize("a"));
    Log.i(StrUtil.capitalize("ab"));
  }

}
