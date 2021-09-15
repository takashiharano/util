package com.libutil.test.file;

import com.libutil.FileUtil;
import com.libutil.test.Log;

public class JoinPathest {

  public static void main(String args[]) {
    test("C:/test", "a.txt");
    test("C:/test/", "a.txt");
    test("C:/path/to", "a.txt");
    test("C:/path/to/", "a.txt");
    test("C:", "/test/a.txt");

    test("/test", "a.txt");
    test("/test", "../a.txt");
    test("/test", "./a.txt");

    test("/path/to", "a.txt");
    test("/path/to", "../a.txt");
    test("/path/to", "./a.txt");

    test("a.txt", "b.txt");
    test(".", "a.txt");
    test("a.txt", ".");
    test(".", ".");

    test(".", "");
    test("", ".");
    test("", "");
  }

  private static void test(String path1, String path2) {
    String path = FileUtil.joinPath(path1, path2);
    Log.i("path1=" + path1 + " path2=" + path2 + " path=" + path);
  }

}
