package com.takashiharano.test.util;

import com.takashiharano.util.JsonBuilder;
import com.takashiharano.util.JsonParser;
import com.takashiharano.util.Log;

public class JsonTest {

  public static void main(String args[]) {
    jsonGetTest();
    buildJsonTest();
  }

  private static void jsonGetTest() {
    String json = "{\"key1\":\"val1\", \"key2\":\"val2\", \"key3\": 1}";
    JsonParser parser;
    try {
      parser = new JsonParser(json);

      String value = parser.getString("key1");
      Log.d("key1=" + value);

      value = parser.getString("key2");
      Log.d("key2=" + value);

      int iVal = parser.getInt("key3");
      Log.d("key3=" + iVal);
    } catch (Exception e1) {
      e1.printStackTrace();
    }

    String json2 = "{\"key1\"}";
    try {
      parser = new JsonParser(json2);
      String value = parser.getString("key1");
      Log.d("key1=" + value);

      Log.d("validate1");
      try {
        JsonParser.eval(json);
      } catch (Exception e) {
        Log.d(e.getMessage());
      }

      Log.d("validate2");
      try {
        JsonParser.eval(json2);
      } catch (Exception e) {
        Log.d(e.getMessage());
      }
    } catch (Exception e1) {
      e1.printStackTrace();
    }

    String json3 = "{\"key1\":[\"1\", \"2\"]}";
    try {
      parser = new JsonParser(json3);
      Object strArray = parser.get("key1");
      Log.d(strArray);
    } catch (Exception e1) {
      e1.printStackTrace();
    }

    String json4 = "{\"key1\":\"val1\", \"key2\":{\"keyA\": \"val2-A\"}, \"key3\": 1}";
    try {
      parser = new JsonParser(json4);
      String value = parser.getString("key2.keyA");
      Log.d("key2.keyA=" + value);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
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

}
