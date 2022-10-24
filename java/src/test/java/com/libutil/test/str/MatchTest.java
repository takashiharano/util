package com.libutil.test.str;

import java.util.regex.Pattern;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class MatchTest {

  public static void main(String args[]) {
    String str = "Abc123";
    Log.d(StrUtil.match(str, "Abc123"));
    Log.d(StrUtil.match(str, "Abc"));
    Log.d(StrUtil.match(str, "abc"));
    Log.d(StrUtil.match(str, "abc", Pattern.CASE_INSENSITIVE));

    Log.d(StrUtil.match(null, "abc"));
    Log.d(StrUtil.match(str, null));
    Log.d(StrUtil.match(null, null));
  }

}
