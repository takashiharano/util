package com.takashiharano.util.zip;

public class ZipUtil {

  /**
   * Compresses the files or directories into a ZIP file.
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
   * Compresses the files or directories into a ZIP file.
   *
   * @param inPath
   *          Target files/directories to compress
   * @param zipPath
   *          ZIP file path
   * @param junkPath
   *          Set true not to store the top directory name
   */
  public static void zip(String inPath, String zipPath, boolean junkPath) {
    ZipCompressor zip = new ZipCompressor(inPath, junkPath);
    zip.compress(zipPath);
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
