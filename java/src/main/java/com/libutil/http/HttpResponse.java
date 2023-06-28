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

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * The class HttpResponse represents a HTTP response.
 */
public class HttpResponse {

  private int status = 0;
  private String statusMessage;
  private Map<String, List<String>> headerFields;
  private HttpCookie[] cookies;
  private byte[] body;
  private int contentLength;
  private Exception exception;

  /**
   * Returns HTTP status code.
   *
   * @return status code
   */
  public int getStatus() {
    return status;
  }

  /**
   * Sets HTTP status code.
   *
   * @param status
   *          status code
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * Returns a status message.
   *
   * @return status message
   */
  public String getStatusMessage() {
    return statusMessage;
  }

  /**
   * Sets a status message.
   *
   * @param statusMessage
   *          status message
   */
  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  /**
   * Returns header fields map.
   *
   * @return header fields map
   */
  public Map<String, List<String>> getHeaderFields() {
    return headerFields;
  }

  /**
   * Sets header fields map.
   *
   * @param headerFields
   *          header fields map
   */
  public void setHeaderFields(Map<String, List<String>> headerFields) {
    this.headerFields = headerFields;
    String[] cookieFields = getHeaderValues("Set-Cookie");
    if (cookieFields == null) {
      return;
    }
    // Set-Cookie: id=abc; Expires=Wed, 28 Jun 2023 09:15:30 GMT; Max-Age=86400;
    // Domain=takashiharano.com; Path=/; Secure; HttpOnly
    cookies = new HttpCookie[cookieFields.length];
    for (int i = 0; i < cookieFields.length; i++) {
      String c = cookieFields[i];
      HttpCookie cookie = new HttpCookie();
      cookie.parse(c);
      cookies[i] = cookie;
    }
  }

  /**
   * Returns the header value corresponding to the given name.<br>
   * If the field has multiple values it returns the first one.
   *
   * @param name
   *          field name
   * @return the value. if the name does not exist, returns null.
   */
  public String getHeaderValue(String name) {
    if (!headerFields.containsKey(name)) {
      return null;
    }
    List<String> fieldValues = headerFields.get(name);
    return fieldValues.get(0);
  }

  /**
   * Returns an array of header values corresponding to the given name.
   *
   * @param name
   *          field name
   * @return the values. if the name does not exist, returns null.
   */
  public String[] getHeaderValues(String name) {
    List<String> valueList = headerFields.get(name);
    if (valueList == null) {
      return null;
    }
    String[] values = new String[valueList.size()];
    valueList.toArray(values);
    return values;
  }

  /**
   * Returns an array of HTTP Cookie.
   *
   * @return an array of HTTP Cookie
   */
  public HttpCookie[] getCookies() {
    return cookies;
  }

  /**
   * Returns a cookie corresponding to the specified cookie-name.
   *
   * @param name
   *          the cookie-name (case-insensitive)
   * @return a cookie. if not found, returns null.
   */
  public HttpCookie getCookie(String name) {
    return getCookie(name, false);
  }

  /**
   * Returns a cookie corresponding to the specified cookie-name.
   *
   * @param name
   *          the cookie-name
   * @param caseSensitive
   *          if true, names are case sensitive.
   * @return a cookie. if not found, returns null.
   */
  public HttpCookie getCookie(String name, boolean caseSensitive) {
    if (cookies == null) {
      return null;
    }
    if (!caseSensitive) {
      name = name.toLowerCase();
    }
    for (int i = 0; i < cookies.length; i++) {
      HttpCookie cookie = cookies[i];
      String cName = cookie.getName();
      if (!caseSensitive) {
        cName = cName.toLowerCase();
      }
      if (cName.equals(name)) {
        return cookie;
      }
    }
    return null;
  }

  /**
   * Returns a cookie value corresponding to the specified cookie-name.
   *
   * @param name
   *          the cookie-name (case-insensitive)
   * @return a cookie value. if not found, returns null.
   */
  public String getCookieValue(String name) {
    return getCookieValue(name, false);
  }

  /**
   * Returns a cookie value corresponding to the specified cookie-name.
   *
   * @param name
   *          the cookie-name
   * @param caseSensitive
   *          if true, names are case sensitive.
   * @return a cookie value. if not found, returns null.
   */
  public String getCookieValue(String name, boolean caseSensitive) {
    HttpCookie cookie = getCookie(name, caseSensitive);
    if (cookie == null) {
      return null;
    }
    return cookie.getValue();
  }

  /**
   * Returns response body as byte array.
   *
   * @return response body
   */
  public byte[] getBody() {
    return body;
  }

  /**
   * Sets response body.
   *
   * @param body
   *          response body
   */
  public void setBody(byte[] body) {
    this.body = body;
  }

  /**
   * Returns the content length of the response.
   *
   * @return the length of the content
   */
  public int getContentLength() {
    return contentLength;
  }

  /**
   * Sets the content-length.
   *
   * @param contentLength
   *          content-length
   */
  public void setContentLength(int contentLength) {
    this.contentLength = contentLength;
  }

  /**
   * Returns the response body as string.
   *
   * @return response text
   */
  public String getResponseText() {
    return getResponseText("UTF-8");
  }

  /**
   * Returns the response body as string.
   *
   * @param charsetName
   *          charset name
   * @return the response text
   */
  public String getResponseText(String charsetName) {
    if (body == null) {
      return null;
    }
    String b;
    try {
      b = new String(body, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return b;
  }

  /**
   * Returns an exception object.
   *
   * @return an exception
   */
  public Exception getException() {
    return exception;
  }

  public void setErrorDetail(Exception exception) {
    this.exception = exception;
    if (exception instanceof java.net.ConnectException) {
      this.statusMessage = "CONNECTION_ERROR";
    } else if (exception instanceof java.net.SocketTimeoutException) {
      this.statusMessage = "TIME_OUT";
    } else if (exception instanceof java.net.UnknownHostException) {
      this.statusMessage = "UNKNOWN_HOST";
    }
  }

}
