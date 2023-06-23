package com.libutil.test.http;

import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;

public class HttpGetTest {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    String url = "https://takashiharano.com/test/";
    String q = "aaa=bbb";

    HttpRequest request = new HttpRequest(url);
    HttpResponse response = request.send(q);

    int status = response.getStatus();
    System.out.println("status = " + status);

    if (status == 200) {
      String body = response.getResponseText();
      System.out.println(body);
    }
  }

}
