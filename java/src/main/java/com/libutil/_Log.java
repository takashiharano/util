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

// public class Log extends _Log {
//
//   public static void init(int level, String moduleName) {
//     instance = new Log();
//     setLevel(level);
//     setModuleName(moduleName);
//   }
//
// }

public class _Log {

  protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXX";
  protected static _Log instance;

  public enum LogLevel {
    FATAL("F", 1), ERROR("E", 2), WARN("W", 3), INFO("I", 4), DEBUG("D", 5);

    private String typeSymbol;
    private int level;

    private LogLevel(String typeSymbol, int level) {
      this.typeSymbol = typeSymbol;
      this.level = level;
    }

    public String getTypeSymbol() {
      return typeSymbol;
    }

    public int getLevel() {
      return level;
    }
  }

  protected String dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
  protected int outputLevel = LogLevel.DEBUG.level;
  protected String moduleName = null;

  /**
   * Returns the instance.
   *
   * @return instance
   */
  public static _Log getInstance() {
    if (instance == null) {
      instance = new _Log();
    }
    return instance;
  }

  /**
   * Initialize the module.
   */
  public _Log() {
    this(5);
  }

  /**
   * Initialize the module.
   *
   * @param level
   *          log level
   */
  public _Log(int level) {
    this(level, null);
  }

  /**
   * Initialize the module.
   *
   * @param level
   *          log level
   * @param module
   *          module name
   */
  public _Log(int level, String module) {
    this(level, module, DEFAULT_DATE_TIME_FORMAT);
  }

  /**
   * Initialize the module.
   *
   * @param level
   *          log level
   * @param module
   *          module name
   * @param format
   *          date time format like "yyyy-MM-dd' 'HH:mm:ss.SSS XX"
   */
  public _Log(int level, String module, String format) {
    outputLevel = level;
    moduleName = module;
    dateTimeFormat = format;
  }

  /**
   * Returns current output level.
   *
   * @return output level
   */
  public static int getLevel() {
    return getInstance().getOutputLevel();
  }

  /**
   * Returns current output level.
   *
   * @return output level
   */
  public int getOutputLevel() {
    return outputLevel;
  }

  /**
   * Sets output level.
   *
   * @param level
   *          output level
   */
  public void setOutputLevel(int level) {
    outputLevel = level;
  }

  /**
   * Sets output level.
   *
   * @param level
   *          output level
   */
  public static void setLevel(int level) {
    getInstance().setOutputLevel(level);
  }

  /**
   * Sets output level.
   *
   * @param level
   *          output level
   */
  public void setOutputLevel(LogLevel level) {
    int lv = level.getLevel();
    setLevel(lv);
  }

  /**
   * Sets output level.
   *
   * @param level
   *          output level
   */
  public static void setLevel(LogLevel level) {
    getInstance().setOutputLevel(level);
  }

  /**
   * Sets date-time format.
   *
   * @param format
   *          date-time format like "yyyy-MM-dd' 'HH:mm:ss.SSS XX"
   */
  public static void setDateTimeFormat(String format) {
    getInstance().setLogDateTimeFormat(format);
  }

  /**
   * Sets date-time format.
   *
   * @param format
   *          date-time format like "yyyy-MM-dd' 'HH:mm:ss.SSS XX"
   */
  public void setLogDateTimeFormat(String format) {
    dateTimeFormat = format;
  }

  /**
   * Sets module name.
   *
   * @param name
   *          module name
   */
  public void setLoggingModuleName(String name) {
    moduleName = name;
  }

  /**
   * Sets module name.
   *
   * @param name
   *          module name
   */
  public static void setModuleName(String name) {
    getInstance().setLoggingModuleName(name);
  }

  /**
   * Debug log.
   *
   * @param o
   *          The Object to be printed.
   */
  public static void d(Object o) {
    getInstance().out(o, LogLevel.DEBUG, 0, true);
  }

  /**
   * Information log.
   *
   * @param o
   *          The Object to be printed.
   */
  public static void i(Object o) {
    getInstance().out(o, LogLevel.INFO, 0, false);
  }

  /**
   * Warning log.
   *
   * @param o
   *          The Object to be printed.
   */
  public static void w(Object o) {
    getInstance().out(o, LogLevel.WARN, 0, false);
  }

  /**
   * Error log.
   *
   * @param o
   *          The Object to be printed.
   */
  public static void e(Object o) {
    getInstance().error(o, LogLevel.ERROR, null);
  }

  /**
   * Error log.
   *
   * @param o
   *          The Object to be printed.
   * @param t
   *          The throwable object to be printed.
   */
  public static void e(Object o, Throwable t) {
    getInstance().error(o, LogLevel.ERROR, t);
  }

  /**
   * Fatal log.
   *
   * @param o
   *          The Object to be printed.
   */
  public static void f(Object o) {
    getInstance().error(o, LogLevel.FATAL, null);
  }

  /**
   * Fatal log.
   *
   * @param o
   *          The Object to be printed.
   * @param t
   *          The throwable object to be printed.
   */
  public static void f(Object o, Throwable t) {
    getInstance().error(o, LogLevel.FATAL, t);
  }

  /**
   * _Log with time measurement.
   *
   * @param msg
   *          message
   * @return current time
   */
  public static long t(String msg) {
    long t1 = System.currentTimeMillis();
    String m = "[T+00:00:00.000] " + msg;
    getInstance().out(m, LogLevel.DEBUG, 0, true);
    return t1;
  }

  /**
   * _Log with time measurement.
   *
   * @param msg
   *          message
   * @param t0
   *          starting time
   * @return current time
   */
  public static long t(String msg, long t0) {
    long t1 = System.currentTimeMillis();
    long delta = t1 - t0;
    String m = "[T+" + DateTime.formatTime(delta, "HH:mm:ss.SSS") + "] " + msg;
    getInstance().out(m, LogLevel.DEBUG, 0, true);
    return t1;
  }

  /**
   * Print stack trace.
   */
  public static void stack() {
    stack(0);
  }

  /**
   * Print stack trace.
   *
   * @param offset
   *          frame offset to output
   */
  public static void stack(int offset) {
    _Log log = getInstance();
    StackTraceElement stack[] = (new Throwable()).getStackTrace();
    log.printLog("Stack:");
    for (int i = offset; i < stack.length; i++) {
      StackTraceElement frame = stack[i];
      String className = frame.getClassName();
      String methodName = frame.getMethodName();
      int lineNum = frame.getLineNumber();
      log.printLog("    at " + className + "#" + methodName + " (L:" + lineNum + ")");
    }
  }

  protected static void printStackTraceString(Throwable t, LogLevel lv, boolean printLine) {
    _Log log = getInstance();
    log.out(t.toString(), lv, 2, printLine);
    log._printStackTraceString(t, lv, printLine);
    while ((t = t.getCause()) != null) {
      log.out("Caused by: " + t.toString(), lv, 2, printLine);
      log._printStackTraceString(t, lv, printLine);
    }
  }

  protected void _printStackTraceString(Throwable t, LogLevel lv, boolean printLine) {
    StackTraceElement[] stack = t.getStackTrace();
    for (int i = 0; i < stack.length; i++) {
      StackTraceElement frame = stack[i];
      String clazz = frame.getClassName();
      String method = frame.getMethodName();
      int line = frame.getLineNumber();
      String m = "    at " + clazz + "#" + method + " (L:" + line + ")";
      out(m, lv, 3, printLine);
    }
  }

  /**
   * Output log message.
   *
   * @param o
   *          message string or object
   * @param lv
   *          log level
   * @param stackFrameOffset
   *          offset of stackframe
   * @param printLine
   *          set true to output method():file:line
   */
  protected void out(Object o, LogLevel lv, int stackFrameOffset, boolean printLine) {
    int logLevel = lv.getLevel();
    if (logLevel > outputLevel) {
      return;
    }
    String message = buildMessage(o, lv, stackFrameOffset, printLine);
    if (logLevel <= LogLevel.FATAL.getLevel()) {
      printLogF(message);
    } else if (logLevel <= LogLevel.ERROR.getLevel()) {
      printLogE(message);
    } else if (logLevel <= LogLevel.WARN.getLevel()) {
      printLogW(message);
    } else if (logLevel <= LogLevel.INFO.getLevel()) {
      printLogI(message);
    } else if (logLevel <= LogLevel.DEBUG.getLevel()) {
      printLogD(message);
    } else {
      printLog(message);
    }
  }

  /**
   * Output error message.
   *
   * @param o
   *          message string or object
   * @param lv
   *          output lebel
   * @param t
   *          The throwable object to be printed
   */
  protected void error(Object o, LogLevel lv, Throwable t) {
    if (lv.getLevel() > outputLevel) {
      return;
    }
    boolean printLine = true;
    if (o instanceof Throwable) {
      printStackTraceString((Throwable) o, lv, printLine);
    } else {
      out(o, lv, 1, printLine);
    }
    if (t != null) {
      printStackTraceString(t, lv, printLine);
    }
  }

  protected String buildMessage(Object o, LogLevel lv, int stackFrameOffset, boolean printLine) {
    String time = DateTime.formatDateTime(dateTimeFormat);
    Thread th = Thread.currentThread();
    long tid = th.getId();

    StringBuilder sb = new StringBuilder(time);
    sb.append(" ");

    sb.append("[");
    sb.append(lv.getTypeSymbol());
    sb.append("]");

    if (moduleName != null) {
      sb.append("[");
      sb.append(moduleName);
      sb.append("]");
    }

    sb.append("[tid:");
    sb.append(tid);
    sb.append("]");

    if (printLine) {
      StackTraceElement[] stack = th.getStackTrace();
      int stackFrameIndex = 4 + stackFrameOffset;
      StackTraceElement frame = stack[stackFrameIndex];
      String method = frame.getMethodName() + "()";
      String fileName = frame.getFileName();
      int line = frame.getLineNumber();
      String fileLine = method + ":" + fileName + ":" + line;
      sb.append("[");
      sb.append(fileLine);
      sb.append("]");
    }

    sb.append(" ");
    if (o == null) {
      sb.append("null");
    } else {
      String typeName = o.getClass().getTypeName();
      if ("byte[]".equals(typeName)) {
        byte[] b = (byte[]) o;
        sb.append("byte[" + b.length + "] = " + HexDumper.toHex(b, 1024));
      } else if ("int[]".equals(typeName)) {
        int[] wk = (int[]) o;
        sb.append("[");
        for (int i = 0; i < wk.length; i++) {
          if (i > 0) {
            sb.append(", ");
          }
          sb.append(wk[i]);
        }
        sb.append("]");
      } else if ("java.lang.String[]".equals(typeName)) {
        String[] wk = (String[]) o;
        sb.append("[");
        for (int i = 0; i < wk.length; i++) {
          if (i > 0) {
            sb.append(", ");
          }
          sb.append(wk[i]);
        }
        sb.append("]");
      } else {
        sb.append(o.toString());
      }
    }

    return sb.toString();
  }

  protected void printLog(Object o) {
    System.out.println(o);
  }

  protected void printLogD(Object o) {
    System.out.println(o);
  }

  protected void printLogI(Object o) {
    System.out.println(o);
  }

  protected void printLogW(Object o) {
    System.out.println(o);
  }

  protected void printLogE(Object o) {
    System.out.println(o);
  }

  protected void printLogF(Object o) {
    System.out.println(o);
  }

}