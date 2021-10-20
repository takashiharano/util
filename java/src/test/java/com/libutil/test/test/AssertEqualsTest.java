package com.libutil.test.test;

import com.libutil.TestUtil;
import com.libutil._Log;

public class AssertEqualsTest {

  public static void main(String args[]) {
    test1();
    test2();
  }

  public static void test1() {
    TestUtil.assertEquals("a", "a");

    TestUtil.assertEquals(123, 123);
    TestUtil.assertEquals(123, 123L);

    TestUtil.assertEquals(1.5, 1.5);
    TestUtil.assertEquals(1.5, 1.5f);

    TestUtil.assertEquals(true, true);
    TestUtil.assertEquals(false, false);

    TestUtil.assertEquals(null, null);

    _Log.i("------------------");

    TestUtil.assertEquals("a", "b");

    TestUtil.assertEquals(123, 124);
    TestUtil.assertEquals(123, 124L);

    TestUtil.assertEquals(1.5, 1.6);
    TestUtil.assertEquals(1.5, 1.6f);

    TestUtil.assertEquals(true, false);
    TestUtil.assertEquals(false, true);

    TestUtil.assertEquals("a", null);
    TestUtil.assertEquals(null, "a");
  }

  public static void test2() {
    TestUtil.assertEquals("EQ-1", "a", "a");

    TestUtil.assertEquals("EQ-2", 123, 123);
    TestUtil.assertEquals("EQ-3", 123, 123L);
  }

}
