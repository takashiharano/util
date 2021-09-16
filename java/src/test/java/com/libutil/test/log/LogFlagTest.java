package com.libutil.test.log;

import com.libutil.test.Log;

public class LogFlagTest {

  public static void main(String args[]) {
    Log.setup(5, "ModuleA");

    logTest();

    Log.print("--setFlag(0)--------");
    Log.setFlag(0);
    logTest();

    Log.print("--setFlag(Log.FLAG_TIME)--------");
    Log.setFlag(Log.FLAG_TIME);
    logTest();

    Log.print("--setDateTimeFormat(\"HH:mm:ss.SSS\")--------");
    Log.setFlag(Log.FLAG_TIME);
    Log.setDateTimeFormat("HH:mm:ss.SSS");
    logTest();

    Log.print("--setFlag(Log.FLAG_TIME | Log.FLAG_LEVEL)--------");
    Log.setFlag(Log.FLAG_TIME | Log.FLAG_LEVEL);
    logTest();

    Log.print("add FLAG_TIME----------");
    Log.setFlag(0);
    Log.addFlag(Log.FLAG_TIME);
    logTest();

    Log.print("add FLAG_LEVEL----------");
    Log.addFlag(Log.FLAG_LEVEL);
    logTest();

    Log.print("remove FLAG_LEVEL----------");
    Log.removeFlag(Log.FLAG_LEVEL);
    logTest();
  }

  private static void logTest() {
    Log.d("THIS_IS_DEBUG_LOG");
    Log.i("THIS_IS_INFO_LOG");
    Log.w("THIS_IS_WARN_LOG");
    Log.e("THIS_IS_ERROR_LOG");
  }

}
