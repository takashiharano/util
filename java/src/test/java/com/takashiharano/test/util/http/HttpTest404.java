package com.takashiharano.test.util.http;

public class HttpTest404 {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    HttpTest.http("https://takashiharano.com/404/", "GET", null, null, null);
  }

}
