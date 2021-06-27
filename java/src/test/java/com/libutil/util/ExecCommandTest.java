package com.libutil.util;

import com.libutil.CommandExecutor;
import com.libutil.Log;

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
      String result = CommandExecutor.execCommand(command);
      Log.d(result);

      result = CommandExecutor.execCommand(command, "SJIS");
      Log.d(result);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
