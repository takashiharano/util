package com.libutil.test.str;

import com.libutil.StrUtil;
import com.libutil.TestUtil;
import com.libutil.test.Log;

public class Rot47Test {

  public static void main(String args[]) {
    test1();
    test2();
    test3();
    test4();
  }

  private static void test1() {
    Log.i("Test1");
    TestUtil.assertEquals(" PQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNO", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"));
    TestUtil.assertEquals("%96 \"F:4< qC@H? u@I yF>AD ~G6C %96 {2KJ s@8]", StrUtil.rot47("The Quick Brown Fox Jumps Over The Lazy Dog."));
    TestUtil.assertEquals("2\r\n3", StrUtil.rot47("a\r\nb"));
    TestUtil.assertEquals("\t", StrUtil.rot47("\t"));
    TestUtil.assertEquals("", StrUtil.rot47(""));
    TestUtil.assertEquals("あ", StrUtil.rot47("あ"));
    TestUtil.assertEquals("2あ", StrUtil.rot47("aあ"));
    TestUtil.assertEquals(null, StrUtil.rot47(null));
  }

  private static void test2() {
    Log.i("Test2");
    TestUtil.assertEquals(" \"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~!", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 1));
    TestUtil.assertEquals("b\r\nc", StrUtil.rot47("a\r\nb", 1));
    TestUtil.assertEquals("\t", StrUtil.rot47("\t", 1));
    TestUtil.assertEquals("", StrUtil.rot47("", 1));
    TestUtil.assertEquals("あ", StrUtil.rot47("あ", 1));
    TestUtil.assertEquals("bあ", StrUtil.rot47("aあ", 1));
    TestUtil.assertEquals(null, StrUtil.rot47(null, 1));
  }

  private static void test3() {
    Log.i("Test3");
    TestUtil.assertEquals(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", StrUtil.rot47(" \"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~!", -1));
    TestUtil.assertEquals("a\r\nb", StrUtil.rot47("b\r\nc", -1));
    TestUtil.assertEquals("\t", StrUtil.rot47("\t", -1));
    TestUtil.assertEquals("", StrUtil.rot47("", -1));
    TestUtil.assertEquals("あ", StrUtil.rot47("あ", -1));
    TestUtil.assertEquals("aあ", StrUtil.rot47("bあ", -1));
    TestUtil.assertEquals(null, StrUtil.rot47(null, -1));
  }

  private static void test4() {
    Log.i("Test4");
    TestUtil.assertEquals(" ~!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", -95)); // -1
    TestUtil.assertEquals(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", -94)); // 0
    TestUtil.assertEquals(" \"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~!", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", -93)); // 1

    TestUtil.assertEquals(" ~!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", -1));
    TestUtil.assertEquals(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 0));
    TestUtil.assertEquals(" \"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~!", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 1));
    TestUtil.assertEquals(" #$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~!\"", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 2));

    TestUtil.assertEquals(" OPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMN", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 46));
    TestUtil.assertEquals(" PQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNO", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 47));
    TestUtil.assertEquals(" QRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOP", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 48));

    TestUtil.assertEquals(" ~!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 93)); // -1
    TestUtil.assertEquals(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 94)); // 0
    TestUtil.assertEquals(" \"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~!", StrUtil.rot47(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 95)); // 1
  }

}
