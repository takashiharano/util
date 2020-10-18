package com.takashiharano.util.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompressor {

  private static final int BUF_SIZE = 1048576;

  private List<File> files;
  private String topLebelDirPath;

  public ZipCompressor() {
    files = new ArrayList<>();
  }

  public ZipCompressor(String inPath) {
    this();
    add(inPath);
  }

  public void add(String path) {
    File file = new File(path);
    add(file);
  }

  public void add(File file) {
    if (file.isDirectory()) {
      topLebelDirPath = file.getAbsolutePath();
      getAllFilesInDir(file, files);
    } else {
      files.add(file);
    }
  }

  public void compress(String destPath) {
    File destFile = new File(destPath);
    try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)));) {
      for (File file : files) {
        addZipEntry(zos, file);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void addZipEntry(ZipOutputStream zos, File file) throws IOException {
    String itemPath;
    if (topLebelDirPath == null) {
      itemPath = file.getName();
    } else {
      itemPath = file.getAbsolutePath().replace(topLebelDirPath + File.separator, "");
    }

    if (file.isDirectory()) {
      ZipEntry entry = new ZipEntry(itemPath + "/");
      zos.putNextEntry(entry);
      return;
    } else {
      ZipEntry entry = new ZipEntry(itemPath);
      zos.putNextEntry(entry);
    }

    try (InputStream is = new BufferedInputStream(new FileInputStream(file));) {
      byte[] buf = new byte[BUF_SIZE];
      int len = 0;
      while ((len = is.read(buf)) != -1) {
        zos.write(buf, 0, len);
      }
      is.close();
    }
  }

  private void getAllFilesInDir(File parentDir, List<File> files) {
    for (File file : parentDir.listFiles()) {
      files.add(file);
      if (file.isDirectory()) {
        getAllFilesInDir(file, files);
      }
    }
  }

}
