package com.libutil.test.csv.parser;

import com.libutil.CsvParser;
import com.libutil.test.Log;

public class ParseLineWithQuoteTest {

  public static void main(String args[]) {
    ParseLineWithQuoteTest tester = new ParseLineWithQuoteTest();
    tester.test1();
  }

  public void test1() {
    test("abc", "[abc]");
    test("'abc'", "[abc]");
    test("\"abc\"", "[\"abc\"]");
    test("\"\"abc\"\"", "[\"\"abc\"\"]"); // ""abc""
    test("\"a\"\"b\"\"c\"", "[\"a\"\"b\"\"c\"]"); // a""b""c

    test("''abc''", "['abc']");

    test("\"abc\",\"123\",\"xyz\"", "[\"abc\"][\"123\"][\"xyz\"]");

    test("abc,\"123\",'xyz'", "[abc][\"123\"][xyz]");

    test("abc,\"12\n3\",'xyz'", "[abc][\"12]");

    test("", "[]");
    test(",", "[][]");
    test(",,", "[][][]");

    test("\"\"", "[\"\"]");
    test("\"\",\"\"", "[\"\"][\"\"]");
    test("\"\",\"\",\"\"", "[\"\"][\"\"][\"\"]");

    test("''", "[]");
    test("'',''", "[][]");
    test("'','',''", "[][][]");

    test("abc\n", "[abc]");
    test("abc\n\n", "[abc]");
  }

  public void test(String s, String expected) {
    CsvParser parser = new CsvParser();
    parser.setQuotation("'");
    String[] a = null;
    try {
      a = parser.parseLine(s);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String result = dump(a);
    String status = "NG";
    if (result.equals(expected)) {
      status = "OK";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append(status);
    sb.append("] ");
    sb.append(result);
    if ("NG".equals(status)) {
      sb.append(" expected=" + expected);
    }
    sb.append(" src=" + CsvTestCommon.replaceSpecialChar(s));
    Log.i(sb.toString());
  }

  private String dump(String[] a) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < a.length; i++) {
      if (i < 0) {
        sb.append(" ");
      }
      String result = CsvTestCommon.replaceSpecialChar(a[i]);
      sb.append("[");
      sb.append(result);
      sb.append("]");
    }
    return sb.toString();
  }

}
