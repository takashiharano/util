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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements the string related processing.
 */
public class StrUtil {

  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final String LINE_SEPARATOR = "\n";

  /**
   * String array to string.
   *
   * @param arr
   *          source array
   * @return the text
   */
  public static String array2text(String[] arr) {
    return array2text(arr, LINE_SEPARATOR);
  }

  /**
   * String array to string.
   *
   * @param arr
   *          source array
   * @param sep
   *          line separator
   * @return the text
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
   *          source string
   * @param newLine
   *          new line code
   * @return converted string
   */
  public static String convertNewLine(String src, String newLine) {
    return src.replaceAll("\r\n", "\n").replaceAll("\r", "\n").replaceAll("\n", newLine);
  }

  /**
   * Counts the number of matches for a regex.
   *
   * @param target
   *          the target string
   * @param regex
   *          the regext
   * @return matched count
   */
  public static int countMatcher(String target, String regex) {
    return countMatcher(target, regex, 0);
  }

  /**
   * Counts the number of matches for a regex.
   *
   * @param target
   *          the target string
   * @param regex
   *          the regex
   * @param flags
   *          flags
   * @return count matched count
   */
  public static int countMatcher(String target, String regex, int flags) {
    return countMatcher(target, regex, flags, false);
  }

  /**
   * Counts the number of matches for a regex.
   *
   * @param target
   *          the target string
   * @param regex
   *          the regex
   * @param complex
   *          true or false
   * @return count matched count
   */
  public static int countMatcher(String target, String regex, boolean complex) {
    return countMatcher(target, regex, 0, complex);
  }

  /**
   * Counts the number of matches for a regex.
   *
   * @param target
   *          the target string
   * @param regex
   *          the regex
   * @param flags
   *          flags
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
   * @return count matched count
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
   * @return count matched count
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
      encoding = DEFAULT_CHARSET;
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
      encoding = DEFAULT_CHARSET;
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
   * Extract a matched part of the input string.<br>
   * e.g., input:"[ABC]" regex:"\\[(.+)\\]" return:"ABC"
   *
   * @param input
   *          an input string
   * @param regex
   *          the regex
   * @return a matched part
   */
  public static String extract(String input, String regex) {
    return extract(input, regex, 0, 1);
  }

  /**
   * Extract a matched part of the input string.
   *
   * @param input
   *          an input string
   * @param regex
   *          the regex
   * @param flags
   *          flags
   * @return a matched part
   */
  public static String extract(String input, String regex, int flags) {
    return extract(input, regex, flags, 1);
  }

  /**
   * Extract a matched part of the input string.
   *
   * @param input
   *          an input string
   * @param regex
   *          the regex
   * @param flags
   *          flags
   * @param index
   *          index
   * @return the input subsequence captured by the given group during the previous
   *         match operation
   */
  public static String extract(String input, String regex, int flags, int index) {
    Pattern p = Pattern.compile(regex, flags);
    Matcher m = p.matcher(input);
    if (m.find() && m.groupCount() >= index) {
      return m.group(index);
    }
    return null;
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
   * Converts a boolean to a string.
   *
   * @param b
   *          a boolean value
   * @return "true" or "false"
   */
  public static String fromBoolean(boolean b) {
    return (b ? "true" : "false");
  }

  /**
   * Converts a double to a string.
   *
   * @param d
   *          the double to be converted.
   * @return a string representation of the argument.
   */
  public static String fromDouble(double d) {
    return Double.toString(d);
  }

  /**
   * Converts a float to a string.
   *
   * @param f
   *          the float to be converted.
   * @return a string representation of the argument.
   */
  public static String fromFloat(float f) {
    return Float.toString(f);
  }

  /**
   * Converts an integer to a string.
   *
   * @param i
   *          an integer to be converted.
   * @return a string representation of the argument in base 10.
   */
  public static String fromInteger(int i) {
    return Integer.toString(i);
  }

  /**
   * Converts along r to a string.
   *
   * @param l
   *          a long to be converted.
   * @return a string representation of the argument in base 10.
   */
  public static String fromLong(long l) {
    return Long.toString(l);
  }

  /**
   * Returns the sentence "N SUBJECT have/has been PRED."
   *
   * @param subject
   *          the subject
   * @param predicate
   *          the predicate
   * @param n
   *          number
   * @return the sentence
   */
  public static String haveBeen(String subject, String predicate, int n) {
    return haveBeen(subject, predicate, n, false);
  }

  /**
   * Returns the sentence "N SUBJECT have/has been PRED."
   *
   * @param subject
   *          the subject
   * @param predicate
   *          the predicate
   * @param n
   *          number
   * @param flag
   *          if set to true, simply add "s" to the subject for plurals
   * @return the sentence
   */
  public static String haveBeen(String subject, String predicate, int n, boolean flag) {
    String s = plural(subject, n, flag) + ' ' + (n == 1 ? "has" : "have") + " been " + predicate + ".";
    return (n == 0 ? "No" : n) + " " + s;
  }

  /**
   * Integer to Decimal formated string.<br>
   * 1000, 3 to 1.000<br>
   * 1, 3 to 0.001
   *
   * @param number
   *          number
   * @param scale
   *          scale
   * @return Decimal formated string
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
   * @return true if the string is empty; "", " ", or null.
   */
  public static boolean isEmpty(String str) {
    return isEmpty(str, true);
  }

  /**
   * Returns if the specified string is empty or not.
   *
   * @param str
   *          the source string
   * @param whitespace
   *          set to true, even blanks will be treated as empty.
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
   * Returns if the given string represents true.<br>
   *
   * @param s
   *          the string to check
   * @return false if the given string is "false"(case insensitive), "0", "" or
   *         null, otherwise true.
   */
  public static boolean isTrue(String s) {
    if (s == null) {
      return false;
    }
    s = s.trim();
    if ("".equals(s) || "0".equals(s) || "false".equals(s.toLowerCase())) {
      return false;
    }
    return true;
  }

  /**
   * Returns if the input sequence matches the pattern.
   *
   * @param target
   *          the target string
   * @param regex
   *          the regex
   * @return if, and only if, a subsequence of the input sequence matches this
   *         matcher's pattern
   */
  public static boolean match(String target, String regex) {
    return match(target, regex, 0);
  }

  /**
   * Returns if the input sequence matches the pattern.
   *
   * @param target
   *          the target string
   * @param regex
   *          the regex
   * @param flags
   *          flags<br>
   *          // UNIX_LINES = 1<br>
   *          // CASE_INSENSITIVE= 2<br>
   *          // COMMENTS = 4<br>
   *          // MULTILINE = 8<br>
   *          // LITERAL = 16<br>
   *          // DOTALL = 32<br>
   *          // UNICODE_CASE = 64<br>
   *          // CANON_EQ = 128<br>
   *          // UNICODE_CHARACTER_CLASS = 256<br>
   * @return true if, and only if, a subsequence of the input sequence matches
   *         this matcher's pattern
   */
  public static boolean match(String target, String regex, int flags) {
    Pattern p = Pattern.compile(regex, flags);
    Matcher m = p.matcher(target);
    return m.find();
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
   * @return a quoted string
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
   * @return a quoted string
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
   * @return the replaced string
   */
  public static String removeSpaceNewline(String str) {
    return str.replaceAll("\\s", "").replaceAll("\\r\\n", "\\n").replaceAll("\\r", "\\n").replaceAll("\\n", "");
  }

  /**
   * Build a repeated string.
   *
   * @param str
   *          source string
   * @param n
   *          number of repeat
   * @return the repeated string
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
   * to [1, 2, 3, "abc", "d ef", "g\"hi", ("jkl" + m), 'xyz', 'a"b b"a']<br>
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

  /**
   * Split the string by line separator.<br>
   * "aaa\nbbb\nccc" to ["aaa", "bbb", "ccc"]
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
   * Converts a string to a boolean value.
   *
   * @param s
   *          a string value
   * @return a boolean value
   */
  public static boolean toBoolean(String s) {
    if (s == null) {
      return false;
    }
    s = s.trim().toLowerCase();
    return s.equals("true");
  }

  /**
   * Converts a string to a double.
   *
   * @param s
   *          a string value
   * @return a double value
   */
  public static double toDouble(String s) {
    return toDouble(s, 0.0);
  }

  /**
   * Converts a string to a double.
   *
   * @param s
   *          a string value
   * @param defaultValue
   *          value for parse error
   * @return a double value
   */
  public static double toDouble(String s, double defaultValue) {
    if (s == null) {
      return defaultValue;
    }
    s = s.trim();
    double v = defaultValue;
    try {
      v = Double.parseDouble(s);
    } catch (NumberFormatException e) {
      // nop
    }
    return v;
  }

  /**
   * Converts a string to a float.
   *
   * @param s
   *          a string value
   * @return a float value
   */
  public static float toFloat(String s) {
    return toFloat(s, 0f);
  }

  /**
   * Converts a string to a float.
   *
   * @param s
   *          a string value
   * @param defaultValue
   *          value for parse error
   * @return a float value
   */
  public static float toFloat(String s, float defaultValue) {
    if (s == null) {
      return defaultValue;
    }
    s = s.trim();
    float v = defaultValue;
    try {
      v = Float.parseFloat(s);
    } catch (NumberFormatException e) {
      // nop
    }
    return v;
  }

  /**
   * Converts a string to a signed decimal integer.
   *
   * @param s
   *          a string value
   * @return an integer value
   */
  public static int toInteger(String s) {
    return toInteger(s, 0);
  }

  /**
   * Converts a string to a signed decimal integer.
   *
   * @param s
   *          a string value
   * @param defaultValue
   *          value for parse error
   * @return an integer value
   */
  public static int toInteger(String s, int defaultValue) {
    if (s == null) {
      return defaultValue;
    }
    s = s.trim();
    int v = defaultValue;
    try {
      v = Integer.parseInt(s);
    } catch (NumberFormatException e) {
      // nop
    }
    return v;
  }

  /**
   * Converts a string to a signed decimal integer.
   *
   * @param s
   *          a string value
   * @return an integer value
   */
  public static long toLong(String s) {
    return toLong(s, 0L);
  }

  /**
   * Converts a string to a signed decimal long.
   *
   * @param s
   *          a string value
   * @param defaultValue
   *          value for parse error
   * @return a long value
   */
  public static long toLong(String s, long defaultValue) {
    if (s == null) {
      return defaultValue;
    }
    s = s.trim();
    long v = defaultValue;
    try {
      v = Long.parseLong(s);
    } catch (NumberFormatException e) {
      // nop
    }
    return v;
  }

  /**
   * Returns a string representation of the object.
   *
   * @param o
   *          the object
   * @return a string representation of the object.
   */
  public static String toString(Object o) {
    return ((o == null) ? "null" : o.toString());
  }

  /**
   * Strip leading and trailing zeros.<br>
   * <br>
   * e.g., 0123.450 to 123.45
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

  /**
   * Converts the Excel column letter to a numeric index.<br>
   * <br>
   * e.g., "A"=1, "B"=2, ... "Z"=26, "AA"=27, ... "XFD"=16384
   *
   * @param s
   *          the letter to convert ("A"-"XFD")
   * @return Index corresponding to a character (1-16384)
   */
  public static int xlscol(String s) {
    return (int) Permutation.getIndex("ABCDEFGHIJKLMNOPQRSTUVWXYZ", s.toUpperCase());
  }

  /**
   * Converts the Excel column index to a letter.<br>
   * <br>
   * e.g., 1="A", 2="B", ... 26="Z", 27="AA", ... 16384="XFD"
   *
   * @param n
   *          the index to convert (1-16384)
   * @return the letter corresponding to the index ("A-"XFD")
   */
  public static String xlscol(int n) {
    return Permutation.getString("ABCDEFGHIJKLMNOPQRSTUVWXYZ", n);
  }

  /**
   * String permutation.
   */
  public static class Permutation {
    /**
     * Count the number of total permutation patterns of the table.
     *
     * @param chars
     *          characters to use
     * @param length
     *          the length
     * @return the number of total pattern
     */
    public static long countTotal(String chars, int length) {
      String[] tbl = chars.split("");
      int c = tbl.length;
      int n = 0;
      for (int i = 1; i <= length; i++) {
        n += Math.pow(c, i);
      }
      return n;
    }

    /**
     * Returns the characters permutation index of the given pattern.
     *
     * @param chars
     *          characters to use
     * @param pattern
     *          a string
     * @return the index
     */
    public static long getIndex(String chars, String pattern) {
      int len = pattern.length();
      int rdx = chars.length();
      long idx = 0;
      for (int i = 0; i < len; i++) {
        int d = len - i - 1;
        String c = pattern.substring(d, d + 1);
        int v = chars.indexOf(c) + 1;
        long n = v * (long) Math.pow(rdx, i);
        idx += n;
      }
      return idx;
    }

    /**
     * Returns the string that appear in the specified order within the permutation
     * of the characters.
     *
     * @param chars
     *          characters to use
     * @param index
     *          the index
     * @return the string
     */
    public static String getString(String chars, long index) {
      if (index <= 0) {
        return "";
      }
      String[] tbl = chars.split("");
      int len = tbl.length;
      ArrayList<Integer> a = new ArrayList<>();
      a.add(-1);
      for (int i = 0; i < index; i++) {
        int j = 0;
        boolean cb = true;
        while (j < a.size()) {
          if (cb) {
            a.set(j, a.get(j) + 1);
            if (a.get(j) > len - 1) {
              a.set(j, 0);
              if (a.size() <= j + 1) {
                a.add(-1);
              }
            } else {
              cb = false;
            }
          }
          j++;
        }
      }
      int strLen = a.size();
      StringBuilder sb = new StringBuilder(strLen);
      for (int i = strLen - 1; i >= 0; i--) {
        sb.append(tbl[a.get(i)]);
      }
      return sb.toString();
    }
  }

}
