package com.takashiharano.test.util.http;

import com.takashiharano.util.Log;
import com.takashiharano.util.http.HttpRequest;
import com.takashiharano.util.http.HttpResponse;

public class HttpTest401 {

  public static void main(String[] args) {
    testOK();
    testNG();
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

  private static void testNG() {
    String url = "https://takashiharano.com/401/";
    HttpRequest http = new HttpRequest(url, "GET");
    http.setAuthentication("user1", "0000");
    HttpResponse res = http.send();
    Log.d(res.getStatus());
    Log.d(res.getResponseText());
    if (res.getStatus() == 0) {
      Log.e(res.getException());
    }
  }

}
