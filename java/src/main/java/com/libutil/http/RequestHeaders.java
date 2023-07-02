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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The class RequestHeaders represents a HTTP request headers.
 */
public class RequestHeaders extends LinkedHashMap<String, String> {

  private static final long serialVersionUID = -7331346952226491145L;

  public RequestHeaders() {
    super();
  }

  /**
   * Returns whether a header field corresponding to the given name exists.
   *
   * @param name
   *          the field name
   * @return true if the field exists; false otherwise
   */
  public boolean has(String name) {
    return this.containsKey(name);
  }

  /**
   * Sets the general request property.<br>
   * If a property with the key already exists, overwrite its value with the new
   * value.<br>
   * NOTE: HTTP requires all request properties which can legally have multiple
   * instances with the same key to use a comma-separated list syntax which
   * enables multiple properties to be appended into a single property.
   * 
   * @param name
   *          the keyword by which the request is known(e.g., "Accept").<br>
   * @param value
   *          the value associated with it.
   */
  public void set(String name, String value) {
    put(name, value);
  }

  /**
   * Stores all the contents of the given Map into this object.<br>
   *
   * @param map
   *          a map consisting of names and values
   */
  public void set(Map<String, String> map) {
    for (Entry<String, String> entry : map.entrySet()) {
      String name = entry.getKey();
      String value = entry.getValue();
      put(name, value);
    }
  }

}
