package com.libutil.test.util;

import com.libutil.Randomizer;
import com.libutil.test.Log;

public class RandomizerTest {

  public static void main(String args[]) {
    Log.i("int       = " + Randomizer.getInt());
    Log.i("int 10    = " + Randomizer.getInt(10));
    Log.i("int 1-6   = " + Randomizer.getInt(1, 6));
    Log.i("int 10-20 = " + Randomizer.getInt(10, 20));

    Log.i("long      = " + Randomizer.getLong());
    Log.i("float     = " + Randomizer.getFloat());
    Log.i("double    = " + Randomizer.getDouble());
    Log.i("boolean   = " + Randomizer.getBoolean());
    Log.i("char      = " + Randomizer.getChar());
    char ch = Randomizer.getChar(0x21, 0x7E);
    Log.i("char      = " + ch + " (" + (int) (ch) + ")");

    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    Log.i("String    = " + Randomizer.getString(chars, 8));
    Log.i("String    = " + Randomizer.getString("あいうえお", 8));
  }

}
