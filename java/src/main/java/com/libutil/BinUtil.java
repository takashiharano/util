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

import java.io.File;
import java.io.IOException;

public class BinUtil {

  /**
   * byte[] -> "01 02 03 ..."
   *
   * @param src
   *          the byte array to dump
   * @return hex string
   */
  public static String bytes2hex(byte[] src) {
    return HexDumper.toHex(src);
  }

  /**
   * byte[] -> "01 02 03 ..."
   *
   * @param src
   *          the byte array to dump
   * @param limit
   *          limit length
   * @return hex string
   */
  public static String bytes2hex(byte[] src, int limit) {
    return HexDumper.toHex(src, limit);
  }

  /**
   * Copy byte array.
   *
   * @param src
   * @param dest
   * @param offset
   * @param size
   */
  public static void copyByteArray(byte[] src, byte[] dest, int offset, int size) {
    for (int i = 0; i < size; i++) {
      dest[offset + i] = src[i];
    }
  }

  /**
   * HEX dump.
   *
   * @param src
   *          the byte array to dump
   * @return hex string
   */
  public static String dumpHex(byte[] src) {
    return HexDumper.dump(src);
  }

  /**
   * HEX dump.
   *
   * @param src
   *          the byte array to dump
   * @param limit
   *          limit length
   * @return hex string
   */
  public static String dumpHex(byte[] src, int limit) {
    return HexDumper.dump(src, limit);
  }

  /**
   * [s]234567890123...[e]
   *
   * @param size
   *          size to generate in bytes
   * @param s
   *          value of the first byte. -1 -> '1'
   * @param e
   *          value of the last byte. -1 -> [0-9] of the end position
   * @return byte array
   */
  public static byte[] getTestBytes(int size, int s, int e) {
    byte[] bytes = new byte[size];
    byte b;
    for (int i = 1; i <= size; i++) {
      if (i == 1) {
        if (s == -1) {
          b = 0x31;
        } else {
          b = (byte) s;
        }
      } else if (i == size) {
        if (s == -1) {
          b = (byte) (i % 10 + 0x30);
        } else {
          b = (byte) e;
        }
      } else {
        b = (byte) (i % 10 + 0x30);
      }
      bytes[i - 1] = b;
    }
    return bytes;
  }

  /**
   * "01 02 03 ..." -> byte[]
   *
   * @param src
   *          hex string
   * @return the byte array
   */
  public static byte[] hex2bytes(String src) {
    src = src.trim().replaceAll("\r\n|\r|\n", "").replaceAll("\\s{2,}", " ");
    String[] arr = src.split(" ");
    byte[] bytes = new byte[arr.length];
    for (int i = 0; i < arr.length; i++) {
      int v = Integer.parseInt(arr[i], 16);
      bytes[i] = (byte) v;
    }
    return bytes;
  }

  /**
   * Write a binary content into a file from the hex string.
   *
   * @param path
   * @param hex
   * @throws IOException
   */
  public static void writeFileFromHex(String path, String hex) throws IOException {
    File file = new File(path);
    writeFileFromHex(file, hex);
  }

  /**
   * Write a binary content into a file from the hex string.
   *
   * @param file
   * @param hex
   * @throws IOException
   */
  public static void writeFileFromHex(File file, String hex) throws IOException {
    byte[] b = hex2bytes(hex);
    FileUtil.write(file, b);
  }

}
