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
   * Capitalizes the string<br>
   * e.g., abc to Abc
   *
   * @param s
   *          the source string
   * @return the capitalized string
   */
  public static String capitalize(String s) {
    if ((s == null) || "".equals(s)) {
      return s;
    }
    String s1 = s.substring(0, 1);
    String s2 = "";
    if (s.length() >= 2) {
      s2 = s.substring(1);
    }
    return s1.toUpperCase() + s2.toLowerCase();
  };

  /**
   * Convert newline control character.
   *
   * @param src
   *          The source string
   * @param newLine
   *          New line code.<br>
   *          Special characters in regular expressions must be escaped.
   * @return The converted string
   */
  public static String convertNewLine(String src, String newLine) {
    return src.replaceAll("\r\n|\r", "\n").replaceAll("\n", newLine);
  }

  /**
   * Counts the number of matches for a regex.
   *
   * @param target
   *          the target string
   * @param regex
   *          the regex
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
   *          target="aaaa"
   *          regex="aa"
  
   *          false:
   *          aa
   *            aa
   *            = 2
   *
   *          true:
   *          aa
   *           aa
   *            aa
   *            = 3
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
   *          the pattern (regex) to count
   * @return count matched count
   */
  public static int countPattern(String str, String pattern) {
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
   * Checks if the two strings are equal.<br>
   *
   * <pre>
   * StrUtil.equals("abc", "abc") = true
   * StrUtil.equals(null, null)   = true
   * StrUtil.equals(null, "abc")  = false
   * StrUtil.equals("abc", null)  = false
   * StrUtil.equals("abc", "ABC") = false
   * StrUtil.equals("abc", "xyz") = false
   * </pre>
   *
   * @param s1
   *          the first string, may be null
   * @param s2
   *          the second string, may be null
   * @return true if the strings are equal (case-sensitive), or both null
   */
  public static boolean equals(String s1, String s2) {
    if ((s1 == null) && (s2 == null)) {
      return true;
    }
    if (s1 == null) {
      return false;
    }
    return s1.equals(s2);
  }

  /**
   * Checks if the two strings are not equal.
   *
   * @param s1
   *          the first string, may be null
   * @param s2
   *          the second string, may be null
   * @return true if the strings are not equal (case-sensitive)
   */
  public static boolean notEquals(String s1, String s2) {
    return !equals(s1, s2);
  }

  /**
   * Compares two strings, ignoring case.<br>
   *
   * <pre>
   * StrUtil.equalsIgnoreCase("abc", "abc") = true
   * StrUtil.equalsIgnoreCase("abc", "ABC") = true
   * StrUtil.equalsIgnoreCase(null, null)   = true
   * StrUtil.equalsIgnoreCase("abc", "xyz") = false
   * StrUtil.equalsIgnoreCase(null, "abc")  = false
   * StrUtil.equalsIgnoreCase("abc", null)  = false
   * StrUtil.equalsIgnoreCase("abc", "xyz") = false
   * </pre>
   *
   * @param s1
   *          the first string, may be null
   * @param s2
   *          the second string, may be null
   * @return true if the strings are equal (case-insensitive), or both null
   */
  public static boolean equalsIgnoreCase(String s1, String s2) {
    if ((s1 == null) && (s2 == null)) {
      return true;
    }
    if ((s1 == null) || (s2 == null)) {
      return false;
    }
    return s1.toLowerCase().equals(s2.toLowerCase());
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
   * Converts a long to a string.
   *
   * @param l
   *          a long to be converted.
   * @return a string representation of the argument in base 10.
   */
  public static String fromLong(long l) {
    return Long.toString(l);
  }

  /**
   * 1234567890123...
   *
   * @param len
   *          length to generate
   * @return the string
   */
  public static String getSequentialString(int len) {
    return getSequentialString(len, null, null, 0);
  }

  /**
   * 1234567890123...
   *
   * @param len
   *          length to generate
   * @param indexOfLineBreak
   *          the column index for inserting line breaks
   * @return the string
   */
  public static String getSequentialString(int len, int indexOfLineBreak) {
    return getSequentialString(len, null, null, indexOfLineBreak);
  }

  /**
   * [s]234567890123...[e]
   *
   * @param len
   *          length to generate
   * @param s
   *          value of the first character. null = "1"
   * @param e
   *          value of the last character. null = [0-9] of the end position
   * @return the string
   */
  public static String getSequentialString(int len, String s, String e) {
    return getSequentialString(len, s, e, 0);
  }

  /**
   * [s]234567890123...[e]
   *
   * @param len
   *          length to generate
   * @param s
   *          value of the first character. null = "1"
   * @param e
   *          value of the last character. null = [0-9] of the end position
   * @param indexOfLineBreak
   *          the column index for inserting line breaks
   * @return the string
   */
  public static String getSequentialString(int len, String s, String e, int indexOfLineBreak) {
    byte[] bytes = new byte[len];
    int lastIndex = len - 1;
    byte b;
    int c = 0;
    for (int i = 0; i < len; i++) {
      c++;
      if (i == 0) {
        if (s == null) {
          b = 0x31;
        } else {
          b = s.getBytes()[0];
        }
      } else if (i == lastIndex) {
        if (s == null) {
          b = (byte) (c % 10 + 0x30);
        } else {
          b = e.getBytes()[0];
        }
      } else {
        b = (byte) (c % 10 + 0x30);
      }
      bytes[i] = b;
      if ((indexOfLineBreak != 0) && ((c % indexOfLineBreak) == 0)) {
        i++;
        bytes[i] = 0x0A;
      }
    }
    return new String(bytes);
  }

  /**
   * Indicates whether the string contains a BOM character.
   *
   * @param s
   *          The string to check for
   * @return true if the string has the BOM, otherwise false
   */
  public static boolean hasBom(String s) {
    if (s != null) {
      if ((s.length() != 0) && (s.codePointAt(0) == 0xFEFF)) {
        return true;
      }
    }
    return false;
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
   * Returns if the specified string contains only ASCII characters.
   *
   * @param s
   *          the string to check, may be null
   * @return true if the string is ASCII
   */
  public static boolean isAscii(String s) {
    if (s == null) {
      return false;
    }
    Pattern p = Pattern.compile("^\\p{ASCII}*$");
    Matcher m = p.matcher(s);
    return m.matches();
  }

  /**
   * Returns if the given string is alphabetic.
   *
   * @param s
   *          the string
   * @return true if the all characters are alphabetic
   */
  public static boolean isAlphabetic(String s) {
    for (int i = 0; i < s.length(); i++) {
      if (!isAlphabetic(s, i)) {
        return false;
      }
    }
    return true;
  };

  /**
   * Returns if the specified character is alphabetic.
   *
   * @param s
   *          the string
   * @param p
   *          position to check
   * @return true if the character is alphabetic
   */
  public static boolean isAlphabetic(String s, int p) {
    int cp = s.codePointAt(p);
    return (((cp >= 0x41) && (cp <= 0x5A)) || ((cp >= 0x61) && (cp <= 0x7A)));
  };

  /**
   * Returns if the given string is upper case.
   *
   * @param s
   *          the string
   * @return true if the all characters are upper case
   */
  public static boolean isUpperCase(String s) {
    for (int i = 0; i < s.length(); i++) {
      if (!isUpperCase(s, i)) {
        return false;
      }
    }
    return true;
  };

  /**
   * Returns if the specified character is upper case.
   *
   * @param s
   *          the string
   * @param p
   *          position to check
   * @return true if the character is upper case
   */
  public static boolean isUpperCase(String s, int p) {
    int cp = s.codePointAt(p);
    return ((cp >= 0x41) && (cp <= 0x5A));
  };

  /**
   * Returns if the given string is lower case.
   *
   * @param s
   *          the string
   * @return true if the all characters are lower case
   */
  public static boolean isLowerCase(String s) {
    for (int i = 0; i < s.length(); i++) {
      if (!isLowerCase(s, i)) {
        return false;
      }
    }
    return true;
  };

  /**
   * Returns if the specified character is lower case.
   *
   * @param s
   *          the string
   * @param p
   *          position to check
   * @return true if the character is lower case
   */
  public static boolean isLowerCase(String s, int p) {
    int cp = s.codePointAt(p);
    return ((cp >= 0x61) && (cp <= 0x7A));
  };

  /**
   * Returns if the specified string contains non ASCII characters.
   *
   * @param s
   *          the string to check, may be null
   * @return true if the string is non ASCII
   */
  public static boolean isNonAscii(String s) {
    if (s == null) {
      return false;
    }
    return !isAscii(s);
  }

  /**
   * Returns if the specified string is empty (""), null or whitespace only.
   *
   * @param str
   *          the string to check, may be null
   * @return true if the string is null, empty or whitespace only
   */
  public static boolean isBlank(String str) {
    if (str == null) {
      return true;
    }
    return str.trim().equals("");
  }

  /**
   * Returns if the specified string is not empty (""), not null and not
   * whitespace only.
   *
   * @param str
   *          the string to check, may be null
   * @return true if the string is not null, not empty and not whitespace only
   */
  public static boolean isNotBlank(String str) {
    return !isBlank(str);
  }

  /**
   * Returns if the specified string is empty ("") or null.
   *
   * @param str
   *          the string to check, may be null
   * @return true if the string is empty or null
   */
  public static boolean isEmpty(String str) {
    if (str == null) {
      return true;
    }
    return str.equals("");
  }

  /**
   * Returns if the specified string is not empty ("") and not null.
   *
   * @param str
   *          the string to check, may be null
   * @return true if the string is not empty and not null
   */
  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  /**
   * Returns if the given string is a float.
   *
   * @param s
   *          the string to check
   * @return true if the string is a float
   */
  public static boolean isFloat(String s) {
    if (s == null) {
      return false;
    }
    return match(s, "^-?\\d+\\.\\d+$");
  }

  /**
   * Returns if the given string is an integer.
   *
   * @param s
   *          the string to check
   * @return true if the string is an integer
   */
  public static boolean isInteger(String s) {
    if (s == null) {
      return false;
    }
    return match(s, "^-?\\d+$");
  }

  /**
   * Returns if the given string is not a number.
   *
   * @param s
   *          the string to check
   * @return true if not a number
   */
  public static boolean isNaN(String s) {
    return !isNumeric(s);
  }

  /**
   * Returns if the given string is a numerical value.
   *
   * @param s
   *          the string to check
   * @return true if the string is a numerical value
   */
  public static boolean isNumeric(String s) {
    return (isInteger(s) || isFloat(s));
  }

  /**
   * Returns if the given string is a number.
   *
   * @param s
   *          the string to check
   * @return true if the string is a number
   */
  public static boolean isNumber(String s) {
    if (s == null) {
      return false;
    }
    return match(s, "^\\d+$");
  }

  /**
   * Returns if the specified character is a number.
   *
   * @param s
   *          the string
   * @param p
   *          position to check
   * @return true if the character is a number
   */
  public static boolean isNumber(String s, int p) {
    int cp = s.codePointAt(p);
    return ((cp >= 0x30) && (cp <= 0x39));
  };

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
   * Left pad a String with a specified string.
   *
   * @param str
   *          the string to pad out, may be null
   * @param pad
   *          the string to pad with
   * @param len
   *          the total length of padded string
   * @return left padded string, null if null string input
   */
  public static String leftPad(String str, String pad, int len) {
    return leftPad(str, pad, len, false);
  }

  /**
   * Left pad a String with a specified string.
   *
   * @param str
   *          the string to pad out, may be null
   * @param pad
   *          the string to pad with
   * @param len
   *          the total length of padded string
   * @param align
   *          if true, aligns the output to exactly the specified length
   * @return left padded string, null if null string input
   */
  public static String leftPad(String str, String pad, int len, boolean align) {
    if (str == null) {
      return null;
    }
    int padLen = len - str.length();
    if (padLen <= 0) {
      if (align) {
        str = str.substring(0, len);
      }
      return str;
    }
    String pd = repeat(pad, padLen);
    StringBuilder sb = new StringBuilder(pd);
    sb.append(str);
    return sb.toString();
  }

  /**
   * Left pad a String with a specified string. (Full-width compatible)
   *
   * @param str
   *          the string to pad out, may be null
   * @param pad
   *          the string to pad with
   * @param len
   *          the total length of padded string (count in half-width)
   * @return left padded string, null if null string input
   */
  public static String leftPadW(String str, String pad, int len) {
    return leftPadW(str, pad, len, false);
  }

  /**
   * Left pad a String with a specified string. (Full-width compatible)
   *
   * @param str
   *          the string to pad out, may be null
   * @param pad
   *          the string to pad with
   * @param len
   *          the total length of padded string (count in half-width)
   * @param align
   *          if true, aligns the output to exactly the specified length
   * @return left padded string, null if null string input
   */
  public static String leftPadW(String str, String pad, int len, boolean align) {
    if (str == null) {
      return null;
    }
    int padLen = len - lenW(str);
    if (padLen <= 0) {
      if ((align) && (str.length() > len)) {
        str = str.substring(0, len);
      }
      return str;
    }
    String pd = repeat(pad, padLen);
    StringBuilder sb = new StringBuilder(pd);
    sb.append(str);
    return sb.toString();
  }

  /**
   * Counts the length of the string, with 1 for half-width and 2 for full-width.
   *
   * @param str
   *          the string to count
   * @return the length of the string
   */
  public static int lenW(String str) {
    int n = 0;
    for (int i = 0, len = str.length(); i < len; i++) {
      int p = str.codePointAt(i);
      if ((p <= 0x7F) || ((p >= 0xFF61) && (p <= 0xFF9F))) {
        // ASCII, half-width Kana
        n++;
      } else {
        n += 2;
      }
      if (p >= 0x10000) {
        i++;
      }
    }
    return n;
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
    if ((target == null) || (regex == null)) {
      return false;
    }
    Pattern p = Pattern.compile(regex, flags);
    Matcher m = p.matcher(target);
    return m.find();
  }

  /**
   * Returns a new double initialized to the value represented by the specified
   * String.
   *
   * @param s
   *          the string to be parsed
   * @return the double value represented by the string argument. returns 0 in
   *         case of errors.
   */
  public static double parseDouble(String s) {
    return parseFloat(s, 0);
  }

  /**
   * Returns a new double initialized to the value represented by the specified
   * String.
   *
   * @param s
   *          the string to be parsed
   * @param defaultValue
   *          the value used if the string does not contain a parsable float.
   * @return the double value represented by the string argument.
   */
  public static double parseDouble(String s, double defaultValue) {
    double v;
    try {
      v = Double.parseDouble(s);
    } catch (Exception e) {
      v = defaultValue;
    }
    return v;
  }

  /**
   * Returns a new float initialized to the value represented by the specified
   * String.
   *
   * @param s
   *          the string to be parsed
   * @return the float value represented by the string argument. returns 0 in case
   *         of errors.
   */
  public static float parseFloat(String s) {
    return parseFloat(s, 0);
  }

  /**
   * Returns a new float initialized to the value represented by the specified
   * String.
   *
   * @param s
   *          the string to be parsed
   * @param defaultValue
   *          the value used if the string does not contain a parsable float.
   * @return the float value represented by the string argument.
   */
  public static float parseFloat(String s, float defaultValue) {
    float v;
    try {
      v = Float.parseFloat(s);
    } catch (Exception e) {
      v = defaultValue;
    }
    return v;
  }

  /**
   * Parses the string argument as a signed decimal integer.
   *
   * @param s
   *          a String containing the int representation to be parsed
   * @return the integer value represented by the argument in decimal. returns 0
   *         in case of errors.
   */
  public static int parseInt(String s) {
    return parseInt(s, 0);
  }

  /**
   * Parses the string argument as a signed decimal integer.
   *
   * @param s
   *          a String containing the int representation to be parsed
   * @param defaultValue
   *          the value used if the string does not contain a parsable integer.
   * @return the integer value represented by the argument in decimal.
   */
  public static int parseInt(String s, int defaultValue) {
    int v;
    try {
      v = Integer.parseInt(s);
    } catch (Exception e) {
      v = defaultValue;
    }
    return v;
  }

  /**
   * Parses the string argument as a signed decimal long.
   *
   * @param s
   *          a String containing the long representation to be parsed
   * @return the integer value represented by the argument in decimal. returns 0
   *         in case of errors.
   */
  public static long parseLong(String s) {
    return parseInt(s, 0);
  }

  /**
   * Parses the string argument as a signed decimal long.
   *
   * @param s
   *          a String containing the long representation to be parsed
   * @param defaultValue
   *          the value used if the string does not contain a parsable integer.
   * @return the integer value represented by the argument in decimal.
   */
  public static long parseLong(String s, long defaultValue) {
    long v;
    try {
      v = Long.parseLong(s);
    } catch (Exception e) {
      v = defaultValue;
    }
    return v;
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
   * Removes the BOM character from the specified string.
   *
   * @param s
   *          the string to remove the BOM
   * @return a string with BOM removed. If the input is null, the output will also
   *         be null.
   */
  public static String removeBom(String s) {
    if (hasBom(s)) {
      s = s.substring(1);
    }
    return s;
  }

  /**
   * Removes white space characters and newlines from the given string.
   *
   * @param str
   *          a string to replace
   * @return the replaced string
   */
  public static String removeSpaceNewline(String str) {
    return str.replaceAll("\\s", "").replaceAll("\\r\\n|\\r|\\n", "");
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
   * Reverses a string.
   *
   * @param s
   *          the string to reverse, may be null
   * @return reversed string
   */
  public static String reverse(String s) {
    if (s == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder(s);
    return sb.reverse().toString();
  }

  /**
   * Right pad a String with a specified string.
   *
   * @param str
   *          the string to pad out, may be null
   * @param pad
   *          the string to pad with
   * @param len
   *          the total length of padded string
   * @return right padded string, null if null string input
   */
  public static String rightPad(String str, String pad, int len) {
    return rightPad(str, pad, len, false);
  }

  /**
   * Right pad a String with a specified string.
   *
   * @param str
   *          the string to pad out, may be null
   * @param pad
   *          the string to pad with
   * @param len
   *          the total length of padded string
   * @param align
   *          if true, aligns the output to exactly the specified length
   * @return right padded string, null if null string input
   */
  public static String rightPad(String str, String pad, int len, boolean align) {
    if (str == null) {
      return null;
    }
    int padLen = len - str.length();
    if (padLen <= 0) {
      if (align) {
        str = str.substring(0, len);
      }
      return str;
    }
    String pd = repeat(pad, padLen);
    StringBuilder sb = new StringBuilder(str);
    sb.append(pd);
    return sb.toString();
  }

  /**
   * Right pad a String with a specified string. (Full-width compatible)
   *
   * @param str
   *          the string to pad out, may be null
   * @param pad
   *          the string to pad with
   * @param len
   *          the total length of padded string (count in half-width)
   * @return right padded string, null if null string input
   */
  public static String rightPadW(String str, String pad, int len) {
    return rightPadW(str, pad, len, false);
  }

  /**
   * Right pad a String with a specified string. (Full-width compatible)
   *
   * @param str
   *          the string to pad out, may be null
   * @param pad
   *          the string to pad with
   * @param len
   *          the total length of padded string (count in half-width)
   * @param align
   *          if true, aligns the output to exactly the specified length
   * @return right padded string, null if null string input
   */
  public static String rightPadW(String str, String pad, int len, boolean align) {
    if (str == null) {
      return null;
    }
    int padLen = len - lenW(str);
    if (padLen <= 0) {
      if ((align) && (str.length() > len)) {
        str = str.substring(0, len);
      }
      return str;
    }
    String pd = repeat(pad, padLen);
    StringBuilder sb = new StringBuilder(str);
    sb.append(pd);
    return sb.toString();
  }

  /**
   * Returns a letter replaced by ROT5.<br>
   * <br>
   * ROT5 is a practice similar to ROT13 that applies to numeric digits (0 to 9).
   *
   * @param s
   *          the source string
   * @return the rotated string
   */
  public static String rot5(String s) {
    return rot5(s, 5);
  }

  /**
   * Returns a letter replaced by ROT5.<br>
   *
   * @param s
   *          the source string
   * @param n
   *          number to shift
   * @return the rotated string
   */
  public static String rot5(String s, int n) {
    if (s == null) {
      return null;
    }
    if ((n < -9) || (n > 9)) {
      n = n % 10;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      int cp = s.codePointAt(i);
      if (isNumber(s, i)) {
        cp += n;
        if (cp > 0x39) {
          cp = 0x2F + (cp - 0x39);
        } else if (cp < 0x30) {
          cp = 0x3A - (0x30 - cp);
        }

      }
      sb.append((char) cp);
    }
    return sb.toString();
  }

  /**
   * Returns a letter replaced by ROT13.<br>
   * <br>
   * ROT13 (ROTate by 13 places) replaces each letter by its partner 13 characters
   * further along the alphabet. For example, HELLO becomes URYYB (or, conversely,
   * URYYB becomes HELLO again). <br>
   * <br>
   * ABCDEFGHIJKLM<br>
   * NOPQRSTUVWXYZ<br>
   * <br>
   * HELLO<br>
   * URYYB
   *
   * @param s
   *          the source string
   * @return the rotated string
   */
  public static String rot13(String s) {
    return rot13(s, 13);
  }

  /**
   * Returns a letter replaced by ROT13.<br>
   *
   * @param s
   *          the source string
   * @param n
   *          number to shift
   * @return the rotated string
   */
  public static String rot13(String s, int n) {
    if (s == null) {
      return null;
    }
    if ((n < -25) || (n > 25)) {
      n = n % 26;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      int cp = s.codePointAt(i);
      if (isAlphabetic(s, i)) {
        cp += n;
        if (isUpperCase(s, i)) {
          if (cp > 0x5A) {
            cp = 0x40 + (cp - 0x5A);
          } else if (cp < 0x41) {
            cp = 0x5B - (0x41 - cp);
          }
        } else if (isLowerCase(s, i)) {
          if (cp > 0x7A) {
            cp = 0x60 + (cp - 0x7A);
          } else if (cp < 0x61) {
            cp = 0x7B - (0x61 - cp);
          }
        }
      }
      sb.append((char) cp);
    }
    return sb.toString();
  }

  /**
   * Returns a letter replaced by ROT18.<br>
   * <br>
   * ROT18 is ROT13 + ROT5 (18 = 13 + 5). Also known as ROT13.5.
   *
   * @param s
   *          the source string
   * @return the rotated string
   */
  public static String rot18(String s) {
    if (s == null) {
      return null;
    }
    s = rot13(s);
    s = rot5(s);
    return s;
  }

  /**
   * Returns a letter replaced by ROT18.
   *
   * @param s
   *          the source string
   * @param n
   *          number to shift
   * @return the rotated string
   */
  public static String rot18(String s, int n) {
    if (s == null) {
      return null;
    }
    s = rot13(s, n);
    s = rot5(s, n);
    return s;
  }

  /**
   * Returns a letter replaced by ROT47.<br>
   * <br>
   * ROT47 is a derivative of ROT13 which, in addition to scrambling the basic
   * letters, treats numbers and common symbols. Instead of using the sequence A–Z
   * as the alphabet, ROT47 uses a larger set of characters from the common
   * character encoding known as ASCII. Specifically, the 7-bit printable
   * characters, excluding space, from decimal 33 '!' through 126 '~', 94 in
   * total.<br>
   * <br>
   * Example:<br>
   * The Quick Brown Fox Jumps Over The Lazy Dog.<br>
   * enciphers to<br>
   * %96 "F:4&lt; qC@H? u@I yF&gt;AD ~G6C %96 {2KJ s@8]
   *
   * @param s
   *          the source string
   * @return the rotated string
   */
  public static String rot47(String s) {
    return rot47(s, 47);
  }

  /**
   * Returns a letter replaced by ROT47.<br>
   *
   * @param s
   *          the source string
   * @param n
   *          number to shift
   * @return the rotated string
   */
  public static String rot47(String s, int n) {
    if (s == null) {
      return null;
    }
    if ((n < -93) || (n > 93)) {
      n = n % 94;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      int cp = s.codePointAt(i);
      if ((cp >= 0x21) && (cp <= 0x7E)) {
        if (n < 0) {
          cp += n;
          if (cp < 0x21)
            cp = 0x7F - (0x21 - cp);
        } else {
          cp = ((cp - 0x21 + n) % 94) + 0x21;
        }
      }
      sb.append((char) cp);
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
    int size = src.length();
    for (int i = 0; i < size; i++) {
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
              if (i < size - 1) {
                start = i + 1;
                len = size - start;
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
   * Converts from half-width to full-width.
   *
   * @param s
   *          the string to convert
   * @return the converted string
   */
  public static String toFullWidth(String s) {
    StringBuffer sb = new StringBuffer(s);
    for (int i = 0, len = sb.length(); i < len; i++) {
      char c = sb.charAt(i);
      if (c >= '!' && c <= '~') {
        sb.setCharAt(i, (char) (c + 65248));
      } else if (c == ' ') {
        sb.setCharAt(i, (char) (0x3000));
      }
    }
    return sb.toString();
  }

  /**
   * Converts from full-width to half-width.
   *
   * @param s
   *          the string to convert
   * @return the converted string
   */
  public static String toHalfWidth(String s) {
    StringBuffer sb = new StringBuffer(s);
    for (int i = 0, len = sb.length(); i < len; i++) {
      char c = sb.charAt(i);
      if (c >= '！' && c <= '～') {
        sb.setCharAt(i, (char) (c - 65248));
      } else if (c == '　') {
        sb.setCharAt(i, ' ');
      } else if ((c == '“') || (c == '”')) {
        sb.setCharAt(i, '"');
      } else if (c == '‘') {
        sb.setCharAt(i, '`');
      } else if (c == '’') {
        sb.setCharAt(i, '\'');
      } else if (c == '〜') {
        sb.setCharAt(i, '~');
      } else if (c == '￥') {
        sb.setCharAt(i, '\\');
      }
    }
    return sb.toString();
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
   * Replaces multiple spaces with a single space
   *
   * @param s
   *          the source string
   * @return the replaced string
   */
  public static String toSingleSpace(String s) {
    if (s == null) {
      return null;
    }
    return s.replaceAll(" {2,}", " ");
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
   * @param s
   *          numeric string
   * @return trimmed value
   */
  public static String trimZeros(String s) {
    if (s == null) {
      return s;
    }
    String[] p = s.trim().split("\\.");
    String i = trimLeadingZeros(p[0]);
    String d = "";
    if (p.length >= 2) {
      d = p[1];
    }
    d = trimTrailingZeros(d);
    String r = i;
    if (!d.equals("")) {
      r += "." + d;
    }
    return r;
  }

  /**
   * Strip leading zeros.
   *
   * @param s
   *          numeric string
   * @return trimmed value
   */
  public static String trimLeadingZeros(String s) {
    if (s == null) {
      return s;
    }
    return (s + "").replaceAll("^([^0]*)0+(.+)$", "$1$2");
  }

  /**
   * Strip trailing zeros.
   *
   * @param s
   *          numeric string
   * @return trimmed value
   */
  public static String trimTrailingZeros(String s) {
    if (s == null) {
      return s;
    }
    return (s + "").replaceAll("(.+?)0*$", "$1");
  };

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
   * Returns the Excel column letter at the position moved by offset.<br>
   * <br>
   * e.g., ("A", 1) = "B", ("B", -1) = "A"
   *
   * @param s
   *          the origin column letter ("A"-"XFD")
   * @param offset
   *          offset
   * @return the letter corresponding to the index ("A-"XFD")
   */
  public static String xlscol(String s, int offset) {
    int n = xlscol(s);
    if (n == 0) {
      return "";
    }
    n += offset;
    return xlscol(n);
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
      StrPermResult r = _getString(chars, index, null);
      return r.s;
    }

    public static StrPermResult _getString(String chars, long index, List<Integer> a) {
      if (index <= 0) {
        StrPermResult r = new StrPermResult("", null);
        return r;
      }

      String[] tbl = chars.split("");
      int len = tbl.length;
      long st;
      if (a == null) {
        a = new ArrayList<>();
        a.add(-1);
        st = 0;
      } else {
        st = index - 1;
      }

      for (long i = st; i < index; i++) {
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

      StrPermResult r = new StrPermResult(sb.toString(), a);
      return r;
    }
  }

  public static class StrPermResult {
    public String s;
    public List<Integer> a;

    public StrPermResult(String s, List<Integer> a) {
      this.s = s;
      this.a = a;
    }
  }

}
