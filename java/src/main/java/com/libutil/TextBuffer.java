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

/**
 * A buffer for building text.
 */
public class TextBuffer {

  private StringBuilder buf;
  private String lineseparator = "\n";

  /**
   * Constructs a string builder with no characters in it and an initial capacity
   * of 16 characters.
   */
  public TextBuffer() {
    this.buf = new StringBuilder();
  }

  /**
   * Constructs a string builder with no characters in it and an initial capacity
   * specified by the capacity argument.
   *
   * @param capacity
   *          the initial capacity
   */
  public TextBuffer(int capacity) {
    this.buf = new StringBuilder(capacity);
  }

  /**
   * Constructs a string builder with no characters in it and an initial capacity
   * specified by the capacity argument.
   *
   * @param capacity
   *          the initial capacity
   * @param lineseparator
   *          line separator
   */
  public TextBuffer(int capacity, String lineseparator) {
    this.buf = new StringBuilder(capacity);
    this.lineseparator = lineseparator;
  }

  /**
   * Constructs a string builder initialized to the contents of the specified
   * string.
   *
   * @param str
   *          the initial contents of the buffer
   */
  public TextBuffer(String str) {
    this.buf = new StringBuilder(str);
  }

  /**
   * Constructs a string builder initialized to the contents of the specified
   * string.
   *
   * @param str
   *          the initial contents of the buffer
   * @param lineseparator
   *          line separator
   */
  public TextBuffer(String str, String lineseparator) {
    this.buf = new StringBuilder(str);
    this.lineseparator = lineseparator;
  }

  /**
   * Appends the string representation of the boolean argument to the sequence.
   *
   * @param b
   *          a boolean
   */
  public void append(boolean b) {
    buf.append(b);
  }

  /**
   * Appends the string representation of the char argument to this sequence.
   *
   * @param c
   *          a char
   */
  public void append(char c) {
    buf.append(c);
  }

  /**
   * Appends the string representation of the char array argument to this
   * sequence.
   *
   * @param str
   *          the characters to be appended.
   */
  public void append(char[] str) {
    buf.append(str);
  }

  /**
   * Appends the string representation of the double argument to this sequence.
   *
   * @param d
   *          a double
   */
  public void append(double d) {
    buf.append(d);
  }

  /**
   * Appends the string representation of the float argument to this sequence.
   *
   * @param f
   *          a float
   */
  public void append(float f) {
    buf.append(f);
  }

  /**
   * Appends the string representation of the int argument to this sequence.
   *
   * @param i
   *          an int
   */
  public void append(int i) {
    buf.append(i);
  }

  /**
   * Appends the string representation of the long argument to this sequence.
   *
   * @param lng
   *          a long
   */
  public void append(long lng) {
    buf.append(lng);
  }

  /**
   * Appends the string representation of the Object argument.<br>
   * The overall effect is exactly as if the argument were converted to a string
   * by the method String.valueOf(Object), and the characters of that string were
   * then appended to this character sequence.
   *
   * @param obj
   *          an Object
   */
  public void append(Object obj) {
    buf.append(obj);
  }

  /**
   * Appends the specified string to this character sequence.
   *
   * @param str
   *          a string
   */
  public void append(String str) {
    buf.append(str);
  }

  /**
   * Appends the specified StringBuffer to this sequence.
   *
   * @param sb
   *          the StringBuffer to append
   */
  public void append(StringBuilder sb) {
    buf.append(sb);
  }

  /**
   * Appends a line separator to the sequence.
   */
  public void appendln() {
    buf.append(lineseparator);
  }

  /**
   * Appends the string representation of the boolean argument and line separator
   * to the sequence.
   *
   * @param b
   *          a boolean
   */
  public void appendln(boolean b) {
    buf.append(b);
    buf.append(lineseparator);
  }

  /**
   * Appends the string representation of the char argument and line separator to
   * this sequence.
   *
   * @param c
   *          a char
   */
  public void appendln(char c) {
    buf.append(c);
    buf.append(lineseparator);
  }

  /**
   * Appends the string representation of the char array argument and line
   * separator to this sequence.
   *
   * @param str
   *          the characters to be appended
   */
  public void appendln(char[] str) {
    buf.append(str);
    buf.append(lineseparator);
  }

  /**
   * Appends the string representation of the double argument and line separator
   * to this sequence.
   *
   * @param d
   *          a double
   */
  public void appendln(double d) {
    buf.append(d);
    buf.append(lineseparator);
  }

  /**
   * Appends the string representation of the float argument and line separator to
   * this sequence.
   *
   * @param f
   *          a float
   */
  public void appendln(float f) {
    buf.append(f);
    buf.append(lineseparator);
  }

  /**
   * Appends the string representation of the long argument and line separator to
   * this sequence.
   *
   * @param lng
   *          a long
   */
  public void appendln(long lng) {
    buf.append(lng);
    buf.append(lineseparator);
  }

  /**
   * Appends the string representation of the Object argument and line separator.
   *
   * @param obj
   *          an Object
   */
  public void appendln(Object obj) {
    buf.append(obj);
    buf.append(lineseparator);
  }

  /**
   * Appends the specified string and line separator to this character sequence.
   *
   * @param str
   *          a string
   */
  public void appendln(String str) {
    buf.append(str);
    buf.append(lineseparator);
  }

  /**
   * Appends the specified StringBuffer and line separator to this sequence.
   *
   * @param sb
   *          the StringBuffer to append
   */
  public void appendln(StringBuilder sb) {
    buf.append(sb);
    buf.append(lineseparator);
  }

  /**
   * Returns the current capacity. The capacity is the amount of storage available
   * for newly inserted characters, beyond which an allocation will occur.
   *
   * @return the current capacity
   */
  public int capacity() {
    return buf.capacity();
  }

  /**
   * Removes the characters in a substring of this sequence. The substring begins
   * at the specified start and extends to the character at index end - 1 or to
   * the end of the sequence if no such character exists. If start is equal to
   * end, no changes are made.
   *
   * @param start
   *          The beginning index, inclusive
   * @param end
   *          The ending index, exclusive
   * @throws StringIndexOutOfBoundsException
   *           if start is negative, greater than length(), or greater than end.
   */
  public void delete(int start, int end) {
    buf.delete(start, end);
  }

  /**
   * Returns the index within this string of the first occurrence of the specified
   * substring. The integer returned is the smallest value k such that:<br>
   * this.toString().startsWith(str, <i>k</i>)<br>
   * is true.
   *
   * @param str
   *          any string
   * @return if the string argument occurs as a substring within this object, then
   *         the index of the first character of the first such substring is
   *         returned; if it does not occur as a substring, -1 is returned.
   */
  public int indexOf(String str) {
    return buf.indexOf(str);
  }

  /**
   * Returns the index within this string of the first occurrence of the specified
   * substring, starting at the specified index. The integer returned is the
   * smallest value k for which:<br>
   * <br>
   * k -ge Math.min(fromIndex, this.length()) and<br>
   * this.toString().startsWith(str, k)<br>
   * <br>
   * If no such value of k exists, then -1 is returned.
   *
   * @param str
   *          the substring for which to search
   * @param fromIndex
   *          the index from which to start the search
   * @return the index within this string of the first occurrence of the specified
   *         substring, starting at the specified index.
   */
  public int indexOf(String str, int fromIndex) {
    return buf.indexOf(str, fromIndex);
  }

  /**
   * Returns the index within this string of the rightmost occurrence of the
   * specified substring. The rightmost empty string "" is considered to occur at
   * the index value this.length(). The returned index is the largest value k such
   * that:<br>
   * <br>
   * this.toString().startsWith(str, k)<br>
   * <br>
   * is true.
   *
   * @param str
   *          the substring to search for
   * @return if the string argument occurs one or more times as a substring within
   *         this object, then the index of the first character of the last such
   *         substring is returned. If it does not occur as a substring, -1 is
   *         returned.
   */
  public int lastIndexOf(String str) {
    return buf.lastIndexOf(str);
  }

  /**
   * Returns the index within this string of the last occurrence of the specified
   * substring. The integer returned is the largest value k such that:<br>
   * <br>
   * k -le Math.min(fromIndex, this.length()) and<br>
   * this.toString().startsWith(str, k)<br>
   * <br>
   * If no such value of k exists, then -1 is returned.
   *
   * @param str
   *          the substring to search for
   * @param fromIndex
   *          the index to start the search from
   * @return the index within this sequence of the last occurrence of the
   *         specified substring.
   */
  public int lastIndexOf(String str, int fromIndex) {
    return buf.lastIndexOf(str, fromIndex);
  }

  /**
   * Returns the length (character count).
   *
   * @return the length of the sequence of characters currently represented by
   *         this object
   */
  public int length() {
    return buf.length();
  }

  /**
   * Appends a line feed to this sequence.
   */
  public void newLine() {
    buf.append(lineseparator);
  }

  /**
   * Sets line separator string for this character sequence.
   *
   * @param sep
   *          The line separator string
   */
  public void setLineSeparator(String sep) {
    this.lineseparator = sep;
  }

  /**
   * Returns a new String that contains a subsequence of characters currently
   * contained in this character sequence. The substring begins at the specified
   * index and extends to the end of this sequence.
   *
   * @param start
   *          The beginning index, inclusive
   * @return The new string
   */
  public String substring(int start) {
    return buf.substring(start);
  }

  /**
   * Returns a new String that contains a subsequence of characters currently
   * contained in this sequence. The substring begins at the specified start and
   * extends to the character at index end - 1.
   *
   * @param start
   *          The beginning index, inclusive
   * @param end
   *          The ending index, exclusive
   * @return The new string
   */
  public String substring(int start, int end) {
    return buf.substring(start, end);
  }

  /**
   * Returns a new String that contains a subsequence of characters currently
   * contained in this character sequence. The substring begins at the specified
   * index and extends to the end of this sequence.<br>
   * If start is a negative number, the index starts counting from the end of the
   * string.
   *
   * @param start
   *          The beginning index, inclusive
   * @return The new string
   */
  public String substr(int start) {
    int len = buf.length();
    if (start < 0) {
      start = len + start;
      if (start < 0) {
        start = 0;
      }
    } else if (start >= len) {
      return "";
    }
    int end = start + len;
    if (end > len) {
      end = len;
    }
    return buf.substring(start, end);
  }

  /**
   * Returns a new String that contains a subsequence of characters currently
   * contained in this sequence. The substring begins at the specified start and
   * extends to the character at index start + len.
   *
   * @param start
   *          The beginning index, inclusive
   * @param len
   *          The length of a new String
   * @return The new string
   */
  public String substr(int start, int len) {
    int l = buf.length();
    if ((start < 0) && (len < 0)) {
      return "";
    }
    if (len < 0) {
      len *= (-1);
      start -= len;
    }
    if (start < 0) {
      return "";
    } else if (start >= l) {
      start = l - 1;
    }
    int end = start + len;
    if (end > l) {
      end = l;
    }
    return buf.substring(start, end);
  }

  /**
   * Returns a string representing the data in this sequence. A new String object
   * is allocated and initialized to contain the character sequence currently
   * represented by this object. This String is then returned. Subsequent changes
   * to this sequence do not affect the contents of the String.
   *
   * @return a string representation of this sequence of characters
   */
  public String toString() {
    return buf.toString();
  }

}
