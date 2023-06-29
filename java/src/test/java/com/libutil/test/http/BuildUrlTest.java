package com.libutil.test.http;

import com.libutil.http.UrlUtil;

public class BuildUrlTest {

  public static void main(String[] args) {
    test1();
    test2();
    test3();
    test4();
    test5();
    test6();
    test7();
    test8();
    test9();
  }

  private static void test(String baseUrl, String path, String query) {
    String url = UrlUtil.buildUrl(baseUrl, path, query);
    System.out.println("url=" + url);
  }

  private static void test1() {
    String baseUrl = "https://takashiharano.com/";
    String path = "test";
    String query = "aaa=bbb";
    test(baseUrl, path, query);
  }

  private static void test2() {
    String baseUrl = "https://takashiharano.com/";
    String path = "/test";
    String query = "aaa=bbb";
    test(baseUrl, path, query);
  }

  private static void test3() {
    String baseUrl = "https://takashiharano.com";
    String path = "test";
    String query = "aaa=bbb";
    test(baseUrl, path, query);
  }

  private static void test4() {
    String baseUrl = "https://takashiharano.com/";
    String path = null;
    String query = null;
    test(baseUrl, path, query);
  }

  private static void test5() {
    String baseUrl = "https://takashiharano.com/";
    String path = "test";
    String query = null;
    test(baseUrl, path, query);
  }

  private static void test6() {
    String baseUrl = "https://takashiharano.com";
    String path = "";
    String query = "";
    test(baseUrl, path, query);
  }

  private static void test7() {
    String baseUrl = "https://takashiharano.com/";
    String path = "";
    String query = "";
    test(baseUrl, path, query);
  }

  private static void test8() {
    String baseUrl = "https://takashiharano.com/";
    String path = "";
    String query = null;
    test(baseUrl, path, query);
  }

  private static void test9() {
    String baseUrl = "https://takashiharano.com/";
    String path = null;
    String query = "";
    test(baseUrl, path, query);
  }

}
