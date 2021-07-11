package com.libutil.test.util;

import com.libutil.CommandExecutor;
import com.libutil.test.Log;

public class CommandTest {
  private static final int LOOP = 1;

  public static void main(String args[]) {
    String[] command = { "cmd", "/c", "dir c:\\test" };
    execCommandTest(command, "SJIS");

    String[] command2 = { "cmd", "/c", "dir c:\\testa" };
    execCommandTest(command2, "SJIS");

    Log.d("------------------");
    execCommandTest2("dir c:\\test", "SJIS");
    execCommandTest2("dir c:\\testa", "SJIS");
  }

  private static void execCommandTest(String[] command, String charset) {
    for (int i = 0; i < LOOP; i++) {
      Log.d("loop=" + i);
      _execCommandTest(command, charset);
    }
  }

  private static void _execCommandTest(String[] command, String charset) {
    CommandExecutor executor = new CommandExecutor();
    try {
      String result = executor.exec(command, charset);
      Log.d(result);

      int exitStatus = executor.getExitStatus();
      Log.d("exit=" + exitStatus);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void execCommandTest2(String command, String charset) {
    try {
      String result = CommandExecutor.execWindowsCommand(command, charset);
      Log.d(result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
