/*
 * The MIT License
 *
 * Copyright 2023 Takashi Harano
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
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class implements the URL string related processing.
 */
public class UrlUtil {

  /**
   * Translates a string into application/x-www-form-urlencodedformat using a
   * specific encoding scheme.
   * 
   * @param s
   *          String to be encoded.
   * @return the encoded string
   */
  public static String encodeURIComponent(String s) {
    if (s == null) {
      return s;
    }
    try {
      s = URLEncoder.encode(s, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // never reached
    }
    return s;
  }

  /**
   * Build a query string from a map.
   *
   * @param map
   *          a map to be converted
   * @return the query string
   */
  public static String buildQueryString(Map<String, String> map) {
    String q = null;
    try {
      q = buildQueryString(map, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // never reached
    }
    return q;
  }

  /**
   * Build a query string from a map.
   *
   * @param map
   *          a map to be converted
   * @param encoding
   *          encoding
   * @return the query string
   * @throws UnsupportedEncodingException
   *           If the named encoding is not supported
   */
  public static String buildQueryString(Map<String, String> map, String encoding) throws UnsupportedEncodingException {
    StringBuilder sb = new StringBuilder();
    int i = 0;
    for (Entry<String, String> entry : map.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      String encKey = URLEncoder.encode(key, encoding);
      String encVal = URLEncoder.encode(value, encoding);
      if (i > 0) {
        sb.append("&");
      }
      sb.append(encKey);
      sb.append("=");
      sb.append(encVal);
      i++;
    }
    return sb.toString();
  }

  /**
   * Join the base URL and a given path.<br>
   * The URL will be BASE_URL/PATH
   *
   * @param baseUrl
   *          the base URL
   * @param path
   *          the path
   * @return baseURL/path
   */
  public static String joinPath(String baseUrl, String path) {
    if ((baseUrl == null) || (path == null) || "".equals(path)) {
      return baseUrl;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(baseUrl);
    if (baseUrl.endsWith("/") && path.startsWith("/")) {
      path = path.substring(1);
    } else if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
      sb.append("/");
    }
    sb.append(path);
    return sb.toString();
  }

  /**
   * Join a base URL and a query string.
   *
   * @param baseUrl
   *          the base URL
   * @param query
   *          a query string
   * @return baseUrl?param
   */
  public static String appendQuery(String baseUrl, String query) {
    if ((baseUrl == null) || (query == null) || "".equals(query)) {
      return baseUrl;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(baseUrl);
    if (baseUrl.contains("?")) {
      sb.append("&");
    } else {
      sb.append("?");
    }
    sb.append(query);
    return sb.toString();
  }

  /**
   * Build a URL consisting of the baseUrl, path and query.
   *
   * @param baseUrl
   *          the base URL
   * @param path
   *          the path following baseUrl
   * @param query
   *          query string
   * @return a URL string. BASE_URL/PATH?QUERY
   */
  public static String buildUrl(String baseUrl, String path, String query) {
    String url = joinPath(baseUrl, path);
    url = appendQuery(url, query);
    return url;
  }

}
