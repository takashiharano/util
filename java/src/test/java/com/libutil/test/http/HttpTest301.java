package com.libutil.test.http;

import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;

public class HttpTest301 {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    String url = "https://takashiharano.com/301/";

    HttpRequest request = new HttpRequest(url);
    HttpResponse response = request.send();

    int status = response.getStatus();
    System.out.println("status = " + status);

    if (status == 200) {
      String body = response.getResponseText();
      System.out.println(body);
    }
  }

}
