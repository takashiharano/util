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
package com.libutil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Builds a JSON string sequentially.
 */
public class JsonBuilder {

  public static final String DEFAULT_CHARSET = "UTF-8";

  StringBuilder buffer;
  int count;
  List<Integer> listCounter;

  /**
   * Constructs a JSON builder with no characters in it.
   */
  public JsonBuilder() {
    buffer = new StringBuilder();
    listCounter = new ArrayList<>();
  }

  /**
   * Constructs a JSON builder initialized to the contents of the specified
   * string.
   * 
   * @param str
   *          the initial contents of the buffer
   */
  public JsonBuilder(String str) {
    this();
    append(str);
  }

  /**
   * Appends a partial JSON string to this sequence.<br>
   *
   * <pre>
   * e.g.,
   * {
   *   <i>STRING</i>
   * </pre>
   *
   * @param str
   *          a string to append
   */
  public void append(String str) {
    buffer.append(str);
  }

  /**
   * Appends a string value to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": "value"</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param value
   *          the value to append
   */
  public void append(String key, String value) {
    _append(key, toJson(value));
  }

  /**
   * Appends an integer value to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": 1</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param value
   *          the value to append
   */
  public void append(String key, int value) {
    _append(key, Integer.toString(value));
  }

  /**
   * Appends a long value to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": 1</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param value
   *          the value to append
   */
  public void append(String key, long value) {
    _append(key, Long.toString(value));
  }

  /**
   * Appends a float value to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": 1.5</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param value
   *          the value to append
   */
  public void append(String key, float value) {
    _append(key, Float.toString(value));
  }

  /**
   * Appends a double value to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": 1.5</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param value
   *          the value to append
   */
  public void append(String key, double value) {
    _append(key, Double.toString(value));
  }

  /**
   * Appends a boolean value to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": true</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param value
   *          the value to append
   */
  public void append(String key, boolean value) {
    _append(key, Boolean.toString(value));
  }

  /**
   * Appends a JSON string to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": JSON</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param json
   *          the JSON string to append
   */
  public void appendObject(String key, String json) {
    _append(key, json);
  }

  /**
   * Encodes a string value in Base64 and appends to this sequence.
   *
   * @param key
   *          the key to append
   * @param value
   *          the value to append
   */
  public void appendWithBase64(String key, String value) {
    String v = encodeBase64(value);
    _append(key, v);
  }

  /**
   * Encodes a byte array in Base64 and appends to this sequence.
   *
   * @param key
   *          the key to append
   * @param value
   *          the value to append
   */
  public void appendWithBase64(String key, byte[] value) {
    String v = "null";
    if (value != null) {
      String encoded = Base64.getEncoder().encodeToString(value);
      v = "\"" + encoded + "\"";
    }
    _append(key, v);
  }

  /**
   * Appends a list of string values to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": ["value1", "value2", ...]</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param array
   *          the array to append
   */
  public void appendList(String key, String[] array) {
    _append(key, toJson(array));
  }

  /**
   * Appends a list of integer values to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": [1, 2, ...]</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param array
   *          the array to append
   */
  public void appendList(String key, int[] array) {
    _append(key, toJson(array));
  }

  /**
   * Appends a list of long values to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": [1, 2, ...]</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param array
   *          the array to append
   */
  public void appendList(String key, long[] array) {
    _append(key, toJson(array));
  }

  /**
   * Appends a list of float values to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": [1.5, 2.0, ...]</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param array
   *          the array to append
   */
  public void appendList(String key, float[] array) {
    _append(key, toJson(array));
  }

  /**
   * Appends a list of double values to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": [1.5, 2.0, ...]</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param array
   *          the array to append
   */
  public void appendList(String key, double[] array) {
    _append(key, toJson(array));
  }

  /**
   * Appends a list of boolean values to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": [true, false, ...]</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param array
   *          the array to append
   */
  public void appendList(String key, boolean[] array) {
    _append(key, toJson(array));
  }

  /**
   * Appends a list of string values to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": ["value1", "value2", ...]</i>
   * </pre>
   *
   * @param key
   *          the key to append
   * @param list
   *          the list to append
   */
  public void appendList(String key, List<String> list) {
    _append(key, toJson(list));
  }

  /**
   * Appends a comma to this sequence.
   */
  public void appendComma() {
    buffer.append(",");
  }

  /**
   * Appends "[" to this sequence.
   */
  public void openList() {
    _newListCounter();
    buffer.append("[");
  }

  /**
   * Appends "[" with a key to this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   <i>"key": [</i>
   * </pre>
   *
   * @param key
   *          the key to append
   */
  public void openList(String key) {
    _newListCounter();
    _append(key, "[");
  }

  /**
   * Appends a string value to the list part of this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   "key": [
   *   <i>"value"</i>
   * </pre>
   *
   * @param value
   *          the value to append
   */
  public void appendListElement(String value) {
    if (value == null) {
      value = "null";
    } else {
      value = _quoteAndEscape(value);
    }
    _appendListElement(value);
  }

  /**
   * Appends an integer value to the list part of this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   "key": [
   *   <i>1</i>
   * </pre>
   *
   * @param value
   *          the value to append
   */
  public void appendListElement(int value) {
    _appendListElement(Integer.toString(value));
  }

  /**
   * Appends a long value to the list part of this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   "key": [
   *   <i>1</i>
   * </pre>
   *
   * @param value
   *          the value to append
   */
  public void appendListElement(long value) {
    _appendListElement(Long.toString(value));
  }

  /**
   * Appends a float value to the list part of this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   "key": [
   *   <i>1.5</i>
   * </pre>
   *
   * @param value
   *          the value to append
   */
  public void appendListElement(float value) {
    _appendListElement(Float.toString(value));
  }

  /**
   * Appends a double value to the list part of this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   "key": [
   *   <i>1.5</i>
   * </pre>
   *
   * @param value
   *          the value to append
   */
  public void appendListElement(double value) {
    _appendListElement(Double.toString(value));
  }

  /**
   * Appends a boolean value to the list part of this sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   "key": [
   *   <i>true</i>
   * </pre>
   *
   * @param value
   *          the value to append
   */
  public void appendListElement(boolean value) {
    _appendListElement(Boolean.toString(value));
  }

  /**
   * Encodes a string value in Base64 and appends to the list part of this
   * sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   "key": [
   *   <i>"BASE64_VALUE"</i>
   * </pre>
   *
   * @param value
   *          the value to append
   */
  public void appendListElementWithBase64(String value) {
    String v = encodeBase64(value);
    _appendListElement(v);
  }

  /**
   * Appends an object represented by JSON string to the list part of this
   * sequence.
   *
   * <pre>
   * e.g.,
   * {
   *   "key": [
   *   <i>JSON</i>
   * </pre>
   *
   * @param json
   *          the JSON string to append
   */
  public void appendObjectToList(String json) {
    _appendListElement(json);
  }

  /**
   * Appends "]" to this sequence.
   */
  public void closeList() {
    buffer.append("]");
    _removeListCounter();
  }

  /**
   * Returns the JSON string representation of this sequence.
   *
   * <pre>
   * e.g.,
   * {BUFFER}
   * </pre>
   */
  public String toString() {
    return toString(false);
  }

  /**
   * Returns the JSON string representation of this sequence.
   *
   * <pre>
   * e.g.,
   * partial=false:
   *   {BUFFER}
   *
   * partial=true:
   *   BUFFER
   * </pre>
   *
   * @param partial
   *          if true, the buffer is returned as is.
   * @return a JSON string representation of this sequence
   */
  public String toString(boolean partial) {
    String json;
    if (partial) {
      json = buffer.toString();
    } else {
      json = "{" + buffer.toString() + "}";
    }
    return json;
  }

  private void _appendListElement(String value) {
    int listCount = _getListCount();
    if (listCount > 0) {
      buffer.append(",");
    }
    buffer.append(value);
    _incrementListCounter();
  }

  private void _append(String key, String value) {
    if (count > 0) {
      buffer.append(",");
    }
    buffer.append(_quoteAndEscape(key));
    buffer.append(":");
    buffer.append(value);
    count++;
  }

  private void _newListCounter() {
    Integer counter = 0;
    listCounter.add(counter);
  }

  private int _getListCount() {
    int listCount = 0;
    int size = listCounter.size();
    if (size > 0) {
      listCount = listCounter.get(size - 1);
    }
    return listCount;
  }

  private void _incrementListCounter() {
    Integer counter = null;
    int size = listCounter.size();
    if (size > 0) {
      int index = size - 1;
      counter = listCounter.get(index);
      counter++;
      listCounter.set(index, counter);
    }
  }

  private void _removeListCounter() {
    int size = listCounter.size();
    if (size > 0) {
      listCounter.remove(size - 1);
    }
  }

  // ------------------------------

  /**
   * Returns JSONized string.
   *
   * @param s
   *          a string
   * @return JSON
   */
  public static String toJson(String s) {
    String json;
    if (s == null) {
      json = "null";
    } else {
      json = _quoteAndEscape(s);
    }
    return json;
  }

  /**
   * Returns a list JSON string.
   *
   * @param array
   *          an array of string values
   * @return a JSON string ["val1", "val2", ...]
   */
  public static String toJson(String[] array) {
    if (array == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      String s = array[i];
      if (s == null) {
        sb.append("null");
      } else {
        sb.append(_quoteAndEscape(s));
      }
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * Returns a list JSON string.
   *
   * @param list
   *          a list of string values
   * @return a JSON string ["val1", "val2", ...]
   */
  public static String toJson(List<String> list) {
    if (list == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < list.size(); i++) {
      if (i > 0) {
        sb.append(",");
      }
      String s = list.get(i);
      if (s == null) {
        sb.append("null");
      } else {
        sb.append(_quoteAndEscape(s));
      }
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * Returns a list JSON string.
   *
   * @param array
   *          an array of integer values
   * @return a JSON string [1, 2, ...]
   */
  public static String toJson(int[] array) {
    if (array == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(array[i]);
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * Returns a list JSON string.
   *
   * @param array
   *          an array of long values
   * @return a JSON string [1, 2, ...]
   */
  public static String toJson(long[] array) {
    if (array == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(array[i]);
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * Returns a list JSON string.
   *
   * @param array
   *          an array of float values
   * @return a JSON string [1, 2, ...]
   */
  public static String toJson(float[] array) {
    if (array == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(array[i]);
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * Returns a list JSON string.
   *
   * @param array
   *          an array of double values
   * @return a JSON string [1, 2, ...]
   */
  public static String toJson(double[] array) {
    if (array == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(array[i]);
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * Returns a list JSON string.
   *
   * @param array
   *          an array of boolean values
   * @return a JSON string [true, false, ...]
   */
  public static String toJson(boolean[] array) {
    if (array == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(array[i]);
    }
    sb.append("]");
    return sb.toString();
  }

  private static String _quoteAndEscape(String value) {
    value = value.replace("\\", "\\\\").replace("\r\n", "\\r\\n").replace("\n", "\\n").replace("\r", "\\r")
        .replace("\t", "\\t");
    value = StrUtil.quote(value);
    return value;
  }

  private String encodeBase64(String s) {
    String v = "null";
    if (s != null) {
      String encoded = null;
      try {
        byte[] srcBytes = s.getBytes(DEFAULT_CHARSET);
        encoded = Base64.getEncoder().encodeToString(srcBytes);
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
      v = "\"" + encoded + "\"";
    }
    return v;
  }

}
