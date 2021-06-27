package com.libutil.util;

import com.libutil.Log;
import com.libutil.SystemUtil;

public class HeapTest {

  public static void main(String args[]) {
    Log.d(SystemUtil.getHeapInfoString());
  }

}
