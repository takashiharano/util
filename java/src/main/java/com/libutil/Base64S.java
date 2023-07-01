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
 * This class implements an encoder and a decoder of the Base64 encoding scheme
 * with XOR.
 */
public class Base64S {

  /**
   * The default charset to be used to encode/decode a string.
   */
  public static final String DEFAULT_CHARSET = "UTF-8";

  /**
   * Encodes the given byte array into a String using the Base64 encoding scheme
   * with XOR. Perform a bitwise XOR prior to encode.<br>
   * Note that if the key is 0, it's normal Base64 encoding.
   *
   * @param src
   *          The byte array to be encoded
   * @param key
   *          The key to take XOR (0-255)
   * @return A String containing the resulting Base64 encoded characters
   */
  public static String encode(byte[] src, int key) {
    byte[] buf = BinUtil.xor(src, key);
    String encoded = Base64.getEncoder().encodeToString(buf);
    return encoded;
  }

  /**
   * Encodes the specified string into a String using the Base64 encoding scheme
   * with XOR. Perform a bitwise XOR prior to encode.<br>
   * Note that if the key is 0, it's normal Base64 encoding.
   *
   * @param src
   *          The string to be encode
   * @param key
   *          The key to take XOR (0-255)
   * @return An encoded string
   */
  public static String encode(String src, int key) {
    return encode(src, key, DEFAULT_CHARSET);
  }

  /**
   * Encodes the specified string into a String using the Base64 encoding scheme
   * with XOR. Perform a bitwise XOR prior to encode.<br>
   * Note that if the key is 0, it's normal Base64 encoding.
   *
   * @param src
   *          The string to be encoded
   * @param key
   *          The key to take XOR (0-255)
   * @param charsetName
   *          The charset to be used to decode the bytes
   * @return An encoded string
   */
  public static String encode(String src, int key, String charsetName) {
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
   * Base64 encoding scheme with XOR. Perform a bitwise XOR to decoded byte
   * array.<br>
   * Note that if the key is 0, it's normal Base64 decoding.
   *
   * @param src
   *          The string to be decoded
   * @param key
   *          The key to take XOR (0-255)
   * @return A newly-allocated byte array containing the decoded bytes.
   * @throws RuntimeException
   *           If failed to decode
   */
  public static byte[] decode(String src, int key) throws RuntimeException {
    byte[] buf = Base64.getDecoder().decode(src);
    return BinUtil.xor(buf, key);
  }

  /**
   * Decodes a Base64 encoded String into an original string using the Base64
   * encoding scheme with XOR. Perform a bitwise XOR to decoded byte array.<br>
   * Note that if the key is 0, it's normal Base64 decoding.
   *
   * @param src
   *          The string to be decoded
   * @param key
   *          The key to take XOR (0-255)
   * @return A decoded string
   */
  public static String decodeString(String src, int key) {
    return decodeString(src, key, DEFAULT_CHARSET);
  }

  /**
   * Decodes a Base64 encoded String into an original string using the Base64
   * encoding scheme with XOR. Perform a bitwise XOR to decoded byte array.<br>
   * Note that if the key is 0, it's normal Base64 decoding.
   *
   * @param src
   *          The string to be decoded
   * @param key
   *          The key to take XOR (0-255)
   * @param charsetName
   *          The charset to be used to decode
   * @return A decoded string
   */
  public static String decodeString(String src, int key, String charsetName) {
    String str = null;
    try {
      byte[] decoded = decode(src, key);
      str = new String(decoded, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return str;
  }

}
