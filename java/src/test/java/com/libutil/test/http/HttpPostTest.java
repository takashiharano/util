package com.libutil.test.http;

import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;
import com.libutil.http.RequestParameters;

public class HttpPostTest {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    String url = "https://takashiharano.com/test/";
    String method = "POST";

    RequestParameters params = new RequestParameters();
    params.put("data1", "value1");
    params.put("data2", "value2");

    String query = params.buildQueryString();

    HttpRequest request = new HttpRequest(url, method);
    HttpResponse response = request.send(query);

    int status = response.getStatus();
    System.out.println("status = " + status);

    if (status == 200) {
      String body = response.getResponseText();
      System.out.println(body);
    }
  }

}
