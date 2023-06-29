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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Cookies extends LinkedHashMap<String, Cookie> {

  private static final long serialVersionUID = 1L;

  /**
   * Converts the given key and value into a Cookie object and associates it with
   * the given key in the map.
   *
   * @param name
   *          the cookie-name
   * @param value
   *          the cookie-value
   */
  public void put(String name, String value) {
    Cookie cookie = new Cookie(name, value);
    put(name, cookie);
  }

  /**
   * Stores all the contents of the given Map into this object.
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

  /**
   * Returns whether a cookie corresponding to the given name exists.
   *
   * @param name
   *          the cookie-name
   * @return true if the cookie exists; false otherwise
   */
  public boolean has(String name) {
    return this.containsKey(name);
  }

  /**
   * Returns an array of the cookie field name.
   *
   * @return an array of the cookie field name.<br>
   *         if there are no fields, returns an empty array [].
   */
  public String[] getNames() {
    List<String> list = new ArrayList<>();
    for (Entry<String, Cookie> entry : this.entrySet()) {
      String name = entry.getKey();
      if (name != null) {
        list.add(name);
      }
    }
    String[] array = new String[list.size()];
    list.toArray(array);
    return array;
  }

  /**
   * Returns a string in HTTP header Cookie field format.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int i = 0;
    for (Entry<String, Cookie> entry : this.entrySet()) {
      Cookie cookie = entry.getValue();
      String name = cookie.getName();
      String value = cookie.getValue();
      if (i > 0) {
        sb.append("; ");
      }
      try {
        String encName = URLEncoder.encode(name, "UTF-8");
        String encVal = URLEncoder.encode(value, "UTF-8");
        sb.append(encName);
        sb.append("=");
        sb.append(encVal);
      } catch (UnsupportedEncodingException e) {
        // never reached
      }
      i++;
    }
    return sb.toString();
  }

}
