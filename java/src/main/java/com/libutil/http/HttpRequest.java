/*
 * The MIT License
 *
 * Copyright 2020 Takashi Harano
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.libutil.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map.Entry;

import com.libutil.Base64Util;
import com.libutil.BinUtil;

/**
 * The class HttpRequest represents a HTTP request.
 */
public class HttpRequest {

  private String url;
  private String method;
  private RequestHeaders requestHeaders;
  private Cookies cookies;
  private Proxy proxy;
  private int connectionTimeoutSec;
  private int readTimeoutSec;
  private boolean redirect;
  private String query;

  public HttpRequest(String url) {
    this.url = url;
    this.method = "GET";
  }

  public HttpRequest(String url, String method) {
    this.url = url;
    this.method = method.toUpperCase();
  }

  public HttpRequest(String uri, String method, Proxy proxy) {
    this(uri, method);
    this.proxy = proxy;
  }

  /**
   * Send an HTTP request without parameters.
   *
   * @return HttpResponse object
   */
  public HttpResponse send() {
    return send((String) null);
  }

  /**
   * Send an HTTP request with parameters.
   *
   * @param params
   *          hash map of parameters
   * @return HttpResponse object
   */
  public HttpResponse send(RequestParameters params) {
    return send(params, "UTF-8");
  }

  /**
   * Send an HTTP request with parameters.
   *
   * @param params
   *          hash map of parameters
   * @param encoding
   *          encoding
   * @return HttpResponse object
   */
  public HttpResponse send(RequestParameters params, String encoding) {
    String data;
    try {
      data = params.buildQueryString(encoding);
    } catch (UnsupportedEncodingException e) {
      HttpResponse response = new HttpResponse();
      response.setStatus(0);
      response.setErrorDetail(e);
      return response;
    }
    return send(data);
  }

  /**
   * Send an HTTP request.
   *
   * @param data
   *          a payload body
   * @return HttpResponse object
   */
  public HttpResponse send(String data) {
    return send(data, null);
  }

  /**
   * Set proxy setting.<br>
   * <br>
   * Proxy proxy = Proxy.NO_PROXY;<br>
   * <br>
   * String proxyHost = "proxy.takashiharano.com";<br>
   * int proxyPort = 8080;<br>
   * SocketAddress socketAddr = new InetSocketAddress(proxyHost, proxyPort);<br>
   * Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddr);<br>
   *
   * @param proxy
   *          proxy object
   */
  public void setProxy(Proxy proxy) {
    this.proxy = proxy;
  }

  /**
   * Set request header map.
   *
   * @param requestHeaders
   *          the hash map of request header
   */
  public void setHeaders(RequestHeaders requestHeaders) {
    this.requestHeaders = requestHeaders;
  }

  /**
   * Sets request header field.
   *
   * @param name
   *          field name
   * @param value
   *          field value
   */
  public void setHeader(String name, String value) {
    if (requestHeaders == null) {
      requestHeaders = new RequestHeaders();
    }
    requestHeaders.put(name, value);
  }

  /**
   * Returns if the request header contains the given field name.
   *
   * @param name
   *          field name (case-sensitive)
   * @return true if exists
   */
  public boolean hasHeader(String name) {
    if (requestHeaders == null) {
      return false;
    }
    return requestHeaders.has(name);
  }

  /**
   * Add Content-Type field into the request header.
   *
   * @param type
   *          the content type
   */
  public void setContentType(String type) {
    setHeader("Content-Type", type);
  }

  /**
   * Sets a query string.
   *
   * @param q
   *          query string
   */
  public void setQueryString(String q) {
    this.query = q;
  }

  /**
   * Add User-Agent field into the request header.
   *
   * @param ua
   *          the user agent
   */
  public void setUserAgent(String ua) {
    setHeader("User-Agent", ua);
  }

  /**
   * Sets cookies object to this object.
   *
   * @param cookies
   *          Cookies object
   */
  public void setCookies(Cookies cookies) {
    this.cookies = cookies;
  }

  /**
   * Sets a cookie name and value.
   *
   * @param name
   *          the cookie-name
   * @param value
   *          the cookie-value
   */
  public void setCookieValue(String name, String value) {
    if (cookies == null) {
      cookies = new Cookies();
    }
    cookies.put(name, value);
  }

  /**
   * Set user name and password to Authorization header.
   *
   * @param user
   *          user name
   * @param pass
   *          password
   */
  public void setAuthentication(String user, String pass) {
    String userPass = user + ":" + pass;
    String authData = Base64Util.encode(userPass);
    setHeader("Authorization", "Basic " + authData);
  }

  /**
   * Set the connection timeout in seconds.
   *
   * @param seconds
   *          timeout value in seconds
   */
  public void setConnectionTimeout(int seconds) {
    this.connectionTimeoutSec = seconds;
  }

  /**
   * Set the read timeout in seconds.
   *
   * @param seconds
   *          timeout value in seconds
   */
  public void setReadTimeout(int seconds) {
    this.readTimeoutSec = seconds;
  }

  /**
   * Set to true if the request should be redirected to the URL indicated in the
   * Location header.
   *
   * @param redirect
   *          true if the request should be redirected; otherwise false
   */
  public void setRedirect(boolean redirect) {
    this.redirect = redirect;
  }

  /**
   * Join the base URL and a given path.<br>
   * The URL will be BASE_URL/PATH
   *
   * @param path
   *          path to append
   */
  public void joinPath(String path) {
    UrlUtil.joinPath(url, path);
  }

  /**
   * Appends a query string to the base URL.<br>
   * 
   * @param q
   *          query string to append
   */
  public void appendQuery(String q) {
    UrlUtil.appendQuery(url, q);
  }

  private boolean isWritableMethod(String method) {
    if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Read byte array from input stream.
   *
   * @param is
   *          input stream
   * @return read bytes
   * @throws IOException
   *           If an I/O error occurs
   */
  private byte[] readStream(InputStream is) throws IOException {
    byte[] buf = null;
    byte[] b = new byte[1048576];
    int size = 0;
    int readSize;
    byte[] wkBuf = null;
    while ((readSize = is.read(b)) != -1) {
      int offset = 0;
      if (buf != null) {
        wkBuf = new byte[size];
        BinUtil.copyByteArray(buf, wkBuf, 0, buf.length);
      }
      offset = size;
      size += readSize;
      buf = new byte[size];
      if (wkBuf != null) {
        BinUtil.copyByteArray(wkBuf, buf, 0, wkBuf.length);
      }
      BinUtil.copyByteArray(b, buf, offset, readSize);
    }
    return buf;
  }

  private HttpResponse send(String data, HttpResponse response) {
    if (response != null) {
      String location = response.getHeaderValue("Location");
      if (location == null) {
        return response;
      }
      this.url = location;
    }
    try {
      response = _send(data);
      int status = response.getStatus();
      if ((status >= 300) && (status <= 399) && redirect) {
        response = send(data, response);
      }
    } catch (Exception e) {
      response = new HttpResponse();
      response.setStatus(0);
      response.setErrorDetail(e);
    }
    return response;
  }

  /**
   * Send an HTTP request.
   *
   * @param data
   *          A payload body. For GET, add it to the URL as a query string.
   * @return HttpResponse object
   * @throws IOException
   *           If an I/O error occurs
   */
  private HttpResponse _send(String data) throws IOException {
    if (query != null) {
      url = UrlUtil.appendQuery(url, query);
    }

    if ("GET".equals(method)) {
      if (data != null) {
        url = UrlUtil.appendQuery(url, data);
      }
    }

    if (proxy == null) {
      proxy = Proxy.NO_PROXY;
    }

    URL urlObj = new URL(url);
    HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection(proxy);
    conn.setRequestMethod(method);
    conn.setInstanceFollowRedirects(false);
    if (connectionTimeoutSec > 0) {
      conn.setConnectTimeout(connectionTimeoutSec * 1000);
    }

    if (readTimeoutSec > 0) {
      conn.setReadTimeout(readTimeoutSec * 1000);
    }

    if ((requestHeaders == null) || !hasHeader("Content-Type")) {
      if ("POST".equals(method) || "PUT".equals(method)) {
        setContentType("application/x-www-form-urlencoded");
      }
    }

    if (cookies != null) {
      setHeader("Cookie", cookies.toString());
    }

    if (requestHeaders != null) {
      for (Entry<String, String> entry : requestHeaders.entrySet()) {
        conn.setRequestProperty(entry.getKey(), entry.getValue());
      }
    }

    if (isWritableMethod(method)) {
      conn.setDoOutput(true);
      OutputStream os = conn.getOutputStream();
      OutputStreamWriter out = new OutputStreamWriter(os);
      if (data != null) {
        out.write(data);
      }
      out.close();
      os.close();
    }

    conn.connect();

    int statusCode = 0;
    String statusMessage = null;
    byte[] body = null;
    InputStream is = null;
    try {
      statusCode = conn.getResponseCode();
      statusMessage = conn.getResponseMessage();
      is = conn.getInputStream();
      if (is != null) {
        body = readStream(is);
        is.close();
      }
    } catch (SocketTimeoutException ste) {
      throw ste;
    } catch (IOException e) {
      is = conn.getErrorStream();
      if (is != null) {
        body = readStream(is);
        is.close();
      }
    } finally {
      if (is != null) {
        is.close();
      }
    }
    HttpResponse response = new HttpResponse();
    response.setStatus(statusCode);
    response.setStatusMessage(statusMessage);
    response.setHeaderFields(conn.getHeaderFields());
    response.setContentLength(conn.getContentLength());
    response.setBody(body);

    conn.disconnect();

    return response;
  }

}
