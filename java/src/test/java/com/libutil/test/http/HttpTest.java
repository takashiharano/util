package com.libutil.test.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.libutil.http.HttpRequest;
import com.libutil.http.HttpResponse;
import com.libutil.http.RequestHeaders;
import com.libutil.http.RequestParameters;
import com.libutil.test.Log;

public class HttpTest {

  private static final String USER_AGENT_ORIGINAL = "Test Agent";
  private static final String DEFAULT_URL = "https://takashiharano.com/test/";

  public static void main(String[] args) {
    test1(args);
  }

  private static void test1(String[] args) {
    String url = DEFAULT_URL;
    if (args.length >= 1) {
      url = args[0];
    }

    String proxyHost = null;
    int proxyPort = -1;
    if (args.length >= 3) {
      // System.setProperty("proxySet", "true");
      // System.setProperty("proxyHost", args[1]);
      // System.setProperty("proxyPort", args[2]);
      proxyHost = args[1];
      proxyPort = Integer.parseInt(args[2]);
    }

    Proxy proxy = Proxy.NO_PROXY;
    if ((proxyHost != null) && (proxyPort > 0)) {
      // "proxy.takashiharano.com", 8080
      SocketAddress socketAddr = new InetSocketAddress(proxyHost, proxyPort);
      proxy = new Proxy(Proxy.Type.HTTP, socketAddr);
    }

    RequestHeaders reqHeaders = new RequestHeaders();
    reqHeaders.put("User-Agent", USER_AGENT_ORIGINAL);

    RequestParameters params = new RequestParameters();
    params.put("abc", "123");
    params.put("あいう", "えお");
    params.put("A&B", "%987");

    String param1 = HttpRequest.buildQueryString(params, "Shift_JIS");
    http(url, "GET", param1, reqHeaders, proxy);
    System.out.println("\n\n");

    String param2 = HttpRequest.buildQueryString(params, "UTF-8");
    http(url, "POST", param2, reqHeaders, proxy);
  }

  /**
   * Do HTTP
   *
   * @param url
   * @param method
   * @param params
   * @param proxy
   * @throws IOException
   */
  public static void http(String url, String method, String params, RequestHeaders reqHeaders, Proxy proxy) {
    HttpRequest httpReq = new HttpRequest(url, method, proxy);
    httpReq.setRequestHeaders(reqHeaders);
    HttpResponse response = httpReq.send(params);

    int statusCode = response.getStatus();
    String statusMessage = response.getStatusMessage();
    Map<String, List<String>> headerFields = response.getHeaderFields();

    StringBuilder sb = new StringBuilder();
    sb.append("--------------------\n");
    sb.append(url + "\n");
    sb.append("statusCode=" + statusCode + "\n");
    sb.append("statusMessage=" + statusMessage + "\n\n");
    sb.append("--- Response Header ---\n");

    // status line
    List<String> statusHeader = headerFields.get(null);
    for (int i = 0; i < statusHeader.size(); i++) {
      sb.append(statusHeader.get(i));
      sb.append("\n");
    }

    // response headers
    for (Entry<String, List<String>> entry : headerFields.entrySet()) {
      String key = entry.getKey();
      if (key == null) {
        continue;
      }
      List<String> headerField = entry.getValue();
      sb.append(key + ": ");
      for (int i = 0; i < headerField.size(); i++) {
        sb.append(headerField.get(i));
        sb.append("\n");
      }
    }
    statusCode = 200;
    // response body
    if (statusCode == 200) {
      sb.append("\n");
      sb.append("--- Body ---\n");
      String body = response.getResponseText();
      sb.append(body);
    } else {
      sb.append("---ERROR---");
    }

    System.out.println(sb.toString());

    if (response.getStatus() == 0) {
      Log.e(response.getException());
    }
  }

}
