package com.libutil.test.props;

import com.libutil.Props;
import com.libutil.test.Log;

public class GetBooleanValueTest {

  public static void main(String args[]) {
    Props props = new Props("C:/test/prop-bool.properties");
    Log.i("1t----------");
    test1t(props);

    Log.i("1f----------");
    test1f(props);

    Log.i("2t----------");
    test2t(props);

    Log.i("2f----------");
    test2f(props);

    Log.i("3t----------");
    test3t(props);

    Log.i("3f----------");
    test3f(props);

    Log.i("4f----------");
    test4f(props);
  }

  public static void test1t(Props props) {
    for (int i = 1; i <= 8; i++) {
      String key = "key" + i + "t";
      boolean value = props.getBooleanValue(key);
      Log.i(key + "=" + value);
    }
  }

  public static void test1f(Props props) {
    for (int i = 1; i <= 8; i++) {
      String key = "key" + i + "f";
      boolean value = props.getBooleanValue(key);
      Log.i(key + "=" + value);
    }
  }

  public static void test2t(Props props) {
    for (int i = 1; i <= 8; i++) {
      String key = "key" + i + "t";
      boolean value = props.getBooleanValue(key, "1");
      Log.i(key + "=" + value);
    }
  }

  public static void test2f(Props props) {
    for (int i = 1; i <= 8; i++) {
      String key = "key" + i + "f";
      boolean value = props.getBooleanValue(key, "1");
      Log.i(key + "=" + value);
    }
  }

  public static void test3t(Props props) {
    String[] tvalues = { "true", "1" };
    for (int i = 1; i <= 8; i++) {
      String key = "key" + i + "t";
      boolean value = props.getBooleanValue(key, tvalues);
      Log.i(key + "=" + value);
    }
  }

  public static void test3f(Props props) {
    String[] tvalues = { "true", "1" };
    for (int i = 1; i <= 8; i++) {
      String key = "key" + i + "f";
      boolean value = props.getBooleanValue(key, tvalues);
      Log.i(key + "=" + value);
    }
  }

  public static void test4f(Props props) {
    String[] tvalues = { null, "", "false" };
    for (int i = 1; i <= 8; i++) {
      String key = "key" + i + "f";
      boolean value = props.getBooleanValue(key, tvalues);
      Log.i(key + "=" + value);
    }
  }

}
