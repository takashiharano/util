package com.libutil.test.str;

import com.libutil.StrUtil;

public class StringPermutationTest {
  public static void main(String args[]) {
    test("abcdefghijklmnopqrstuvwxyz", "ffff");
  }

  // Core i7-7700 3.6GHz
  // lastIndex(total count) = 475254
  // targetIndex = 109674
  // 10000: ntp 391ms
  // 20000: acof 1343ms
  // 30000: ariv 2886ms
  // 40000: bgdl 5107ms
  // 50000: buyb 8125ms
  // 60000: cjsr 11860ms
  // 70000: cynh 16203ms
  // 80000: dnhx 21321ms
  // 90000: eccn 27126ms
  // 100000: eqxd 33531ms
  // 109674: ffff
  // 40.524s
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
      if (i % 10000 == 0) {
        elapsed = System.currentTimeMillis() - start;
        System.out.println(i + ": " + str + " " + elapsed + "ms");
      }
    }
    elapsed = System.currentTimeMillis() - start;
    String elapsedTime = StrUtil.intnum2decimal(elapsed, 3);
    System.out.println(elapsedTime + "s");
  }

}
