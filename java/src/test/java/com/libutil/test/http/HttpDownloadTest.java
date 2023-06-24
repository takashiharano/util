package com.libutil.test.http;

import java.io.IOException;

import com.libutil.FileUtil;
import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;

public class HttpDownloadTest {

  public static void main(String[] args) {
    test();
  }

  private static void test() {
    String file = "test.zip";
    String url = "https://takashiharano.com/test/" + file;

    HttpRequest request = new HttpRequest(url);
    HttpResponse response = request.send();

    int status = response.getStatus();
    System.out.println("status = " + status);

    if (status == 200) {
      byte[] b = response.getBody();
      try {
        String savePath = "C:/tmp/" + file;
        FileUtil.write(savePath, b);
        System.out.println("OK");
      } catch (IOException ioe) {
        System.out.println(ioe);
      }
    }
  }

}
