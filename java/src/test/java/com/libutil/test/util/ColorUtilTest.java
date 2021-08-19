package com.libutil.test.util;

import com.libutil.ColorUtil;
import com.libutil.test.Log;

public class ColorUtilTest {

  public static void main(String args[]) {
    colorTest();
    rgb10to16Test();
    rgb16to10Test();
  }

  private static void colorTest() {
    Log.d("color=" + ColorUtil.adjust("#00bfff", 50, 0));
    Log.d("color=" + ColorUtil.adjust("#00bfff", 0, 50));
  }

  private static void rgb10to16Test() {
    Log.d("rgb10to16");
    Log.d(ColorUtil.rgb10to16(0, 191, 255));
    Log.d(ColorUtil.rgb10to16(255, 255, 255));
    Log.d(ColorUtil.rgb10to16(10, 255, 255));
  }

  private static void rgb16to10Test() {
    Log.d("rgb16to10");
    Log.d(ColorUtil.rgb16to10("#00bfff"));
    Log.d(ColorUtil.rgb16to10("#fff"));
    Log.d(ColorUtil.rgb16to10("#0affff"));
  }

}
