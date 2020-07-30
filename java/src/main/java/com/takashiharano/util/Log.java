package com.takashiharano.util;

import java.math.BigDecimal;

public class Log {

  private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd' 'HH:mm:ss.SSS XX";
  private static String dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;

  public enum LogLevel {
    X("X", 0), FATAL("F", 1), ERROR("E", 2), WARN("W", 3), INFO("I", 4), DEBUG("D", 5);

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

  private static int outputLevel = LogLevel.DEBUG.level;
  private static String moduleName = null;

  /**
   * Initialize the Log module.
   *
   * @param level
   *          log level
   */
  public static void init(int level) {
    init(level, null);
  }

  /**
   * Initialize the Log module.
   *
   * @param level
   *          log level
   * @param module
   *          module name
   */
  public static void init(int level, String module) {
    init(level, module, DEFAULT_DATE_TIME_FORMAT);
  }

  /**
   * Initialize the Log module.
   *
   * @param level
   *          log level
   * @param module
   *          module name
   * @param format
   *          date time format like "yyyy-MM-dd' 'HH:mm:ss.SSS XX"
   */
  public static void init(int level, String module, String format) {
    outputLevel = level;
    moduleName = module;
    dateTimeFormat = format;
  }

  /**
   * Returns current output level.
   *
   * @return output level
   */
  public static int getOutputLevel() {
    return outputLevel;
  }

  /**
   * Sets output level.
   *
   * @param level
   *          output level
   */
  public static void setOutputLevel(LogLevel level) {
    int lv = level.getLevel();
    setOutputLevel(lv);
  }

  /**
   * Sets output level.
   *
   * @param level
   */
  public static void setOutputLevel(int level) {
    outputLevel = level;
  }

  /**
   * Returns current date-time format.
   *
   * @return date-time format
   */
  public static String getDateTimeFormat() {
    return dateTimeFormat;
  }

  /**
   * Sets date-time format.
   *
   * @param format
   *          date-time format like "yyyy-MM-dd' 'HH:mm:ss.SSS XX"
   */
  public static void setDateTimeFormat(String format) {
    dateTimeFormat = format;
  }

  /**
   * Returns current module name.
   *
   * @return module name
   */
  public static String getModuleName() {
    return moduleName;
  }

  /**
   * Sets module name.
   *
   * @param name
   *          module name
   */
  public static void setModuleName(String name) {
    moduleName = name;
  }

  public static void x(Object o) {
    Log.out(o, LogLevel.X, 0, true);
  }

  /**
   * Debug log.
   *
   * @param o
   */
  public static void d(Object o) {
    Log.out(o, LogLevel.DEBUG, 0, true);
  }

  /**
   * Information log.
   *
   * @param o
   */
  public static void i(Object o) {
    Log.out(o, LogLevel.INFO, 0, false);
  }

  /**
   * Warning log.
   *
   * @param o
   */
  public static void w(Object o) {
    Log.out(o, LogLevel.WARN, 0, false);
  }

  /**
   * Error log.
   *
   * @param o
   */
  public static void e(Object o) {
    Log.error(o, LogLevel.ERROR, null);
  }

  /**
   * Error log.
   *
   * @param o
   * @param t
   */
  public static void e(Object o, Throwable t) {
    Log.error(o, LogLevel.ERROR, t);
  }

  /**
   * Fatal log.
   *
   * @param o
   */
  public static void f(Object o) {
    Log.error(o, LogLevel.FATAL, null);
  }

  /**
   * Fatal log.
   *
   * @param o
   * @param t
   */
  public static void f(Object o, Throwable t) {
    Log.error(o, LogLevel.FATAL, t);
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
    StackTraceElement stack[] = (new Throwable()).getStackTrace();
    System.out.println("Stack:");
    for (int i = offset; i < stack.length; i++) {
      StackTraceElement frame = stack[i];
      String className = frame.getClassName();
      String methodName = frame.getMethodName();
      int lineNum = frame.getLineNumber();
      System.out.println("    at " + className + "#" + methodName + " (L:" + lineNum + ")");
    }
  }

  public static long timeStart() {
    return System.nanoTime();
  }

  public static void timeEnd(long start) {
    long delta = System.nanoTime() - start;
    double delta_ = (double) delta / 1000000;
    Log.d("delta = " + delta + " (" + delta_ + ")");
    if (delta >= 1000) {
      timeEndMilli(start);
    } else {
      timeEndNano(start);
    }
  }

  public static void timeEndNano(long start) {
    Log.d(System.nanoTime() - start + " ns");
  }

  public static void timeEndMilli(long start) {
    long delta = System.nanoTime() - start;
    double time = (double) delta / 1000000;
    BigDecimal bd = new BigDecimal(time);
    bd.setScale(3, BigDecimal.ROUND_HALF_UP);
    Log.d(bd.doubleValue() + " ms");
  }

  /**
   * Output log message.
   *
   * @param o
   *          message string or object
   * @param lv
   *          log level
   * @param stackFrameOffset
   * @param printLine
   *          set true to output method():file:line
   */
  private static void out(Object o, LogLevel lv, int stackFrameOffset, boolean printLine) {
    if (lv.getLevel() <= outputLevel) {
      String message = buildMessage(o, lv, stackFrameOffset, printLine);
      System.out.println(message);
    }
  }

  /**
   * Output error message.
   *
   * @param o
   * @param lv
   * @param t
   */
  private static void error(Object o, LogLevel lv, Throwable t) {
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

  private static String buildMessage(Object o, LogLevel lv, int stackFrameOffset, boolean printLine) {
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

  private static void printStackTraceString(Throwable t, LogLevel lv, boolean printLine) {
    out(t.toString(), lv, 2, printLine);
    _printStackTraceString(t, lv, printLine);
    while ((t = t.getCause()) != null) {
      out("Caused by: " + t.toString(), lv, 2, printLine);
      _printStackTraceString(t, lv, printLine);
    }
  }

  private static void _printStackTraceString(Throwable t, LogLevel lv, boolean printLine) {
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

}
