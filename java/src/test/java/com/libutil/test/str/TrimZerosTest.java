package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class TrimZerosTest {

  public static void main(String args[]) {
    Log.i("trimZeros() ----------");
    testTrimZeros();

    Log.i("trimLeadingZeros() ----------");
    testTrimLeadingZeros();

    Log.i("trimTrailingZeros() ----------");
    testTrimTrailingZeros();
  }

  private static void testTrimZeros() {
    String[][] testData = { { "01.0", "1.0" }, { "01.01", "1.01" }, { "01.010", "1.01" }, { "01.010", "1.01" },
        { "010.0", "10.0" }, { "010.01", "10.01" }, { "010.010", "10.01" }, { "-010.010", "-10.01" }, { "-00", "-0" },
        { "-00.00", "-0.0" }, { "", "" }, { null, null } };
    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in = data[0];
      String data_expected = data[1];

      String out = StrUtil.trimZeros(data_in);
      String ret;
      if (out == null) {
        if (data_expected == null) {
          ret = "OK";
        } else {
          ret = "NG";
        }
      } else if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void testTrimLeadingZeros() {
    String[][] testData = { { "01", "1" }, { "001", "1" }, { "0010", "10" }, { "010", "10" }, { "1", "1" },
        { "0", "0" }, { "00", "0" }, { "10", "10" }, { "-010", "-10" }, { "", "" }, { null, null } };
    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in = data[0];
      String data_expected = data[1];

      String out = StrUtil.trimLeadingZeros(data_in);
      String ret;
      if (out == null) {
        if (data_expected == null) {
          ret = "OK";
        } else {
          ret = "NG";
        }
      } else if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

  private static void testTrimTrailingZeros() {
    String[][] testData = { { "01", "01" }, { "001", "001" }, { "0010", "001" }, { "010", "01" }, { "1", "1" },
        { "0", "0" }, { "00", "0" }, { "10", "1" }, { "-010", "-01" }, { "", "" }, { null, null } };
    for (int i = 0; i < testData.length; i++) {
      String[] data = testData[i];
      String data_in = data[0];
      String data_expected = data[1];

      String out = StrUtil.trimTrailingZeros(data_in);
      String ret;
      if (out == null) {
        if (data_expected == null) {
          ret = "OK";
        } else {
          ret = "NG";
        }
      } else if (out.equals(data_expected)) {
        ret = "OK";
      } else {
        ret = "NG";
      }
      Log.i("[" + ret + "] IN=" + data_in + " " + "EXP=" + data_expected + " OUT=" + out);
    }
  }

}
