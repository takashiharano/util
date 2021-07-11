package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class RemoveSpaceNewlineTest {

  public static void main(String args[]) {
    Log.d("\"" + StrUtil.removeSpaceNewline(" abc\n123 456 ") + "\"");
  }

}
