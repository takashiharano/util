package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class SnipTest {

  public static void main(String args[]) {
    Log.i(StrUtil.snip(null));
    Log.i(StrUtil.snip("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    Log.i(StrUtil.snip("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 3, 3));
    Log.i(StrUtil.snip("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 3, 3, "..."));

    Log.i(StrUtil.snip("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 3, 0));
    Log.i(StrUtil.snip("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 0, 3, "..."));

    Log.i(StrUtil.snip("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 3, 0, ""));
    Log.i(StrUtil.snip("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 0, 3, ""));

    Log.i(StrUtil.snip("ABCD", 2, 2, ""));
    Log.i(StrUtil.snip("ABCD", 2, 2, ".."));
    Log.i(StrUtil.snip("ABCD", 1, 2, ".."));
    Log.i(StrUtil.snip("ABCD", 2, 1, ".."));
    Log.i(StrUtil.snip("ABCD", 1, 1, ".."));
    Log.i(StrUtil.snip("ABCD", 4, 1, ".."));
  }

}
