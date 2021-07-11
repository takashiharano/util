package com.libutil.test.system;

import com.libutil.SystemUtil;
import com.libutil.test.Log;

public class GetSystemEnvTest {

  public static void main(String args[]) {
    String result = SystemUtil.getAllSystemEnv();
    Log.i(result);
  }

}
