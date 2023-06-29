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
import java.util.LinkedHashMap;

/**
 * The class RequestParameters represents a HTTP request parameters.
 */
public class RequestParameters extends LinkedHashMap<String, String> {

  private static final long serialVersionUID = -7713124708514108249L;

  public RequestParameters() {
    super();
  }

  /**
   * Build a query string from hash table with default encoding UTF-8.
   *
   * @return the query string
   */
  public String buildQueryString() {
    return UrlUtil.buildQueryString(this);
  }

  /**
   * Build a query string from hash table.
   *
   * @param encoding
   *          encoding
   * @return the query string
   * @throws UnsupportedEncodingException
   *           If the named encoding is not supported
   */
  public String buildQueryString(String encoding) throws UnsupportedEncodingException {
    return UrlUtil.buildQueryString(this, encoding);
  }

  public String toString() {
    return buildQueryString();
  }

}
