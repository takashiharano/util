package com.libutil.test.system;

import com.libutil.SystemUtil;
import com.libutil.test.Log;

public class GcTest {

  public static void main(String args[]) {
    String result = SystemUtil.gc();
    Log.i(result);
  }

}
