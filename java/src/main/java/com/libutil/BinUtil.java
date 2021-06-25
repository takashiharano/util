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
