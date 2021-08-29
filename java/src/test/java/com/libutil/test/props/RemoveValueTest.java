package com.libutil.test.props;

import com.libutil.Props;
import com.libutil.test.Log;

public class RemoveValueTest {
  public static void main(String args[]) {
    RemoveValueTest tester = new RemoveValueTest();
    tester.test1();
    tester.test2();
  }

  private void test1() {
    Props props = new Props("C:/test/prop.properties");
    props.removeValue("key1");
    String values = props.getAllProperties();
    Log.i(values);
  }

  private void test2() {
    Props props = new Props("C:/test/prop.properties");
    props.removeValue("xxxx");
    String values = props.getAllProperties();
    Log.i(values);
  }

}
