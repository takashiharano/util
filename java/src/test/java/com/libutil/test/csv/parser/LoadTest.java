package com.libutil.test.csv.parser;

import com.libutil.CsvParser;
import com.libutil.test.Log;

public class LoadTest {

  public static void main(String args[]) {
    LoadTest tester = new LoadTest();
    tester.test();
  }

  public void test() {
    String path = "C:/test/csvtest1.csv";
    CsvParser parser = new CsvParser();
    try {
      String[][] a = parser.load(path);
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
