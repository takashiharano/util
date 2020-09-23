package com.takashiharano.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

public class Util {

  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final String LINE_SEPARATOR = System.lineSeparator();

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
   * Returns human-readable time string.<br>
   * 171959000 -> "1d 23h 45m 59s"
   *
   * @param millis
   *          milliseconds
   * @return time string
   */
  public static String getTimeString(long millis) {
    return getTimeString(millis, false, false);
  }

  /**
   * Returns human-readable time string.<br>
   * 171959789 -> "1d 23h 45m 59s 789ms"
   *
   * @param millis
   *          milliseconds
   * @param hr
   *          true=24h : false=1d 0h
   * @param precision
   *          set true to treat milliseconds
   * @return time string
   */
  public static String getTimeString(long millis, boolean hr, boolean precision) {
    long DAY = 86400000;
    long HOUR = 3600000;
    long MINUTE = 60000;
    long SECOND = 1000;

    long wk = millis;
    boolean sign = false;
    long days = 0;
    long hours = 0;
    long hrs = 0;
    long minutes = 0;
    long seconds = 0;
    long milliseconds = 0;
    boolean flg = false;

    if (wk < 0) {
      sign = true;
      wk *= (-1);
    }

    if (wk >= DAY) {
      days = wk / DAY;
    }

    if (wk >= HOUR) {
      hours = (wk / HOUR);
      wk -= hours * HOUR;
    }
    hrs = hours - days * 24;

    if (wk >= MINUTE) {
      minutes = wk / MINUTE;
      wk -= minutes * MINUTE;
    }

    if (wk >= SECOND) {
      seconds = wk / 1000;
    }

    milliseconds = wk - (seconds * 1000);

    StringBuilder sb = new StringBuilder();
    if (sign) {
      sb.append("-");
    }

    if (!hr && (days > 0)) {
      flg = true;
      sb.append(days + "d ");
    }

    if (hr && (hours > 0)) {
      flg = true;
      sb.append(hours + "h ");
    } else if (flg || (hrs > 0)) {
      flg = true;
      sb.append(hrs + "h ");
    }

    if (flg || (minutes > 0)) {
      sb.append(minutes + "m ");
    }

    if (precision) {
      if (millis >= 60000) {
        if (milliseconds == 0) {
          sb.append(seconds + "s");
        } else {
          sb.append(seconds + "s " + milliseconds + "ms");
        }
      } else {
        if (seconds == 0) {
          if (milliseconds == 0) {
            sb.append("0s");
          } else {
            sb.append(milliseconds + "ms");
          }
        } else {
          String ms = Long.toString(milliseconds).replaceAll("0+$", "");
          if (milliseconds == 0) {
            sb.append(seconds + "s");
          } else if (milliseconds < 10) {
            sb.append(seconds + ".00" + ms + "s");
          } else if (milliseconds < 100) {
            sb.append(seconds + ".0" + ms + "s");
          } else {
            sb.append(seconds + "." + ms + "s");
          }
        }
      }
    } else {
      sb.append(seconds + "s");
    }

    return sb.toString();
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
   * byte[] -> "01 02 03 ..."
   *
   * @param src
   *          the byte array to dump
   * @return
   */
  public static String bytes2hex(byte[] src) {
    return HexDumper.toHex(src);
  }

  /**
   * byte[] -> "01 02 03 ..."
   *
   * @param src
   *          the byte array to dump
   * @param limit
   *          limit length
   * @return
   */
  public static String bytes2hex(byte[] src, int limit) {
    return HexDumper.toHex(src, limit);
  }

  /**
   * HEX dump.
   *
   * @param src
   *          the byte array to dump
   * @return
   */
  public static String dumpHex(byte[] src) {
    return HexDumper.dump(src);
  }

  /**
   * HEX dump.
   *
   * @param src
   *          the byte array to dump
   * @param limit
   *          limit length
   * @return
   */
  public static String dumpHex(byte[] src, int limit) {
    return HexDumper.dump(src, limit);
  }

  /**
   * [s]234567890123...[e]
   *
   * @param size
   *          size to generate in bytes
   * @param s
   *          value of the first byte. -1 -> '1'
   * @param e
   *          value of the last byte. -1 -> [0-9] of the end position
   * @return bytes
   */
  public static byte[] getTestBytes(int size, int s, int e) {
    byte[] bytes = new byte[size];
    byte b;
    for (int i = 1; i <= size; i++) {
      if (i == 1) {
        if (s == -1) {
          b = 0x31;
        } else {
          b = (byte) s;
        }
      } else if (i == size) {
        if (s == -1) {
          b = (byte) (i % 10 + 0x30);
        } else {
          b = (byte) e;
        }
      } else {
        b = (byte) (i % 10 + 0x30);
      }
      bytes[i - 1] = b;
    }
    return bytes;
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
  public static String getFileHash(String path, String algorithm) {
    byte[] b = FileUtil.readFile(path);
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
    byte[] b = FileUtil.readFile(file);
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
    String elapsed = getTimeString(t2 - t1, false, true);
    String details = info1.getPercent() + "% -> " + info2.getPercent() + "% (" + elapsed + ")";
    Log.i("GC: " + details);
    return details;
  }

}
