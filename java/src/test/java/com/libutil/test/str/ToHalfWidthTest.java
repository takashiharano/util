package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.test.Log;

public class ToHalfWidthTest {

  public static void main(String args[]) {
    Log.i(StrUtil.toHalfWidth(
        "　！＂＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～“”‘'’〜￥"));
  }

}
