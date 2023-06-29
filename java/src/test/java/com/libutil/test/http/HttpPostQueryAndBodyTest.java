package com.libutil.test.http;

import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;

public class HttpPostQueryAndBodyTest {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    String url = "https://takashiharano.com/test/";
    String q = "qs=abc";
    String data = "data1";

    HttpRequest req = new HttpRequest(url, "POST");
    req.setQueryString(q);
    HttpResponse res = req.send(data);

    int status = res.getStatus();
    System.out.println("status = " + status);

    if (status == 200) {
      String body = res.getResponseText();
      System.out.println(body);
    }
  }

}
