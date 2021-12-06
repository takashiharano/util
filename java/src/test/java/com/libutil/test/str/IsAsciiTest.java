package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.TestUtil;
import com.libutil.test.Log;

public class IsAsciiTest {

  public static void main(String args[]) {
    Log.i("--- ASCII ---");
    TestUtil.assertTrue(StrUtil
        .isAscii(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"));
    TestUtil.assertTrue(StrUtil.isAscii("\t"));
    TestUtil.assertFalse(StrUtil.isAscii(""));
    TestUtil.assertFalse(StrUtil.isAscii(null));
    TestUtil.assertFalse(StrUtil.isAscii("あ"));
    TestUtil.assertFalse(StrUtil.isAscii("aあ"));

    Log.i("--- non ASCII ---");
    TestUtil.assertFalse(StrUtil.isNonAscii(
        " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"));
    TestUtil.assertFalse(StrUtil.isNonAscii("\t"));
    TestUtil.assertFalse(StrUtil.isNonAscii(""));
    TestUtil.assertFalse(StrUtil.isNonAscii(null));
    TestUtil.assertTrue(StrUtil.isNonAscii("あ"));
    TestUtil.assertTrue(StrUtil.isNonAscii("aあ"));
  }

}
