package com.libutil.util.http;

import com.libutil.Log;
import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;

public class HttpConnErrorTest {

  public static void main(String[] args) {
    test("http://xxx.local/");
    test("http://192.168.0.253/");
  }

  private static void test(String url) {
    Log.d("==========");
    Log.d("url=" + url);
    HttpRequest httpReq = new HttpRequest(url, "GET");
    HttpResponse res = httpReq.send();
    if (res.getStatus() == 0) {
      Log.e(res.getStatusMessage());
      Exception e = res.getException();
      Log.e(e);
    }
    Log.d(res.getResponseText());
    Log.d("Done");
  }

}
