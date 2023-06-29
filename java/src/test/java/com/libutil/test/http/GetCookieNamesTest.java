package com.libutil.test.http;

import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;

public class GetCookieNamesTest {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    String url = "https://takashiharano.com/test/cookie.cgi";
    String q = "aaa=bbb";

    HttpRequest req = new HttpRequest(url);
    HttpResponse res = req.send(q);

    int status = res.getStatus();
    System.out.println("status = " + status);

    String[] names = res.getCookieNames();
    if (names == null) {
      System.out.println("null");
      return;
    }

    for (int i = 0; i < names.length; i++) {
      System.out.println("[" + i + "] " + names[i]);
    }
  }

}
