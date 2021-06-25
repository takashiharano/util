package com.libutil.util;

import com.libutil.StrUtil;
import com.libutil.StringPermutation;

public class StringPermutationTest {
  public static void main(String args[]) {
    test("abcdefghijklmnopqrstuvwxyz", "ffff");
  }

  public static void test(String chars, String target) {
    if (chars == null) {
      chars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    }
    int digit = target.length();
    long lastIndex = StringPermutation.count(chars, digit);
    System.out.println("lastIndex: " + lastIndex);

    long targetIndex = StringPermutation.index(chars, target);
    System.out.println("targetIndex: " + targetIndex);

    long start = System.currentTimeMillis();
    long elapsed = 0;
    for (int i = 1; i <= lastIndex; i++) {
      String str = StringPermutation.getString(chars, i);
      if (str.equals(target)) {
        System.out.println(i + ": " + str);
        break;
      }
      if (i % 1000 == 0) {
        elapsed = System.currentTimeMillis() - start;
        System.out.println(i + ": " + str + " " + elapsed + "ms");
      }
    }
    elapsed = System.currentTimeMillis() - start;
    String elapsedTime = StrUtil.intnum2decimal(elapsed, 3);
    System.out.println(elapsedTime + "s");
  }

}
