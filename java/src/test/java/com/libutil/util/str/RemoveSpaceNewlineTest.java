package com.libutil.util.str;

import com.libutil.Log;
import com.libutil.StrUtil;

public class RemoveSpaceNewlineTest {

  public static void main(String args[]) {
    Log.d("\"" + StrUtil.removeSpaceNewline(" abc\n123 456 ") + "\"");
  }

}
