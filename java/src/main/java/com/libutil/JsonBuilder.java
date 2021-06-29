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

import java.util.ArrayList;
import java.util.List;

public class JsonBuilder {

  StringBuilder buffer;
  int count;
  List<Integer> listCounter;
  boolean isPartial;

  public JsonBuilder() {
    this(false);
  }

  public JsonBuilder(boolean isPartial) {
    buffer = new StringBuilder();
    listCounter = new ArrayList<>();
    this.isPartial = isPartial;
    if (!isPartial) {
      buffer.append("{");
    }
  }

  public void append(String value) {
    buffer.append(value);
  }

  public void append(String key, String value) {
    if (value == null) {
      value = "null";
    } else {
      value = _quoteAndEscape(value);
    }
    _append(key, value);
  }

  public void append(String key, int value) {
    _append(key, Integer.toString(value));
  }

  public void append(String key, long value) {
    _append(key, Long.toString(value));
  }

  public void append(String key, float value) {
    _append(key, Float.toString(value));
  }

  public void append(String key, double value) {
    _append(key, Double.toString(value));
  }

  public void append(String key, boolean value) {
    _append(key, Boolean.toString(value));
  }

  public void appendObject(String key, String json) {
    _append(key, json);
  }

  public void appendList(String key, String[] array) {
    if (array == null) {
      _append(key, "null");
      return;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(_quoteAndEscape(array[i]));
    }
    sb.append("]");
    _append(key, sb.toString());
  }

  public void appendList(String key, int[] array) {
    if (array == null) {
      _append(key, "null");
      return;
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
    _append(key, sb.toString());
  }

  public void appendList(String key, long[] array) {
    if (array == null) {
      _append(key, "null");
      return;
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
    _append(key, sb.toString());
  }

  public void appendList(String key, float[] array) {
    if (array == null) {
      _append(key, "null");
      return;
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
    _append(key, sb.toString());
  }

  public void appendList(String key, double[] array) {
    if (array == null) {
      _append(key, "null");
      return;
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
    _append(key, sb.toString());
  }

  public void appendList(String key, List<String> list) {
    if (list == null) {
      _append(key, "null");
      return;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    int size = list.size();
    for (int i = 0; i < size; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(_quoteAndEscape(list.get(i)));
    }
    sb.append("]");
    _append(key, sb.toString());
  }

  public void appendComma() {
    buffer.append(",");
  }

  public void openList() {
    _newListCounter();
    buffer.append("[");
  }

  public void openList(String key) {
    _newListCounter();
    _append(key, "[");
  }

  public void closeList() {
    buffer.append("]");
    _removeListCounter();
  }

  public void appendListItem(String value) {
    if (value == null) {
      value = "null";
    } else {
      value = _quoteAndEscape(value);
    }
    _appendListItem(value);
  }

  public void appendListItemJson(String json) {
    _appendListItem(json);
  }

  public void appendListItem(int value) {
    _appendListItem(Integer.toString(value));
  }

  public void appendListItem(long value) {
    _appendListItem(Long.toString(value));
  }

  public void appendListItem(float value) {
    _appendListItem(Float.toString(value));
  }

  public void appendListItem(double value) {
    _appendListItem(Double.toString(value));
  }

  public void appendListItem(boolean value) {
    _appendListItem(Boolean.toString(value));
  }

  private void _appendListItem(String value) {
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

  private String _quoteAndEscape(String value) {
    value = value.replace("\\", "\\\\").replace("\r\n", "\\r\\n").replace("\n", "\\n").replace("\r", "\\r")
        .replace("\t", "\\t");
    value = StrUtil.quote(value);
    return value;
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

  public String toString() {
    String json;
    if (isPartial) {
      json = buffer.toString();
    } else {
      json = buffer.toString() + "}";
    }
    return json;
  }

}