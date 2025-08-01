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
package com.libutil;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * File and directory manipulation.
 */
public class FileUtil {

  public static final String DEFAULT_CHARSET = "UTF-8";
  public static String LINE_SEPARATOR = "\n";

  public static final byte[] BOM_UTF8 = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
  public static final byte[] BOM_UTF16BE = { (byte) 0xFE, (byte) 0xFF };
  public static final byte[] BOM_UTF16LE = { (byte) 0xFF, (byte) 0xFE };
  public static final byte[] BOM_UTF32BE = { (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF };
  public static final byte[] BOM_UTF32LE = { (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00 };

  /**
   * Append a line to text file.
   *
   * @param path
   *          file path
   * @param newLine
   *          new line
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void appendLine(String path, String newLine) throws IOException {
    appendLine(path, newLine, 0);
  }

  /**
   * Append a line to text file.
   *
   * @param path
   *          file path
   * @param newLine
   *          new line
   * @param maxLines
   *          number of lines to limit
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void appendLine(String path, String newLine, int maxLines) throws IOException {
    String[] lines = readTextAsArray(path);
    int start = 0;
    if ((maxLines > 0) && (lines.length >= maxLines)) {
      start = lines.length - (maxLines - 1);
    }

    StringBuilder newContent = new StringBuilder();
    for (int i = start; i < lines.length; i++) {
      newContent.append(lines[i] + LINE_SEPARATOR);
    }

    newContent.append(newLine + LINE_SEPARATOR);
    write(path, newContent.toString());
  }

  /**
   * Check if the given bytes corresponding to the given charset.
   *
   * @param src
   *          string byte array
   * @param charsetName
   *          charset name
   * @return true if the byte array matches the charset
   */
  public static boolean checkCharset(byte[] src, String charsetName) {
    try {
      byte[] tmp = new String(src, charsetName).getBytes(charsetName);
      return Arrays.equals(tmp, src);
    } catch (UnsupportedEncodingException e) {
      return false;
    }
  }

  /**
   * Copies the file to the destination path.
   *
   * @param src
   *          the file path to copy. e.g., "/path/to/file.ext"
   * @param dest
   *          the destination path. e.g., "/path/to/destdir/
   * @return true if and only if the copying succeeded; false otherwise
   */
  public static boolean copy(File src, String dest) {
    String from = src.getAbsolutePath();
    return copy(from, dest, false);
  }

  /**
   * Copies the file to the destination path.
   *
   * @param src
   *          the file path to copy. e.g., "/path/to/file.ext"
   * @param dest
   *          the destination path. e.g., "/path/to/destdir/
   * @param replaceExisting
   *          if true, replaces the existing path
   * @return true if and only if the copying succeeded; false otherwise
   */
  public static boolean copy(File src, String dest, boolean replaceExisting) {
    String from = src.getAbsolutePath();
    return copy(from, dest, replaceExisting);
  }

  /**
   * Copies the file to the destination path.
   *
   * @param src
   *          the file path to copy. e.g., "/path/to/file.ext"
   * @param dest
   *          the destination path. e.g., "/path/to/destdir/
   * @return true if and only if the copying succeeded; false otherwise
   */
  public static boolean copy(String src, String dest) {
    return copy(src, dest, false);
  }

  /**
   * Copies the file to the destination path.
   *
   * @param src
   *          the file path to copy. e.g., "/path/to/file.ext"
   * @param dest
   *          the destination path. e.g., "/path/to/destdir/
   * @param replaceExisting
   *          if true, replaces the existing path
   * @return true if and only if the copying succeeded; false otherwise
   */
  public static boolean copy(String src, String dest, boolean replaceExisting) {
    File srcFile = new File(src);
    File destFile = new File(dest);

    if (srcFile.isDirectory()) {
      dest = dest.replace("\\", "/");
      if (destFile.isDirectory() || dest.endsWith("/")) {
        dest = joinPath(dest, srcFile.getName());
        mkParentDir(dest);
      }
    }

    return _copy(src, dest, replaceExisting);
  }

  private static boolean _copy(String src, String dest, boolean replaceExisting) {
    File srcFile = new File(src);
    File destFile = new File(dest);

    boolean hasError = false;

    if (srcFile.isDirectory()) {
      if (destFile.isFile()) {
        return false;
      }

      String srcDirAbsPath = srcFile.getAbsolutePath();

      String[] fileNameList = listDirFileNames(src);
      for (int i = 0; i < fileNameList.length; i++) {
        String fileName = fileNameList[i];
        String srcFilePath = joinPath(srcDirAbsPath, fileName);
        String destFilePath = joinPath(dest, fileName);
        boolean copied = _copy(srcFilePath, destFilePath, replaceExisting);
        if (!copied) {
          hasError = true;
        }
      }

      return (hasError ? false : true);
    } else {
      return copyOne(src, dest, replaceExisting);
    }
  }

  private static boolean copyOne(String src, String dest, boolean replaceExisting) {
    File srcFile = new File(src);
    File destFile = new File(dest);

    boolean hasError = false;
    dest = dest.replace("\\", "/");
    if (destFile.isDirectory() || dest.endsWith("/")) {
      String srcFileName = srcFile.getName();
      dest = joinPath(dest, srcFileName);
    }

    Path from = Paths.get(src);
    Path to = Paths.get(dest);

    mkParentDir(dest);
    boolean copied = copyFile(from, to, replaceExisting);
    boolean ret = false;
    if (copied && !hasError) {
      ret = true;
    }

    return ret;
  }

  private static boolean copyFile(Path from, Path to, boolean replaceExisting) {
    try {
      if (replaceExisting) {
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
      } else {
        Files.copy(from, to);
      }
    } catch (FileAlreadyExistsException fae) {
      return false;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  /**
   * Creates a new empty file.
   *
   * @param path
   *          the new file path
   * @return true if the named file does not exist and was successfully created;
   *         false if the named file already exists or failed to create
   */
  public static boolean create(String path) {
    File file = new File(path);
    return create(file);
  }

  /**
   * Creates a new empty file.
   *
   * @param path
   *          the new file path
   * @param createParent
   *          if true, create the parent directories
   * @return true if the named file does not exist and was successfully created;
   *         false if the named file already exists or failed to create
   */
  public static boolean create(String path, boolean createParent) {
    File file = new File(path);
    return create(file, createParent);
  }

  /**
   * Creates a new empty file.
   *
   * @param file
   *          the new file path
   * @return true if the named file does not exist and was successfully created;
   *         false if the named file already exists or failed to create
   */
  public static boolean create(File file) {
    return create(file, false);
  }

  /**
   * Creates a new empty file.
   *
   * @param file
   *          the new file path
   * @param createParent
   *          if true, create the parent directories
   * @return true if the named file does not exist and was successfully created;
   *         false if the named file already exists or failed to create
   */
  public static boolean create(File file, boolean createParent) {
    if (createParent) {
      mkParentDir(file);
    }
    try {
      return file.createNewFile();
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Base64 file to file decoder.
   *
   * @param srcPath
   *          Base64 text file path
   * @param destPath
   *          file path to save the decoded file
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void decodeBase64FileToFile(String srcPath, String destPath) throws IOException {
    String b64 = FileUtil.readText(srcPath);
    if (b64 != null) {
      b64 = b64.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
      FileUtil.writeFromBase64(destPath, b64);
    }
  }

  /**
   * Deletes the file or directory.<br>
   * If this pathname denotes a directory, then the directory must be empty in
   * order to be deleted.
   *
   * @param path
   *          the path of file or directory
   * @return true if and only if the file or directory is successfully deleted;
   *         false otherwise
   */
  public static boolean delete(String path) {
    File file = new File(path);
    boolean deleted = file.delete();
    return deleted;
  }

  /**
   * Deletes the file or directory.
   *
   * @param path
   *          the file or directory path to delete
   * @param recursive
   *          If true, delete recursive
   * @return true if all files are deleted; false otherwise
   */
  public static boolean delete(String path, boolean recursive) {
    File file = new File(path);
    return delete(file, recursive);
  }

  /**
   * Deletes the file or directory.
   *
   * @param file
   *          the file object to delete
   * @param recursive
   *          If true, delete recursive
   * @return true if all files are deleted; false otherwise
   */
  public static boolean delete(File file, boolean recursive) {
    boolean allDeleted = true;
    if (recursive && file.isDirectory()) {
      File[] files = file.listFiles();
      if (files != null) {
        for (int i = 0; i < files.length; i++) {
          File f = files[i];
          if (f.isDirectory()) {
            delete(f, recursive);
          } else {
            if (!f.delete()) {
              allDeleted = false;
            }
          }
        }
      }
      if (!file.delete()) {
        allDeleted = false;
      }
    } else {
      allDeleted = file.delete();
    }
    return allDeleted;
  }

  /**
   * Base64 file to file encoder.
   *
   * @param srcPath
   *          file path to encode
   * @param destPath
   *          file path to save the Base64 encoded string
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void encodeBase64FileToFile(String srcPath, String destPath) throws IOException {
    encodeBase64FileToFile(srcPath, destPath, 76);
  }

  /**
   * Base64 file to file encoder.
   *
   * @param srcPath
   *          file path to encode
   * @param destPath
   *          file path to save the Base64 encoded string
   * @param newlinePosition
   *          newline position. 0=do not insert newline
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void encodeBase64FileToFile(String srcPath, String destPath, int newlinePosition) throws IOException {
    String b64 = FileUtil.readAsBase64(srcPath);
    if (b64 == null) {
      return;
    }
    if (newlinePosition > 0) {
      b64 = insertNewLine(b64, newlinePosition);
    }
    FileUtil.write(destPath, b64);
  }

  private static String insertNewLine(String str, int pos) {
    StringBuilder sb = new StringBuilder();
    int p = 0;
    while (p < str.length()) {
      int endIndex = p + pos;
      if (endIndex >= str.length()) {
        endIndex = str.length();
      }
      String s = str.substring(p, endIndex);
      sb.append(s);
      sb.append("\r\n");
      p += pos;
    }
    return sb.toString();
  }

  /**
   * Tests whether the file or directory denoted by this abstract path name
   * exists.
   *
   * @param path
   *          the path of file or directory
   * @return true if and only if the file or directory denoted by this abstract
   *         pathname exists; false otherwise (including the case of null)
   */
  public static boolean exists(String path) {
    if (path == null) {
      return false;
    }
    File file = new File(path);
    return file.exists();
  }

  /**
   * Returns the name of the file or directory denoted by the given pathname.<br>
   * <br>
   * e.g., "C:/test/abc.txt" to "abc.txt"
   *
   * @param path
   *          file path
   * @return file name
   */
  public static String getFileName(String path) {
    File file = new File(path);
    String name = file.getName();
    return name;
  }

  /**
   * Returns the file size.
   *
   * @param path
   *          file path
   * @return file size. 0L if the file is directory or file does not exist.
   */
  public static long getFileSize(String path) {
    File file = new File(path);
    return getFileSize(file);
  }

  /**
   * Returns the file size.
   *
   * @param file
   *          the file object
   * @return file size. 0L if the file is directory or file does not exist.
   */
  public static long getFileSize(File file) {
    if (file.exists() && file.isFile()) {
      return file.length();
    } else {
      return 0;
    }
  }

  /**
   * Returns the name of extension.<br>
   * <br>
   * e.g., "C:/test/abc.txt" to "txt"
   *
   * @param path
   *          file path (absolute or related)
   * @return file extension
   */
  public static String getExtension(String path) {
    String extension = "";
    int i = path.lastIndexOf('.');
    if (i > 0) {
      extension = path.substring(i + 1);
    }
    return extension;
  }

  /**
   * Detects charset and returns its name.
   *
   * @param src
   *          string byte array
   * @return charset name
   */
  public static String getCharsetName(byte[] src) {
    if (src.length >= 3) {
      if ((src[0] == (byte) 0xFE) && (src[1] == (byte) 0xFF)) {
        // big-endian
        return "UTF-16BE";
      } else if ((src[0] == (byte) 0xFF) && (src[1] == (byte) 0xFE)) {
        // little-endian
        return "UTF-16LE";
      }
    }

    String[] CHARSET_NAMES = { "UTF-16", "UTF-8", "EUC_JP", "SJIS" };
    for (int i = 0; i < CHARSET_NAMES.length; i++) {
      String charsetName = CHARSET_NAMES[i];
      if (checkCharset(src, charsetName)) {
        return charsetName;
      }
    }
    return null;
  }

  /**
   * Returns the current path.
   *
   * @return the current path
   */
  public static String getCurrentPath() {
    String path = new File(".").getAbsoluteFile().getParent();
    return path;
  }

  /**
   * Returns hash value of the file content.
   *
   * @param path
   *          file path to read
   * @param algorithm
   *          hash algorithm (MD5 / SHA-1 / SHA-256 / SHA-512)
   * @return hash value
   */
  public static String getHash(String path, String algorithm) {
    File file = new File(path);
    return getHash(file, algorithm);
  }

  /**
   * Returns hash value of the file content.
   *
   * @param file
   *          file to read
   * @param algorithm
   *          hash algorithm (MD5 / SHA-1 / SHA-256 / SHA-512)
   * @return hash value
   */
  public static String getHash(File file, String algorithm) {
    byte[] b = FileUtil.read(file);
    if (b == null) {
      return null;
    }
    String hash = null;
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      byte[] h = md.digest(b);
      hash = toHexString(h);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    return hash;
  }

  /**
   * Returns the pathname string of this abstract pathname's parent, or null if
   * this pathname does not name a parent directory. The parent of an abstract
   * pathname consists of the pathname's prefix, if any, and each name in the
   * pathname's name sequence except for the last. If the name sequence is empty
   * then the pathname does not name a parent directory.
   *
   * e.g., "a/b/c.txt" to "/a/b"
   *
   * @param path
   *          file path
   * @return parent path
   */
  public static String getParentPath(String path) {
    File file = new File(path);
    return getParentPath(file);
  }

  /**
   * Returns parent path of the file.<br>
   * e.g., "a/b/c.txt" to "/a/b"
   *
   * @param file
   *          an file object
   * @return parent path
   */
  public static String getParentPath(File file) {
    return file.getParent();
  }

  /**
   * Joins the specified paths by appropriately padding them with "/"
   *
   * @param path1
   *          the first path segment to join
   * @param path2
   *          the second path segment to join
   * @return the concatenated path with "/"
   */
  public static String joinPath(String path1, String path2) {
    path1 = path1.replace("\\", "/").replaceAll("/$", "");
    path2 = path2.replace("\\", "/").replaceAll("^/", "");
    String path = path1 + "/" + path2;
    return path;
  }

  /**
   * Returns an array of abstract pathnames denoting the files in the directory
   * denoted by this abstract pathname. If this abstract pathname does not denote
   * a directory, then this method returns null. Otherwise an array of File
   * objects is returned, one for each file or directory in the directory.
   * Pathnames denoting the directory itself and the directory's parent directory
   * are not included in the result. Each resulting abstract pathname is
   * constructed from this abstract pathname using the File(File, String)
   * constructor. Therefore if this pathname is absolute then each resulting
   * pathname is absolute; if this pathname is relative then each resulting
   * pathname will be relative to the same directory.
   * 
   * There is no guarantee that the name strings in the resulting array will
   * appear in any specific order; they are not, in particular, guaranteed to
   * appear in alphabetical order.
   * 
   * @param path
   *          the target path
   * @return an file object array
   */
  public static File[] listFiles(String path) {
    File file = new File(path);
    return file.listFiles();
  }

  /**
   * Returns an array of strings naming the directories in the directory.
   *
   * @param dirPath
   *          the directory path
   * @return an array of strings naming the directories
   */
  public static String[] listDirNames(String dirPath) {
    File dir = new File(dirPath);
    if (!dir.exists() || !dir.isDirectory()) {
      return new String[0];
    }
    File[] files = listFiles(dirPath);
    List<String> names = new ArrayList<>();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      if (file.isDirectory()) {
        names.add(file.getName());
      }
    }
    String[] dirNames = new String[names.size()];
    names.toArray(dirNames);
    return dirNames;
  }

  /**
   * Returns an array of strings naming the files in the directory.
   *
   * @param dirPath
   *          the target path
   * @return an array of strings naming the files
   */
  public static String[] listFileNames(String dirPath) {
    File dir = new File(dirPath);
    if (!dir.exists() || !dir.isDirectory()) {
      return new String[0];
    }
    File[] files = listFiles(dirPath);
    List<String> names = new ArrayList<>();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      if (file.isFile()) {
        names.add(file.getName());
      }
    }
    String[] fileNames = new String[names.size()];
    names.toArray(fileNames);
    return fileNames;
  }

  /**
   * Returns an array of strings naming the files and directories in the
   * directory.
   * 
   * @param dirPath
   *          the target path
   * @return an array of strings naming the files and directories
   */
  public static String[] listDirFileNames(String dirPath) {
    File dir = new File(dirPath);
    if (!dir.exists() || !dir.isDirectory()) {
      return new String[0];
    }
    File[] files = listFiles(dirPath);
    List<String> names = new ArrayList<>();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      names.add(file.getName());
    }
    String[] fileNames = new String[names.size()];
    names.toArray(fileNames);
    return fileNames;
  }

  /**
   * Creates the directory named by this abstract pathname, including any
   * necessary but nonexistent parent directories. Note that if this operation
   * fails it may have succeeded in creating some of the necessary parent
   * directories.
   *
   * @param path
   *          the path of directory
   * @return true if and only if the directory was created,along with all
   *         necessary parent directories; false otherwise
   */
  public static boolean mkdir(String path) {
    File file = new File(path);
    boolean created = file.mkdirs();
    return created;
  }

  /**
   * Creates the parent directories.
   *
   * @param path
   *          the path of directory
   * @return true if and only if the directory was created
   */
  public static boolean mkParentDir(String path) {
    File file = new File(path);
    return mkParentDir(file);
  }

  /**
   * Creates the parent directories.
   *
   * @param file
   *          the target file
   * @return true if and only if the directory was created
   */
  public static boolean mkParentDir(File file) {
    boolean created = false;
    String parent = getParentPath(file);
    if ((parent != null) && !exists(parent)) {
      created = mkdir(parent);
    }
    return created;
  }

  /**
   * Moves the file to the destination path.
   *
   * @param src
   *          the file path to move. e.g., "/path/to/file.ext"
   * @param dest
   *          the destination path. e.g., "path/to/destdir
   * @return true if and only if the moving succeeded; false otherwise
   */
  public static boolean move(String src, String dest) {
    return move(src, dest, false);
  }

  /**
   * Moves the file to the destination path.
   *
   * @param src
   *          the file path to move. e.g., "/path/to/file.ext"
   * @param dest
   *          the destination path. e.g., "path/to/destdir
   * @param replaceExisting
   *          if true, replaces the existing path
   * @return true if and only if the moving succeeded; false otherwise
   */
  public static boolean move(String src, String dest, boolean replaceExisting) {
    if ("".equals(getExtension(dest))) {
      String fileName = getFileName(src);
      dest = joinPath(dest, fileName);
    }
    Path from = Paths.get(src);
    Path to = Paths.get(dest);

    mkParentDir(dest);

    boolean moved = false;
    try {
      if (replaceExisting) {
        Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
      } else {
        Files.move(from, to);
      }
      moved = true;
    } catch (FileAlreadyExistsException fae) {
      // nop
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return moved;
  }

  /**
   * Moves the file to the destination path.
   *
   * @param src
   *          the file to move
   * @param dest
   *          the destination path. e.g., "path/to/destdir
   * @return true if and only if the moving succeeded; false otherwise
   */
  public static boolean move(File src, String dest) {
    return move(src, dest, false);
  }

  /**
   * Moves the file to the destination path.
   *
   * @param src
   *          the file to move
   * @param dest
   *          the destination path. e.g., "path/to/destdir
   * @param replaceExisting
   *          if true, replaces the existing path
   * @return true if and only if the moving succeeded; false otherwise
   */
  public static boolean move(File src, String dest, boolean replaceExisting) {
    String from = src.getAbsolutePath();
    return move(from, dest, replaceExisting);
  }

  /**
   * Read a binary file.
   *
   * @param path
   *          file path
   * @return byte array of the file content. Returns null if the file does not
   *         exist, or in case of a read error.
   */
  public static byte[] read(String path) {
    File file = new File(path);
    return read(file);
  }

  /**
   * Read a binary file.
   *
   * @param file
   *          the file object
   * @return byte array of the file content. Returns null if the file does not
   *         exist, or in case of a read error.
   */
  public static byte[] read(File file) {
    if (!file.exists()) {
      return null;
    }
    byte[] content = new byte[(int) file.length()];
    try (FileInputStream fis = new FileInputStream(file)) {
      @SuppressWarnings("unused")
      int readSize = fis.read(content, 0, content.length);
    } catch (IOException ioe) {
      content = null;
    }
    return content;
  }

  /**
   * Read a text file.
   *
   * @param path
   *          file path
   * @return text content. Returns null if the file does not exist, or in case of
   *         a read error.
   */
  public static String readText(String path) {
    return readText(path, DEFAULT_CHARSET);
  }

  /**
   * Read a text file.
   *
   * @param file
   *          the file object to read
   * @return text content. Returns null if the file does not exist, or in case of
   *         a read error.
   */
  public static String readText(File file) {
    return readText(file, DEFAULT_CHARSET);
  }

  /**
   * Read a text file.
   *
   * @param path
   *          file path to read
   * @param charsetName
   *          charset name. set null to auto detection
   * @return text content. Returns null if the file does not exist, or in case of
   *         a read error.
   */
  public static String readText(String path, String charsetName) {
    File file = new File(path);
    return readText(file, charsetName);
  }

  /**
   * Read a text file.
   *
   * @param file
   *          file object to read
   * @param charsetName
   *          charset name. set null to auto detection
   * @return text content. Returns null if the file does not exist, or in case of
   *         a read error.
   */
  public static String readText(File file, String charsetName) {
    byte[] content = read(file);
    if (content == null) {
      return null;
    }
    if (charsetName == null) {
      charsetName = getCharsetName(content);
      if (charsetName == null) {
        charsetName = DEFAULT_CHARSET;
      }
    }
    String text;
    try {
      text = new String(content, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    text = removeBom(text);
    return text;
  }

  /**
   * Read a text file as an array.
   *
   * @param path
   *          file path
   * @return text content. Returns null if the file does not exist, or in case of
   *         a read error.
   */
  public static String[] readTextAsArray(String path) {
    return readTextAsArray(path, DEFAULT_CHARSET);
  }

  /**
   * Read a text file as an array.
   *
   * @param path
   *          file path to read
   * @param charsetName
   *          charset name to decode
   * @return text content. Returns null if the file does not exist, or in case of
   *         a read error.
   */
  public static String[] readTextAsArray(String path, String charsetName) {
    if (!exists(path)) {
      return null;
    }
    if (charsetName == null) {
      charsetName = DEFAULT_CHARSET;
    }
    Path filePath = Paths.get(path);
    List<String> lines;
    try {
      lines = Files.readAllLines(filePath, Charset.forName(charsetName));
    } catch (IOException e) {
      return null;
    }

    int size = lines.size();
    if (size > 0) {
      String firstLine = lines.get(0);
      firstLine = removeBom(firstLine);
      lines.set(0, firstLine);
    }

    String[] text = new String[size];
    lines.toArray(text);
    return text;
  }

  /**
   * Read a file as Base64 encoded string.
   *
   * @param path
   *          file path to read
   * @return file content in Base64 encoded string. Returns null if the file does
   *         not exist, or in case of a read error.
   */
  public static String readAsBase64(String path) {
    File file = new File(path);
    return readAsBase64(file);
  }

  /**
   * Read a file as Base64 encoded string.
   *
   * @param file
   *          the file object to read
   * @return file content in Base64 encoded string. Returns null if the file does
   *         not exist, or in case of a read error.
   */
  public static String readAsBase64(File file) {
    String encoded = null;
    byte[] bytes = read(file);
    if (bytes != null) {
      encoded = Base64.getEncoder().encodeToString(bytes);
    }
    return encoded;
  }

  /**
   * Renames the file to the dest pathname.
   *
   * @param src
   *          the source path to rename
   * @param dest
   *          the new pathname
   * @return true if and only if the renaming succeeded; false otherwise
   * @throws SecurityException
   *           If a security manager exists and its
   *           SecurityManager.checkWrite(java.lang.String) method denies write
   *           access to either the old or new pathnames
   * @throws NullPointerException
   *           If parameter dest is null
   */
  public static boolean rename(String src, String dest) {
    String destDir = getParentPath(dest);
    if (destDir == null) {
      String parent = getParentPath(src);
      dest = joinPath(parent, dest);
    }
    File f1 = new File(src);
    File f2 = new File(dest);
    return rename(f1, f2);
  }

  /**
   * Renames the file to the dest pathname.
   *
   * @param src
   *          The source abstract pathname for the named file
   * @param dest
   *          The new abstract pathname for the named file
   * @return true if and only if the renaming succeeded; false otherwise
   * @throws SecurityException
   *           If a security manager exists and its
   *           SecurityManager.checkWrite(java.lang.String) method denies write
   *           access to either the old or new pathnames
   * @throws NullPointerException
   *           If parameter dest is null
   */
  public static boolean rename(File src, File dest) {
    return src.renameTo(dest);
  }

  /**
   * Sets line separator.
   *
   * @param sep
   *          line separator
   */
  public static void setLineSeparator(String sep) {
    LINE_SEPARATOR = sep;
  }

  /**
   * Write a content using a byte array into a file.
   *
   * @param path
   *          file path
   * @param content
   *          the content to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(String path, byte[] content) throws IOException {
    File file = new File(path);
    write(file, content);
  }

  /**
   * Write a content using a byte array into a file.
   *
   * @param file
   *          the file object
   * @param content
   *          the content to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(File file, byte[] content) throws IOException {
    mkParentDir(file);
    try (FileOutputStream fos = new FileOutputStream(file); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
      bos.write(content, 0, content.length);
      bos.flush();
    } catch (IOException e) {
      throw e;
    }
  }

  /**
   * Write a text into a file.
   *
   * @param path
   *          file path
   * @param content
   *          the text content to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(String path, String content) throws IOException {
    write(path, content, DEFAULT_CHARSET);
  }

  /**
   * Write a text into a file.
   *
   * @param path
   *          file path
   * @param content
   *          the text content to write
   * @param charsetName
   *          charset name
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(String path, String content, String charsetName) throws IOException {
    File file = new File(path);
    write(file, content, charsetName);
  }

  /**
   * Write a text into a file.
   *
   * @param file
   *          the file object
   * @param content
   *          the text content to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(File file, String content) throws IOException {
    mkParentDir(file);
    write(file, content, DEFAULT_CHARSET);
  }

  /**
   * Write a text into a file.
   * 
   * @param file
   *          the file object
   * @param content
   *          the text content to write
   * @param charsetName
   *          charset name. e.g., "UTF-8", "Shift_JIS"
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(File file, String content, String charsetName) throws IOException {
    mkParentDir(file);
    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName))) {
      bw.write(content);
    } catch (IOException e) {
      throw e;
    }
  }

  /**
   * Decodes a Base64 encoded String and writes it to a file.
   *
   * @param path
   *          file path
   * @param base64
   *          the content that is encoded in Base64 to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void writeFromBase64(String path, String base64) throws IOException {
    File file = new File(path);
    writeFromBase64(file, base64);
  }

  /**
   * Decodes a Base64 encoded String and writes it to a file.
   *
   * @param file
   *          the file object
   * @param base64
   *          the content that is encoded in Base64 to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void writeFromBase64(File file, String base64) throws IOException {
    byte[] content = Base64.getDecoder().decode(base64);
    write(file, content);
  }

  /**
   * Write a text with BOM into a file.
   *
   * @param path
   *          the file path
   * @param content
   *          the text content to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void writeWithBom(String path, String content) throws IOException {
    writeWithBom(path, content, DEFAULT_CHARSET);
  }

  /**
   * Write a text with BOM into a file.
   *
   * @param path
   *          the file path
   * @param content
   *          the text content to write
   * @param charsetName
   *          charset name (UTF-8 / UTF-16BE / UTF-16LE)
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void writeWithBom(String path, String content, String charsetName) throws IOException {
    File file = new File(path);
    writeWithBom(file, content, charsetName);
  }

  /**
   * Write a text with BOM into a file.
   *
   * @param file
   *          the file object
   * @param content
   *          the text content to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void writeWithBom(File file, String content) throws IOException {
    writeWithBom(file, content, DEFAULT_CHARSET);
  }

  /**
   * Write a text with BOM into a file.
   *
   * @param file
   *          the file object
   * @param content
   *          the text content to write
   * @param charsetName
   *          charset name (UTF-8 / UTF-16BE / UTF-16LE)
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void writeWithBom(File file, String content, String charsetName) throws IOException {
    mkParentDir(file);
    try (FileOutputStream fos = new FileOutputStream(file); BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, charsetName))) {
      byte[] bom = null;
      if ("UTF-8".equals(charsetName)) {
        bom = BOM_UTF8;
      } else if ("UTF-16BE".equals(charsetName)) {
        bom = BOM_UTF16BE;
      } else if ("UTF-16LE".equals(charsetName)) {
        bom = BOM_UTF16LE;
      }
      if (bom != null) {
        fos.write(bom);
      }
      bw.write(content);
    } catch (IOException e) {
      throw e;
    }
  }

  private static String removeBom(String s) {
    if (s.startsWith("\uFEFF")) {
      s = s.substring(1);
    }
    return s;
  }

  /**
   * Converts a byte array to lowercase hex string.
   *
   * @param bytes
   *          byte array
   * @return hex string (lowercase)
   */
  private static String toHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(String.format("%02x", b & 0xff));
    }
    return sb.toString();
  }

}
