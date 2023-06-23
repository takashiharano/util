package com.libutil.test.http;

import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;
import com.libutil.test.Log;

public class HttpTest401OK {

  public static void main(String[] args) {
    testOK();
  }

  private static void testOK() {
    String url = "https://takashiharano.com/401/";
    HttpRequest http = new HttpRequest(url, "GET");
    http.setAuthentication("user1", "1111");
    HttpResponse res = http.send();
    Log.d(res.getStatus());
    Log.d(res.getResponseText());
    if (res.getStatus() == 0) {
      Log.e(res.getException());
    }
  }

}
