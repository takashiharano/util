package com.libutil.test.http;

import com.libutil.http.HttpCookie;
import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;
import com.libutil.test.Log;

public class HttpGetCookiesTest {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    String url = "https://takashiharano.com/test/cookie.cgi";
    String params = "aaa=bbb";

    HttpRequest req = new HttpRequest(url);
    HttpResponse res = req.send(params);

    int status = res.getStatus();
    String statusMessage = res.getStatusMessage();

    Log.i("status=" + status + " " + statusMessage);

    HttpCookie[] cookies = res.getCookies();
    if (cookies == null) {
      Log.i("cookie = null");
      return;
    }

    for (int i = 0; i < cookies.length; i++) {
      HttpCookie cookie = cookies[i];
      Log.i("----");
      Log.i("[" + i + "] " + cookie);
      Log.i(cookie.getName() + "=" + cookie.getValue());
      Log.i("  name=" + cookie.getName());
      Log.i("  value=" + cookie.getValue());
      Log.i("Expires=" + cookie.getExpires());
      Log.i("Max-Age=" + cookie.getMaxAge());
      Log.i("Domain=" + cookie.getDomain());
      Log.i("Path=" + cookie.getPath());
      Log.i("Secure=" + cookie.isSecure());
      Log.i("HttpOnly=" + cookie.isHttpOnly());
    }
  }

}
