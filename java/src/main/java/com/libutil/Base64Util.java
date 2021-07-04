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

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * This class implements the Base64 encode and decode.
 */
public class Base64Util {

  public static final String DEFAULT_CHARSET = "UTF-8";

  /**
   * Decodes a Base64 encoded String into a string using the Base64 encoding
   * scheme.
   *
   * @param src
   *          the string to decode
   * @return the decoded string.
   */
  public static String decode(String src) {
    return decode(src, DEFAULT_CHARSET);
  }

  /**
   * Decodes a Base64 encoded String into a string using the Base64 encoding
   * scheme.
   *
   * @param src
   *          the string to decode
   * @param charsetName
   *          charset name
   * @return the decoded string.
   */
  public static String decode(String src, String charsetName) {
    if (src == null) {
      return null;
    }
    byte[] decoded = Base64.getDecoder().decode(src);
    String str = null;
    try {
      str = new String(decoded, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return str;
  }

  /**
   * Decodes a Base64 encoded String into a newly-allocated byte array using the
   * Base64 encoding scheme.
   *
   * @param src
   *          the string to decode
   * @return A newly-allocated byte array containing the decoded bytes.
   */
  public static byte[] decodeB(String src) {
    if (src == null) {
      return new byte[0];
    }
    byte[] decoded = Base64.getDecoder().decode(src);
    return decoded;
  }

  /**
   * Encodes the specified byte array into a String using the Base64 encoding
   * scheme.
   *
   * @param src
   *          the string to encode
   * @return A String containing the resulting Base64 encoded characters
   */
  public static String encode(String src) {
    return encode(src, DEFAULT_CHARSET);
  }

  /**
   * Encodes the specified byte array into a String using the Base64 encoding
   * scheme.
   *
   * @param src
   *          the string to encode
   * @param charsetName
   *          charset name
   * @return A String containing the resulting Base64 encoded characters
   */
  public static String encode(String src, String charsetName) {
    if (src == null) {
      return null;
    }
    String encoded = null;
    byte[] srcBytes;
    try {
      srcBytes = src.getBytes(charsetName);
      encoded = Base64.getEncoder().encodeToString(srcBytes);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return encoded;
  }

  /**
   * Encodes the specified byte array into a String using the Base64 encoding
   * scheme.
   *
   * @param src
   *          the byte array to encode
   * @return A String containing the resulting Base64 encoded characters
   */
  public static String encode(byte[] src) {
    if (src == null) {
      return null;
    }
    String encoded = Base64.getEncoder().encodeToString(src);
    return encoded;
  }

}
