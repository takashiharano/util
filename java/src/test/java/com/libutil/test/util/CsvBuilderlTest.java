package com.libutil.test.util;

import com.libutil.CsvBuilder;
import com.libutil.test.Log;

public class CsvBuilderlTest {

  public static void main(String args[]) {
    buildTest();
  }

  private static void buildTest() {
    CsvBuilder builder = new CsvBuilder();
    builder.append("abc");
    builder.append(123);
    builder.append(123.4);
    builder.append(true);
    builder.append(null);
    Log.d(builder);

    builder.nextRecord();
    builder.append("zyx");
    builder.append(222);
    builder.append(987.6);
    builder.append(false);
    builder.append(null);
    Log.d(builder);

    builder.nextRecord();
    builder.append("a\"aa\r\nbbb");
    builder.append(222);
    builder.append(987.6);
    builder.append(false);
    builder.append(null);
    Log.d(builder);

    Log.d("---");

    builder = new CsvBuilder("\t");
    builder.append("abc");
    builder.append(123);
    builder.append(123.4);
    builder.append(true);
    builder.append(null);
    Log.d(builder);

    builder = new CsvBuilder(true);
    builder.append("abc");
    builder.append(123);
    builder.append(123.4);
    builder.append(true);
    builder.append(null);
    Log.d(builder);

    builder = new CsvBuilder("\t", true);
    builder.append("abc");
    builder.append(123);
    builder.append(123.4);
    builder.append(true);
    builder.append(null);
    Log.d(builder);

    String chunk = "aa,bb,12";
    builder = new CsvBuilder(",", true);
    builder.append("abc");
    builder.append(123);
    builder.appendChunk(chunk);
    Log.d(builder);

    builder = new CsvBuilder(",", true);
    builder.appendChunk(chunk);
    builder.append("abc");
    builder.append(123);
    Log.d(builder);

    builder = new CsvBuilder(",", true);
    builder.append("abc");
    builder.appendChunk(chunk);
    builder.append(123);
    Log.d(builder);
  }

}
