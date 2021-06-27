package com.libutil.util.system;

import com.libutil.Log;
import com.libutil.SystemUtil;

public class GetSystemEnvTest {

  public static void main(String args[]) {
    String result = SystemUtil.getAllSystemEnv();
    Log.i(result);
  }

}
