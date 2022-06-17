package com.libutil.test.csv.builder;

import com.libutil.CsvBuilder;
import com.libutil.test.Log;

public class QuotationAppendTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    CsvBuilder builder;

    builder = new CsvBuilder();
    builder.append("abc\"123\"xyz");
    Log.i(builder);

    builder = new CsvBuilder(true);
    builder.append("abc\"123\"xyz");
    Log.i(builder);
  }

}
