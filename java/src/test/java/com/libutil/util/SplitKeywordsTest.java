package com.libutil.util;

import com.libutil.Log;
import com.libutil.Util;

public class SplitKeywordsTest {

  public static void main(String args[]) {
    String src = " 1 2 3 \"abc\" \"d ef\"  \"g\\\"hi\" (\"jkl\" + m) 'xyz' 'a\"b b\"a'";
    String[] keywords = Util.splitKeywords(src, 0);
    Log.i(src);
    Log.i(keywords);

    src = "";
    keywords = Util.splitKeywords(src, 0);
    Log.i(src);
    Log.i(keywords);

    src = " ";
    keywords = Util.splitKeywords(src, 0);
    Log.i(src);
    Log.i(keywords);

    src = null;
    keywords = Util.splitKeywords(src, 0);
    Log.i(src);
    Log.i(keywords);
  }

}
