package com.libutil.test.csv.builder;

import com.libutil.CsvBuilder;
import com.libutil.test.Log;

public class QuotationTest {

  public static void main(String args[]) {
    test("abc");
    test("abc\"1\"xyz");
    test("abc\"1");

    test2("abc");
    test2("abc\"1\"xyz");
    test2("abc\"1");
    test2("abc'1");
  }

  private static void test(String s) {
    String q = CsvBuilder.quote(s);
    Log.i(q);
  }

  private static void test2(String s) {
    String q = CsvBuilder.quote(s, "'", "!");
    Log.i(q);
  }

}
