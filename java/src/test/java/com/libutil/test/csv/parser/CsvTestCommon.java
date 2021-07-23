package com.libutil.test.csv.parser;

public class CsvTestCommon {

  public static String replaceSpecialChar(String s) {
    return s.replace("\r\n", "[CRLF]").replace("\r", "[CR]").replace("\n", "[LF]").replace("\t", "[TAB]");
  }
}
