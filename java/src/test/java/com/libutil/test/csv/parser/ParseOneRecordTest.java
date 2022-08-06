package com.libutil.test.csv.parser;

import com.libutil.CsvParser;
import com.libutil.test.Log;

public class ParseOneRecordTest {

  public static void main(String args[]) {
    ParseOneRecordTest tester = new ParseOneRecordTest();
    tester.test1();
  }

  public void test1() {
    test("abc", "[abc]");
    test("\"abc\"", "[abc]");
    test("\"\"abc\"\"", "[\"abc\"]"); // "abc"
    test("\"a\"\"b\"\"c\"", "[a\"b\"c]"); // a"b"c

    test("\"abc\",\"123\",\"xyz\"", "[abc][123][xyz]");

    test("abc,\"123\",'xyz'", "[abc][123]['xyz']");

    test("abc,\"12\n3\",'xyz'", "[abc][12[LF]3]['xyz']");
    test("abc,\"\"123\"\",xyz", "[abc][\"123\"][xyz]"); // [abc]["123"][xyz]
    test("abc,\"\"123\"\",x\"yz", "[abc][\"123\"][x\"yz]"); // [abc]["123"][x"yz]
    test("abc,\"\"123\"\",x\"y\"z", "[abc][\"123\"][x\"y\"z]"); // [abc]["123"][x"y"z]
    test("abc,\"\"123\"\",\"xyz,999", "[abc][\"123\"][xyz,999]"); // [abc]["123"][xyz,999]

    test("abc,\"\"123\"\",\"x,\"yz,999", "[abc][\"123\"][\"x,\"yz][999]"); // [abc]["123"]["x,"yz][999]

    test("", "[]");
    test(",", "[][]");
    test(",,", "[][][]");

    test("\"\"", "[]");
    test("\"\",\"\"", "[][]");
    test("\"\",\"\",\"\"", "[][][]");

    test("\"\"\"\"\"", "[\"\"]");
    test("\"\"\"\"\"\",\"\"\"\"\"\"", "[\"\"][\"\"]");
    test("\"\"\"\"\"\",\"\"\"\"\"\",\"\"\"\"\"\"", "[\"\"][\"\"][\"\"]");

    test("abc\n", "[abc[LF]]");
    test("abc\n\n", "[abc[LF][LF]]");
  }

  public void test(String s, String expected) {
    CsvParser parser = new CsvParser();
    String[] a = null;
    try {
      a = parser.parseOneRecord(s);
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
    if (a == null) {
      return "null";
    }
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
