package com.libutil.test.http;

import com.libutil.http.Cookies;
import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;

public class SetCookiesTest {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    String url = "https://takashiharano.com/test/";
    String q = "aaa=bbb";

    Cookies cookies = new Cookies();
    cookies.put("key1", "aaa");
    cookies.put("key2", "bbb");
    cookies.put("key日", "あいう");

    HttpRequest req = new HttpRequest(url);
    req.setCookies(cookies);

    HttpResponse res = req.send(q);
    int status = res.getStatus();
    System.out.println("status = " + status);

    if (status == 200) {
      String body = res.getResponseText();
      System.out.println(body);
    }
  }

}
