package com.takashiharano.test.util;

import com.takashiharano.util.Log;
import com.takashiharano.util.Util;

public class GcTest {

  public static void main(String args[]) {
    String result = Util.gc();
    Log.i(result);
  }

}
