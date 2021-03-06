package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class SplitKeywordsTest {

  public static void main(String args[]) {
    test("abc");
    test("abc xyz");
    test("abc xyz 123");
    test(" 1 2 3 \"abc\" \"d ef\"  \"g\\\"hi\" (\"jkl\" + m) 'xyz' 'a\"b b\"a'");
    test("");
    test(" ");
    test(null);
  }

  private static void test(String src) {
    String[] keywords = StrUtil.splitKeywords(src, 0);
    Log.i("----------");
    Log.i(src);
    Log.i(keywords);
  }

}
