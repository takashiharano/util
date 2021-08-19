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

import java.security.SecureRandom;

/**
 * Generates random values.
 */
public class RandomUtil {

  private static SecureRandom r = new SecureRandom();

  /**
   * Returns a random integer value.
   *
   * @return random integer value
   */
  public static int getInt() {
    return r.nextInt();
  }

  /**
   * Returns a random integer value.
   *
   * @param max
   *          the max value
   * @return 0-max
   */
  public static int getInt(int max) {
    return r.nextInt(max + 1);
  }

  /**
   * Returns a random integer value.
   *
   * @param min
   *          the minimum value
   * @param max
   *          the max value
   * @return min-max
   */
  public static int getInt(int min, int max) {
    int i;
    max++;
    do {
      i = r.nextInt(max);
    } while (i < min);
    return i;
  }

  /**
   * Returns a random double value.<br>
   * e.g.,) 0.8697886198087033
   *
   * @return 0.0 - 1.0
   */
  public static double getDouble() {
    return r.nextDouble();
  }

  /**
   * Returns a random boolean value
   *
   * @return true or false
   */
  public static boolean getBoolean() {
    return r.nextBoolean();
  }

  /**
   * Returns a random string.<br>
   * <br>
   * getString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
   * 8);<br>
   * out: "TWEQWhq2"
   * 
   * @param table
   *          characters to use
   * @param len
   *          length of the output
   * @return a randomly-generated string
   */
  public static String getString(String table, int len) {
    char[] cTable = table.toCharArray();
    int lastIdx = cTable.length - 1;
    char[] buf = new char[len];
    for (int i = 0; i < len; i++) {
      int idx = r.nextInt(lastIdx + 1);
      buf[i] = cTable[idx];
    }
    String ret = new String(buf);
    return ret;
  }

}
