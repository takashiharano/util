package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class RemoveSpaceNewlineTest {

  public static void main(String args[]) {
    String str = " abc\n123 456\r\nxyz\rABC\tDEF ";
    Log.d("\"" + str + "\"");
    Log.d("\"" + StrUtil.removeSpaceNewline(str) + "\"");
  }

}
