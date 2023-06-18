package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class SplitKeywordsTest {

  public static void main(String args[]) {
    test("aaa");
    test("aaa bbb");
    test("aaa bbb ccc");

    test("\"aaa\"");
    test("\"aaa bbb\"");
    test("\"aaa bbb\" ccc");
    test("aaa \"bbb ccc\"");
    test("aaa \"bbb ccc\" ddd");
    test("aaa \"bbb\"");
    test("aaa \"bbb\" ccc");

    test("aaa:\"bbb ccc\"");
    test("aaa bbb:\"ccc ddd\"");
    test("aaa bbb:\"ccc ddd\" eee");

    test(" 1 2 3 \"abc\" \"d ef\"  \"g\\\"hi\" (\"jkl\" + m) 'xyz' 'a\"b b\"a'");
    test("");
    test(" ");
    test(null);
  }

  private static void test(String src) {
    String[] keywords = StrUtil.splitKeywords(src, 0);
    Log.i("----------");
    Log.i("src=[" + src + "]");
    for (int i = 0; i < keywords.length; i++) {
      Log.i("[" + i + "] " + keywords[i]);
    }
  }

}
