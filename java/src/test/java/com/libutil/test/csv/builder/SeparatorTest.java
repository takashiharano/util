package com.libutil.test.csv.builder;

import com.libutil.CsvBuilder;
import com.libutil.test.Log;

public class SeparatorTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    CsvBuilder builder;

    builder = new CsvBuilder(",");
    builder.append("abc");
    builder.append("xyz");
    Log.i(builder);

    builder = new CsvBuilder("\t");
    builder.append("abc");
    builder.append("xyz");
    Log.i(builder);

    builder = new CsvBuilder(",");
    builder.append("a,bc");
    builder.append("xyz");
    Log.i(builder);

    builder = new CsvBuilder("\t");
    builder.append("a\tbc");
    builder.append("xyz");
    Log.i(builder);

    builder = new CsvBuilder("\t");
    builder.append("a,bc");
    builder.append("xyz");
    Log.i(builder);
  }

}
