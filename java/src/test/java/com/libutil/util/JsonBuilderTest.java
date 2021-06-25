package com.libutil.util;

import java.util.ArrayList;
import java.util.List;

import com.libutil.JsonBuilder;
import com.libutil.Log;

public class JsonBuilderTest {

  public static void main(String args[]) {
    buildJsonTest();
  }

  private static void buildJsonTest() {
    JsonBuilder jb = new JsonBuilder();
    jb.append("status", "OK");
    Log.d(jb);

    jb.append("key1", "abc");
    Log.d(jb);

    jb.append("key2", 123);
    Log.d(jb);

    jb.append("key3", 123.5);
    Log.d(jb);

    jb.append("key4", true);
    Log.d(jb);

    jb.append("key5", null);
    Log.d(jb);

    String json = "[\"a\", \"b\"]";
    jb.appendObject("key6", json);
    Log.d(jb);

    String[] strArray = { "x", "y" };
    jb.appendList("key7", strArray);
    Log.d(jb);

    long[] intArray = { 1, 2 };
    jb.appendList("key8", intArray);
    Log.d(jb);

    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");
    jb.appendList("key9", list);
    Log.d(jb);
  }

}
