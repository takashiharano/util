package com.libutil.test.system;

import com.libutil.SystemUtil;
import com.libutil.test.Log;

public class GetOsNameTest {

  public static void main(String args[]) {
    String osName = SystemUtil.getOsName();
    Log.i("osName=" + osName);
  }

}
