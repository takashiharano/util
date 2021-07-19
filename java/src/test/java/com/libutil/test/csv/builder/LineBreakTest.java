package com.libutil.test.csv.builder;

import com.libutil.CsvBuilder;
import com.libutil.test.Log;

public class LineBreakTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    CsvBuilder builder;

    builder = new CsvBuilder();
    builder.append("abc\r\nxyz");
    Log.i(builder);

    builder = new CsvBuilder();
    builder.append("abc\nxyz");
    Log.i(builder);

    builder = new CsvBuilder(true);
    builder.append("abc\nxyz");
    Log.i(builder);
  }

}
