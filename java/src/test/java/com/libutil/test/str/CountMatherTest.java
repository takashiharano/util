package com.libutil.test.str;

import java.util.regex.Pattern;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class CountMatherTest {

  public static void main(String args[]) {
    String str = "aaaaABCabc";

    Log.d(StrUtil.countMatcher(str, "aa"));
    Log.d(StrUtil.countMatcher(str, "aa", true));

    Log.d(StrUtil.countMatcher(str, "x"));
    Log.d(StrUtil.countMatcher(str, "x", true));

    Log.d(StrUtil.countMatcher(str, "abc"));
    Log.d(StrUtil.countMatcher(str, "abc", Pattern.CASE_INSENSITIVE));

    Log.d(StrUtil.countMatcher(str, "ABC"));
    Log.d(StrUtil.countMatcher(str, "ABC", Pattern.CASE_INSENSITIVE));
  }

}
