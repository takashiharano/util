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
//   public static void setup(int level, String moduleName) {
//     instance = new Log();
//     setLevel(level);
//     setModuleName(moduleName);
//   }
//
// }

/**
 * Logger.
 */
public class _Log {

  protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXX";
  protected static _Log instance;

  public static final int FLAG_TIME = 1;
  public static final int FLAG_LEVEL = 2;
  public static final int FLAG_MODULE_NAME = 4;
  public static final int FLAG_TID = 8;
  public static final int FLAG_LINE = 16;

  /**
   * Log level.
   */
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
  protected int flag = FLAG_TIME | FLAG_LEVEL | FLAG_MODULE_NAME | FLAG_TID | FLAG_LINE;
  protected long logT0 = 0;

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
   * Sets output flag.
   *
   * @param flag
   *          flag values
   */
  public void setOutputFlag(int flag) {
    this.flag = flag;
  }

  /**
   * Adds output flag.
   *
   * @param flag
   *          flag value
   */
  public void addOutputFlag(int flag) {
    this.flag |= flag;
  }

  /**
   * Removes output flag.
   *
   * @param flag
   *          flag value
   */
  public void removeOutputFlag(int flag) {
    this.flag &= ~flag;
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
  public void setOutputLevel(LogLevel level) {
    int lv = level.getLevel();
    setOutputLevel(lv);
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
   * Returns the instance.
   *
   * @return instance
   */
  public static synchronized _Log getInstance() {
    if (instance == null) {
      instance = new _Log();
    }
    return instance;
  }

  /**
   * Sets output flag.
   *
   * @param flag
   *          flag values
   */
  public static void setFlag(int flag) {
    getInstance().setOutputFlag(flag);
  }

  /**
   * Adds output flag.
   *
   * @param flag
   *          flag value
   */
  public static void addFlag(int flag) {
    getInstance().addOutputFlag(flag);
  }

  /**
   * Removes output flag.
   *
   * @param flag
   *          flag value
   */
  public static void removeFlag(int flag) {
    getInstance().removeOutputFlag(flag);
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
   * Logging with time measurement.
   *
   * @param msg
   *          message
   * @return elapsed time in milliseconds
   */
  public static long t(String msg) {
    long t1 = System.currentTimeMillis();
    _Log self = getInstance();
    long t0 = self.logT0;
    if (t0 == 0) {
      t0 = t1;
      self.logT0 = t0;
    }
    long delta = t1 - t0;
    String m = "[" + DateTime.formatTime(delta, "TsnHH:mm:ss.SSS") + "] " + msg;
    i(m);
    return delta;
  }

  /**
   * Logging with time measurement.
   *
   * @param msg
   *          message
   * @param t0
   *          offset; set 0 to reset
   * @return elapsed time in milliseconds
   */
  public static long t(String msg, long t0) {
    getInstance().logT0 = System.currentTimeMillis() - t0;
    return t(msg);
  }

  /**
   * Resets t0 for t().
   */
  public static void resetT0() {
    setT0(0);
  }

  /**
   * Sets t0 for t().
   *
   * @param t0
   *          offset; set 0 to reset
   */
  public static void setT0(long t0) {
    getInstance().logT0 = System.currentTimeMillis() - t0;
  }

  public static void print(Object o) {
    _Log l = getInstance();
    l.printLog(l.dump(o));
  }

  /**
   * Dumps code-points of the specified string.
   *
   * @param s
   *          the string to dump
   */
  public static void dumpString(String s) {
    if (s == null) {
      i("[null]");
    } else if (s.length() == 0) {
      i("[]");
    } else {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < s.length(); i++) {
        sb.append("[");
        sb.append(Integer.toString(s.codePointAt(i), 16).toUpperCase());
        sb.append("]");
      }
      i(sb.toString());
    }
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
    Thread th = Thread.currentThread();
    StringBuilder sb = new StringBuilder();

    if ((flag & FLAG_TIME) != 0) {
      String time = DateTime.now(dateTimeFormat);
      sb.append(time);
      sb.append(" ");
    }

    StringBuilder inf = new StringBuilder();
    if ((flag & FLAG_LEVEL) != 0) {
      inf.append("[");
      inf.append(lv.getTypeSymbol());
      inf.append("]");
    }

    if (((flag & FLAG_MODULE_NAME) != 0) && (moduleName != null)) {
      inf.append("[");
      inf.append(moduleName);
      inf.append("]");
    }

    if ((flag & FLAG_TID) != 0) {
      long tid = th.getId();
      inf.append("[tid:");
      inf.append(tid);
      inf.append("]");
    }

    if (((flag & FLAG_LINE) != 0) && printLine) {
      StackTraceElement[] stack = th.getStackTrace();
      int stackFrameIndex = 4 + stackFrameOffset;
      StackTraceElement frame = stack[stackFrameIndex];
      String method = frame.getMethodName() + "()";
      String fileName = frame.getFileName();
      int line = frame.getLineNumber();
      String fileLine = method + ":" + fileName + ":" + line;
      inf.append("[");
      inf.append(fileLine);
      inf.append("]");
    }

    if (inf.length() > 0) {
      sb.append(inf);
      sb.append(" ");
    }
    sb.append(dump(o));

    return sb.toString();
  }

  protected String dump(Object o) {
    StringBuilder sb = new StringBuilder();
    if (o == null) {
      sb.append("null");
    } else {
      String typeName = o.getClass().getTypeName();
      if ("byte[]".equals(typeName)) {
        byte[] b = (byte[]) o;
        sb.append("byte[" + b.length + "] = " + BinUtil.toHexString(b, 1024));
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
