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

import java.util.List;

/**
 * Builds a CSV text. https://www.ietf.org/rfc/rfc4180.txt
 */
public class CsvBuilder {

  private static final String DEFAULT_SEPARATOR = ",";
  private static final String DEFAULT_LINE_BREAK = "\r\n";
  private static final String DEFAULT_QUOTATION = "\"";
  private static final String DEFAULT_ESCAPE_CHAR = "\"";

  private StringBuilder buffer;
  private String escape;
  private String lineBreak;
  private String quotation;
  private String separator;
  private boolean shouldQuote;
  private int colCount;
  private int rowCount;

  /**
   * Constructs a CSV builder with the separator "," and no quotes.
   */
  public CsvBuilder() {
    this(DEFAULT_SEPARATOR, false);
  }

  /**
   * Constructs a CSV builder with the specified separator and no quotes.
   *
   * @param separator
   *          the separator
   */
  public CsvBuilder(String separator) {
    this(separator, false);
  }

  /**
   * Constructs a CSV builder with the separator ",".
   *
   * @param shouldQuote
   *          if true, builds with quotations
   */
  public CsvBuilder(boolean shouldQuote) {
    this(DEFAULT_SEPARATOR, shouldQuote);
  }

  /**
   * Constructs a CSV builder.
   *
   * @param separator
   *          the separator
   * @param shouldQuote
   *          if true, builds with quotations
   */
  public CsvBuilder(String separator, boolean shouldQuote) {
    buffer = new StringBuilder();
    this.separator = separator;
    this.shouldQuote = shouldQuote;
    this.escape = DEFAULT_ESCAPE_CHAR;
    this.lineBreak = DEFAULT_LINE_BREAK;
    this.quotation = DEFAULT_QUOTATION;
    this.rowCount = 1;
  }

  /**
   * Appends a string value to this sequence.
   *
   * @param value
   *          the value to append
   */
  public void append(String value) {
    if (value == null) {
      value = "";
    }
    _append(value, shouldQuote);
  }

  /**
   * Appends an integer value to this sequence.
   *
   * @param value
   *          the value to append
   */
  public void append(int value) {
    _append(Integer.toString(value), shouldQuote);
  }

  /**
   * Appends a long value to this sequence.
   *
   * @param value
   *          the value to append
   */
  public void append(long value) {
    _append(Long.toString(value), shouldQuote);
  }

  /**
   * Appends a float value to this sequence.
   *
   * @param value
   *          the value to append
   */
  public void append(float value) {
    _append(Float.toString(value), shouldQuote);
  }

  /**
   * Appends a double value to this sequence.
   *
   * @param value
   *          the value to append
   */
  public void append(double value) {
    _append(Double.toString(value), shouldQuote);
  }

  /**
   * Appends a boolean value to this sequence.
   *
   * @param value
   *          the value to append
   */
  public void append(boolean value) {
    _append(Boolean.toString(value), shouldQuote);
  }

  /**
   * Appends string values to this sequence using an array.
   *
   * @param values
   *          the array to append
   */
  public void append(String[] values) {
    for (int i = 0; i < values.length; i++) {
      append(values[i]);
    }
  }

  /**
   * Appends string values to this sequence using a list.
   *
   * @param values
   *          the list to append
   */
  public void append(List<String> values) {
    for (int i = 0, size = values.size(); i < size; i++) {
      append(values.get(i));
    }
  }

  /**
   * Appends integer values to this sequence using an array.
   *
   * @param values
   *          the array to append
   */
  public void append(int[] values) {
    for (int i = 0; i < values.length; i++) {
      append(values[i]);
    }
  }

  /**
   * Appends long values to this sequence using an array.
   *
   * @param values
   *          the array to append
   */
  public void append(long[] values) {
    for (int i = 0; i < values.length; i++) {
      append(values[i]);
    }
  }

  /**
   * Appends float values to this sequence using an array.
   *
   * @param values
   *          the array to append
   */
  public void append(float[] values) {
    for (int i = 0; i < values.length; i++) {
      append(values[i]);
    }
  }

  /**
   * Appends double values to this sequence using an array.
   *
   * @param values
   *          the array to append
   */
  public void append(double[] values) {
    for (int i = 0; i < values.length; i++) {
      append(values[i]);
    }
  }

  /**
   * Appends boolean values to this sequence using an array.
   *
   * @param values
   *          the array to append
   */
  public void append(boolean[] values) {
    for (int i = 0; i < values.length; i++) {
      append(values[i]);
    }
  }

  /**
   * Appends a string as is as a chunk to this sequence.
   *
   * <pre>
   * e.g.,
   * CHUNK="bbb,ccc"
   * aaa,CHUNK
   * </pre>
   *
   * Note that the counter values ​​are inconsistent.
   *
   * @param chunk
   *          the chunk data
   */
  public void appendChunk(String chunk) {
    if (colCount > 0) {
      buffer.append(separator);
    }
    buffer.append(chunk);
    colCount++;
  }

  /**
   * Appends a line break to this sequence.
   */
  public void nextRecord() {
    buffer.append(lineBreak);
    colCount = 0;
    rowCount++;
  }

  /**
   * Sets the escape character for this sequence.
   *
   * @param escape
   *          the escape character
   */
  public void setEscapeChar(String escape) {
    this.escape = escape;
  }

  /**
   * Sets the line delimiter for this sequence.
   *
   * @param lineBreak
   *          the line delimiter
   */
  public void setLineBreak(String lineBreak) {
    this.lineBreak = lineBreak;
  }

  /**
   * Sets the quotation character for this sequence.
   *
   * @param quotation
   *          the quotation character
   */
  public void setQuotationChar(String quotation) {
    this.quotation = quotation;
  }

  /**
   * Sets the separator for this sequence.
   *
   * @param separator
   *          the separator
   */
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  /**
   * Returns the count of rows.
   *
   * @return the count of rows
   */
  public int countRows() {
    return rowCount;
  }

  /**
   * Returns the count of columns.
   *
   * @return the count of columns
   */
  public int countColumns() {
    return colCount;
  }

  /**
   * Returns the CSV string representation of this sequence.
   *
   * @return the CSV string
   */
  public String toString() {
    return buffer.toString();
  }

  private void _append(String value, boolean shouldQuote) {
    if (colCount > 0) {
      buffer.append(separator);
    }
    if (shouldQuote) {
      value = quote(value, quotation, escape);
    } else if (value.contains("\r\n") || value.contains("\n") || value.contains("\r") || value.contains(quotation)
        || value.contains(separator)) {
      value = quote(value, quotation, escape);
    }
    buffer.append(value);
    colCount++;
  }

  private String quote(String src, String quot, String esc) {
    String s = src.replace(quot, esc + quot);
    StringBuilder sb = new StringBuilder();
    sb.append(quot);
    sb.append(s);
    sb.append(quot);
    return sb.toString();
  }

}
