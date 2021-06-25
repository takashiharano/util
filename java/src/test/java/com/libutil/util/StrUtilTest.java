package com.libutil.util;

import com.libutil.FileUtil;
import com.libutil.Log;
import com.libutil.StrUtil;

public class StrUtilTest {

  public static void main(String args[]) {
    text2arrayTest();
    countStrPatternTest();
    isIntegerTest();
    isFloatTest();
    isNumberTest();
    isNaNTest();
    quoteTest();
    intnum2decimalTest();
    trimZerosTest();
    encodeUriTest();
    decodeUriTest();
    escapeHtmlTest();
  }

  private static void text2arrayTest() {
    String str = FileUtil.readText("C:/test/a.txt");
    String[] arr = StrUtil.text2array(str);
    for (int i = 0; i < arr.length; i++) {
      Log.d("str=" + arr[i]);
    }
  }

  private static void countStrPatternTest() {
    String str = "abc123abc456xyz";
    Log.d("bc=" + StrUtil.countStrPattern(str, "bc"));
    Log.d("\\d\\d=" + StrUtil.countStrPattern(str, "\\d\\d"));
  }

  private static void isIntegerTest() {
    String str = "1";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "-1";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "1234";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "1.2";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "-1.2";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "1.";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "-1.";
    Log.d(str + "=" + StrUtil.isInteger(str));
  }

  private static void isFloatTest() {
    String str = "1";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "-1";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "1234";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "1.2";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "-1.2";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "1.";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "-1.";
    Log.d(str + "=" + StrUtil.isFloat(str));
  }

  private static void isNumberTest() {
    Log.d("isNumberTest");

    String str = "1";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "-1";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "1234";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "1.2";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "-1.2";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "1.";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "-1.";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "abc";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = null;
    Log.d(str + "=" + StrUtil.isNumber(str));
  }

  private static void isNaNTest() {
    Log.d("isNaNTest");

    String str = "1";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "-1";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "1234";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "1.2";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "-1.2";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "1.";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "-1.";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "abc";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = null;
    Log.d(str + "=" + StrUtil.isNaN(str));
  }

  private static void quoteTest() {
    Log.d(StrUtil.quote("abc"));
    Log.d(StrUtil.quote("a\"b\"c"));
    Log.d(StrUtil.quote("a\"b\"c", "\"", "\""));
    Log.d(StrUtil.quote("a\"b\"c", "'"));
    Log.d(StrUtil.quote("a\"b\"'c", "'"));
  }

  private static void intnum2decimalTest() {
    Log.d("----");
    Log.d(StrUtil.intnum2decimal(1000, 3));
    Log.d(StrUtil.intnum2decimal(100, 3));
    Log.d(StrUtil.intnum2decimal(10, 3));
    Log.d(StrUtil.intnum2decimal(1, 3));
    Log.d(StrUtil.intnum2decimal(0, 3));
    Log.d("----");
    Log.d(StrUtil.intnum2decimal(-1000, 3));
    Log.d(StrUtil.intnum2decimal(-100, 3));
    Log.d(StrUtil.intnum2decimal(-10, 3));
    Log.d(StrUtil.intnum2decimal(-1, 3));
    Log.d(StrUtil.intnum2decimal(-0, 3));
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
