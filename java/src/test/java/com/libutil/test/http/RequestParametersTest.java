package com.libutil.test.http;

import com.libutil.http.RequestParameters;
import com.libutil.test.Log;

public class RequestParametersTest {

  public static void main(String[] args) {
    test1();
    test2();
    test3();
    testJa();
  }

  public static void test1() {
    RequestParameters params = new RequestParameters();
    params.put("key1", "aaa");
    Log.i(params);
  }

  public static void test2() {
    RequestParameters params = new RequestParameters();
    params.put("key1", "aaa");
    params.put("key2", "bbb");
    Log.i(params);
  }

  public static void test3() {
    RequestParameters params = new RequestParameters();
    params.put("key1", "aaa");
    params.put("key2", "bbb");
    params.put("key3", "ccc");
    Log.i(params);
  }

  public static void testJa() {
    RequestParameters params = new RequestParameters();
    params.put("keyJa", "あいう");
    Log.i(params);
  }

}
