package com.libutil.test.datetime;

import com.libutil.DateTime;
import com.libutil.test.Log;

public class ToStringTest {

  public static void main(String args[]) {

    DateTime datetime = new DateTime();
    Log.i(datetime.toString());
    Log.i(datetime.toString("yyyyMMdd'T'HHmmssSSSXX"));
  }

}
