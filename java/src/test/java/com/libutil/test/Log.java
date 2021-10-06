package com.libutil.test;

import com.libutil.Logger;

public class Log extends Logger {

  public static void setup(int level, String moduleName) {
    instance = new Log();
    Log.setLevel(level);
    Log.setModuleName(moduleName);
  }

}
