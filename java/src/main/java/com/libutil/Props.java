/*
 * The MIT License
 *
 * Copyright 2021 Takashi Harano
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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

/**
 * The Props class represents a persistent set of properties.
 */
public class Props {

  protected String filePath;
  protected Properties properties;

  /**
   * Creates an empty property list with no values.
   */
  public Props() {
    properties = new Properties();
  }

  /**
   * Creates a property list with the values ​​loaded from the file.
   *
   * @param filePath
   *          the properties file path
   */
  public Props(String filePath) {
    filePath = filePath.replace("\\", "/");
    this.filePath = filePath;
    properties = new Properties();
    load(filePath);
  }

  private void load(String filePath) {
    try (FileInputStream fis = new FileInputStream(filePath)) {
      properties.load(fis);
    } catch (IOException ioe) {
      String message = "Failed to load properties file: " + filePath;
      throw new RuntimeException(message, ioe);
    }
  }

  /**
   * Returns the file path of the properties.
   *
   * @return file path
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * Returns the value for the specified key.
   *
   * @param key
   *          the key whose associated value is to be returned
   * @return the value in this property list with the specified key value.
   */
  public String getValue(String key) {
    return getValue(key, null);
  }

  /**
   * Returns the value for the specified key.
   *
   * @param key
   *          the key of the value
   * @param defaultValue
   *          the value if specified key is not found
   * @return the value in this property list with the specified key value.
   */
  public String getValue(String key, String defaultValue) {
    String value = properties.getProperty(key);
    if (value == null) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * Returns the value for the specified key as an integer.
   *
   * @param key
   *          the key of the value
   * @return the value in this property list with the specified key value.
   */
  public int getIntValue(String key) {
    return getIntValue(key, 0);
  }

  /**
   * Returns the value for the specified key as an integer.
   *
   * @param key
   *          the key of the value
   * @param defaultValue
   *          the value if specified key is not found
   * @return the value in this property list with the specified key value.
   */
  public int getIntValue(String key, int defaultValue) {
    int v;
    try {
      String value = getValue(key);
      v = Integer.parseInt(value);
    } catch (Exception e) {
      v = defaultValue;
    }
    return v;
  }

  /**
   * Returns the value for the specified key as a long.
   *
   * @param key
   *          the key of the value
   * @return the value in this property list with the specified key value.
   */
  public long getLongValue(String key) {
    return getLongValue(key, 0L);
  }

  /**
   * Returns the value for the specified key as a long.
   *
   * @param key
   *          the key of the value
   * @param defaultValue
   *          the value if specified key is not found
   * @return the value in this property list with the specified key value.
   */
  public long getLongValue(String key, long defaultValue) {
    long v;
    try {
      String value = getValue(key);
      v = Long.parseLong(value);
    } catch (Exception e) {
      v = defaultValue;
    }
    return v;
  }

  /**
   * Returns the value for the specified key as a float.
   *
   * @param key
   *          the key of the value
   * @return the value in this property list with the specified key value.
   */
  public float getFloatValue(String key) {
    return getFloatValue(key, 0f);
  }

  /**
   * Returns the value for the specified key as a float.
   *
   * @param key
   *          the key of the value
   * @param defaultValue
   *          the value if specified key is not found
   * @return the value in this property list with the specified key value.
   */
  public float getFloatValue(String key, float defaultValue) {
    float v;
    try {
      String value = getValue(key);
      v = Float.parseFloat(value);
    } catch (Exception e) {
      v = defaultValue;
    }
    return v;
  }

  /**
   * Returns the value for the specified key as a double.
   *
   * @param key
   *          the key of the value
   * @return the value in this property list with the specified key value.
   */
  public double getDoubleValue(String key) {
    return getDoubleValue(key, 0);
  }

  /**
   * Returns the value for the specified key as a double.
   *
   * @param key
   *          the key of the value
   * @param defaultValue
   *          the value if specified key is not found
   * @return the value in this property list with the specified key value.
   */
  public double getDoubleValue(String key, double defaultValue) {
    double v;
    try {
      String value = getValue(key);
      v = Double.parseDouble(value);
    } catch (Exception e) {
      v = defaultValue;
    }
    return v;
  }

  /**
   * Returns the value for the specified key as a boolean.
   *
   * @param key
   *          the key of the value
   * @return the value in this property list with the specified key value.
   */
  public boolean getBooleanValue(String key) {
    return getBooleanValue(key, "true");
  }

  /**
   * Returns the value for the specified key as a boolean.
   *
   * @param key
   *          the key of the value
   * @param valueAsTrue
   *          the value to be true
   * @return the value in this property list with the specified key value.
   */
  public boolean getBooleanValue(String key, String valueAsTrue) {
    String value = getValue(key);
    return valueAsTrue.equals(value);
  }

  /**
   * Returns the value for the specified key as a boolean.
   *
   * @param key
   *          the key of the value
   * @param valuesAsTrue
   *          the values to be true
   * @return the value in this property list with the specified key value.
   */
  public boolean getBooleanValue(String key, String[] valuesAsTrue) {
    String value = getValue(key);
    for (int i = 0; i < valuesAsTrue.length; i++) {
      if (valuesAsTrue[i].equals(value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the keys of the properties.
   *
   * @return keys
   */
  public String[] getKeys() {
    Enumeration<?> names = properties.propertyNames();
    ArrayList<String> keys = new ArrayList<>();
    while (names.hasMoreElements()) {
      String key = (String) names.nextElement();
      keys.add(key);
    }
    return keys.toArray(new String[keys.size()]);
  }

  /**
   * Returns the number of keys in this hashtable.
   *
   * @return the number of keys in this hashtable
   */
  public int size() {
    return properties.size();
  }

  /**
   * Returns all properties as a key = value pair.
   *
   * @return properties
   */
  public String dumpAllProperties() {
    StringBuilder sb = new StringBuilder();
    Enumeration<?> keys = properties.propertyNames();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      String value = getValue(key);
      sb.append(key + "=" + value + "\n");
    }
    return sb.toString();
  }

}
