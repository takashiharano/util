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
 * Build CSV.<br>
 * https://www.ietf.org/rfc/rfc4180.txt
 */
public class CsvBuilder {
  private static final String LINE_DELIMITER = "\r\n";
  private static final String QUOTATION = "\"";
  private static final String ESCAPE_CHAR = "\"";

  private StringBuilder buffer;
  private String separator;
  private boolean quote;
  private int colCount;
  private int rowCount;

  public CsvBuilder() {
    this(",", false);
  }

  public CsvBuilder(String separator) {
    this(separator, false);
  }

  public CsvBuilder(boolean quote) {
    this(",", quote);
  }

  public CsvBuilder(String separator, boolean quote) {
    buffer = new StringBuilder();
    this.separator = separator;
    this.quote = quote;
    this.rowCount = 1;
  }

  public void append(String value) {
    if (value == null) {
      value = "";
    }
    _append(value, quote);
  }

  public void append(int value) {
    _append(Integer.toString(value), quote);
  }

  public void append(long value) {
    _append(Long.toString(value), quote);
  }

  public void append(float value) {
    _append(Float.toString(value), quote);
  }

  public void append(double value) {
    _append(Double.toString(value), quote);
  }

  public void append(boolean value) {
    _append(Boolean.toString(value), quote);
  }

  public void appendChunk(String chunk) {
    _append(chunk, false);
  }

  private void _append(String value, boolean quote) {
    if (colCount > 0) {
      buffer.append(separator);
    }
    if (quote) {
      value = StrUtil.quote(value, QUOTATION, ESCAPE_CHAR);
    } else if (value.contains("\r\n") || value.contains("\n") || value.contains("\r")) {
      value = StrUtil.quote(value, QUOTATION, ESCAPE_CHAR);
    }
    buffer.append(value);
    colCount++;
  }

  public void nextRecord() {
    buffer.append(LINE_DELIMITER);
    colCount = 0;
    rowCount++;
  }

  public int getRowCount() {
    return rowCount;
  }

  public int getColCount() {
    return colCount;
  }

  public String toString() {
    return buffer.toString();
  }

}
