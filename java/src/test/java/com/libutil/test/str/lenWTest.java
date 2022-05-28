package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class lenWTest {

  public static void main(String args[]) {
    Log.i("-- 1 --");
    Log.i(StrUtil.lenW(" "));
    Log.i(StrUtil.lenW("A"));
    Log.i(StrUtil.lenW("~"));
    Log.i(StrUtil.lenW("｡"));
    Log.i(StrUtil.lenW("ｱ"));
    Log.i(StrUtil.lenW("ﾟ"));

    Log.i("-- 2 --");
    Log.i(StrUtil.lenW("あ"));
    Log.i(StrUtil.lenW("🍺"));

    Log.i("---");
    Log.i(StrUtil.lenW("abc"));
    Log.i(StrUtil.lenW("あいう"));
    Log.i(StrUtil.lenW("abcあいう"));
    Log.i(StrUtil.lenW("aあ🍺"));
    Log.i(StrUtil.lenW("aあ🍺🌀"));
  }

}
