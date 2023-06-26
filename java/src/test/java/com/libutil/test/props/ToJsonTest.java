package com.libutil.test.props;

import com.libutil.Props;
import com.libutil.test.Log;

public class ToJsonTest {

  public static void main(String args[]) {
    Props props = new Props("C:/test/prop1.properties");
    test1(props);
  }

  public static void test1(Props props) {
    Log.i(props.toJSON());
  }

}
