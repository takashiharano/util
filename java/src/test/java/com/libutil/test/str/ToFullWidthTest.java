package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class ToFullWidthTest {

  public static void main(String args[]) {
    Log.i(StrUtil.toFullWidth(
        " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"));
  }

}
