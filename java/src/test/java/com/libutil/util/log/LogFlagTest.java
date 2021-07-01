package com.libutil.util.log;

import com.libutil.Log;

public class LogFlagTest {

  public static void main(String args[]) {
    Log.setup(5, "ModuleA");

    logTest();

    Log.print("----------");
    Log.setFlag(0);
    logTest();

    Log.print("----------");
    Log.setFlag(Log.FLAG_TIME);
    logTest();

    Log.print("----------");
    Log.setFlag(Log.FLAG_TIME);
    Log.setDateTimeFormat("HH:mm:ss.SSS");
    logTest();

    Log.print("----------");
    Log.setFlag(Log.FLAG_TIME | Log.FLAG_LEVEL);
    logTest();
  }

  private static void logTest() {
    Log.d("DEBUG");
    Log.i("INFO");
    Log.w("WARN");
    Log.e("ERROR");
  }

}
