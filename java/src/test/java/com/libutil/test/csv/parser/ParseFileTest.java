package com.libutil.test.csv.parser;

import com.libutil.CsvParser;
import com.libutil.FileUtil;
import com.libutil.test.Log;

public class ParseFileTest {

  public static void main(String args[]) {
    ParseFileTest tester = new ParseFileTest();
    tester.test1();
  }

  public void test1() {
    String csvText = FileUtil.readText("C:/test/csvtest1.tsv");
    test(csvText);
  }

  public void test(String s) {
    CsvParser parser = new CsvParser("\t");
    Log.i("src=" + CsvTestCommon.replaceSpecialChar(s));
    try {
      String[][] a = parser.parse(s);
      Log.i(a.length + " records");
      for (int i = 0; i < a.length; i++) {
        dump(i + 1, a[i]);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
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
