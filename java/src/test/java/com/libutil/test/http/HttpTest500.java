package com.libutil.test.http;

public class HttpTest500 {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    HttpTest.http("https://takashiharano.com/500/", "POST", null, null, null);
  }

}
