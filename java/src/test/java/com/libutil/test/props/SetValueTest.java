package com.libutil.test.props;

import com.libutil.Props;
import com.libutil.test.Log;

public class SetValueTest {

  public static void main(String args[]) {
    SetValueTest tester = new SetValueTest();
    tester.test();
    tester.test1();
    tester.test2();
    tester.test3();
    tester.test4();
    tester.test5();
    tester.test6();
    tester.test7();
  }

  private void test() {
    Props props = new Props();
    props.setValue("key1", "ABC");
    String values = props.getAllProperties();
    Log.i(values);
  }

  private void test1() {
    Props props = new Props("C:/test/prop1.properties");
    props.setValue("keyA", "AAA");
    String values = props.getAllProperties();
    Log.i(values);
  }

  private void test2() {
    int v = 1;
    Props props = new Props("C:/test/prop1.properties");
    props.setValue("keyInt", v);
    String values = props.getAllProperties();
    Log.i(values);
  }

  private void test3() {
    long v = 1;
    Props props = new Props("C:/test/prop1.properties");
    props.setValue("keyLong", v);
    String values = props.getAllProperties();
    Log.i(values);
  }

  private void test4() {
    float v = 1.5f;
    Props props = new Props("C:/test/prop1.properties");
    props.setValue("keyFloat", v);
    String values = props.getAllProperties();
    Log.i(values);
  }

  private void test5() {
    double v = 1.5;
    Props props = new Props("C:/test/prop1.properties");
    props.setValue("keyDouble", v);
    String values = props.getAllProperties();
    Log.i(values);
  }

  private void test6() {
    boolean v = true;
    Props props = new Props("C:/test/prop1.properties");
    props.setValue("keyBoolean", v);
    String values = props.getAllProperties();
    Log.i(values);
  }

  private void test7() {
    String v = null;
    Props props = new Props("C:/test/prop1.properties");
    props.setValue("keyNull", v);
    String values = props.getAllProperties();
    Log.i(values);
  }

}
