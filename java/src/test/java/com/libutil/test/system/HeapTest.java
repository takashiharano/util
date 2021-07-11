package com.libutil.test.system;

import com.libutil.SystemUtil;
import com.libutil.test.Log;

public class HeapTest {

  public static void main(String args[]) {
    Log.d(SystemUtil.getHeapInfoString());
  }

}
