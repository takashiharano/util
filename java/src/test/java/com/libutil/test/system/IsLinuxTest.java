package com.libutil.test.system;

import com.libutil.SystemUtil;
import com.libutil.test.Log;

public class IsLinuxTest {

  public static void main(String args[]) {
    Log.i("isLinux=" + SystemUtil.isLinux());
  }

}
