package com.takashiharano.util;

public class JsonBuilder {

  StringBuilder buffer;
  int count;

  public JsonBuilder() {
    buffer = new StringBuilder();
    buffer.append("{");
  }

  public void append(String key, String value) {
    if (value == null) {
      value = "null";
    } else {
      value = StrUtil.quote(value);
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
      sb.append(StrUtil.quote(array[i]));
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

  private void _append(String key, String value) {
    if (count > 0) {
      buffer.append(",");
    }
    buffer.append(StrUtil.quote(key));
    buffer.append(":");
    buffer.append(value);
    count++;
  }

  public String toString() {
    String json = buffer.toString() + "}";
    return json;
  }

}
