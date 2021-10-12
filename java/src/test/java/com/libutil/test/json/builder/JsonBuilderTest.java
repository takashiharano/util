package com.libutil.test.json.builder;

import com.libutil.JsonBuilder;
import com.libutil.test.Log;

public class JsonBuilderTest {

  public static void main(String args[]) {
    buildJsonTest();

    appendTest();
    appendStringTest();
    appendIntegerTest();
    appendLongTest();
    appendFloatTest();
    appendDoubleTest();
    appendBooleanTest();
    appendObjectTest();

    Log.i("toString ----------");
    toStringTest();

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
  }

  private static void appendTest() {
    JsonBuilder jb = new JsonBuilder();
    String v = "JSON";
    jb.append(v);
    Log.i("append()       : " + jb);
  }

  private static void appendStringTest() {
    JsonBuilder jb = new JsonBuilder();
    String v = "abc";
    jb.append("key1", v);
    Log.i("append(String) : " + jb);
  }

  private static void appendIntegerTest() {
    JsonBuilder jb = new JsonBuilder();
    int v = 1;
    jb.append("key1", v);
    Log.i("append(int)    : " + jb);
  }

  private static void appendLongTest() {
    JsonBuilder jb = new JsonBuilder();
    long v = 1L;
    jb.append("key1", v);
    Log.i("append(long)   : " + jb);
  }

  private static void appendFloatTest() {
    JsonBuilder jb = new JsonBuilder();
    float v = 1.5f;
    jb.append("key1", v);
    Log.i("append(float)  : " + jb);
  }

  private static void appendDoubleTest() {
    JsonBuilder jb = new JsonBuilder();
    double v = 1.5;
    jb.append("key1", v);
    Log.i("append(double) : " + jb);
  }

  private static void appendBooleanTest() {
    JsonBuilder jb = new JsonBuilder();
    boolean v = true;
    jb.append("key1", v);
    Log.i("append(boolean): " + jb);
  }

  private static void appendObjectTest() {
    JsonBuilder jb = new JsonBuilder();
    String v = "{\"key1\":\"val1\"}";
    jb.appendObject("key1", v);
    Log.i("appendObject() : " + jb);
  }

  private static void toStringTest() {
    JsonBuilder jb = new JsonBuilder();
    String v = "JSON";
    jb.append(v);
    Log.i("toString()     : " + jb.toString());
    Log.i("toStringAsIs() : " + jb.toStringAsIs());
  }

}
