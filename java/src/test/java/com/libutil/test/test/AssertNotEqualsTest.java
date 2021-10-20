package com.libutil.test.test;

import com.libutil.TestUtil;
import com.libutil._Log;

public class AssertNotEqualsTest {

  public static void main(String args[]) {
    test1();
    test2();
  }

  public static void test1() {
    TestUtil.assertNotEquals("a", "a");

    TestUtil.assertNotEquals(123, 123);
    TestUtil.assertNotEquals(123, 123L);

    TestUtil.assertNotEquals(1.5, 1.5);
    TestUtil.assertNotEquals(1.5, 1.5f);

    TestUtil.assertNotEquals(true, true);
    TestUtil.assertNotEquals(false, false);

    TestUtil.assertNotEquals(null, null);

    _Log.i("------------------");

    TestUtil.assertNotEquals("a", "b");

    TestUtil.assertNotEquals(123, 124);
    TestUtil.assertNotEquals(123, 124L);

    TestUtil.assertNotEquals(1.5, 1.6);
    TestUtil.assertNotEquals(1.5, 1.6f);

    TestUtil.assertNotEquals(true, false);
    TestUtil.assertNotEquals(false, true);

    TestUtil.assertNotEquals("a", null);
    TestUtil.assertNotEquals(null, "a");
  }

  public static void test2() {
    TestUtil.assertNotEquals("NE-1", "a", "b");

    TestUtil.assertNotEquals("NE-2", 123, 124);
    TestUtil.assertNotEquals("NE-3", 123, 124L);
  }

}
