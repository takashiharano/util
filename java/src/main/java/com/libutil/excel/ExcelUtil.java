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
package com.libutil.excel;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class ExcelUtil {

  /**
   * Get the sheet object with the given name.
   *
   * @param workbook
   *          Workbook
   * @param name
   *          of the sheet
   * @return Sheet with the name provided or null if it does not exist
   */
  public static Sheet getSheet(Workbook workbook, String name) {
    Sheet sheet = workbook.getSheet(name);
    return sheet;
  }

  /**
   * Get the Sheet object at the given index.
   *
   * @param workbook
   *          workbook
   * @param index
   *          index of the sheet number (0-based physical and logical)
   * @return Sheet at the provided index
   */
  public static Sheet getSheet(Workbook workbook, int index) {
    Sheet sheet = workbook.getSheetAt(index);
    return sheet;
  }

  /**
   * Get the cell with the given position.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @return the cell
   */
  public static Cell getCell(Sheet sheet, String address) {
    CellReference reference = new CellReference(address);
    int rowIndex = reference.getRow();
    Row row = sheet.getRow(rowIndex);
    Cell cell = null;
    if (row != null) {
      int colIndex = reference.getCol();
      cell = row.getCell(colIndex);
    }
    return cell;
  }

  /**
   * Get the value of the cell as a string.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @return the value
   */
  public static String getStringCellValue(Sheet sheet, String address) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    String value = cell.getStringCellValue();
    return value;
  }

  /**
   * Set a string value for the cell.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @param value
   *          the value
   */
  public static void setCellValue(Sheet sheet, String address, String value) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    cell.setCellValue(value);
  }

  /**
   * Get the value of the cell as a number.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @return the numeric cell value
   */
  public static double getNumericCellValue(Sheet sheet, String address) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    double value = cell.getNumericCellValue();
    return value;
  }

  /**
   * Set a numeric value for the cell.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @param value
   *          the value
   */
  public static void setCellValue(Sheet sheet, String address, int value) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    cell.setCellValue(value);
  }

  /**
   * Get the value of the cell as a boolean.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @return true or false
   */
  public static boolean getBooleanCellValue(Sheet sheet, String address) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    boolean value = cell.getBooleanCellValue();
    return value;
  }

  /**
   * Set a boolean value for the cell.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @param value
   *          true or false
   */
  public static void setCellValue(Sheet sheet, String address, boolean value) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    cell.setCellValue(value);
  }

  /**
   * Get the value of the cell as a date.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @return date
   */
  public static Date getDateCellValue(Sheet sheet, String address) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    Date value = cell.getDateCellValue();
    return value;
  }

  /**
   * Converts the supplied date to its equivalent Excel numeric value and sets
   * that into the cell.
   *
   * @param sheet
   *          sheet
   * @param address
   *          address
   * @param value
   *          date
   */
  public static void setCellValue(Sheet sheet, String address, Date value) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    cell.setCellValue(value);
  }

  /**
   * Return a formula for the cell, for example, SUM(A1:C1).
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @return the formula
   */
  public static String getCellFormula(Sheet sheet, String address) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    String value = cell.getCellFormula();
    return value;
  }

  /**
   * Sets formula for this cell.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @param formula
   *          the formula
   */
  public static void setCellFormula(Sheet sheet, String address, String formula) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    cell.setCellFormula(formula);
  }

  /**
   * Returns comment associated with this cell.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @return the cell comment
   */
  public static Comment getCellComment(Sheet sheet, String address) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    Comment value = cell.getCellComment();
    return value;
  }

  /**
   * Assign a comment to this cell.
   *
   * @param sheet
   *          sheet
   * @param address
   *          cell address
   * @param comment
   *          the cell comment
   */
  public static void setCellComment(Sheet sheet, String address, Comment comment) {
    Cell cell = ExcelUtil.getCell(sheet, address);
    cell.setCellComment(comment);
  }

  /**
   * Returns column index from the given column name.
   *
   * @param name
   *          the column name
   * @return "A" = 0, "B" = 1, ... "Z" = 25, "AA" = 26
   */
  public static int getColIndex(String name) {
    int index = CellReference.convertColStringToIndex(name);
    return index;
  }

  /**
   * Returns column name at the index.
   *
   * @param index
   *          0 origin
   * @return 0 = "A", 1 = "B", ... 25 = "Z", 26 = "AA"
   */
  public static String getColName(int index) {
    String colName = CellReference.convertNumToColString(index);
    return colName;
  }

  /**
   * Create a new cell object within the row and return it.
   *
   * @param sheet
   *          sheet
   * @param rowIndex
   *          row index
   * @param colIndex
   *          column index
   * @return Cell a high level representation of the created cell.
   */
  public static Cell createCell(Sheet sheet, int rowIndex, int colIndex) {
    return createCell(sheet, rowIndex, colIndex, null);
  }

  /**
   * Create a new cell object within the row and return it.
   *
   * @param sheet
   *          sheet
   * @param rowIndex
   *          row index
   * @param colIndex
   *          column index
   * @param value
   *          value
   * @return Cell a high level representation of the created cell.
   */
  public static Cell createCell(Sheet sheet, int rowIndex, int colIndex, String value) {
    Row row = sheet.getRow(rowIndex);
    if (row == null) {
      row = sheet.createRow(rowIndex);
    }
    Cell cell = row.createCell(colIndex);
    if (value != null) {
      cell.setCellValue(value);
    }
    return cell;
  }

  /**
   * Set the background color of the cell with RGB value.
   *
   * @param cellStyle
   *          cell style
   * @param colorCode
   *          #rrggbb
   */
  public static void setCellColor(CellStyle cellStyle, String colorCode) {
    Color color = Color.decode(colorCode);
    DefaultIndexedColorMap map = new DefaultIndexedColorMap();
    XSSFColor xssFColor = new XSSFColor(color, map);
    ((XSSFCellStyle) cellStyle).setFillForegroundColor(xssFColor);
  }

  /**
   * Set the background color of the cell with the indexed color value.
   *
   * @param cellStyle
   *          cell style
   * @param indexedColor
   *          background color
   */
  public static void setCellColor(CellStyle cellStyle, IndexedColors indexedColor) {
    cellStyle.setFillForegroundColor(indexedColor.getIndex());
  }

  /**
   * Set the font name of the cell.
   *
   * @param workbook
   *          workbook
   * @param cell
   *          cell
   * @param fontName
   *          font name
   */
  public static void setCellFont(Workbook workbook, Cell cell, String fontName) {
    setCellFont(workbook, cell, fontName, 0, null);
  }

  /**
   * Set the font size of the cell.
   *
   * @param workbook
   *          workbook
   * @param cell
   *          cell
   * @param fontSize
   *          font size
   */
  public static void setCellFont(Workbook workbook, Cell cell, int fontSize) {
    setCellFont(workbook, cell, null, fontSize, null);
  }

  /**
   * Set the font name and size of the cell.
   *
   * @param workbook
   *          workbook
   * @param cell
   *          cell
   * @param fontName
   *          font name
   * @param fontSize
   *          font size
   */
  public static void setCellFont(Workbook workbook, Cell cell, String fontName, int fontSize) {
    setCellFont(workbook, cell, fontName, fontSize, null);
  }

  /**
   * Set the font of the cell.
   *
   * @param workbook
   *          workbook
   * @param cell
   *          cell
   * @param fontName
   *          font name
   * @param fontSize
   *          font size
   * @param colorCode
   *          #rrggbb
   */
  public static void setCellFont(Workbook workbook, Cell cell, String fontName, int fontSize, String colorCode) {
    Font font = workbook.createFont();

    if (fontName != null) {
      font.setFontName(fontName);
    }

    if (fontSize > 0) {
      System.out.println("aaa");
      font.setFontHeightInPoints((short) fontSize);
    }

    if (colorCode != null) {
      Color color = Color.decode(colorCode);
      DefaultIndexedColorMap map = new DefaultIndexedColorMap();
      XSSFColor xssFColor = new XSSFColor(color, map);
      ((XSSFFont) font).setColor(xssFColor);
    }

    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setFont(font);
    cell.setCellStyle(cellStyle);
  }

  /**
   * Set the font color of the cell.
   *
   * @param workbook
   *          workbook
   * @param cell
   *          cell
   * @param colorCode
   *          #rrggbb
   */
  public static void setCellFontColor(Workbook workbook, Cell cell, String colorCode) {
    setCellFont(workbook, cell, null, 0, colorCode);
  }

  /**
   * Set the width of the cell.
   *
   * @param sheet
   *          sheet
   * @param columnIndex
   *          column index
   * @param width
   *          width
   */
  public static void setColumnWidth(Sheet sheet, int columnIndex, int width) {
    int w = width * 256;
    sheet.setColumnWidth(columnIndex, w);
  }

  /**
   * Set the height of the row.
   *
   * @param row
   *          row
   * @param height
   *          height
   */
  public static void setRowHeight(Row row, int height) {
    short h = (short) (height * 20);
    row.setHeight(h);
  }

  /**
   * Merge the cells.
   *
   * @param sheet
   *          sheet
   * @param firstRow
   *          first row
   * @param firstCol
   *          first column
   * @param lastRow
   *          last row
   * @param lastCol
   *          last column
   */
  public static void addMergedRegion(Sheet sheet, int firstRow, int firstCol, int lastRow, int lastCol) {
    sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
  }

  /**
   * Load an Excel book from the given path.
   *
   * @param path
   *          File path to load
   * @return Workbook object
   */
  public static Workbook loadWorkBook(String path) {
    File file = new File(path);
    Workbook workbook = null;

    try {
      workbook = WorkbookFactory.create(file);
    } catch (EncryptedDocumentException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return workbook;
  }

  /**
   * Write the Excel book to the file.
   *
   * @param workbook
   *          workbook
   * @param path
   *          file path to save
   */
  public static void saveExcelBook(Workbook workbook, String path) {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(path);
      workbook.write(fos);
    } catch (Exception e) {
      System.out.println(e.toString());
    } finally {
      try {
        fos.close();
      } catch (Exception e) {
        System.out.println(e.toString());
      }
    }
  }

}
