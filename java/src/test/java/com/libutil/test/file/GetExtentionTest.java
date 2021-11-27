package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.TestUtil;

public class GetExtentionTest {

  public static void main(String args[]) {
    TestUtil.assertEquals("txt", FileUtil.getExtension("abc.txt"));
    TestUtil.assertEquals("txt", FileUtil.getExtension("C:/test/abc.txt"));
    TestUtil.assertEquals("txt", FileUtil.getExtension("./abc.txt"));
    TestUtil.assertEquals("txt", FileUtil.getExtension("../abc.txt"));
    TestUtil.assertEquals("", FileUtil.getExtension("abc"));
    TestUtil.assertEquals("", FileUtil.getExtension("C:/test/abc"));
    TestUtil.assertEquals("", FileUtil.getExtension("C:/test/abc/"));
    TestUtil.assertEquals("", FileUtil.getExtension("./abc"));
    TestUtil.assertEquals("", FileUtil.getExtension("./abc/"));
    TestUtil.assertEquals("", FileUtil.getExtension(".abc"));
  }

}
