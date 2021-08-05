package com.libutil.test.json.builder;

import java.util.ArrayList;
import java.util.List;

import com.libutil.JsonBuilder;
import com.libutil.test.Log;

public class AppendListTest {

  public static void main(String args[]) {
    appendStringListTest();
    appendIntegerListTest();
    appendLongListTest();
    appendFloatListTest();
    appendDoubleListTest();
    appendBoolenListTest();
  }

  private static void appendStringListTest() {
    List<String> list = new ArrayList<>();
    list.add("a");
    list.add(null);
    list.add("c");
    JsonBuilder jb = new JsonBuilder();
    jb.appendList("key1", list);
    Log.i("String:  " + jb);

    String[] array = { "a", null, "c" };
    jb = new JsonBuilder();
    jb.appendList("key1", array);
    Log.i("String : " + jb);
  }

  private static void appendIntegerListTest() {
    int[] array = { 1, 2 };
    JsonBuilder jb = new JsonBuilder();
    jb.appendList("key1", array);
    Log.i("int    : " + jb);
  }

  private static void appendLongListTest() {
    long[] array = { 1L, 2L };
    JsonBuilder jb = new JsonBuilder();
    jb.appendList("key1", array);
    Log.i("long   : " + jb);
  }

  private static void appendFloatListTest() {
    float[] array = { 1.5f, 2.0f };
    JsonBuilder jb = new JsonBuilder();
    jb.appendList("key1", array);
    Log.i("float  : " + jb);
  }

  private static void appendDoubleListTest() {
    double[] array = { 1.5f, 2.0f };
    JsonBuilder jb = new JsonBuilder();
    jb.appendList("key1", array);
    Log.i("double : " + jb);
  }

  private static void appendBoolenListTest() {
    boolean[] array = { true, false };
    JsonBuilder jb = new JsonBuilder();
    jb.appendList("key1", array);
    Log.i("boolean: " + jb);
  }

}
