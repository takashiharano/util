package com.libutil.test.csv.parser;

import com.libutil.CsvParser;
import com.libutil.test.Log;

public class ParseTsvTest {

  public static void main(String args[]) {
    ParseTsvTest tester = new ParseTsvTest();
    tester.test1();
  }

  public void test1() {
    test("\"abc\"\t\"123\"\t\"xyz\""); // abc,123,xyz

    test("abc\t\"123\"\t'xyz'"); // abc,123,'xyz'

    test("abc\t\"12\n3\"\t'xyz'"); // abc,12[LF]3,'xyz'

    test("");
    test("\t");
    test("\t\t");

    test("\"\"");
    test("\"\"\t\"\"");
    test("\"\"\t\"\"\t\"\"");
  }

  public void test(String s) {
    CsvParser parser = new CsvParser("\t");
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
