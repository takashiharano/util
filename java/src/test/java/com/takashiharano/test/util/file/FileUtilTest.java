package com.takashiharano.test.util.file;

import java.io.IOException;

import com.takashiharano.util.FileUtil;
import com.takashiharano.util.Log;

public class FileUtilTest {

  public static void main(String args[]) {
    readBinaryFileTest();
    readTextFileTest();
    // readTextFileTest10000();
    // readTextFileTest10000AsArray();
    // readFileArrayTest();
    readFileAsBase64Test();
    // writeFileTest();
    // getParentPathTest();
    listFileNamesTest();
    listDirNamesTest();
    listDirFileNamesTest();
    // getFileNameTest();
    // getExtensionTest();
  }

  private static void readBinaryFileTest() {
    String path = "C:/test/a.txt";
    byte[] b = FileUtil.read(path);
    Log.d(b);
  }

  private static void readTextFileTest() {
    String path = "C:/test/a.txt";
    String str = FileUtil.readText(path);
    Log.d(str);
  }

  private static void readTextFileTest10000() {
    String path = "C:/test/100000lines.txt";
    long t1 = System.currentTimeMillis();
    String str = FileUtil.readText(path); // 160-170ms
    // String str = FileUtil.readTextFile(path, "UTF-8"); // 30ms
    long t2 = System.currentTimeMillis();
    Log.d("t=" + (t2 - t1));
  }

  private static void readTextFileTest10000AsArray() {
    String path = "C:/test/100000lines.txt";
    long t1 = System.currentTimeMillis();
    String[] str = FileUtil.readTextAsArray(path); // 65ms
    long t2 = System.currentTimeMillis();
    Log.d("t=" + (t2 - t1));
  }

  private static void readFileArrayTest() {
    Log.i("UTF-8");
    _readFileArrayTest("C:/test/a.txt");
    Log.i("");

    Log.i("UTF-8-BOM");
    _readFileArrayTest("C:/test/a_utf8_bom.txt");
    Log.i("");

    Log.i("UTF-16BE-BOM");
    _readFileArrayTest("C:/test/a_utf16_be_bom.txt");
    Log.i("");

    Log.i("UTF-16LE-BOM");
    _readFileArrayTest("C:/test/a_utf16_le_bom.txt");
    Log.i("");

    Log.i("Shift_JIS");
    _readFileArrayTest("C:/test/a_sjis.txt");
    Log.i("");

    Log.i("EUC");
    _readFileArrayTest("C:/test/a_euc.txt");
    Log.i("");

    Log.i("--- not supported ---");
    Log.i("UTF-16BE");
    _readFileArrayTest("C:/test/a_utf16_be.txt");
    Log.i("");

    Log.i("UTF-16LE");
    _readFileArrayTest("C:/test/a_utf16_le.txt");
    Log.i("");

    Log.i("BIG5");
    _readFileArrayTest("C:/test/a_big5.txt");
    Log.i("");

    Log.i("GB2312");
    _readFileArrayTest("C:/test/a_gb2312.txt");
    Log.i("");
  }

  private static void _readFileArrayTest(String path) {
    byte[] bytes = FileUtil.read(path);
    String charsetName = FileUtil.getCharsetName(bytes);
    Log.d("charset=" + charsetName);
    String[] text = FileUtil.readTextAsArray(path, charsetName);
    if (text == null) {
      Log.e("READ_ERROOR");
      return;
    }
    for (int i = 0; i < text.length; i++) {
      Log.d((i + 1) + ": " + text[i]);
    }
  }

  private static void readFileAsBase64Test() {
    String path = "C:/test/a.txt";
    String str = FileUtil.readAsBase64(path);
    Log.d(str);
  }

  private static void mkdirTest() {
    boolean created = FileUtil.mkdir("C:/test/a/b/c/x.txt");
    Log.d(created);
  }

  private static void getParentPathTest() {
    String parent = FileUtil.getParentPath("C:/test/a/b/c/x.txt");
    Log.d(parent);
  }

  private static void writeFileTest() {
    String parent = "C:/test/";
    String path1 = parent + "file1.txt";
    String content1 = "abc\nあいうえお\n华语";
    try {
      FileUtil.write(path1, content1);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String path2 = parent + "file2.txt";
    byte[] content2 = { 97, 98, 99 };
    try {
      FileUtil.write(path2, content2);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Log.d("done");
  }

  private static void listFileNamesTest() {
    Log.d("Files ----------");
    String[] files = FileUtil.listFileNames("C:/test/");
    for (int i = 0; i < files.length; i++) {
      Log.d(i + ": " + files[i]);
    }
  }

  private static void listDirNamesTest() {
    Log.d("Dirs ----------");
    String[] files = FileUtil.listDirNames("C:/test/");
    for (int i = 0; i < files.length; i++) {
      Log.d(i + ": " + files[i]);
    }
  }

  private static void listDirFileNamesTest() {
    Log.d("Dirs & Files ----------");
    String[] files = FileUtil.listDirFileNames("C:/test/");
    for (int i = 0; i < files.length; i++) {
      Log.d(i + ": " + files[i]);
    }
  }

  private static void getFileNameTest() {
    Log.d(FileUtil.getFileName("C:/test/abc.txt"));
  }

  private static void getExtensionTest() {
    Log.d(FileUtil.getExtension("abc.txt"));
    Log.d(FileUtil.getExtension("C:/test/abc.txt"));
    Log.d(FileUtil.getExtension("abc"));
    Log.d(FileUtil.getExtension("C:/test/abc"));
    Log.d(FileUtil.getExtension("C:/test/abc/"));
    Log.d(FileUtil.getExtension("./abc.txt"));
    Log.d(FileUtil.getExtension("../abc.txt"));
    Log.d(FileUtil.getExtension("./abc"));
    Log.d(FileUtil.getExtension("./abc/"));
    Log.d(FileUtil.getExtension(".abc"));
  }

}
