package com.libutil.test.test;

import com.libutil.TestUtil;

public class AssertGreaterThanTest {

  public static void main(String args[]) {
    testAssertGreaterThan();
    testAssertGreaterThanOrEqual();
  }

  public static void testAssertGreaterThan() {
    TestUtil.assertGreaterThan(2, 1);
    TestUtil.assertGreaterThan(2, 2);
    TestUtil.assertGreaterThan(1, 2);

    TestUtil.assertGreaterThan(2L, 1L);
    TestUtil.assertGreaterThan(2L, 2L);
    TestUtil.assertGreaterThan(1L, 2L);

    TestUtil.assertGreaterThan(2.0f, 1.5f);
    TestUtil.assertGreaterThan(2.0f, 2.0f);
    TestUtil.assertGreaterThan(1.5f, 2.0f);

    TestUtil.assertGreaterThan(2.0, 1.5);
    TestUtil.assertGreaterThan(2.0, 2.0);
    TestUtil.assertGreaterThan(1.5, 2.0);

    TestUtil.assertGreaterThan("GreaterThan1", 2, 1);
    TestUtil.assertGreaterThan("GreaterThan2", 2, 2);
    TestUtil.assertGreaterThan("GreaterThan3", 1, 2);

    TestUtil.assertGreaterThan("GreaterThan1", 2L, 1L);
    TestUtil.assertGreaterThan("GreaterThan2", 2L, 2L);
    TestUtil.assertGreaterThan("GreaterThan3", 1L, 2L);

    TestUtil.assertGreaterThan("GreaterThan1", 2.0f, 1.5f);
    TestUtil.assertGreaterThan("GreaterThan2", 2.0f, 2.0f);
    TestUtil.assertGreaterThan("GreaterThan3", 1.5f, 2.0f);

    TestUtil.assertGreaterThan("GreaterThan1", 2.0, 1.5);
    TestUtil.assertGreaterThan("GreaterThan2", 2.0, 2.0);
    TestUtil.assertGreaterThan("GreaterThan3", 1.5, 2.0);
  }

  public static void testAssertGreaterThanOrEqual() {
    TestUtil.assertGreaterThanOrEqual(2, 1);
    TestUtil.assertGreaterThanOrEqual(2, 2);
    TestUtil.assertGreaterThanOrEqual(1, 2);

    TestUtil.assertGreaterThanOrEqual(2L, 1L);
    TestUtil.assertGreaterThanOrEqual(2L, 2L);
    TestUtil.assertGreaterThanOrEqual(1L, 2L);

    TestUtil.assertGreaterThanOrEqual(2.0f, 1.5f);
    TestUtil.assertGreaterThanOrEqual(2.0f, 2.0f);
    TestUtil.assertGreaterThanOrEqual(1.5f, 2.0f);

    TestUtil.assertGreaterThanOrEqual(2.0, 1.5);
    TestUtil.assertGreaterThanOrEqual(2.0, 2.0);
    TestUtil.assertGreaterThanOrEqual(1.5, 2.0);

    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual1", 2, 1);
    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual2", 2, 2);
    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual3", 1, 2);

    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual1", 2L, 1L);
    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual2", 2L, 2L);
    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual3", 1L, 2L);

    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual1", 2.0f, 1.5f);
    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual2", 2.0f, 2.0f);
    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual3", 1.5f, 2.0f);

    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual1", 2.0, 1.5);
    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual2", 2.0, 2.0);
    TestUtil.assertGreaterThanOrEqual("GreaterThanOrEqual3", 1.5, 2.0);
  }

}
