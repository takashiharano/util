package com.takashiharano.test.util;

import com.takashiharano.util.Log;
import com.takashiharano.util.Util;

public class ExecCommandTest {

  public static void main(String args[]) {
    execCommandTest();
  }

  private static void execCommandTest() {
    for (int i = 0; i < 1; i++) {
      _execCommandTest();
    }
  }

  private static void _execCommandTest() {
    String[] command = { "cmd", "/c", "dir c:\\tmp" };
    try {
      String result = Util.execCommand(command);
      Log.d(result);

      result = Util.execCommand(command, "SJIS");
      Log.d(result);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
