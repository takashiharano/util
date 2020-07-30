package com.takashiharano.util;

import java.util.ArrayList;

public class StringPermutation {
  /**
   * Count the number of total patterns of the table
   *
   * @param chars
   * @param digit
   * @return
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
   * Given pattern's index
   *
   * @param chars
   * @param pattern
   * @return
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
   * Returns a String pattern at the specified position
   *
   * @param chars
   * @param idx
   * @return
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
