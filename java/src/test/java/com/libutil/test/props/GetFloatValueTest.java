package com.libutil.test.props;

import com.libutil.Props;
import com.libutil.test.Log;

public class GetFloatValueTest {

  public static void main(String args[]) {
    Props props = new Props("C:/test/prop1.properties");
    test1(props);
    Log.i("----------");
    test2(props);
  }

  public static void test1(Props props) {
    for (int i = 1; i <= 9; i++) {
      String key = "key" + i;
      float value = props.getFloatValue(key);
      Log.i(key + "=" + value);
    }
  }

  public static void test2(Props props) {
    for (int i = 1; i <= 9; i++) {
      String key = "key" + i;
      float value = props.getFloatValue(key, 9.8f);
      Log.i(key + "=" + value);
    }
  }

}
