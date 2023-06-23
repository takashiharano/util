package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class NormalizeNumberTest {

  public static void main(String args[]) {
    test("0", "0");
    test("1", "1");
    test("01", "1");
    test("001", "1");
    test("10", "10");
    test("010", "10");
    test("0010", "10");
    test("00100", "100");
    test("0.1", "0.1");
    test("0.0", "0");
    test("1.2", "1.2");
    test("1.20", "1.2");
    test("1.200", "1.2");
    test("1.0", "1");
    test("1.00", "1");
    test("1.02", "1.02");
    test("1.020", "1.02");
    test("01.20", "1.2");
    test("01.200", "1.2");
    test("001.20", "1.2");
    test("001.200", "1.2");
    test("1.", "1");
    test("01.", "1");
    test(".1", "0.1");
    test(".10", "0.1");

    test("-0", "-0");
    test("-1", "-1");
    test("-01", "-1");
    test("-001", "-1");
    test("-10", "-10");
    test("-010", "-10");
    test("-0010", "-10");
    test("-00100", "-100");
    test("-0.1", "-0.1");
    test("-0.0", "-0");
    test("-1.2", "-1.2");
    test("-1.20", "-1.2");
    test("-1.200", "-1.2");
    test("-1.0", "-1");
    test("-1.00", "-1");
    test("-1.02", "-1.02");
    test("-1.020", "-1.02");
    test("-01.20", "-1.2");
    test("-01.200", "-1.2");
    test("-001.20", "-1.2");
    test("-001.200", "-1.2");
    test("-1.", "-1");
    test("-01.", "-1");
    test("-.1", "-0.1");
    test("-.10", "-0.1");

    test("+0", "0");
    test("+1", "1");
    test("+01", "1");
    test("+001", "1");
    test("+10", "10");
    test("+010", "10");
    test("+0010", "10");
    test("+00100", "100");
    test("+0.1", "0.1");
    test("+0.0", "0");
    test("+1.2", "1.2");
    test("+1.20", "1.2");
    test("+1.200", "1.2");
    test("+1.0", "1");
    test("+1.00", "1");
    test("+1.02", "1.02");
    test("+1.020", "1.02");
    test("+01.20", "1.2");
    test("+01.200", "1.2");
    test("+001.20", "1.2");
    test("+001.200", "1.2");
    test("+1.", "1");
    test("+01.", "1");
    test("+.1", "0.1");
    test("+.10", "0.1");

    test(null, null);
    test("", "");
    test("abc", "abc");
    test("1.2.3", "1.2.3");
  }

  private static void test(String s, String exp) {
    String st = "NG";
    String got = StrUtil.normalizeNumber(s);
    if ((got == null) && (exp == null)) {
      st = "OK";
    } else if (got.equals(exp)) {
      st = "OK";
    }
    String ret = "[" + st + "] " + s + " exp=" + exp + " got=" + got;
    Log.i(ret);
  }

}
