package com.libutil.test.csv.parser;

import com.libutil.CsvParser;
import com.libutil.test.Log;

public class ParseEmptyTest {

  public static void main(String args[]) {
    ParseEmptyTest tester = new ParseEmptyTest();
    tester.test1();
  }

  public void test1() {
    test("");
    test(",");
    test(",,");

    test("\"\"");
    test("\"\",\"\"");
    test("\"\",\"\",\"\"");

    test("\r\n");
    test("\r");
    test("\n");

    test("\n\n");

    test("\"\"\n");
    test("\n\"\"");
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
