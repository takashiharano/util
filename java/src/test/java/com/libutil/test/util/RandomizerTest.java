package com.libutil.test.util;

import com.libutil.RandomGenerator;
import com.libutil.test.Log;

public class RandomizerTest {

  public static void main(String args[]) {
    Log.i("int       = " + RandomGenerator.getInt());
    Log.i("int 10    = " + RandomGenerator.getInt(10));
    Log.i("int 1-6   = " + RandomGenerator.getInt(1, 6));
    Log.i("int 10-20 = " + RandomGenerator.getInt(10, 20));

    Log.i("long      = " + RandomGenerator.getLong());
    Log.i("float     = " + RandomGenerator.getFloat());
    Log.i("double    = " + RandomGenerator.getDouble());
    Log.i("boolean   = " + RandomGenerator.getBoolean());
    Log.i("char      = " + RandomGenerator.getChar());
    char ch = RandomGenerator.getChar(0x21, 0x7E);
    Log.i("char      = " + ch + " (" + (int) (ch) + ")");

    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    Log.i("String    = " + RandomGenerator.getString(chars, 8));
    Log.i("String    = " + RandomGenerator.getString("あいうえお", 8));
  }

}
