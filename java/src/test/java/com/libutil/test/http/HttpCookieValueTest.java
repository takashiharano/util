package com.libutil.test.http;

import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;
import com.libutil.test.Log;

public class HttpCookieValueTest {

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
    _test(res, "aaa");
    _test(res, "bbb");
    _test(res, "ccc");
    _test(res, "AAA");
    _test(res, "BBB");
    _test(res, "CCC");

    Log.i("----------");

    _test2(res, "aaa");
    _test2(res, "bbb");
    _test2(res, "ccc");
    _test2(res, "AAA");
    _test2(res, "BBB");
    _test2(res, "CCC");
  }

  private static void _test(HttpResponse res, String name) {
    String value = res.getCookieValue(name);
    Log.i(name += "=" + value);
  }

  private static void _test2(HttpResponse res, String name) {
    String value = res.getCookieValue(name, true);
    Log.i(name += "=" + value);
  }

}
