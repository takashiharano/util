package com.libutil.test.csv.builder;

import com.libutil.CsvBuilder;
import com.libutil.test.Log;

public class AppendChunkTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    CsvBuilder builder;
    String chunk;

    chunk = "aa,bb,cc";
    builder = new CsvBuilder(",", false);
    builder.append("123");
    builder.appendChunk(chunk);
    Log.i(builder);

    chunk = "\"aa\",\"bb\",\"cc\"";
    builder = new CsvBuilder(",", true);
    builder.append("123");
    builder.appendChunk(chunk);
    Log.i(builder);

    chunk = "aa,bb,cc";
    builder = new CsvBuilder(",", true);
    builder.append("123");
    builder.appendChunk(chunk);
    Log.i(builder);

    chunk = "\"aa\",\"bb\",\"cc\"";
    builder = new CsvBuilder(",", false);
    builder.append("123");
    builder.appendChunk(chunk);
    Log.i(builder);

    Log.i("----------");

    chunk = "aa,bb,cc";
    builder = new CsvBuilder(",", false);
    builder.appendChunk(chunk);
    builder.append(123);
    Log.i(builder);

    chunk = "\"aa\",\"bb\",\"cc\"";
    builder = new CsvBuilder(",", true);
    builder.appendChunk(chunk);
    builder.append("123");
    Log.i(builder);

    chunk = "aa,bb,cc";
    builder = new CsvBuilder(",", true);
    builder.appendChunk(chunk);
    builder.append("123");
    Log.i(builder);

    chunk = "\"aa\",\"bb\",\"cc\"";
    builder = new CsvBuilder(",", false);
    builder.appendChunk(chunk);
    builder.append("123");
    Log.i(builder);

  }

}
