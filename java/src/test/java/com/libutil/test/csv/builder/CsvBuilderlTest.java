package com.libutil.test.csv.builder;

import com.libutil.CsvBuilder;
import com.libutil.test.Log;

public class CsvBuilderlTest {

  public static void main(String args[]) {
    buildTest();
  }

  private static void buildTest() {
    CsvBuilder builder;
    builder = new CsvBuilder();
    builder.append("abc");
    builder.append("xyz");
    Log.i(builder);

    builder = new CsvBuilder("\t");
    builder.append("abc");
    builder.append("xyz");
    Log.i(builder);

    builder = new CsvBuilder(true);
    builder.append("abc");
    builder.append("xyz");
    Log.i(builder);

    builder = new CsvBuilder(true);
    builder.setQuotationChar("'");
    builder.append("abc");
    builder.append("xyz");
    Log.i(builder);

    builder = new CsvBuilder(true);
    builder.setQuotationChar("'");
    builder.setEscapeChar("\\");
    builder.append("a'b'c");
    builder.append("x\"y\"z");
    builder.append("1,2");
    Log.i(builder);

    builder = new CsvBuilder(false);
    builder.setQuotationChar("'");
    builder.setEscapeChar("\\");
    builder.append("a'b'c");
    builder.append("x\"y\"z");
    builder.append("1,2");
    Log.i(builder);

    builder = new CsvBuilder();
    builder.append("abc");
    builder.append(1);
    builder.append(2L);
    builder.append(1.5f);
    builder.append(9.8);
    builder.append(true);
    builder.append((String) null);
    Log.i(builder);

  }

}
