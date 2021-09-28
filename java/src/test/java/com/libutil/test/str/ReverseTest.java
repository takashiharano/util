package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class ReverseTest {

  public static void main(String args[]) {
    Log.i(StrUtil.reverse("abc"));
    Log.i(StrUtil.reverse("あいう"));
    Log.i(StrUtil.reverse("abcあいう"));
    Log.i(StrUtil.reverse("aあ🍺"));
    Log.i(StrUtil.reverse(""));
    Log.i(StrUtil.reverse(null));
  }

}
