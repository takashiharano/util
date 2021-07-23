package com.libutil.test.csv.parser;

import com.libutil.CsvParser;
import com.libutil.test.Log;

public class MultiLineTest {

  public static void main(String args[]) {
    MultiLineTest tester = new MultiLineTest();
    tester.test1();
  }

  public void test1() {
    Log.i("[LF]----------");
    test("abc\nxyz");
    test("abc\nxyz\n");
    test("abc\nxyz\n\n");

    Log.i("[CRLF]----------");
    test("abc\r\nxyz");
    test("abc\r\nxyz\r\n");
    test("abc\r\nxyz\r\n\r\n");

    Log.i("[LF] 3fields ----------");
    test("abc,123\nxyz,987");
    test("abc,123\nxyz,987\n");
    test("abc,123\nxyz,987\n\n");

    Log.i("----------");
    test("\"abc\ndef\",123\nxyz,987");
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
      sb.append("[");
      sb.append(CsvTestCommon.replaceSpecialChar(a[i]));
      sb.append("]");
    }
    Log.i(sb.toString());
  }

}
