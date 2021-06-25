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
