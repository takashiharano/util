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

import java.io.File;
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

  private String separator;
  private String quotation;

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
    this(separator, DEFAULT_QUOTATION);
  }

  /**
   * Constructs a CSV parser with the specified separator and quotation.
   *
   * @param separator
   *          the character that the parser will treat as the separator
   * @param quotation
   *          the character for quote
   */
  public CsvParser(String separator, String quotation) {
    this.separator = separator;
    setQuotation(quotation);
  }

  /**
   * Load the CSV file and parses the contents.
   *
   * @param path
   *          the file path to read
   * @return The list of elements. [ROW][COL]<br>
   *         Returns null if the file does not exist, or in case of a read error.
   */
  public String[][] load(String path) {
    File file = new File(path);
    return load(file);
  }

  /**
   * Load the CSV file and parses the contents.
   *
   * @param file
   *          the file object to read
   * @return The list of elements. [ROW][COL]<br>
   *         Returns null if the file does not exist, or in case of a read error.
   */
  public String[][] load(File file) {
    String text = FileUtil.readText(file);
    if (text == null) {
      return null;
    }
    return parse(text);
  }

  /**
   * Parses the CSV text and returns an array of elements.
   *
   * @param csvText
   *          The string to parse
   * @return The list of elements. [ROW][COL]
   */
  public String[][] parse(String csvText) {
    csvText = csvText.replaceAll("\r\n|\r", "\n");
    List<List<String>> rows = new ArrayList<>();
    List<String> cols = new ArrayList<>();
    int enveloped = 0;
    int quotCount = 0;
    int colBeginIndex = 0;
    int len = csvText.length();
    int i;
    for (i = 0; i < len; i++) {
      String c = String.valueOf(csvText.charAt(i));
      if (c.equals(quotation)) {
        quotCount++;
        if (colBeginIndex == i) {
          enveloped = -1;
        }
      } else if (separator.equals(c) || "\n".equals(c)) {
        if (enveloped == 1) {
          if (!isQuotedPropery(csvText, len, i, quotCount)) {
            enveloped = 0;
          }
        }
        if ((enveloped == 0) || (enveloped != 0) && (quotCount % 2 == 0)) {
          String val = getColumnValue(csvText, colBeginIndex, i, enveloped);
          cols.add(val);
          colBeginIndex = i + 1;
          enveloped = 0;
          quotCount = 0;
          if ("\n".equals(c)) {
            storeRow(rows, cols);
            cols = new ArrayList<>();
          }
        }
      } else {
        if (enveloped == -1) {
          enveloped = (quotCount % 2 == 0) ? 0 : 1;
        }
      }
    }

    int lastIndex = i - 1;
    if (lastIndex > 0) {
      String lastCh = String.valueOf(csvText.charAt(lastIndex));
      if ((enveloped != 0) && !quotation.equals(lastCh)) {
        enveloped = 0;
        colBeginIndex++;
      }
    }

    String val = getColumnValue(csvText, colBeginIndex, i, enveloped);
    cols.add(val);
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
   * Parses one record of the CSV text and returns an array of the elements.<br>
   * Generally, a record is one row of data.
   *
   * @param csvText
   *          one row of the CSV
   * @return The list of elements
   */
  public String[] parseOneRecord(String csvText) {
    csvText = csvText.replaceAll("\r\n|\r", "\n");

    List<String> cols = new ArrayList<>();
    int enveloped = 0;
    int quotCount = 0;
    int colBeginIndex = 0;
    int len = csvText.length();
    int i;
    for (i = 0; i < len; i++) {
      String c = String.valueOf(csvText.charAt(i));
      if (c.equals(quotation)) {
        quotCount++;
        if (colBeginIndex == i) {
          enveloped = -1;
        }
      } else if (separator.equals(c)) {
        if (enveloped == 1) {
          if (!isQuotedPropery(csvText, len, i, quotCount)) {
            enveloped = 0;
          }
        }
        if ((enveloped == 0) || (enveloped != 0) && (quotCount % 2 == 0)) {
          String val = getColumnValue(csvText, colBeginIndex, i, enveloped);
          cols.add(val);
          colBeginIndex = i + 1;
          enveloped = 0;
          quotCount = 0;
        }
      } else {
        if (enveloped == -1) {
          enveloped = (quotCount % 2 == 0) ? 0 : 1;
        }
      }
    }

    int lastIndex = i - 1;
    if (lastIndex > 0) {
      String lastCh = String.valueOf(csvText.charAt(lastIndex));
      if ((enveloped != 0) && !quotation.equals(lastCh)) {
        enveloped = 0;
        colBeginIndex++;
      }
    }

    String val = getColumnValue(csvText, colBeginIndex, i, enveloped);
    cols.add(val);

    String[] fields = new String[cols.size()];
    cols.toArray(fields);

    return fields;
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
    this.escapedQuotation = quotation + quotation;
    this.quotedEmpty = quotation + quotation;
  }

  private String getColumnValue(String s, int colBeginIndex, int p, int enveloped) {
    String val = s.substring(colBeginIndex, p);
    if (quotedEmpty.equals(val)) {
      val = "";
    } else {
      if ((enveloped != 0) && (val.length() >= 2)) {
        val = val.substring(1, val.length() - 1);
      }
      val = val.replace(escapedQuotation, quotation);
    }
    return val;
  }

  private void storeRow(List<List<String>> rows, List<String> cols) {
    rows.add(cols);
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

  private boolean isQuotedPropery(String s, int len, int pos, int quotCount) {
    if (quotCount % 2 == 0) {
      if (pos + 1 < len) {
        String lastCh = String.valueOf(s.charAt(pos - 1));
        if (!quotation.equals(lastCh)) {
          return false;
        }
      }
    }
    return true;
  }

}
