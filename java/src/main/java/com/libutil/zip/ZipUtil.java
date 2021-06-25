package com.libutil.zip;

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
