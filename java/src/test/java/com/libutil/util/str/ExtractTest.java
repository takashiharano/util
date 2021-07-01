package com.libutil.util.str;

import java.util.regex.Pattern;

import com.libutil.Log;
import com.libutil.StrUtil;

public class ExtractTest {

  public static void main(String args[]) {
    String str = "Abc123";
    Log.d(StrUtil.extract(str, "(\\d+)"));
    Log.d(StrUtil.extract(str, "(Abc)"));
    Log.d(StrUtil.extract(str, "(abc)", Pattern.CASE_INSENSITIVE));
    Log.d(StrUtil.extract(str, "(Ab)c(\\d+)", 0, 2));

    Log.d(StrUtil.extract(str, "(abc)"));

    Log.d(StrUtil.extract(str, "\\d"));
  }

}
