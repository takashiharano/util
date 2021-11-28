package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.TestUtil;

public class GetFileNameTest {

  public static void main(String args[]) {
    TestUtil.assertEquals("abc.txt", FileUtil.getFileName("C:/test/abc.txt"));
  }

}
