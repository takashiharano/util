package com.takashiharano.test.util;

import com.takashiharano.util.Log;
import com.takashiharano.util.Util;

public class UtilTest {

  public static void main(String args[]) {
    Log.d(Util.getHeapInfoString());
    encodeBase64Test();
    decodeBase64Test();
    randomTest();
    execCommandTest();
    getTestBytesTest();
    fileHashTest();
  }

  private static void encodeBase64Test() {
    String str = "abc";
    Log.d("encodeBase64(\"" + str + "\")");
    String encoded = Util.encodeBase64(str);
    Log.d(encoded);

    str = "";
    Log.d("encodeBase64(\"" + str + "\")");
    encoded = Util.encodeBase64("");
    Log.d(encoded);

    str = null;
    Log.d("encodeBase64(null)");
    encoded = Util.encodeBase64("");
    Log.d(encoded);

    byte[] b = { 97, 98, 99 };
    Log.d("encodeBase64([97,98,99]");
    encoded = Util.encodeBase64(b);
    Log.d(encoded);
  }

  private static void decodeBase64Test() {
    String str = "YWJj";
    Log.d("decodeBase64(\"" + str + "\")");
    String decoded = Util.decodeBase64(str);
    Log.d(decoded);

    str = "";
    Log.d("decodeBase64(\"" + str + "\")");
    decoded = Util.decodeBase64(str);
    Log.d(decoded);

    str = null;
    Log.d("decodeBase64(null)");
    decoded = Util.decodeBase64(str);
    Log.d(decoded);

    str = "YWJj";
    Log.d("decodeBase64B(\"" + str + "\")");
    byte[] d = Util.decodeBase64B(str);
    for (int i = 0; i < d.length; i++) {
      Log.d(d[i]);
    }
  }

  private static void randomTest() {
    Log.d("random=" + Util.randomInt());
    Log.d("random10=" + Util.randomInt(10));
    Log.d("random1-6=" + Util.randomInt(1, 6));
    Log.d("random10-20=" + Util.randomInt(10, 20));

    Log.d("randomDouble=" + Util.randomDouble());

    Log.d("randomBoolean=" + Util.randomBoolean());

    Log.d("randomString=" + Util.randomString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", 8));
    Log.d("randomString=" + Util.randomString("あいうえお", 8));
  }

  private static void execCommandTest() {
    for (int i = 0; i < 1; i++) {
      _execCommandTest();
    }
  }

  private static void _execCommandTest() {
    String[] command = { "cmd", "/c", "dir c:\\tmp" };
    try {
      String result = Util.execCommand(command);
      Log.d(result);

      result = Util.execCommand(command, "SJIS");
      Log.d(result);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void getTestBytesTest() {
    byte[] b;
    b = Util.getTestBytes(1, -1, -1);
    Log.d(b);

    b = Util.getTestBytes(2, -1, -1);
    Log.d(b);

    b = Util.getTestBytes(10, -1, -1);
    Log.d(b);

    b = Util.getTestBytes(1, 0x21, 0x23);
    Log.d(b);

    b = Util.getTestBytes(2, 0x21, 0x23);
    Log.d(b);

    b = Util.getTestBytes(3, 0x21, 0x23);
    Log.d(b);

    b = Util.getTestBytes(10, 0x21, 0x23);
    Log.d(b);

    b = Util.getTestBytes(12, 0x21, 0x23);
    Log.d(b);
  }

  private static void fileHashTest() {
    String path = "C:/test/a.txt";
    Log.d(Util.getFileHash(path, "SHA-1"));
    Log.d(Util.getFileHash(path, "SHA-256"));
    Log.d(Util.getFileHash(path, "SHA-512"));
  }

}
