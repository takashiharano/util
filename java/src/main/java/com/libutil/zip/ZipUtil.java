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

/**
 * This class implements ZIP manipulations.
 */
public class ZipUtil {

  /**
   * Archives the files or directories into a ZIP file.
   *
   * @param inPath
   *          Target files/directories to compress
   * @param zipPath
   *          ZIP file path
   */
  public static void zip(String inPath, String zipPath) {
    zip(inPath, zipPath, false);
  }

  /**
   * Archives the files or directories into a ZIP file.
   *
   * @param inPath
   *          Target files/directories to compress
   * @param zipPath
   *          ZIP file path
   * @param junkPath
   *          Set true not to store the top directory name
   */
  public static void zip(String inPath, String zipPath, boolean junkPath) {
    ZipArchiver zip = new ZipArchiver(inPath, junkPath);
    zip.compressToFile(zipPath);
  }

  /**
   * Archives the files or directories into a ZIP file.
   *
   * @param inPaths
   *          The path list of the target files/directories to compress
   * @param zipPath
   *          ZIP file path
   */
  public static void zip(String[] inPaths, String zipPath) {
    ZipArchiver zip = new ZipArchiver(false);
    for (int i = 0; i < inPaths.length; i++) {
      zip.add(inPaths[i]);
    }
    zip.compressToFile(zipPath);
  }

  /**
   * Archives the files or directories, and returns the byte array.
   *
   * @param inPath
   *          Target files/directories to compress
   * @return byte array
   */
  public static byte[] zip(String inPath) {
    return zip(inPath, false);
  }

  /**
   * Archives the files or directories, and returns the byte array.
   *
   * @param inPath
   *          Target files/directories to compress
   * @param junkPath
   *          Set true not to store the top directory name
   * @return byte array
   */
  public static byte[] zip(String inPath, boolean junkPath) {
    ZipArchiver zip = new ZipArchiver(inPath, junkPath);
    return zip.compressOnMemory();
  }

  /**
   * Archives the files or directories, and returns the byte array.
   *
   * @param inPaths
   *          The list of the target files/directories to compress
   * @return byte array
   */
  public static byte[] zip(String[] inPaths) {
    ZipArchiver zip = new ZipArchiver(false);
    for (int i = 0; i < inPaths.length; i++) {
      zip.add(inPaths[i]);
    }
    return zip.compressOnMemory();
  }

  /**
   * Extracts the files or directories from a ZIP file.
   *
   * @param zipPath
   *          ZIP file path
   * @param destPath
   *          Output destination path
   */
  public static void unzip(String zipPath, String destPath) {
    ZipExtractor zip = new ZipExtractor(zipPath);
    zip.extract(destPath);
  }

  /**
   * Extracts a file or directory from a ZIP file.
   *
   * @param zipPath
   *          ZIP file path
   * @param itemPath
   *          Target item path to extract
   * @param destPath
   *          Output destination path
   */
  public static void unzip(String zipPath, String itemPath, String destPath) {
    ZipExtractor zip = new ZipExtractor(zipPath);
    zip.extract(destPath, itemPath);
  }

}
