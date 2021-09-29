package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class GetSequentialStringTest {

  public static void main(String args[]) {
    getTestBytesTest();
  }

  private static void getTestBytesTest() {
    String s;
    s = StrUtil.getSequentialString(1);
    Log.d(s);

    s = StrUtil.getSequentialString(2);
    Log.d(s);

    s = StrUtil.getSequentialString(10);
    Log.d(s);

    s = StrUtil.getSequentialString(1, "!", "#");
    Log.d(s);

    s = StrUtil.getSequentialString(2, "!", "#");
    Log.d(s);

    s = StrUtil.getSequentialString(3, "!", "#");
    Log.d(s);

    s = StrUtil.getSequentialString(10, "!", "#");
    Log.d(s);

    s = StrUtil.getSequentialString(12, "!", "#");
    Log.d(s);

    s = StrUtil.getSequentialString(16, "!", "#", 5);
    Log.d(s);
  }

}
