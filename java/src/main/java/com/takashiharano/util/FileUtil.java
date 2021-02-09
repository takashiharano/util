package com.takashiharano.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class FileUtil {

  public static final String DEFAULT_CHARSET = "UTF-8";
  public static String LINE_SEPARATOR = "\n";

  /**
   * Append a line to text file.
   *
   * @param path
   *          file path
   * @param newLine
   *          new line
   * @throws IOException
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
   * Creates a new empty file.
   *
   * @param path
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
   * @param createParent
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
   * @param createParent
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
   * Deletes the file or directory denoted by this abstract pathname. If this
   * pathname denotes a directory, then the directory must be empty in order to be
   * deleted.
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
   * Tests whether the file or directory denoted by this abstract path name
   * exists.
   *
   * @param path
   *          the path of file or directory
   * @return true if and only if the file or directory denoted by this abstract
   *         pathname exists; false otherwise
   */
  public static boolean exists(String path) {
    File file = new File(path);
    return file.exists();
  }

  /**
   * Returns the name of the file or directory denoted by the given pathname.<br>
   * <br>
   * e.g., "C:/test/abc.txt" -> "abc.txt"
   *
   * @param path
   * @return file name
   */
  public static String getFileName(String path) {
    File file = new File(path);
    String name = file.getName();
    return name;
  }

  /**
   * Returns the name of extension.<br>
   * <br>
   * e.g., "C:/test/abc.txt" -> "txt"
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
   * Returns the current path.
   *
   * @return
   */
  public static String getCurrentPath() {
    String path = new File(".").getAbsoluteFile().getParent();
    return path;
  }

  /**
   * Returns the length of the file denoted by this abstract pathname.
   *
   * @param path
   *          file path
   * @return The length, in bytes, of the file denoted by this abstract pathname,
   *         or 0L if the file does not exist.
   */
  public static long getFileLen(String path) {
    File file = new File(path);
    return file.length();
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
   * Returns the pathname string of this abstract pathname's parent, or null if
   * this pathname does not name a parent directory. The parent of an abstract
   * pathname consists of the pathname's prefix, if any, and each name in the
   * pathname's name sequence except for the last. If the name sequence is empty
   * then the pathname does not name a parent directory.
   *
   * e.g., "a/b/c.txt" -> "/a/b"
   *
   * @param path
   * @return parent path
   */
  public static String getParentPath(String path) {
    File file = new File(path);
    return getParentPath(file);
  }

  /**
   * Returns parent path of the file.<br>
   * e.g., "a/b/c.txt" -> "/a/b"
   *
   * @param file
   * @return parent path
   */
  public static String getParentPath(File file) {
    String parent = file.getParent();
    return parent;
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
   * @return
   */
  public static File[] listFiles(String path) {
    File file = new File(path);
    return file.listFiles();
  }

  /**
   * Returns an array of strings naming the directories in the directory.
   *
   * @param dirPath
   * @return
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
   * @return
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
    if (!exists(parent)) {
      created = mkdir(parent);
    }
    return created;
  }

  /**
   * Read a binary file.
   *
   * @param path
   * @return byte array of the file content. If the file does not exist, this
   *         method returns null.
   */
  public static byte[] read(String path) {
    File file = new File(path);
    return read(file);
  }

  /**
   * Read a binary file.
   *
   * @param file
   * @return byte array of the file content. If the file does not exist, this
   *         method returns null.
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
      content = new byte[0];
    }
    return content;
  }

  /**
   * Read a text file.
   *
   * @param path
   * @return text content. If the file does not exist, this method returns null.
   */
  public static String readText(String path) {
    return readText(path, DEFAULT_CHARSET);
  }

  /**
   * Read a text file.
   *
   * @param file
   * @return text content. If the file does not exist, this method returns null.
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
   * @return text content. If the file does not exist, this method returns null.
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
   * @return text content. If the file does not exist, this method returns null.
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
    return text;
  }

  /**
   * Read a text file as an array.
   *
   * @param path
   * @return text content
   */
  public static String[] readTextAsArray(String path) {
    return readTextAsArray(path, DEFAULT_CHARSET);
  }

  /**
   * Read a text file as an array.
   *
   * @param path
   * @param charsetName
   * @return text content
   */
  public static String[] readTextAsArray(String path, String charsetName) {
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
    String[] text = new String[lines.size()];
    lines.toArray(text);
    return text;
  }

  /**
   * Read a file as Base64 encoded string.
   *
   * @param path
   * @return file content in Base64 encoded string. If the file does not exist,
   *         this method returns null.
   */
  public static String readAsBase64(String path) {
    File file = new File(path);
    return readAsBase64(file);
  }

  /**
   * Read a file as Base64 encoded string.
   *
   * @param file
   * @return file content in Base64 encoded string. If the file does not exist,
   *         this method returns null.
   */
  public static String readAsBase64(File file) {
    String encoded = null;
    byte[] bytes = read(file);
    if ((bytes != null) && (bytes.length != 0)) {
      encoded = Base64.getEncoder().encodeToString(bytes);
    }
    return encoded;
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
   * @param content
   * @throws IOException
   */
  public static void write(String path, byte[] content) throws IOException {
    File file = new File(path);
    write(file, content);
  }

  /**
   * Write a content using a byte array into a file.
   *
   * @param file
   * @param content
   * @throws IOException
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
   * @param content
   * @throws IOException
   */
  public static void write(String path, String content) throws IOException {
    File file = new File(path);
    write(file, content);
  }

  /**
   * Write a text into a file.
   *
   * @param file
   * @param content
   * @throws IOException
   */
  public static void write(File file, String content) throws IOException {
    mkParentDir(file);
    try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw);) {
      bw.write(content);
    } catch (IOException e) {
      throw e;
    }
  }

  /**
   * Decodes a Base64 encoded String and writes it to a file.
   *
   * @param path
   * @param base64
   * @throws IOException
   */
  public static void writeFromBase64(String path, String base64) throws IOException {
    File file = new File(path);
    writeFromBase64(file, base64);
  }

  /**
   * Decodes a Base64 encoded String and writes it to a file.
   *
   * @param file
   * @param base64
   * @throws IOException
   */
  public static void writeFromBase64(File file, String base64) throws IOException {
    byte[] content = Base64.getDecoder().decode(base64);
    write(file, content);
  }

}
