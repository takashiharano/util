package com.libutil.test.csv.parser;

import com.libutil.CsvParser;
import com.libutil.test.Log;

public class ParseTest {

  public static void main(String args[]) {
    ParseTest tester = new ParseTest();
    tester.test1();
  }

  public void test1() {
    test("abc");
    test("\"abc\""); // abc
    test("\"\"abc\"\""); // "abc"
    test("\"a\"\"b\"\"c\""); // a"b"c

    test("\"abc\",\"123\",\"xyz\""); // [abc][123][xyz]

    test("abc,\"123\",'xyz'"); // [abc][123]['xyz']

    test("abc,\"12\n3\",'xyz'"); // [abc][12[LF]3]['xyz']
    test("abc,\"\"123\"\",xyz"); // [abc]["123"][xyz]
    test("abc,\"\"123\"\",x\"yz"); // [abc]["123"][x"yz]
    test("abc,\"\"123\"\",x\"y\"z"); // [abc]["123"][x"y"z]
    test("abc,\"\"123\"\",\"xyz,999"); // [abc]["123"]["xyz,999]

    test("abc,\"\"123\"\",\"x,\"yz,999"); // [abc]["123"]["x,"yz,999]
  }

  public void test(String s) {
    CsvParser parser = new CsvParser();
    Log.i("src=" + CsvTestCommon.replaceSpecialChar(s));
    try {
      String[][] a = parser.parse(s);
      for (int i = 0; i < a.length; i++) {
        dump(i + 1, a[i]);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    Log.i("");
  }

  private void dump(int row, String[] a) {
    StringBuilder sb = new StringBuilder();
    sb.append(row);
    sb.append(": ");
    for (int i = 0; i < a.length; i++) {
      if (i < 0) {
        sb.append(" ");
      }
      String result = CsvTestCommon.replaceSpecialChar(a[i]);
      sb.append("[");
      sb.append(result);
      sb.append("]");
    }
    Log.i(sb.toString());
  }

}
