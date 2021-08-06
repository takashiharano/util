package com.libutil.test.json.builder;

import com.libutil.JsonBuilder;
import com.libutil.test.Log;

public class BuildListTest {

  public static void main(String args[]) {
    buildListTest();
  }

  private static void buildListTest() {
    JsonBuilder jb = new JsonBuilder();
    jb.openList("key1");
    jb.appendListElement("a");
    jb.appendListElement(1);
    jb.appendListElement(2L);
    jb.appendListElement(1.5f);
    jb.appendListElement(9.8f);
    jb.appendListElement(true);
    jb.appendListElement(null);
    jb.appendListElementWithBase64("abc");
    jb.appendListElementWithBase64("");
    jb.appendListElementWithBase64(null);
    jb.closeList();
    Log.i("list: " + jb);

    String objectJson1 = "{\"key1\":\"abc\"}";
    String objectJson2 = "{\"key2\":\"xyz\"}";
    jb = new JsonBuilder();
    jb.openList();
    jb.appendObjectToList(objectJson1);
    jb.appendObjectToList(objectJson2);
    jb.closeList();
    Log.i("list: " + jb.toString(true));
  }

}
