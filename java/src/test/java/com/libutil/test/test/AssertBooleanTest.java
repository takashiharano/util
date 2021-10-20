package com.libutil.test.test;

import com.libutil.TestUtil;

public class AssertBooleanTest {

  public static void main(String args[]) {
    test();
  }

  public static void test() {
    TestUtil.assertTrue(true);
    TestUtil.assertTrue(false);
    TestUtil.assertTrue("True1", true);
    TestUtil.assertTrue("True2", false);

    TestUtil.assertFalse(false);
    TestUtil.assertFalse(true);
    TestUtil.assertFalse("False1", false);
    TestUtil.assertFalse("False2", true);
  }

}
