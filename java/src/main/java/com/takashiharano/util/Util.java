package com.takashiharano.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final String LINE_SEPARATOR = "\n";

  /**
   * Returns the current time in milliseconds.
   *
   * @return the difference, measured in milliseconds, between the current time
   *         and midnight, January 1, 1970 UTC.
   */
  public static long now() {
    return System.currentTimeMillis();
  }

  /**
   * Encodes the specified byte array into a String using the Base64 encoding
   * scheme.
   *
   * @param src
   *          the string to encode
   * @return A String containing the resulting Base64 encoded characters
   */
  public static String encodeBase64(String src) {
    return encodeBase64(src, DEFAULT_CHARSET);
  }

  /**
   * Encodes the specified byte array into a String using the Base64 encoding
   * scheme.
   *
   * @param src
   *          the string to encode
   * @param charsetName
   *          charset name
   * @return A String containing the resulting Base64 encoded characters
   */
  public static String encodeBase64(String src, String charsetName) {
    if (src == null) {
      return null;
    }
    String encoded = null;
    byte[] srcBytes;
    try {
      srcBytes = src.getBytes(charsetName);
      encoded = Base64.getEncoder().encodeToString(srcBytes);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return encoded;
  }

  /**
   * Encodes the specified byte array into a String using the Base64 encoding
   * scheme.
   *
   * @param src
   *          the byte array to encode
   * @return A String containing the resulting Base64 encoded characters
   */
  public static String encodeBase64(byte[] src) {
    if (src == null) {
      return null;
    }
    String encoded = Base64.getEncoder().encodeToString(src);
    return encoded;
  }

  /**
   * Decodes a Base64 encoded String into a string using the Base64 encoding
   * scheme.
   *
   * @param src
   *          the string to decode
   * @return the decoded string.
   */
  public static String decodeBase64(String src) {
    return decodeBase64(src, DEFAULT_CHARSET);
  }

  /**
   * Decodes a Base64 encoded String into a string using the Base64 encoding
   * scheme.
   *
   * @param src
   *          the string to decode
   * @param charsetName
   *          charset name
   * @return the decoded string.
   */
  public static String decodeBase64(String src, String charsetName) {
    if (src == null) {
      return null;
    }
    byte[] decoded = Base64.getDecoder().decode(src);
    String str = null;
    try {
      str = new String(decoded, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return str;
  }

  /**
   * Decodes a Base64 encoded String into a newly-allocated byte array using the
   * Base64 encoding scheme.
   *
   * @param src
   *          the string to decode
   * @return A newly-allocated byte array containing the decoded bytes.
   */
  public static byte[] decodeBase64B(String src) {
    if (src == null) {
      return new byte[0];
    }
    byte[] decoded = Base64.getDecoder().decode(src);
    return decoded;
  }

  /**
   * Base64 decoder. (file to file)
   *
   * @param srcPath
   *          Base64 text file path
   * @param destPath
   *          path to save the decoded file
   * @throws IOException
   */
  public static void fileToFileBase64Decoder(String srcPath, String destPath) throws IOException {
    String b64 = FileUtil.readText(srcPath);
    FileUtil.writeFromBase64(destPath, b64);
  }

  /**
   * Returns current path
   *
   * @return
   */
  public static String getCurrentPath() {
    String currentPath = new File(".").getAbsoluteFile().getParent();
    return currentPath;
  }

  /**
   * Returns random integer value.
   *
   * @return random integer value
   */
  public static int randomInt() {
    long seed = System.nanoTime();
    Random r = new Random(seed);
    return r.nextInt();
  }

  /**
   * Returns random integer value.
   *
   * @param max
   * @return 0-max
   */
  public static int randomInt(int max) {
    long seed = System.nanoTime();
    Random r = new Random(seed);
    return r.nextInt(max + 1);
  }

  /**
   * Returns random integer value.
   *
   * @param min
   * @param max
   * @return min-max
   */
  public static int randomInt(int min, int max) {
    Random r = new Random(System.currentTimeMillis());
    int i;
    max++;
    do {
      i = r.nextInt(max);
    } while (i < min);
    return i;
  }

  /**
   * Returns random double value.<br>
   * e.g.,) 0.8697886198087033
   *
   * @return 0.0 - 1.0
   */
  public static double randomDouble() {
    long seed = System.nanoTime();
    Random r = new Random(seed);
    return r.nextDouble();
  }

  public static boolean randomBoolean() {
    long seed = System.nanoTime();
    Random r = new Random(seed);
    return r.nextBoolean();
  }

  /**
   * randomString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
   * 8);<br>
   * -> "TWEQWhq2"
   * 
   * @param table
   * @param len
   * @return
   */
  public static String randomString(String table, int len) {
    char[] cTable = table.toCharArray();
    int lastIdx = cTable.length - 1;
    char[] buf = new char[len];
    for (int i = 0; i < len; i++) {
      int idx = randomInt(lastIdx);
      buf[i] = cTable[idx];
    }
    String ret = new String(buf);
    return ret;
  }

  /**
   * Returns the word in the singular or plural, depending on the number
   * specified.
   *
   * @param word
   *          the word
   * @param n
   *          number of the target
   * @return the word in the singular or plural
   */
  public static String plural(String word, int n) {
    return plural(word, n, false);
  }

  /**
   * Returns the word in the singular or plural, depending on the number
   * specified.
   *
   * @param word
   *          the word
   * @param n
   *          number of the target
   * @param flag
   *          if set to true, adds "s" simply
   * @return the word in the singular or plural
   */
  public static String plural(String word, int n, boolean flag) {
    if (n == 1) {
      return word;
    }

    if (flag) {
      return word + "s";
    }

    String r = "s$";
    Pattern p = Pattern.compile(r, Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(word);
    if (m.find()) {
      return word + "es";
    }
    r = "ch$";
    p = Pattern.compile(r, Pattern.CASE_INSENSITIVE);
    m = p.matcher(word);
    if (m.find()) {
      return word + "es";
    }
    r = "sh$";
    p = Pattern.compile(r, Pattern.CASE_INSENSITIVE);
    m = p.matcher(word);
    if (m.find()) {
      return word + "es";
    }
    r = "x$";
    p = Pattern.compile(r, Pattern.CASE_INSENSITIVE);
    m = p.matcher(word);
    if (m.find()) {
      return word + "es";
    }
    r = "o$";
    p = Pattern.compile(r, Pattern.CASE_INSENSITIVE);
    m = p.matcher(word);
    if (m.find()) {
      return word + "es";
    }

    r = "y$";
    p = Pattern.compile(r);
    m = p.matcher(word);
    if (m.find()) {
      return word.replaceAll(r, "ies");
    }
    r = "Y$";
    p = Pattern.compile(r);
    m = p.matcher(word);
    if (m.find()) {
      return word.replaceAll(r, "IES");
    }

    r = "f$";
    p = Pattern.compile(r);
    m = p.matcher(word);
    if (m.find()) {
      return word.replaceAll(r, "ves");
    }
    r = "F$";
    p = Pattern.compile(r);
    m = p.matcher(word);
    if (m.find()) {
      return word.replaceAll(r, "VES");
    }

    r = "fe$";
    p = Pattern.compile(r);
    m = p.matcher(word);
    if (m.find()) {
      return word.replaceAll(r, "ves");
    }
    r = "FE$";
    p = Pattern.compile(r);
    m = p.matcher(word);
    if (m.find()) {
      return word.replaceAll(r, "VES");
    }

    return word + "s";
  }

  /**
   * Returns the sentence "N SUBJECT have/has been PRED."
   *
   * @param subject
   * @param predicate
   * @param n
   * @return the sentence
   */
  public static String haveBeen(String subject, String predicate, int n) {
    return haveBeen(subject, predicate, n, false);
  }

  /**
   * Returns the sentence "N SUBJECT have/has been PRED."
   *
   * @param subject
   * @param predicate
   * @param n
   * @param flag
   *          if set to true, simply add "s" to the subject for plurals
   * @return the sentence
   */
  public static String haveBeen(String subject, String predicate, int n, boolean flag) {
    String s = plural(subject, n, flag) + ' ' + (n == 1 ? "has" : "have") + " been " + predicate + ".";
    return (n == 0 ? "No" : n) + " " + s;
  }

  /**
   * Copy byte array.
   *
   * @param src
   * @param dest
   * @param offset
   * @param size
   */
  public static void copyByteArray(byte[] src, byte[] dest, int offset, int size) {
    for (int i = 0; i < size; i++) {
      dest[offset + i] = src[i];
    }
  }

  /**
   * Read byte array from input stream.
   *
   * @param is
   *          input stream
   * @return read bytes
   * @throws IOException
   */
  public static byte[] readStream(InputStream is) throws IOException {
    byte[] buf = null;
    byte[] b = new byte[1048576];
    int size = 0;
    int readSize;
    byte[] wkBuf = null;
    while ((readSize = is.read(b)) != -1) {
      int offset = 0;
      if (buf != null) {
        wkBuf = new byte[size];
        copyByteArray(buf, wkBuf, 0, buf.length);
      }
      offset = size;
      size += readSize;
      buf = new byte[size];
      if (wkBuf != null) {
        copyByteArray(wkBuf, buf, 0, wkBuf.length);
      }
      copyByteArray(b, buf, offset, readSize);
    }
    return buf;
  }

  /**
   * Execute a command.
   *
   * @param command
   *          a string array containing the program and its arguments
   * @return command result
   * @throws Exception
   */
  public static String execCommand(String[] command) throws Exception {
    return execCommand(command, DEFAULT_CHARSET);
  }

  /**
   * Execute a command.
   *
   * @param command
   *          a string array containing the program and its arguments
   * @param charset
   *          charset name
   * @return command result
   * @throws Exception
   */
  public static String execCommand(String[] command, String charset) throws Exception {
    CommandExecutor executor = new CommandExecutor();
    return executor.exec(command, 0, charset, null);
  }

  /**
   * Execute Linux command.
   *
   * @param command
   *          command string
   * @return command result
   * @throws Exception
   */
  public static String execLinuxCommand(String command) throws Exception {
    return execLinuxCommand(command, "UTF-8");
  }

  /**
   * Execute Linux command.
   *
   * @param command
   *          command string
   * @param charset
   *          charset name
   * @return command result
   * @throws Exception
   */
  public static String execLinuxCommand(String command, String charset) throws Exception {
    String osName = System.getProperty("os.name");
    if ("Linux".equals(osName)) {
      String[] cmds = { "/bin/sh", "-c", command };
      return execCommand(cmds, charset);
    } else {
      throw new Exception("Linux command is not available on " + osName);
    }
  }

  /**
   * Execute Windows command.
   *
   * @param command
   *          command string
   * @return command result
   * @throws Exception
   */
  public static String execWindowsCommand(String command) throws Exception {
    return execWindowsCommand(command, "SJIS");
  }

  /**
   * Execute Windows command.
   *
   * @param command
   *          command string
   * @param charset
   *          charset name
   * @return command result
   * @throws Exception
   */
  public static String execWindowsCommand(String command, String charset) throws Exception {
    String osName = System.getProperty("os.name");
    if ((osName != null) && osName.startsWith("Windows")) {
      String[] cmds = { "cmd", "/c", command };
      return execCommand(cmds, charset);
    } else {
      throw new Exception("Windows command is not available on " + osName);
    }
  }

  // --------------------------------------------------------------------------
  /**
   * Returns hash value of the file content.
   *
   * @param path
   *          file path to read
   * @param algorithm
   *          hash algorithm (MD5 / SHA-1 / SHA-256 / SHA-512)
   * @return hash value
   */
  public static String getFileHash(String path, String algorithm) {
    byte[] b = FileUtil.read(path);
    String hash = Hash.getHash(b, algorithm);
    return hash;
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
  public static String getFileHash(File file, String algorithm) {
    byte[] b = FileUtil.read(file);
    String hash = Hash.getHash(b, algorithm);
    return hash;
  }

  /**
   * Print the system environment.
   */
  public static void printSystemEnv() {
    Log.i("[env]");
    Map<String, String> envMap = System.getenv();
    for (Map.Entry<String, String> entry : envMap.entrySet()) {
      Log.i(entry);
    }
  }

  /**
   * Returns the heap information string.
   */
  public static String getHeapInfoString() {
    HeapInfo info = new HeapInfo();
    long total = info.getTotal();
    long free = info.getFree();
    long max = info.getMax();
    long used = info.getUsed();
    String percent = info.getPercent();
    return "Heap: total=" + total + " / used=" + used + "(" + percent + "%) / free=" + free + " / max=" + max;
  }

  /**
   * Runs the garbage collector.
   *
   * @return execution result details
   */
  public static String gc() {
    HeapInfo info1 = new HeapInfo();
    long t1 = System.currentTimeMillis();
    Runtime.getRuntime().gc();
    long t2 = System.currentTimeMillis();
    HeapInfo info2 = new HeapInfo();
    String elapsed = Time.ms2str(t2 - t1);
    String details = info1.getPercent() + "% -> " + info2.getPercent() + "% (" + elapsed + ")";
    Log.i("GC: " + details);
    return details;
  }

  /**
   * Splits the given string by space character.
   *
   * @param src
   *          the source string
   * @return the array of the split strings. if the src is null, returns [].
   */
  public static String[] splitKeywords(String src) {
    return splitKeywords(src, 0);
  }

  /**
   * Splits the given string by space character.<br>
   * The limit parameter controls the number of times the pattern is applied and
   * therefore affects the length of the resulting array.<br>
   * <br>
   * e.g.,<br>
   * 1 2 3 "abc" "d ef" "g\"hi" ("jkl" + m) 'xyz' 'a"b b"a'<br>
   * -> [1, 2, 3, "abc", "d ef", "g\"hi", ("jkl" + m), 'xyz', 'a"b b"a']<br>
   * 
   * @param src
   *          the source string
   * @param limit
   *          the result threshold, as described above
   * @return the array of the split strings. if the src is null, returns [].
   */
  public static String[] splitKeywords(String src, int limit) {
    if (src == null) {
      return new String[0];
    }
    List<String> keywords = new ArrayList<>();
    int start = 0;
    int len = 0;
    boolean srch = true;
    char quot = 0;
    int paren = 0;
    char ch;
    String str = "";
    for (int i = 0; i < src.length(); i++) {
      len++;
      ch = src.charAt(i);
      switch (ch) {
        case ' ':
          if (srch || (quot != 0) || (paren > 0)) {
            continue;
          } else {
            srch = true;
            str = src.substring(start, start + len);
            keywords.add(str);
            if (keywords.size() + 1 == limit) {
              if (i < src.length() - 1) {
                start = i + 1;
                len = src.length() - start;
                str = src.substring(start, start + len);
                keywords.add(str);
                i = src.length();
              }
            }
          }
          break;
        case '(':
          if (srch) {
            start = i;
            len = 0;
            srch = false;
          }
          if (quot == 0) {
            paren++;
          }
          break;
        case ')':
          if (srch) {
            start = i;
            len = 0;
            srch = false;
          } else if (paren > 0) {
            if ((i > 0) && (src.charAt(i - 1) == '\\')) {
              continue;
            }
            paren--;
          }
          break;
        case '"':
        case '\'':
          if (paren > 0) {
            continue;
          } else if (srch) {
            start = i;
            len = 0;
            srch = false;
            quot = ch;
          } else if (ch == quot) {
            if ((i > 0) && (src.charAt(i - 1) == '\\')) {
              continue;
            }
            quot = 0;
          }
          break;
        default:
          if (srch) {
            start = i;
            len = 0;
            srch = false;
          }
      }
    }
    len++;
    if (!srch) {
      str = src.substring(start, start + len);
      keywords.add(str);
    }

    String[] ret;
    if (keywords.size() == 0) {
      ret = new String[0];
    } else {
      ret = new String[keywords.size()];
      keywords.toArray(ret);
    }
    return ret;
  }

}
