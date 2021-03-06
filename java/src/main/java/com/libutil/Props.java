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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Props class represents a persistent set of properties.
 */
public class Props {

  public static final String DEFAULT_CHARSET = "UTF-8";
  public static String LINE_SEPARATOR = "\n";

  protected String filePath;
  protected LinkedHashMap<String, String> properties;

  /**
   * Creates an empty property list with no values.
   */
  public Props() {
    properties = new LinkedHashMap<>();
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
    properties = new LinkedHashMap<>();
    load(filePath);
  }

  private void load(String filePath) {
    String[] content = readTextFileAsArray(filePath);
    if (content == null) {
      String message = "Failed to load properties file: " + filePath;
      throw new RuntimeException(message);
    }
    parse(content);
  }

  public static String[] readTextFileAsArray(String path) {
    String charsetName = DEFAULT_CHARSET;
    Path filePath = Paths.get(path);
    List<String> lines;
    try {
      lines = Files.readAllLines(filePath, Charset.forName(charsetName));
    } catch (IOException e) {
      return null;
    }
    String[] text = new String[lines.size()];
    lines.toArray(text);
    return text;
  }

  private void parse(String[] content) {
    String key = null;
    StringBuilder valBuf = null;
    boolean cnt = false;
    for (int i = 0; i < content.length; i++) {
      String value;
      String line = content[i];

      if (cnt) {
        value = extractValue(line);
        if (isContinuationLine(value)) {
          value = extractContinuationValue(value);
          valBuf.append(value);
        } else {
          cnt = false;
          valBuf.append(value);
          value = decodeEscaped5c(valBuf.toString());
          properties.put(key, value);
        }
        valBuf.append(value);
        continue;
      }

      String[] pair = line.split("=", 2);
      String pt1 = pair[0].trim();
      if ("".equals(pt1) || pt1.startsWith("#")) {
        continue;
      }

      if (pair.length < 2) {
        key = pt1;
        properties.put(key, "");
        continue;
      }

      key = pt1;
      value = extractValue(pair[1]);
      if (isContinuationLine(value)) {
        value = extractContinuationValue(value);
        valBuf = new StringBuilder();
        valBuf.append(value);
        cnt = true;
      } else {
        value = decodeEscaped5c(value);
        properties.put(key, value);
      }
    }
  }

  private String extractValue(String s) {
    s = trimLeadingSpace(s);
    s = decodeEscaped(s);
    return s;
  }

  private String extractContinuationValue(String s) {
    return s.replaceAll("\\\\\\s*$", "");
  }

  private String trimLeadingSpace(String s) {
    return s.replaceAll("^\\s+", "");
  }

  private boolean isContinuationLine(String s) {
    s = s.trim();
    Pattern p = Pattern.compile("[^\\\\]\\\\$");
    Matcher m = p.matcher(s);
    return m.find();
  }

  private String decodeEscaped(String s) {
    s = s.replaceAll("[^\\\\]\\\\r", "\r");
    s = s.replaceAll("[^\\\\]\\\\n", "\n");
    s = s.replaceAll("[^\\\\]\\\\t", "\t");
    return s;
  }

  private String decodeEscaped5c(String s) {
    s = s.replaceAll("\\\\(.)", "$1");
    return s;
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
    String value = properties.get(key);
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
   * Returns all properties as a key = value pair.
   *
   * @return properties
   */
  public String getAllProperties() {
    StringBuilder sb = new StringBuilder();
    for (Entry<String, String> entry : properties.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      sb.append(key);
      sb.append("=");
      sb.append(value);
      sb.append(LINE_SEPARATOR);
    }
    return sb.toString();
  }

  /**
   * Returns the keys of the properties.
   *
   * @return keys
   */
  public String[] getKeys() {
    ArrayList<String> keys = new ArrayList<>();
    for (Entry<String, String> entry : properties.entrySet()) {
      String key = entry.getKey();
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

}
