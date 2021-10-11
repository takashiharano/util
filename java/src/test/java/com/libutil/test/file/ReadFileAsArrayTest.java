package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class ReadFileAsArrayTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    Log.i("UTF-8");
    readFileAsArrayTest("C:/test/a.txt");
    Log.i("");

    Log.i("UTF-8-BOM");
    readFileAsArrayTest("C:/test/a_utf8_bom.txt");
    Log.i("");

    Log.i("UTF-16BE-BOM");
    readFileAsArrayTest("C:/test/a_utf16_be_bom.txt");
    Log.i("");

    Log.i("UTF-16LE-BOM");
    readFileAsArrayTest("C:/test/a_utf16_le_bom.txt");
    Log.i("");

    Log.i("Shift_JIS");
    readFileAsArrayTest("C:/test/a_sjis.txt");
    Log.i("");

    Log.i("EUC");
    readFileAsArrayTest("C:/test/a_euc.txt");
    Log.i("");

    Log.i("--- not supported ---");
    Log.i("UTF-16BE");
    readFileAsArrayTest("C:/test/a_utf16_be.txt");
    Log.i("");

    Log.i("UTF-16LE");
    readFileAsArrayTest("C:/test/a_utf16_le.txt");
    Log.i("");

    Log.i("BIG5");
    readFileAsArrayTest("C:/test/a_big5.txt");
    Log.i("");

    Log.i("GB2312");
    readFileAsArrayTest("C:/test/a_gb2312.txt");
    Log.i("");

    Log.i("NotFound");
    readFileAsArrayTest("C:/test/_.txt");
    Log.i("");
  }

  private static void readFileAsArrayTest(String path) {
    String charsetName = null;
    if (FileUtil.exists(path)) {
      byte[] bytes = FileUtil.read(path);
      charsetName = FileUtil.getCharsetName(bytes);
    }
    Log.d("charset=" + charsetName);
    String[] text = FileUtil.readTextAsArray(path, charsetName);
    if (text == null) {
      Log.e("READ_ERROOR");
      return;
    }
    for (int i = 0; i < text.length; i++) {
      Log.d((i + 1) + ": " + text[i]);
    }
  }

}
