package com.libutil.test.props;

import com.libutil.Props;
import com.libutil.test.Log;

public class SizeTest {

  public static void main(String args[]) {
    Props props = new Props("C:/test/prop1.properties");
    Log.i(props.size());
  }

}
