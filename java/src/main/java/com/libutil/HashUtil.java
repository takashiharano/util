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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class implements the hash related processing.
 */
public class HashUtil {

  /**
   * Returns hash string.
   *
   * @param input
   *          the string for hash
   * @param algorithm
   *          the name of the algorithm.<br>
   *          (MD5 / SHA-1 / SHA-256 / SHA-512)
   * @return the hash string
   */
  public static String getHashString(String input, String algorithm) {
    byte[] b = input.getBytes(StandardCharsets.UTF_8);
    String hash = getHashString(b, algorithm);
    return hash;
  }

  /**
   * Returns hash string.
   *
   * @param input
   *          input byte array
   * @param algorithm
   *          the name of the algorithm.<br>
   *          (MD5 / SHA-1 / SHA-256 / SHA-512)
   * @return the hash string
   */
  public static String getHashString(byte[] input, String algorithm) {
    byte[] b = getHash(input, algorithm);
    return toHexString(b);
  }

  /**
   * Returns the hash value.
   *
   * @param input
   *          input byte array
   * @param algorithm
   *          the name of the algorithm.<br>
   *          (MD5 / SHA-1 / SHA-256 / SHA-512)
   * @return the array of bytes for the resulting hash value
   */
  public static byte[] getHash(byte[] input, String algorithm) {
    byte[] hash = null;
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      hash = md.digest(input);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    return hash;
  }

  /**
   * Converts a byte array to lowercase hex string.
   *
   * @param bytes
   *          byte array
   * @return hex string (lowercase)
   */
  private static String toHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(String.format("%02x", b & 0xff));
    }
    return sb.toString();
  }

}
