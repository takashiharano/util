package com.libutil;

public class TestUtil {

  // Equals ---
  public static boolean assertEquals(Object expected, Object actual) {
    return assertEquals("", expected, actual);
  }

  public static boolean assertEquals(String message, Object expected, Object actual) {
    boolean ok = false;
    String op = "!=";
    String strExpected = "" + expected;
    String strActual = "" + actual;
    if (StrUtil.equals(strExpected, strActual)) {
      ok = true;
      op = "==";
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=" + strExpected + " "
        + op + " ACTUAL=" + strActual);
    return ok;
  }

  // NotEquals ---
  public static boolean assertNotEquals(Object expected, Object actual) {
    return assertNotEquals("", expected, actual);
  }

  public static boolean assertNotEquals(String message, Object expected, Object actual) {
    boolean ok = false;
    String op = "==";
    String strExpected = "" + expected;
    String strActual = "" + actual;
    if (StrUtil.notEquals(strExpected, strActual)) {
      ok = true;
      op = "!=";
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=" + strExpected + " "
        + op + " ACTUAL=" + strActual);
    return ok;
  }

  // LessThan ---
  public static boolean assertLessThan(Integer expected, Integer actual) {
    return assertLessThan("", (long) expected, (long) actual);
  }

  public static boolean assertLessThan(String message, Integer expected, Integer actual) {
    return assertLessThan(message, (long) expected, (long) actual);
  }

  public static boolean assertLessThan(Long expected, Long actual) {
    return assertLessThan("", expected, actual);
  }

  public static boolean assertLessThan(String message, Long expected, Long actual) {
    boolean ok = false;
    String op = "<=";
    if (actual < expected) {
      ok = true;
      op = ">";
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=" + expected + " " + op
        + " ACTUAL=" + actual);
    return ok;
  }

  public static boolean assertLessThan(Float expected, Float actual) {
    return assertLessThan("", (double) expected, (double) actual);
  }

  public static boolean assertLessThan(String message, Float expected, Float actual) {
    return assertLessThan(message, (double) expected, (double) actual);
  }

  public static boolean assertLessThan(Double expected, Double actual) {
    return assertLessThan("", expected, actual);
  }

  public static boolean assertLessThan(String message, Double expected, Double actual) {
    boolean ok = false;
    String op = "<=";
    if (actual < expected) {
      ok = true;
      op = ">";
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=" + expected + " " + op
        + " ACTUAL=" + actual);
    return ok;
  }

  // LessThanOrEqual ---
  public static boolean assertLessThanOrEqual(Integer expected, Integer actual) {
    return assertLessThanOrEqual("", (long) expected, (long) actual);
  }

  public static boolean assertLessThanOrEqual(String message, Integer expected, Integer actual) {
    return assertLessThanOrEqual(message, (long) expected, (long) actual);
  }

  public static boolean assertLessThanOrEqual(Long expected, Long actual) {
    return assertLessThanOrEqual("", expected, actual);
  }

  public static boolean assertLessThanOrEqual(String message, Long expected, Long actual) {
    boolean ok = false;
    String op = "<";
    if (actual <= expected) {
      ok = true;
      op = ">=";
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=" + expected + " " + op
        + " ACTUAL=" + actual);
    return ok;
  }

  public static boolean assertLessThanOrEqual(Float expected, Float actual) {
    return assertLessThanOrEqual("", (double) expected, (double) actual);
  }

  public static boolean assertLessThanOrEqual(String message, Float expected, Float actual) {
    return assertLessThanOrEqual(message, (double) expected, (double) actual);
  }

  public static boolean assertLessThanOrEqual(Double expected, Double actual) {
    return assertLessThanOrEqual("", expected, actual);
  }

  public static boolean assertLessThanOrEqual(String message, Double expected, Double actual) {
    boolean ok = false;
    String op = "<";
    if (actual <= expected) {
      ok = true;
      op = ">=";
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=" + expected + " " + op
        + " ACTUAL=" + actual);
    return ok;
  }

  // GreaterThan ---
  public static boolean assertGreaterThan(String message, Integer expected, Integer actual) {
    return assertGreaterThan(message, (long) expected, (long) actual);
  }

  public static boolean assertGreaterThan(Integer expected, Integer actual) {
    return assertGreaterThan("", (long) expected, (long) actual);
  }

  public static boolean assertGreaterThan(Long expected, Long actual) {
    return assertGreaterThan("", expected, actual);
  }

  public static boolean assertGreaterThan(String message, Long expected, Long actual) {
    boolean ok = false;
    String op = ">=";
    if (actual > expected) {
      ok = true;
      op = "<";
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=" + expected + " " + op
        + " ACTUAL=" + actual);
    return ok;
  }

  public static boolean assertGreaterThan(Float expected, Float actual) {
    return assertGreaterThan("", (double) expected, (double) actual);
  }

  public static boolean assertGreaterThan(String message, Float expected, Float actual) {
    return assertGreaterThan(message, (double) expected, (double) actual);
  }

  public static boolean assertGreaterThan(Double expected, Double actual) {
    return assertGreaterThan("", expected, actual);
  }

  public static boolean assertGreaterThan(String message, Double expected, Double actual) {
    boolean ok = false;
    String op = ">=";
    if (actual > expected) {
      ok = true;
      op = "<";
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=" + expected + " " + op
        + " ACTUAL=" + actual);
    return ok;
  }

  // GreaterThanOrEqual ---
  public static boolean assertGreaterThanOrEqual(String message, Integer expected, Integer actual) {
    return assertGreaterThan(message, (long) expected, (long) actual);
  }

  public static boolean assertGreaterThanOrEqual(Integer expected, Integer actual) {
    return assertGreaterThan("", (long) expected, (long) actual);
  }

  public static boolean assertGreaterThanOrEqual(Long expected, Long actual) {
    return assertGreaterThan("", expected, actual);
  }

  public static boolean assertGreaterThanOrEqual(String message, Long expected, Long actual) {
    boolean ok = false;
    String op = ">";
    if (actual >= expected) {
      ok = true;
      op = "<=";
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=" + expected + " " + op
        + " ACTUAL=" + actual);
    return ok;
  }

  public static boolean assertGreaterThanOrEqual(Float expected, Float actual) {
    return assertGreaterThanOrEqual("", (double) expected, (double) actual);
  }

  public static boolean assertGreaterThanOrEqual(String message, Float expected, Float actual) {
    return assertGreaterThanOrEqual(message, (double) expected, (double) actual);
  }

  public static boolean assertGreaterThanOrEqual(Double expected, Double actual) {
    return assertGreaterThanOrEqual("", expected, actual);
  }

  public static boolean assertGreaterThanOrEqual(String message, Double expected, Double actual) {
    boolean ok = false;
    String op = ">";
    if (actual > expected) {
      ok = true;
      op = "<=";
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=" + expected + " " + op
        + " ACTUAL=" + actual);
    return ok;
  }

  // True ---
  public static boolean assertTrue(Boolean actual) {
    return assertTrue("", actual);
  }

  public static boolean assertTrue(String message, Boolean actual) {
    boolean ok = false;
    if (actual) {
      ok = true;
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=true ACTUAL=" + actual);
    return ok;
  }

  // False ---
  public static boolean assertFalse(Boolean actual) {
    return assertFalse("", actual);
  }

  public static boolean assertFalse(String message, Boolean actual) {
    boolean ok = false;
    if (!actual) {
      ok = true;
    }
    _Log.i("[" + (ok ? "OK" : "NG") + "] " + message + ("".equals(message) ? "" : ": ") + "EXP=false ACTUAL=" + actual);
    return ok;
  }

}
