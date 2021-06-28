package com.libutil;

public class Log extends _Log {

  public static void init(int level, String moduleName) {
    instance = new Log();
    Log.setLevel(level);
    Log.setModuleName(moduleName);
  }

}
