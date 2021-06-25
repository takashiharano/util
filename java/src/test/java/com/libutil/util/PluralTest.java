package com.libutil.util;

import com.libutil.Log;
import com.libutil.Util;

public class PluralTest {

  public static void main(String args[]) {
    test("apple");
    test("APPLE");
    test("bus");
    test("dish");
    test("potato");
    test("piano");
    test("piano", true);
    test("city");
    test("CITY");
    test("knife");
    test("KNIFE");
  }

  private static void test(String word) {
    Log.d(word + "(1)=" + Util.plural(word, 1));
    Log.d(word + "(0)=" + Util.plural(word, 0));
    Log.d(word + "(2)=" + Util.plural(word, 2));
    Log.d(word + "(-1)=" + Util.plural(word, -1));
  }

  private static void test(String word, boolean flag) {
    Log.d(word + "(1)=" + Util.plural(word, 1, flag));
    Log.d(word + "(0)=" + Util.plural(word, 0, flag));
    Log.d(word + "(2)=" + Util.plural(word, 2, flag));
    Log.d(word + "(-1)=" + Util.plural(word, -1, flag));
  }

}
