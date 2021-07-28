package com.libutil.test.json.builder;

import com.libutil.JsonBuilder;
import com.libutil.test.Log;

public class AppendWithBase64Test {

  public static void main(String args[]) {
    AppendWithBase64Test tester = new AppendWithBase64Test();
    tester.test();
  }

  private void test() {
    byte[] b = { 0x61, 0x62, 0x63 };
    byte[] b2 = null;
    String strNull = null;
    JsonBuilder jb = new JsonBuilder();
    jb.appendWithBase64("key1", "abc");
    jb.appendWithBase64("key2", strNull);
    jb.appendWithBase64("key3", "");
    jb.appendWithBase64("key4", b);
    jb.appendWithBase64("key5", b2);
    Log.i(jb.toString());
  }

}
