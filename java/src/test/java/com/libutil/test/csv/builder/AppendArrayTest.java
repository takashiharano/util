package com.libutil.test.csv.builder;

import com.libutil.CsvBuilder;
import com.libutil.test.Log;

public class AppendArrayTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    CsvBuilder builder;

    String chunk = "aa,bb,12";
    builder = new CsvBuilder(",", true);
    builder.append("abc");
    builder.append(123);
    builder.appendChunk(chunk);
    Log.i(builder);

    builder = new CsvBuilder(",", true);
    builder.appendChunk(chunk);
    builder.append("abc");
    builder.append(123);
    Log.i(builder);

    builder = new CsvBuilder(",", true);
    builder.append("abc");
    builder.appendChunk(chunk);
    builder.append(123);
    Log.i(builder);
  }

}
