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

import javax.xml.bind.DatatypeConverter;

/**
 * This class implements the hash related processing.
 */
public class HashUtil {

  public static String md5(byte[] input) {
    return getHash(input, "MD5");
  }

  public static String md5(String input) {
    return getHash(input, "MD5");
  }

  public static String sha1(byte[] input) {
    return getHash(input, "SHA-1");
  }

  public static String sha1(String input) {
    return getHash(input, "SHA-1");
  }

  public static String sha256(byte[] input) {
    return getHash(input, "SHA-256");
  }

  public static String sha256(String input) {
    return getHash(input, "SHA-256");
  }

  public static String sha512(byte[] input) {
    return getHash(input, "SHA-512");
  }

  public static String sha512(String input) {
    return getHash(input, "SHA-512");
  }

  public static String getHash(String input, String algorithm) {
    byte[] b = input.getBytes(StandardCharsets.UTF_8);
    String hash = getHash(b, algorithm);
    return hash;
  }

  /**
   * Returns hash value.
   *
   * @param input
   *          input byte array
   * @param algorithm
   *          hash algorithm (MD5 / SHA-1 / SHA-256 / SHA-512)
   * @return hash value
   */
  public static String getHash(byte[] input, String algorithm) {
    String hash = null;
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      byte[] b = md.digest(input);
      hash = DatatypeConverter.printHexBinary(b).toLowerCase();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    return hash;
  }

}
