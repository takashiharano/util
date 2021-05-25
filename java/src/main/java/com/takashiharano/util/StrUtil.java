package com.takashiharano.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {

  /**
   * String array to string.
   *
   * @param arr
   *          source array
   * @return
   */
  public static String array2text(String[] arr) {
    return array2text(arr, Util.LINE_SEPARATOR);
  }

  /**
   * String array to string.
   *
   * @param arr
   *          source array
   * @param sep
   *          line separator
   * @return
   */
  public static String array2text(String[] arr, String sep) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < arr.length; i++) {
      sb.append(arr[i] + sep);
    }
    return sb.toString();
  }

  /**
   * Convert newline control character.
   *
   * @param src
   * @param newLine
   * @return converted string
   */
  public static String convertNewLine(String src, String newLine) {
    return src.replaceAll("\r\n", "\n").replaceAll("\r", "\n").replaceAll("\n", newLine);
  }

  /**
   * Counts the number of matches for a regex.
   *
   * @param target
   * @param regex
   * @return count
   */
  public static int countMatcher(String target, String regex) {
    return countMatcher(target, regex, 0);
  }

  /**
   * Counts the number of matches for a regex.
   *
   * @param target
   * @param regex
   * @param flags
   * @return count
   */
  public static int countMatcher(String target, String regex, int flags) {
    return countMatcher(target, regex, flags, false);
  }

  /**
   * Counts the number of matches for a regex.
   *
   * @param target
   * @param regex
   * @param complex
   * @return count
   */
  public static int countMatcher(String target, String regex, boolean complex) {
    return countMatcher(target, regex, 0, complex);
  }

  /**
   * Counts the number of matches for a regex.
   *
   * @param target
   * @param regex
   * @param flags
   * @param complex
   *          matcher pattern
   *
   *          <pre>
   *          aaaa
  
   *          false:
   *          aa
   *            aa
   *
   *          true:
   *          aa
   *           aa
   *            aa
   *          </pre>
   * 
   * @return count
   */
  public static int countMatcher(String target, String regex, int flags, boolean complex) {
    Pattern p = Pattern.compile(regex, flags);
    Matcher m = p.matcher(target);
    int count = 0;
    if (complex) {
      int i = 0;
      while (m.find(i)) {
        count++;
        i = m.start() + 1;
      }
    } else {
      while (m.find()) {
        count++;
      }
    }
    return count;
  }

  /**
   * Count the specified pattern in the given string.
   *
   * @param str
   *          the string to check
   * @param pattern
   *          the pattern to count
   * @return count
   */
  public static int countStrPattern(String str, String pattern) {
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(str);
    int count = 0;
    while (m.find()) {
      count++;
    }
    return count;
  }

  /**
   * Decodes a application/x-www-form-urlencoded string using a specific encoding
   * scheme.
   *
   * @param src
   *          the String to decode
   * @return the newly decoded String
   */
  public static String decodeUri(String src) {
    return decodeUri(src, null);
  }

  /**
   * Decodes a application/x-www-form-urlencoded string using a specific encoding
   * scheme.
   *
   * @param src
   *          the String to decode
   * @param encoding
   *          The name of a supported character encoding
   * @return the newly decoded String
   */
  public static String decodeUri(String src, String encoding) {
    if (encoding == null) {
      encoding = "UTF-8";
    }
    String decoded;
    try {
      decoded = URLDecoder.decode(src, encoding);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return decoded;
  }

  /**
   * Translates a string into application/x-www-form-urlencoded format using a
   * specific encoding scheme.
   *
   * @param src
   *          the String to encode
   * @return the newly encoded String
   */
  public static String encodeUri(String src) {
    return encodeUri(src, null);
  }

  /**
   * Translates a string into application/x-www-form-urlencoded format using a
   * specific encoding scheme.
   *
   * @param src
   *          the String to encode
   * @param encoding
   *          The name of a supported character encoding
   * @return the newly encoded String
   */
  public static String encodeUri(String src, String encoding) {
    if (encoding == null) {
      encoding = "UTF-8";
    }
    String encoded;
    try {
      encoded = URLEncoder.encode(src, encoding);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return encoded;
  }

  /**
   * Escaping a string for HTML.
   *
   * @param src
   *          a string to escape
   * @return escaped string
   */
  public static String escapeHtml(String src) {
    String escaped = src.replace("&", "&amp;");
    escaped = escaped.replace("<", "&lt;");
    escaped = escaped.replace(">", "&gt;");
    escaped = escaped.replace("\"", "&quot;");
    escaped = escaped.replace("'", "&#39;");
    return escaped;
  }

  /**
   * Specialization of format.
   *
   * @param number
   *          the long number to format
   * @return the formatted String
   */
  public static String formatNumber(long number) {
    NumberFormat nf = NumberFormat.getNumberInstance();
    return nf.format(number);
  }

  /**
   * Specialization of format.
   *
   * @param number
   *          the double number to format
   * @return the formatted String
   */
  public static String formatNumber(double number) {
    NumberFormat nf = NumberFormat.getNumberInstance();
    return nf.format(number);
  }

  /**
   * Returns the input subsequence captured by the given group during the previous
   * match operation.<br>
   * For a matcher m, input sequence s, and group index g, the expressions
   * m.group(g) and s.substring(m.start(g), m.end(g)) are equivalent.<br>
   * <br>
   * Capturing groups are indexed from left to right, starting at one. Group zero
   * denotes the entire pattern, so the expression m.group(0) is equivalent to
   * m.group().<br>
   * <br>
   * If the match was successful but the group specified failed to match any part
   * of the input sequence, then null is returned. Note that some groups, for
   * example (a*), match the empty string. This method will return the empty
   * string when such a group successfully matches the empty string in the input.
   *
   * @param target
   * @param regex
   * @param flags
   * @param index
   * @return
   */
  public static String group(String target, String regex, int flags, int index) {
    Pattern p = Pattern.compile(regex, flags);
    Matcher m = p.matcher(target);
    if (m.find()) {
      return m.group(index);
    }
    return null;
  }

  /**
   * Integer to Decimal formated string.<br>
   * 1000, 3 -> 1.000<br>
   * 1, 3 -> 0.001
   *
   * @param number
   * @param scale
   * @return
   */
  public static String intnum2decimal(long number, int scale) {
    if (number == 0) {
      return "0";
    }

    String src = Long.toString(number);
    if (number < 0) {
      src = src.substring(1);
    }

    int len = src.length();
    String formatted;
    if (len <= scale) {
      String zeros = repeat("0", scale - len);
      formatted = "0." + zeros + src;
    } else {
      String i = src.substring(0, len - scale);
      String d = src.substring(len - scale, len);
      formatted = i + "." + d;
    }

    if (number < 0) {
      formatted = "-" + formatted;
    }

    return formatted;
  }

  /**
   * Returns if the specified string is empty or not.
   *
   * @param str
   *          the source string
   * @return true if the string is empty.
   */
  public static boolean isEmpty(String str) {
    return isEmpty(str, false);
  }

  /**
   * Returns if the specified string is empty or not.
   *
   * @param str
   *          the source string
   * @param whitespace
   *          set to true, whitespace only will be treated as empty
   * @return true if the string is empty.
   */
  public static boolean isEmpty(String str, boolean whitespace) {
    if (str == null) {
      return true;
    }
    if (whitespace) {
      str = str.trim();
    }
    return str.equals("");
  }

  /**
   * Returns if the given string is a float.
   *
   * @param str
   *          the string to check
   * @return true if the string is a float
   */
  public static boolean isFloat(String str) {
    if (str == null) {
      return false;
    }
    return match(str, "^-?\\d+\\.\\d+$", 0);
  }

  /**
   * Returns if the given string is an integer.
   *
   * @param str
   *          the string to check
   * @return true if the string is an integer
   */
  public static boolean isInteger(String str) {
    if (str == null) {
      return false;
    }
    return match(str, "^-?\\d+$", 0);
  }

  /**
   * Returns if the given string is not a number.
   *
   * @param str
   *          the string to check
   * @return true if not a number
   */
  public static boolean isNaN(String str) {
    return !isNumber(str);
  }

  /**
   * Returns if the given string is a number.
   *
   * @param str
   *          the string to check
   * @return true if the string is a number
   */
  public static boolean isNumber(String str) {
    return (isInteger(str) || isFloat(str));
  }

  /**
   * Attempts to find the next subsequence of the input sequence that matches the
   * pattern.<br>
   * This method starts at the beginning of this matcher's region, or, if a
   * previous invocation of the method was successful and the matcher has not
   * since been reset, at the first character not matched by the previous
   * match.<br>
   * If the match succeeds then more information can be obtained via the start,
   * end, and group methods.
   *
   * @param target
   * @param regex
   * @param flags
   * @return true if, and only if, a subsequence of the input sequence matches
   *         this matcher's pattern
   */
  public static boolean match(String target, String regex, int flags) {
    Pattern p = Pattern.compile(regex, flags);
    Matcher m = p.matcher(target);
    return m.find();
  }

  /**
   * Quote the specified string.
   *
   * @param src
   *          source string
   * @return Quoted string
   */
  public static String quote(String src) {
    return quote(src, null, null);
  }

  /**
   * Quote the specified string.
   *
   * @param src
   *          source string
   * @param quote
   *          character to enclose fields
   * @return
   */
  public static String quote(String src, String quote) {
    return quote(src, quote, null);
  }

  /**
   * Quote the specified string.
   *
   * @param src
   *          source string
   * @param quote
   *          character to enclose fields
   * @param esc
   *          escape character
   * @return Quoted string
   */
  public static String quote(String src, String quote, String esc) {
    if (quote == null) {
      quote = "\"";
    }
    if (esc == null) {
      esc = "\\";
    }
    String s = src.replace(quote, esc + quote);
    StringBuilder sb = new StringBuilder();
    sb.append(quote);
    sb.append(s);
    sb.append(quote);
    return sb.toString();
  }

  /**
   * Remove white space characters and newlines from the given string.
   *
   * @param str
   *          a string to replace
   * @return replaced string
   */
  public static String removeSpaceNewline(String str) {
    return str.replaceAll("\s", "").replaceAll("\r\n", "\n").replaceAll("\r", "\n").replaceAll("\n", "");
  }

  /**
   * Build a repeated string.
   *
   * @param str
   *          source string
   * @param n
   *          number of repeat
   * @return repeated string
   */
  public static String repeat(String str, int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
      sb.append(str);
    }
    return sb.toString();
  }

  /**
   * Splits the string.
   *
   * @param src
   *          the string to split
   * @param separator
   *          the delimiting regular expression
   * @return the array of strings computed by splitting this string around matches
   *         of the given regular expression
   */
  public static String[] split(String src, String separator) {
    return src.split(separator, -1);
  }

  /**
   * Split the string by line separator.<br>
   * "aaa\nbbb\nccc" -> ["aaa", "bbb", "ccc"]
   * 
   * @param src
   *          the string to split
   * @return the split array of strings
   */
  public static String[] text2array(String src) {
    String text = convertNewLine(src, "\n");
    String[] arr = text.split("\n", -1);
    if ((arr.length >= 2) && (arr[arr.length - 1].equals(""))) {
      String[] tmp = new String[arr.length - 1];
      for (int i = 0; i < arr.length - 1; i++) {
        tmp[i] = arr[i];
      }
      arr = tmp;
    }
    return arr;
  }

  /**
   * Strip leading and trailing zeros.<br>
   * <br>
   * e.g., 0123.450 -> 123.45
   *
   * @param value
   *          numeric string
   * @return trimmed value
   */
  public static String trimZeros(String value) {
    value = value.trim();
    String sign = "";
    if (value.charAt(0) == '-') {
      sign = "-";
      value = value.substring(1);
    }

    String[] p = value.split("\\.");
    String i = p[0];
    String d = "";
    if (p.length >= 2) {
      d = p[1];
    }

    i = i.replaceAll("^0+", "");
    if (i.equals("")) {
      i = "0";
    }

    String r = i;

    d = d.replaceAll("0+$", "");
    if (!d.equals("")) {
      r += "." + d;
    }

    if (!r.equals("0")) {
      r = sign + r;
    }

    return r;
  }

}
