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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {

  private static final int BUF_SIZE = 1048576;

  private String zipPath;

  public ZipExtractor(String zipPath) {
    this.zipPath = zipPath;
  }

  public void extract(String destPath) {
    extract(destPath, null);
  }

  public void extract(String destPath, String targetItem) {
    if (!destPath.endsWith("/")) {
      destPath += "/";
    }
    try (FileInputStream fis = new FileInputStream(zipPath);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ZipInputStream zis = new ZipInputStream(bis);) {
      ZipEntry zipentry;
      while ((zipentry = zis.getNextEntry()) != null) {
        String itemName = zipentry.getName();
        if (targetItem == null) {
          out(zis, zipentry, destPath);
        } else if (itemName.equals(targetItem)) {
          out(zis, zipentry, destPath);
          break;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void out(ZipInputStream zis, ZipEntry zipentry, String destPath) {
    String itemName = zipentry.getName();
    String outPath = destPath + itemName;

    if (zipentry.isDirectory()) {
      mkdirs(outPath);
      return;
    } else {
      mkParentDir(outPath);
    }

    try (FileOutputStream fos = new FileOutputStream(outPath);
        BufferedOutputStream bos = new BufferedOutputStream(fos);) {
      byte[] data = new byte[BUF_SIZE];
      int len = 0;
      while ((len = zis.read(data)) != -1) {
        bos.write(data, 0, len);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void mkdirs(String path) {
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
    }
  }

  public void mkParentDir(String path) {
    File file = new File(path);
    String parentPath = file.getParent();
    mkdirs(parentPath);
  }

}
