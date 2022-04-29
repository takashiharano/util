package com.libutil.test.base64;

import com.libutil.Base64Util;
import com.libutil.test.Log;

public class Base64Test {

  public static void main(String args[]) {
    encodeTest();
    decodeTest();
  }

  private static void encodeTest() {
    String str = "abc";
    Log.d("encode(\"" + str + "\")");
    String encoded = Base64Util.encode(str);
    Log.d(encoded);

    str = "";
    Log.d("encode(\"" + str + "\")");
    encoded = Base64Util.encode(str);
    Log.d(encoded);

    str = null;
    Log.d("encode(null)");
    encoded = Base64Util.encode(str);
    Log.d(encoded);

    byte[] b = { 97, 98, 99 };
    Log.d("encode([97,98,99]");
    encoded = Base64Util.encode(b);
    Log.d(encoded);
  }

  private static void decodeTest() {
    String str = "YWJj";
    Log.d("decodeToString(\"" + str + "\")");
    String decoded = Base64Util.decodeToString(str);
    Log.d(decoded);

    str = "";
    Log.d("decodeToString(\"" + str + "\")");
    decoded = Base64Util.decodeToString(str);
    Log.d(decoded);

    str = null;
    Log.d("decodeToString(null)");
    decoded = Base64Util.decodeToString(str);
    Log.d(decoded);

    str = "YWJj";
    Log.d("decode(\"" + str + "\")");
    byte[] d = Base64Util.decode(str);
    for (int i = 0; i < d.length; i++) {
      Log.d(d[i]);
    }

    str = null;
    Log.d("decode(null)");
    d = Base64Util.decode(str);
    Log.d(d);
  }

}
