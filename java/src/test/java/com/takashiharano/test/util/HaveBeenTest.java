package com.takashiharano.test.util;

import com.takashiharano.util.Log;
import com.takashiharano.util.Util;

public class HaveBeenTest {

  public static void main(String args[]) {
    test("record", "deleted");
  }

  private static void test(String subject, String predicate) {
    Log.d("(1)=" + Util.haveBeen(subject, predicate, 1));
    Log.d("(0)=" + Util.haveBeen(subject, predicate, 0));
    Log.d("(2)=" + Util.haveBeen(subject, predicate, 2));
    Log.d("(-1)=" + Util.haveBeen(subject, predicate, -1));
  }

}
