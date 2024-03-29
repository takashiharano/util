package com.libutil.test.str;

import com.libutil.FileUtil;
import com.libutil.StrUtil;
import com.libutil.test.Log;

public class StrUtilTest {

  public static void main(String args[]) {
    texttoArrayTest();
    countPatternTest();
    quoteTest();
    intToDecimalTest();
    trimZerosTest();
    encodeUriTest();
    decodeUriTest();
    escapeHtmlTest();
  }

  private static void texttoArrayTest() {
    String str = FileUtil.readText("C:/test/a.txt");
    String[] arr = StrUtil.textToArray(str);
    for (int i = 0; i < arr.length; i++) {
      Log.d("str=" + arr[i]);
    }
  }

  private static void countPatternTest() {
    String str = "abc123abc456xyz";
    Log.d("bc=" + StrUtil.countPattern(str, "bc"));
    Log.d("\\d\\d=" + StrUtil.countPattern(str, "\\d\\d"));
  }

  private static void quoteTest() {
    Log.d(StrUtil.quote("abc"));
    Log.d(StrUtil.quote("a\"b\"c"));
    Log.d(StrUtil.quote("a\"b\"c", "\"", "\""));
    Log.d(StrUtil.quote("a\"b\"c", "'"));
    Log.d(StrUtil.quote("a\"b\"'c", "'"));
  }

  private static void intToDecimalTest() {
    Log.d("----");
    Log.d(StrUtil.intNumToDecimal(1000, 3));
    Log.d(StrUtil.intNumToDecimal(100, 3));
    Log.d(StrUtil.intNumToDecimal(10, 3));
    Log.d(StrUtil.intNumToDecimal(1, 3));
    Log.d(StrUtil.intNumToDecimal(0, 3));
    Log.d("----");
    Log.d(StrUtil.intNumToDecimal(-1000, 3));
    Log.d(StrUtil.intNumToDecimal(-100, 3));
    Log.d(StrUtil.intNumToDecimal(-10, 3));
    Log.d(StrUtil.intNumToDecimal(-1, 3));
    Log.d(StrUtil.intNumToDecimal(-0, 3));
  }

  private static void trimZerosTest() {
    Log.d("----");
    _trimZerosTest("123");
    _trimZerosTest("0123");
    _trimZerosTest("123.1");
    _trimZerosTest("0123.10");
    _trimZerosTest("1230");
    _trimZerosTest("01230");
    _trimZerosTest("-123");
    _trimZerosTest("-0123");
    _trimZerosTest("-123.1");
    _trimZerosTest("-0123.10");
    _trimZerosTest("-123.01");
    _trimZerosTest("-123.010");
    _trimZerosTest("0");
    _trimZerosTest("0.0");
    _trimZerosTest("-0");
    _trimZerosTest("-0.0");
  }

  private static void _trimZerosTest(String v) {
    Log.d(v + ": " + StrUtil.trimZeros(v));
  }

  private static void encodeUriTest() {
    _encodeUriTest("abc");
    _encodeUriTest("abc=123");
    _encodeUriTest(" &%+-/*=.");
    _encodeUriTest("あいう");
  }

  private static void _encodeUriTest(String s) {
    Log.d(s + ": " + StrUtil.encodeUri(s));
  }

  private static void decodeUriTest() {
    _decodeUriTest("abc");
    _decodeUriTest("abc%3D123");
    _decodeUriTest("+%26%25%2B-%2F*%3D.");
    _decodeUriTest("%E3%81%82%E3%81%84%E3%81%86");
  }

  private static void _decodeUriTest(String s) {
    Log.d(s + ": " + StrUtil.decodeUri(s));
  }

  private static void escapeHtmlTest() {
    String str = "&<>\"'";
    String escaped = StrUtil.escapeHtml(str);
    Log.d(escaped);
  }

}
