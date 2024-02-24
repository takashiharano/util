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
import java.util.List;

/**
 * Generates all permutations of a given string.
 */
public class StringPermutation {

  /**
   * Count the number of total permutation patterns of the table.
   *
   * @param chars
   *          characters to use
   * @param length
   *          the length
   * @return the number of total pattern
   */
  public static long countTotal(String chars, int length) {
    String[] tbl = chars.split("");
    int c = tbl.length;
    int n = 0;
    for (int i = 1; i <= length; i++) {
      n += Math.pow(c, i);
    }
    return n;
  }

  /**
   * Returns the characters permutation index of the given pattern.
   *
   * @param chars
   *          characters to use
   * @param pattern
   *          a string
   * @return the index
   */
  public static long getIndex(String chars, String pattern) {
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
   * Returns the string that appear in the specified order within the permutation
   * of the characters.
   *
   * @param chars
   *          characters to use
   * @param index
   *          the index
   * @return the string
   */
  public static String getString(String chars, long index) {
    StringPermutationResult r = getString(chars, index, null);
    return r.getString();
  }

  /**
   * Returns the string that appear in the specified order within the permutation
   * of the characters.
   *
   * @param chars
   *          characters to use
   * @param index
   *          the index
   * @param indexes
   *          the list of index that indicates the previous state to speed up
   *          calculation.
   * @return the string
   */
  public static StringPermutationResult getString(String chars, long index, List<Integer> indexes) {
    if (index <= 0) {
      StringPermutationResult r = new StringPermutationResult("", null);
      return r;
    }

    String[] tbl = chars.split("");
    int len = tbl.length;
    long start;
    if (indexes == null) {
      indexes = new ArrayList<>();
      indexes.add(-1);
      start = 0;
    } else {
      start = index - 1;
    }

    for (long i = start; i < index; i++) {
      int j = 0;
      boolean cb = true;
      while (j < indexes.size()) {
        if (cb) {
          indexes.set(j, indexes.get(j) + 1);
          if (indexes.get(j) > len - 1) {
            indexes.set(j, 0);
            if (indexes.size() <= j + 1) {
              indexes.add(-1);
            }
          } else {
            cb = false;
          }
        }
        j++;
      }
    }

    int strLen = indexes.size();
    StringBuilder sb = new StringBuilder(strLen);
    for (int i = strLen - 1; i >= 0; i--) {
      int idx = indexes.get(i);
      String ch = tbl[idx];
      sb.append(ch);
    }

    StringPermutationResult r = new StringPermutationResult(sb.toString(), indexes);
    return r;
  }

  public static class StringPermutationResult {
    private String str;
    private List<Integer> indexes;

    public StringPermutationResult(String str, List<Integer> indexes) {
      this.str = str;
      this.indexes = indexes;
    }

    public String getString() {
      return str;
    }

    public List<Integer> getIndexes() {
      return indexes;
    }
  }

}
