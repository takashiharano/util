/*
 * The MIT License
 *
 * Copyright 2021 Takashi Harano
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

import java.util.ArrayList;
import java.util.List;

/**
 * Parses a CSV text.
 * <p>
 * This Parser is meant to parse according to the RFC4180 specification.<br>
 * https://www.ietf.org/rfc/rfc4180.txt
 * </p>
 */
public class CsvParser {

  private static final String DEFAULT_SEPARATOR = ",";
  private static final String DEFAULT_QUOTATION = "\"";
  private static final String DEFAULT_ESCAPE = "\"";

  private String separator;
  private String quotation;
  private String escapeChar;

  private String escapedQuotation;
  private String quotedEmpty;

  /**
   * Constructs a CSV parser with the separator ",".
   */
  public CsvParser() {
    this(DEFAULT_SEPARATOR);
  }

  /**
   * Constructs a CSV parser with the specified separator.
   *
   * @param separator
   *          the character that the parser will treat as the separator
   */
  public CsvParser(String separator) {
    this.separator = separator;
    this.quotation = DEFAULT_QUOTATION;
    this.escapeChar = DEFAULT_ESCAPE;
    this.escapedQuotation = escapeChar + quotation;
    this.quotedEmpty = quotation + quotation;
  }

  /**
   * Parses the CSV text and returns an array of elements.
   *
   * @param csvText
   *          The string to parse
   * @return The list of elements. [ROW][COL]
   */
  public String[][] parse(String csvText) {
    List<List<String>> rows = new ArrayList<>();
    List<String> cols = new ArrayList<>();
    boolean inBlock = false;
    boolean shouldRemoveQuot = false;
    int colBeginIndex = 0;
    int len = csvText.length();
    int i;
    for (i = 0; i < len; i++) {
      String c = String.valueOf(csvText.charAt(i));
      if (c.equals(quotation)) {
        if (colBeginIndex == i) {
          inBlock = true;
        } else if (isEndOfBlock(csvText, colBeginIndex, i, separator)) {
          if (inBlock) {
            shouldRemoveQuot = true;
          }
          inBlock = false;
        }
      } else if (separator.equals(c)) {
        if (!inBlock) {
          colBeginIndex = storeColumn(cols, csvText, colBeginIndex, i, shouldRemoveQuot);
          shouldRemoveQuot = false;
        }
      } else if ("\r".equals(c)) {
        if (!inBlock) {
          boolean f = true;
          int j = i + 1;
          if (j < len) {
            String c1 = String.valueOf(csvText.charAt(j));
            if ("\n".equals(c1)) {
              // CRLF
              colBeginIndex = storeColumn(cols, csvText, colBeginIndex, i, shouldRemoveQuot);
              shouldRemoveQuot = false;
              storeRow(rows, cols);
              cols = new ArrayList<>();
              f = false;
              colBeginIndex++;
              i++;
            }
          }
          if (f) {
            // CR
            colBeginIndex = storeColumn(cols, csvText, colBeginIndex, i, shouldRemoveQuot);
            shouldRemoveQuot = false;
            storeRow(rows, cols);
            cols = new ArrayList<>();
          }
        }
      } else if ("\n".equals(c)) {
        if (!inBlock) {
          // LF
          colBeginIndex = storeColumn(cols, csvText, colBeginIndex, i, shouldRemoveQuot);
          shouldRemoveQuot = false;
          storeRow(rows, cols);
          cols = new ArrayList<>();
        }
      }
    }
    colBeginIndex = storeColumn(cols, csvText, colBeginIndex, i, shouldRemoveQuot);
    if ((!((cols.size() == 1) && "".equals(cols.get(0)))) || (isLastLineEmpty(csvText))) {
      storeRow(rows, cols);
    }

    String[][] arrRows = new String[rows.size()][cols.size()];
    int size = rows.size();
    for (i = 0; i < size; i++) {
      List<String> row = rows.get(i);
      String[] aCols = new String[row.size()];
      row.toArray(aCols);
      arrRows[i] = aCols;
    }
    return arrRows;
  }

  /**
   * Parses a line of CSV text and returns an array of elements.
   *
   * @param line
   *          a line of CSV
   * @return The list of elements
   */
  public String[] parseLine(String line) {
    String[] arrCols;
    List<String> cols = new ArrayList<>();
    boolean inBlock = false;
    boolean shouldRemoveQuot = false;
    int colBeginIndex = 0;
    int len = line.length();
    int i;
    for (i = 0; i < len; i++) {
      String c = String.valueOf(line.charAt(i));
      if (c.equals(quotation)) {
        if (colBeginIndex == i) {
          inBlock = true;
        } else if (isEndOfBlock(line, colBeginIndex, i, separator)) {
          if (inBlock) {
            shouldRemoveQuot = true;
          }
          inBlock = false;
        }
      } else if (separator.equals(c)) {
        if (!inBlock) {
          colBeginIndex = storeColumn(cols, line, colBeginIndex, i, shouldRemoveQuot);
          shouldRemoveQuot = false;
        }
      } else if ("\r".equals(c)) {
        if (!inBlock) {
          boolean f = true;
          int j = i + 1;
          if (j < len) {
            String c1 = String.valueOf(line.charAt(j));
            if ("\n".equals(c1)) {
              // CRLF
              colBeginIndex = storeColumn(cols, line, colBeginIndex, i, shouldRemoveQuot);
              arrCols = new String[cols.size()];
              cols.toArray(arrCols);
              return arrCols;
            }
          }
          if (f) {
            // CR
            colBeginIndex = storeColumn(cols, line, colBeginIndex, i, shouldRemoveQuot);
            arrCols = new String[cols.size()];
            cols.toArray(arrCols);
            return arrCols;
          }
        }
      } else if ("\n".equals(c)) {
        if (!inBlock) {
          // LF
          colBeginIndex = storeColumn(cols, line, colBeginIndex, i, shouldRemoveQuot);
          arrCols = new String[cols.size()];
          cols.toArray(arrCols);
          return arrCols;
        }
      }
    }
    colBeginIndex = storeColumn(cols, line, colBeginIndex, i, shouldRemoveQuot);
    arrCols = new String[cols.size()];
    cols.toArray(arrCols);
    return arrCols;
  }

  /**
   * Returns the character that the parser will treat as the separator.
   *
   * @return the separator
   */
  public String getSeparator() {
    return separator;
  }

  /**
   * Sets the character that the parser will treat as the separator.
   *
   * @param separator
   *          the separator
   */
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  /**
   * Returns the character that the parser will treat as the quotation.
   *
   * @return the quotation
   */
  public String getQuotation() {
    return quotation;
  }

  /**
   * Sets the character that the parser will treat as the quotation.
   *
   * @param quotation
   *          the quotation
   */
  public void setQuotation(String quotation) {
    this.quotation = quotation;
    this.escapedQuotation = escapeChar + quotation;
    this.quotedEmpty = quotation + quotation;
  }

  /**
   * Returns the character that the parser will treat as the escape.
   *
   * @return the escape character
   */
  public String getEscapeChar() {
    return escapeChar;
  }

  /**
   * Sets the character that the parser will treat as the escape.
   *
   * @param escapeChar
   *          the escape character
   */
  public void setEscapeChar(String escapeChar) {
    this.escapeChar = escapeChar;
    this.escapedQuotation = escapeChar + quotation;
  }

  private int storeColumn(List<String> cols, String s, int colBeginIndex, int p, boolean shouldRemoveQuot) {
    String val = s.substring(colBeginIndex, p);
    if (quotedEmpty.equals(val)) {
      val = "";
    } else {
      if (shouldRemoveQuot && !isQuotEscaped(s)) {
        val = val.substring(1, val.length() - 1);
      }
      val = val.replace(escapedQuotation, quotation);
    }
    cols.add(val);
    return p + 1;
  }

  private void storeRow(List<List<String>> rows, List<String> cols) {
    rows.add(cols);
  }

  private boolean isEndOfBlock(String s, int beginIndex, int p, String separator) {
    if (p == s.length() - 1) {
      return true;
    }
    String c;
    int count = 0;
    for (int i = p; i >= beginIndex; i--) {
      c = String.valueOf(s.charAt(i));
      if (quotation.equals(c)) {
        count++;
      } else {
        break;
      }
    }
    if ((count == 2) && (p - beginIndex == 1)) {
      return true;
    } else if ((count % 2) == 0) {
      return false;
    }
    String nextCh = String.valueOf(s.charAt(p + 1));
    if (separator.equals(nextCh)) {
      return true;
    }
    return false;
  }

  private boolean isQuotEscaped(String s) {
    if (s.length() <= 4) {
      return false;
    }
    String start = s.substring(0, 2);
    String end = s.substring(s.length() - 2);
    if ((escapedQuotation.equals(start)) && (escapedQuotation.equals(end))) {
      return true;
    }
    return false;
  }

  private boolean isLastLineEmpty(String s) {
    if (s.length() <= 2) {
      return false;
    }
    String last = s.substring(s.length() - 2);
    if (quotedEmpty.equals(last)) {
      return true;
    }
    return false;
  }

}
