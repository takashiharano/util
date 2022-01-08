package com.libutil.test.system;

import java.io.File;

import com.libutil.CommandExecutor;
import com.libutil.test.Log;

public class ExecCommandTest {

  public static void main(String args[]) {
    execCommandTest();
  }

  private static void execCommandTest() {
    execTest();

    Log.i("-- execWindowsCommand --------------");
    execWindowsCommandTest();
  }

  private static void execTest() {
    String command = "dir c:\\tmp";
    try {
      Log.i("----------------");
      String result = CommandExecutor.exec(command);
      Log.d(result);

      Log.i("----------------");
      result = CommandExecutor.exec(command, "UTF-8");
      Log.d(result);

      Log.i("----------------");
      result = CommandExecutor.exec("dir");
      Log.d(result);

      Log.i("----------------");
      File dir = new File("C:/tmp");
      result = CommandExecutor.exec("dir", "UTF-8", 0, dir);
      Log.d(result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void execWindowsCommandTest() {
    String command = "dir c:\\tmp";
    try {
      String result = CommandExecutor.execWindowsCommand(command);
      Log.d(result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
