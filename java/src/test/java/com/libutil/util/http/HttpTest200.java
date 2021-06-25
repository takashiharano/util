package com.libutil.util.http;

public class HttpTest200 {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    HttpTest.http("https://takashiharano.com/test/", "GET", null, null, null);
  }

}
