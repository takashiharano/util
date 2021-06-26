/*
 * The MIT License
 *
 * Copyright 2020 Takashi Harano
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.libutil.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchiver {

  private static final int BUF_SIZE = 1048576;

  private List<File> files;
  private String topLevelDirPath;
  private boolean junkPath; // don't record directory names
  private int level = 9; // 0-9

  public ZipArchiver() {
    files = new ArrayList<>();
  }

  public ZipArchiver(String inPath) {
    this();
    add(inPath);
  }

  public ZipArchiver(String inPath, boolean junkPath) {
    this();
    this.junkPath = junkPath;
    add(inPath);
  }

  public ZipArchiver(boolean junkPath) {
    this();
    this.junkPath = junkPath;
  }

  public void add(String path) {
    File file = new File(path);
    add(file);
  }

  public void add(File file) {
    if (file.isDirectory()) {
      if (junkPath) {
        topLevelDirPath = file.getAbsolutePath();
        if (!topLevelDirPath.endsWith(File.separator)) {
          topLevelDirPath += File.separator;
        }
      } else {
        topLevelDirPath = file.getParent();
        files.add(file);
      }
      getAllFilesInDir(files, file);
    } else {
      files.add(file);
    }
  }

  public void compressToFile(String destPath) {
    File destFile = new File(destPath);
    try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)));) {
      if (level != -1) {
        zos.setLevel(level);
      }
      for (File file : files) {
        addZipEntry(zos, file);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public byte[] compressOnMemory() {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ZipOutputStream zos = new ZipOutputStream(out);) {
      if (level != -1) {
        zos.setLevel(level);
      }
      for (File file : files) {
        addZipEntry(zos, file);
      }
      zos.close();
      out.close();
      byte[] b = out.toByteArray();
      return b;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setLevel(int level) {
    if (level < 0) {
      level = -1;
    } else if (level > 9) {
      level = 9;
    }
    this.level = level;
  }

  private void addZipEntry(ZipOutputStream zos, File file) throws IOException {
    String sepChar = File.separator;
    String itemPath;
    if (topLevelDirPath == null) {
      itemPath = file.getName();
    } else {
      itemPath = file.getAbsolutePath().replace(topLevelDirPath, "");
    }

    String entryName;
    if (file.isDirectory()) {
      entryName = itemPath + sepChar;
    } else {
      entryName = itemPath;
    }
    if (entryName.startsWith(sepChar)) {
      entryName = entryName.substring(1);
    }
    ZipEntry entry = new ZipEntry(entryName);

    long lastModified = file.lastModified();
    entry.setTime(lastModified);
    zos.putNextEntry(entry);

    if (!file.isDirectory()) {
      try (InputStream is = new BufferedInputStream(new FileInputStream(file));) {
        byte[] buf = new byte[BUF_SIZE];
        int len = 0;
        while ((len = is.read(buf)) != -1) {
          zos.write(buf, 0, len);
        }
        is.close();
      }
    }
    zos.closeEntry();
  }

  private void getAllFilesInDir(List<File> files, File parentDir) {
    for (File file : parentDir.listFiles()) {
      files.add(file);
      if (file.isDirectory()) {
        getAllFilesInDir(files, file);
      }
    }
  }

}
