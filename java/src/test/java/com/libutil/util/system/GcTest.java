package com.libutil.util.system;

import com.libutil.Log;
import com.libutil.SystemUtil;

public class GcTest {

  public static void main(String args[]) {
    String result = SystemUtil.gc();
    Log.i(result);
  }

}
