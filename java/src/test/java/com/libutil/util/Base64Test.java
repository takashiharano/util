package com.libutil.util;

import com.libutil.Log;
import com.libutil.Util;

public class Base64Test {

  public static void main(String args[]) {
    encodeBase64Test();
    decodeBase64Test();
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

}
