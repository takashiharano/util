package com.libutil.test.json.builder;

import com.libutil.JsonBuilder;
import com.libutil.test.Log;

public class ToJsonTest {

  public static void main(String args[]) {
    toJsonTest();
  }

  private static void toJsonTest() {
    String[] strList = { "abc", "xyz" };
    Log.i("String[] : " + JsonBuilder.toJson(strList));

    int[] intList = { 1, 2 };
    Log.i("int[]    : " + JsonBuilder.toJson(intList));

    long[] longList = { 1, 2 };
    Log.i("long[]   : " + JsonBuilder.toJson(longList));

    float[] floatList = { 1.5f, 2.0f };
    Log.i("float[]  : " + JsonBuilder.toJson(floatList));

    double[] doubleList = { 1.5, 2.0 };
    Log.i("double[] : " + JsonBuilder.toJson(doubleList));

    boolean[] booleanList = { true, false };
    Log.i("boolean[]: " + JsonBuilder.toJson(booleanList));
  }

}
