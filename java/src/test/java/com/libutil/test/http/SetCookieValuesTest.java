package com.libutil.test.http;

import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;

public class SetCookieValuesTest {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    String url = "https://takashiharano.com/test/";
    String q = "aaa=bbb";

    HttpRequest req = new HttpRequest(url);
    req.setCookieValue("key1", "aaa");
    req.setCookieValue("key2", "bbb");
    req.setCookieValue("key日", "あいう");
    HttpResponse res = req.send(q);

    int status = res.getStatus();
    System.out.println("status = " + status);

    if (status == 200) {
      String body = res.getResponseText();
      System.out.println(body);
    }
  }

}
