package com.libutil.test.json.parser;

import com.libutil.JsonParser;
import com.libutil.test.Log;

public class JsonParserTest {

  public static void main(String args[]) {
    jsonParseTest();
  }

  private static void jsonParseTest() {
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

}
