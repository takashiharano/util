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

import java.util.ArrayList;

public class StringPermutation {
  /**
   * Count the number of total patterns of the table.
   *
   * @param chars
   *          characters to use
   * @param digit
   *          the length
   * @return the number of total pattern
   */
  public static long count(String chars, int digit) {
    String[] tbl = chars.split("");
    int c = tbl.length;
    int n = 0;
    for (int i = 1; i <= digit; i++) {
      n += Math.pow(c, i);
    }
    return n;
  }

  /**
   * Returns the index of the given pattern.
   *
   * @param chars
   *          characters to use
   * @param pattern
   *          a string
   * @return the index
   */
  public static long index(String chars, String pattern) {
    int len = pattern.length();
    int rdx = chars.length();
    long idx = 0;
    for (int i = 0; i < len; i++) {
      int d = len - i - 1;
      String c = pattern.substring(d, d + 1);
      int v = chars.indexOf(c) + 1;
      long n = v * (long) Math.pow(rdx, i);
      idx += n;
    }
    return idx;
  }

  /**
   * Returns a String pattern at the specified position.
   *
   * @param chars
   *          characters to use
   * @param idx
   *          the index
   * @return the string
   */
  public static String getString(String chars, long idx) {
    String[] tbl = chars.split("");
    int len = tbl.length;
    ArrayList<Integer> a = new ArrayList<>();
    a.add(-1);
    for (int i = 0; i < idx; i++) {
      int j = 0;
      boolean cb = true;
      while (j < a.size()) {
        if (cb) {
          a.set(j, a.get(j) + 1);
          if (a.get(j) > len - 1) {
            a.set(j, 0);
            if (a.size() <= j + 1) {
              a.add(-1);
            }
          } else {
            cb = false;
          }
        }
        j++;
      }
    }
    int strLen = a.size();
    StringBuilder sb = new StringBuilder(strLen);
    for (int i = strLen - 1; i >= 0; i--) {
      sb.append(tbl[a.get(i)]);
    }
    return sb.toString();
  }

}
