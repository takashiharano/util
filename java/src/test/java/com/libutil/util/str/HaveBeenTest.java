package com.libutil.util.str;

import com.libutil.Log;
import com.libutil.StrUtil;

public class HaveBeenTest {

  public static void main(String args[]) {
    test("record", "deleted");
  }

  private static void test(String subject, String predicate) {
    Log.i("(1)=" + StrUtil.haveBeen(subject, predicate, 1));
    Log.i("(0)=" + StrUtil.haveBeen(subject, predicate, 0));
    Log.i("(2)=" + StrUtil.haveBeen(subject, predicate, 2));
    Log.i("(-1)=" + StrUtil.haveBeen(subject, predicate, -1));
  }

}
