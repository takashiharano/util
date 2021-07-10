package com.libutil.test.props;

import com.libutil.Props;
import com.libutil.test.Log;

public class GetValueTest {

  public static void main(String args[]) {
    Props props = new Props("C:/test/prop1.properties");
    test1(props);
    Log.i("----------");
    test2(props);
  }

  public static void test1(Props props) {
    for (int i = 0; i <= 20; i++) {
      String key = "key" + i;
      String value = props.getValue(key);
      if (value == null) {
        Log.i(key + "=null");
      } else {
        Log.i(key + "=\"" + value + "\"");
      }
    }
  }

  public static void test2(Props props) {
    for (int i = 0; i <= 20; i++) {
      String key = "key" + i;
      String value = props.getValue(key, "NOT_FOUND");
      if (value == null) {
        Log.i(key + "=null");
      } else {
        Log.i(key + "=\"" + value + "\"");
      }
    }
  }

}
