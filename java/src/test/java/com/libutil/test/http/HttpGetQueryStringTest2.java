package com.libutil.test.http;

import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;

public class HttpGetQueryStringTest2 {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    String url = "https://takashiharano.com/test/?aaa=bbb";
    String q = "qs=abc";

    HttpRequest req = new HttpRequest(url);
    req.setQueryString(q);
    HttpResponse res = req.send();

    int status = res.getStatus();
    System.out.println("status = " + status);

    if (status == 200) {
      String body = res.getResponseText();
      System.out.println(body);
    }
  }

}
