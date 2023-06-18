package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class IsNumericTest {

  public static void main(String args[]) {
    isIntegerTest();
    isFloatTest();
    isNumericTest();
    isNaNTest();
    isNumberTest();
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

    str = "+1";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "+1.2";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = ".1";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "-.1";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "+.1";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "abc";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = "";
    Log.d(str + "=" + StrUtil.isInteger(str));

    str = null;
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

    str = "+1";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "+1.2";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = ".1";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "-.1";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "+.1";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "abc";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = "";
    Log.d(str + "=" + StrUtil.isFloat(str));

    str = null;
    Log.d(str + "=" + StrUtil.isFloat(str));
  }

  private static void isNumericTest() {
    Log.d("isNumberTest");

    String str = "1";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "-1";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "1234";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "1.2";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "-1.2";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "1.";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "-1.";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "+1";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "+1.2";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = ".1";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "-.1";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "+.1";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "abc";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = "";
    Log.d(str + "=" + StrUtil.isNumeric(str));

    str = null;
    Log.d(str + "=" + StrUtil.isNumeric(str));
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

    str = "+1";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "+1.2";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = ".1";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "-.1";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "+.1";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "abc";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = "";
    Log.d(str + "=" + StrUtil.isNaN(str));

    str = null;
    Log.d(str + "=" + StrUtil.isNaN(str));
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

    str = "+1";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "+1.2";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = ".1";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "-.1";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "+.1";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "abc";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "";
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = null;
    Log.d(str + "=" + StrUtil.isNumber(str));

    str = "１";
    Log.d(str + "=" + StrUtil.isNumber(str));
  }

}
