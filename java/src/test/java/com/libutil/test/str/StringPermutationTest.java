package com.libutil.test.str;

import com.libutil.StrUtil;

public class StringPermutationTest {
  public static void main(String args[]) {
    test("abcdefghijklmnopqrstuvwxyz", "ffff");
  }

  public static void test(String chars, String target) {
    if (chars == null) {
      chars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    }
    int digit = target.length();
    long lastIndex = StrUtil.Permutation.countTotal(chars, digit);
    System.out.println("lastIndex(total count) =  " + lastIndex);

    long targetIndex = StrUtil.Permutation.getIndex(chars, target);
    System.out.println("targetIndex =  " + targetIndex);

    long start = System.currentTimeMillis();
    long elapsed = 0;
    for (int i = 1; i <= lastIndex; i++) {
      String str = StrUtil.Permutation.getString(chars, i);
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
