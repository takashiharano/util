package com.libutil.test.test;

import com.libutil.TestUtil;

public class AssertLessThanTest {

  public static void main(String args[]) {
    testAssertLessThan();
    testAssertLessThanOrEqual();
  }

  public static void testAssertLessThan() {
    TestUtil.assertLessThan(2, 1);
    TestUtil.assertLessThan(2, 2);
    TestUtil.assertLessThan(1, 2);

    TestUtil.assertLessThan(2L, 1L);
    TestUtil.assertLessThan(2L, 2L);
    TestUtil.assertLessThan(1L, 2L);

    TestUtil.assertLessThan(2.0f, 1.5f);
    TestUtil.assertLessThan(2.0f, 2.0f);
    TestUtil.assertLessThan(1.5f, 2.0f);

    TestUtil.assertLessThan(2.0, 1.5);
    TestUtil.assertLessThan(2.0, 2.0);
    TestUtil.assertLessThan(1.5, 2.0);

    TestUtil.assertLessThan("LessThan1", 2, 1);
    TestUtil.assertLessThan("LessThan2", 2, 2);
    TestUtil.assertLessThan("LessThan3", 1, 2);

    TestUtil.assertLessThan("LessThan1", 2L, 1L);
    TestUtil.assertLessThan("LessThan2", 2L, 2L);
    TestUtil.assertLessThan("LessThan3", 1L, 2L);

    TestUtil.assertLessThan("LessThan1", 2.0f, 1.5f);
    TestUtil.assertLessThan("LessThan2", 2.0f, 2.0f);
    TestUtil.assertLessThan("LessThan3", 1.5f, 2.0f);

    TestUtil.assertLessThan("LessThan1", 2.0, 1.5);
    TestUtil.assertLessThan("LessThan2", 2.0, 2.0);
    TestUtil.assertLessThan("LessThan3", 1.5, 2.0);
  }

  public static void testAssertLessThanOrEqual() {
    TestUtil.assertLessThanOrEqual(2, 1);
    TestUtil.assertLessThanOrEqual(2, 2);
    TestUtil.assertLessThanOrEqual(1, 2);

    TestUtil.assertLessThanOrEqual(2L, 1L);
    TestUtil.assertLessThanOrEqual(2L, 2L);
    TestUtil.assertLessThanOrEqual(1L, 2L);

    TestUtil.assertLessThanOrEqual(2.0f, 1.5f);
    TestUtil.assertLessThanOrEqual(2.0f, 2.0f);
    TestUtil.assertLessThanOrEqual(1.5f, 2.0f);

    TestUtil.assertLessThanOrEqual(2.0, 1.5);
    TestUtil.assertLessThanOrEqual(2.0, 2.0);
    TestUtil.assertLessThanOrEqual(1.5, 2.0);

    TestUtil.assertLessThanOrEqual("LessThanOrEqual1", 2, 1);
    TestUtil.assertLessThanOrEqual("LessThanOrEqual2", 2, 2);
    TestUtil.assertLessThanOrEqual("LessThanOrEqual3", 1, 2);

    TestUtil.assertLessThanOrEqual("LessThanOrEqual1", 2L, 1L);
    TestUtil.assertLessThanOrEqual("LessThanOrEqual2", 2L, 2L);
    TestUtil.assertLessThanOrEqual("LessThanOrEqual3", 1L, 2L);

    TestUtil.assertLessThanOrEqual("LessThanOrEqual1", 2.0f, 1.5f);
    TestUtil.assertLessThanOrEqual("LessThanOrEqual2", 2.0f, 2.0f);
    TestUtil.assertLessThanOrEqual("LessThanOrEqual3", 1.5f, 2.0f);

    TestUtil.assertLessThanOrEqual("LessThanOrEqual1", 2.0, 1.5);
    TestUtil.assertLessThanOrEqual("LessThanOrEqual2", 2.0, 2.0);
    TestUtil.assertLessThanOrEqual("LessThanOrEqual3", 1.5, 2.0);
  }

}
