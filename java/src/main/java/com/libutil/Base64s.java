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
package com.libutil;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64s (with "s" standing for "secure") is a derivative encoding scheme of
 * Base64.<br>
 * This class implements an encoder and a decoder of the Base64 encoding scheme
 * with XOR.
 */
public class Base64s {

  /**
   * The default charset to be used to encode/decode a string.
   */
  public static final String DEFAULT_CHARSET = "UTF-8";

  /**
   * Encodes the given byte array into a String using the Base64 encoding scheme.
   * Performs a bitwise XOR by source and key before encoding.<br>
   * Note that if the key is empty, it's normal Base64 encoding.
   *
   * @param src
   *          The byte array to be encoded
   * @param key
   *          The key to take XOR
   * @return A String containing the resulting Base64 encoded characters
   */
  public static String encode(byte[] src, String key) {
    byte[] k = null;
    try {
      k = key.getBytes(DEFAULT_CHARSET);
    } catch (UnsupportedEncodingException e) {
      // never reached
    }
    byte[] buf = _encode(src, k);
    String encoded = Base64.getEncoder().encodeToString(buf);
    return encoded;
  }

  /**
   * Encodes the specified string into a String using the Base64 encoding scheme.
   * Performs a bitwise XOR by source and key before encoding.<br>
   * Note that if the key is empty, it's normal Base64 encoding.
   *
   * @param src
   *          The string to be encode
   * @param key
   *          The key to take XOR
   * @return An encoded string
   */
  public static String encode(String src, String key) {
    return encode(src, key, DEFAULT_CHARSET);
  }

  /**
   * Encodes the specified string into a String using the Base64 encoding scheme.
   * Performs a bitwise XOR by source and key before encoding.<br>
   * Note that if the key is empty, it's normal Base64 encoding.
   *
   * @param src
   *          The string to be encoded
   * @param key
   *          The key to take XOR
   * @param charsetName
   *          The charset to be used to decode the bytes
   * @return An encoded string
   */
  public static String encode(String src, String key, String charsetName) {
    String endoded = null;
    try {
      byte[] srcBytes = src.getBytes(charsetName);
      endoded = encode(srcBytes, key);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return endoded;
  }

  /**
   * Decodes a Base64 encoded String into a newly-allocated byte array using the
   * Base64 encoding scheme. Performs a bitwise XOR by decoded byte array and the
   * key.<br>
   * Note that if the key is empty, it's normal Base64 decoding.
   *
   * @param src
   *          The string to be decoded
   * @param key
   *          The key to take XOR
   * @return A newly-allocated byte array containing the decoded bytes.
   * @throws RuntimeException
   *           If failed to decode
   */
  public static byte[] decode(String src, String key) throws RuntimeException {
    byte[] k = null;
    try {
      k = key.getBytes(DEFAULT_CHARSET);
    } catch (UnsupportedEncodingException e) {
      // never reached
    }
    byte[] buf = Base64.getDecoder().decode(src);
    return _decode(buf, k);
  }

  /**
   * Decodes a Base64 encoded String into an original string using the Base64
   * encoding scheme. Performs a bitwise XOR by decoded byte array and the
   * key.<br>
   * Note that if the key is empty, it's normal Base64 decoding.
   *
   * @param src
   *          The string to be decoded
   * @param key
   *          The key to take XOR
   * @return A decoded string
   */
  public static String decodeString(String src, String key) {
    return decodeString(src, key, DEFAULT_CHARSET);
  }

  /**
   * Decodes a Base64 encoded String into an original string using the Base64
   * encoding scheme. Performs a bitwise XOR by decoded byte array and the
   * key.<br>
   * Note that if the key is empty, it's normal Base64 decoding.
   *
   * @param src
   *          The string to be decoded
   * @param key
   *          The key to take XOR
   * @param charsetName
   *          The charset to be used to decode
   * @return A decoded string
   */
  public static String decodeString(String src, String key, String charsetName) {
    String str = null;
    try {
      byte[] decoded = decode(src, key);
      str = new String(decoded, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return str;
  }

  private static byte[] _encode(byte[] src, byte[] key) {
    if ((src.length == 0) || (key.length == 0)) {
      return src;
    }

    int d = key.length - src.length;
    if (d < 0) {
      d = 0;
    }

    byte[] buf = new byte[src.length + d + 1];

    int i;
    for (i = 0; i < src.length; i++) {
      buf[i] = (byte) (src[i] ^ key[i % key.length]);
    }

    int j = i;
    for (i = 0; i < d; i++) {
      buf[j] = (byte) (255 ^ key[j % key.length]);
      j++;
    }

    buf[j] = (byte) d;
    return buf;
  }

  private static byte[] _decode(byte[] src, byte[] key) {
    if ((src.length == 0) || (key.length == 0)) {
      return src;
    }

    int d = src[src.length - 1] & 255;
    int len = src.length - d - 1;
    if (len < 0) {
      len = 0;
    }
    byte[] buf = new byte[len];

    for (int i = 0; i < len; i++) {
      buf[i] = (byte) (src[i] ^ key[i % key.length]);
    }

    return buf;
  }

}
