package com.libutil.util.http;

import com.libutil.Log;
import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;

public class HttpTest200a {

  public static void main(String[] args) {
    test();
  }

  private static void test() {

    String url = "https://takashiharano.com/test/";
    HttpRequest httpReq = new HttpRequest(url, "GET");
    // httpReq.setUserAgent("test-UA");
    // httpReq.setContentType("abc");
    // HttpResponse res = httpReq.send("abc=123");
    HttpResponse res = httpReq.send();
    Log.d(res.getStatus());
    Log.d(res.getResponseText());
    if (res.getStatus() == 0) {
      Log.e(res.getException());
    }
  }

}
