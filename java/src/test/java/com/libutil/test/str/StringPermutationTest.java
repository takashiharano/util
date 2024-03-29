package com.libutil.test.str;

import java.util.List;

import com.libutil.StrUtil;
import com.libutil.StringPermutation;
import com.libutil.StringPermutation.StringPermutationResult;

public class StringPermutationTest {
  public static void main(String args[]) {
    String chars = "abcdefghijklmnopqrstuvwxyz";
    String target = "ffff";

    // test(chars, target);
    testF(chars, target);
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
    long lastIndex = StringPermutation.countTotal(chars, digit);
    System.out.println("lastIndex(total count) =  " + lastIndex);

    long targetIndex = StringPermutation.getIndex(chars, target);
    System.out.println("targetIndex =  " + targetIndex);

    long start = System.currentTimeMillis();
    long elapsed = 0;
    for (int i = 1; i <= lastIndex; i++) {
      String str = StringPermutation.getString(chars, i);
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
    String elapsedTime = StrUtil.intNumToDecimal(elapsed, 3);
    System.out.println(elapsedTime + "s");
  }

  // 10000: ntp 54ms
  // 20000: acof 80ms
  // 30000: ariv 101ms
  // 40000: bgdl 111ms
  // 50000: buyb 120ms
  // 60000: cjsr 131ms
  // 70000: cynh 139ms
  // 80000: dnhx 149ms
  // 90000: eccn 157ms
  // 100000: eqxd 164ms
  // 109674: ffff
  // 0.173s
  public static void testF(String chars, String target) {
    if (chars == null) {
      chars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    }
    int digit = target.length();
    long lastIndex = StringPermutation.countTotal(chars, digit);
    System.out.println("lastIndex(total count) =  " + lastIndex);

    long targetIndex = StringPermutation.getIndex(chars, target);
    System.out.println("targetIndex =  " + targetIndex);

    List<Integer> indexes = null;
    long start = System.currentTimeMillis();
    long elapsed = 0;
    for (int i = 1; i <= lastIndex; i++) {
      StringPermutationResult r = StringPermutation.getString(chars, i, indexes);

      String str = r.getString();
      if (str.equals(target)) {
        System.out.println(i + ": " + str);
        break;
      }

      if (i % 10000 == 0) {
        elapsed = System.currentTimeMillis() - start;
        System.out.println(i + ": " + str + " " + elapsed + "ms");
      }

      indexes = r.getIndexes();
    }
    elapsed = System.currentTimeMillis() - start;
    String elapsedTime = StrUtil.intNumToDecimal(elapsed, 3);
    System.out.println(elapsedTime + "s");
  }

}
