package com.takashiharano.test.util.http;

import com.takashiharano.util.Log;
import com.takashiharano.util.http.HttpRequest;
import com.takashiharano.util.http.HttpResponse;

public class HttpTimeoutTest {

  public static void main(String[] args) {
    readTimeoutTest();
    connectionTimeoutTest();
  }

  private static void readTimeoutTest() {
    String url = "https://takashiharano.com/test/sleep.cgi?t=3";
    Log.d("Start");
    _readTimeoutTest(url, 1);
    _readTimeoutTest(url, 3);
    _readTimeoutTest(url, 5);
  }

  private static void _readTimeoutTest(String url, int timeout) {
    Log.d("----------");
    Log.d("url=" + url);
    Log.d("timeout=" + timeout);
    HttpRequest httpReq = new HttpRequest(url, "GET");
    httpReq.setReadTimeout(timeout);

    HttpResponse res = httpReq.send();

    if (res.getStatus() == 0) {
      Log.e(res.getStatusMessage());
      Exception e = res.getException();
      Log.e(e);
    }
    Log.d(res.getResponseText());
  }

  private static void connectionTimeoutTest() {
    Log.d("==========");
    String url = "http://localhost/";
    _connectionTimeoutTest(url, 10);
    Log.d("Done");
  }

  private static void _connectionTimeoutTest(String url, int timeout) {
    Log.d("----------");
    Log.d("url=" + url);
    Log.d("timeout=" + timeout);
    HttpRequest httpReq = new HttpRequest(url, "GET");
    httpReq.setConnectionTimeout(timeout);

    HttpResponse res = httpReq.send();
    if (res.getStatus() == 0) {
      Log.e(res.getStatusMessage());
      Exception e = res.getException();
      Log.e(e);
    }
    Log.d(res.getResponseText());
  }

}
