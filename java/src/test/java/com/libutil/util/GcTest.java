package com.libutil.util;

import com.libutil.Log;
import com.libutil.Util;

public class GcTest {

  public static void main(String args[]) {
    String result = Util.gc();
    Log.i(result);
  }

}
