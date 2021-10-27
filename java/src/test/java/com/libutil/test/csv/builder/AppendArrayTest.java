package com.libutil.test.csv.builder;

import java.util.ArrayList;
import java.util.List;

import com.libutil.CsvBuilder;
import com.libutil.TestUtil;

public class AppendArrayTest {

  public static void main(String args[]) {
    test();
  }

  private static void test() {
    CsvBuilder builder;

    String[] sArr = { "abc", "xyz" };
    builder = new CsvBuilder();
    builder.append(sArr);
    TestUtil.assertEquals("abc,xyz", builder);

    List<String> sList = new ArrayList<>();
    sList.add("abc");
    sList.add("xyz");
    builder = new CsvBuilder();
    builder.append(sList);
    TestUtil.assertEquals("abc,xyz", builder);

    int[] iArr = { 1, 2 };
    builder = new CsvBuilder();
    builder.append(iArr);
    TestUtil.assertEquals("1,2", builder);

    long[] lArr = { 1L, 2L };
    builder = new CsvBuilder();
    builder.append(lArr);
    TestUtil.assertEquals("1,2", builder);

    float[] fArr = { 1.5f, 2.1f };
    builder = new CsvBuilder();
    builder.append(fArr);
    TestUtil.assertEquals("1.5,2.1", builder);

    double[] dArr = { 1.5, 2.1 };
    builder = new CsvBuilder();
    builder.append(dArr);
    TestUtil.assertEquals("1.5,2.1", builder);

    boolean[] bArr = { true, false };
    builder = new CsvBuilder();
    builder.append(bArr);
    TestUtil.assertEquals("true,false", builder);
  }

}
