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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class Time represents a specific instant in time, with millisecond
 * precision.
 */
public class Time {

  public static final long DAY = 86400000;
  public static final long HOUR = 3600000;
  public static final long MINUTE = 60000;
  public static final long SECOND = 1000;

  private long millis;
  private int days;
  private int hours;
  private int hours24;
  private int minutes;
  private int seconds;
  private int milliseconds;

  /**
   * Allocates a Time object and initializes it to represent the specified number
   * of milliseconds.
   *
   * @param millis
   *          the time in milliseconds
   */
  public Time(long millis) {
    long wk = millis;
    if (millis < 0) {
      wk *= (-1);
    }
    int d = (int) (wk / DAY);
    int hh = 0;
    if (wk >= HOUR) {
      hh = (int) (wk / HOUR);
      wk -= (hh * HOUR);
    }
    int mi = 0;
    if (wk >= MINUTE) {
      mi = (int) (wk / MINUTE);
      wk -= (mi * MINUTE);
    }
    int ss = (int) (wk / SECOND);
    int sss = (int) (wk - (ss * SECOND));

    this.millis = millis;
    this.days = d;
    this.hours = hh;
    this.hours24 = hh - d * 24;
    this.minutes = mi;
    this.seconds = ss;
    this.milliseconds = sss;
  }

  /**
   * Allocates a Time object and initializes it to represent the specified clock
   * format string.
   *
   * @param str
   *          clock format string. "12:34", "12:34:56", "12:34:56.789"
   */
  public Time(String str) {
    this(parseMillis(str));
  }

  public long getMillis() {
    return millis;
  }

  public void setMillis(long millis) {
    this.millis = millis;
  }

  public int getDays() {
    return days;
  }

  public void setDays(int days) {
    this.days = days;
  }

  public int getHours() {
    return hours;
  }

  public void setHours(int hours) {
    this.hours = hours;
  }

  public int getHours24() {
    return hours24;
  }

  public void setHours24(int hours24) {
    this.hours24 = hours24;
  }

  public int getMinutes() {
    return minutes;
  }

  public void setMinutes(int minutes) {
    this.minutes = minutes;
  }

  public int getSeconds() {
    return seconds;
  }

  public void setSeconds(int seconds) {
    this.seconds = seconds;
  }

  public int getMilliseconds() {
    return milliseconds;
  }

  public void setMilliseconds(int milliseconds) {
    this.milliseconds = milliseconds;
  }

  /**
   * To string the time.
   *
   * @param format
   *          "[+-]HH:mm:ss.SSS", "[+-]HR:mm:ss.SSS", "[+-]Dd HH24:mm:ss.SSS"
   * @return the formatted time string
   */
  public String toString(String format) {
    int signType = getSignType(format);
    String sign = "";
    if (millis < 0) {
      sign = "-";
    } else if (signType == 1) {
      sign = "+";
    }

    String d = days + "";

    String hr = ((hours < 10) ? "0" + hours : hours + "");

    String hh24 = "00" + hours24;
    hh24 = hh24.substring(hh24.length() - 2);

    String hh = hh24;
    if (hours >= 24) {
      hh = d + "d" + hh24;
    }

    String mm = "00" + minutes;
    mm = mm.substring(mm.length() - 2);

    String ss = "00" + seconds;
    ss = ss.substring(ss.length() - 2);

    String f3 = "000" + milliseconds;
    f3 = f3.substring(f3.length() - 3);

    String r = format;
    if (signType == 0) {
      r = sign + r;
    }
    r = r.replace("D", d);
    r = r.replace("-", sign);
    r = r.replace("+", sign);
    r = r.replace("HR", hr);
    r = r.replace("HH24", hh24);
    r = r.replace("HH", hh);
    r = r.replace("mm", mm);
    r = r.replace("ss", ss);
    r = r.replace("SSS", f3);
    return r;
  }

  /**
   * To string the time with unit.
   *
   * @param byHours
   *          -ge 24h instead of days. true: 47h 45m 59s
   * @param f
   *          to display millis. true: 1d 23h 45m 59s 123
   * @return the time string
   */
  public String toStringWithUnit(boolean byHours, boolean f) {
    StringBuilder sb = new StringBuilder();
    if (this.millis < 0) {
      sb.append("-");
    }
    boolean d = false;
    if (!byHours && (this.days > 0)) {
      d = true;
      sb.append(this.days + "d ");
    }
    if (byHours && (this.hours > 0)) {
      d = true;
      sb.append(this.hours + "h ");
    } else if (d || (this.hours24 > 0)) {
      d = true;
      sb.append(this.hours24 + "h ");
    }
    if (d || (this.minutes > 0)) {
      d = true;
      sb.append(this.minutes + "m ");
    }
    if (f) {
      if (this.millis >= SECOND) {
        if (this.milliseconds == 0) {
          sb.append(this.seconds + "s");
        } else {
          sb.append(this.seconds + "s " + this.milliseconds + "ms");
        }
      } else {
        sb.append(this.milliseconds + "ms");
      }
    } else {
      sb.append(this.seconds + "s");
    }
    return sb.toString();
  }

  /**
   * Adds t2 to t1 and returns the result.
   *
   * @param t1
   *          the time 1. e.g., "12:34"
   * @param t2
   *          the time 2. e.g., "01:30"
   * @return the calculated Time object
   */
  public static Time addTime(String t1, String t2) {
    long ms1 = parseMillis(t1);
    long ms2 = parseMillis(t2);
    Time time = new Time(ms1 + ms2);
    return time;
  }

  /**
   * Subtracts t2 from t1 and returns the result.
   *
   * @param t1
   *          the time 1. e.g., "12:34"
   * @param t2
   *          the time 2. e.g., "01:30"
   * @return the calculated Time object
   */
  public static Time subTime(String t1, String t2) {
    long ms1 = parseMillis(t1);
    long ms2 = parseMillis(t2);
    Time time = new Time(ms1 - ms2);
    return time;
  }

  /**
   * Multiplies t by v and returns the result.
   *
   * @param t
   *          the time. e.g., "12:34"
   * @param v
   *          number to multiply
   * @return the calculated Time object
   */
  public static Time multiTime(String t, int v) {
    long ms = parseMillis(t);
    Time time = new Time(ms * v);
    return time;
  }

  /**
   * Divides t by v and return the result.
   *
   * @param t
   *          the time. e.g., "12:34"
   * @param v
   *          number to divide
   * @return the calculated Time object
   */
  public static Time divTime(String t, int v) {
    long ms = parseMillis(t);
    Time time = new Time(ms / v);
    return time;
  }

  /**
   * Convert milliseconds to a time string.<br>
   *
   * @param millis
   *          milliseconds to format
   * @return the formatted time string like "12:34:56.789", "1d12:34:56.789"
   */
  public static String formatTime(long millis) {
    return formatTime(millis, "HH:mm:ss.SSS");
  }

  /**
   * Convert milliseconds to a time string.<br>
   * <br>
   * e.g.,<br>
   * (1234, "HH:mm:ss.SSS") to "00:00:01.234"<br>
   * (171954123, "HR:mm:ss.SSS") to "47:45:54.123"<br>
   * (171954123, "HH:mm:ss.SSS") to "1d23:45:54.123"<br>
   * (171954123, "Dd HH24:mm:ss.SSS") to "1d 23:45:54.123"<br>
   *
   * @param millis
   *          milliseconds to format
   * @param format
   *          "HH:mm:ss.SSS", "HR:mm:ss.SSS", "Dd HH24:mm:ss.SSS"
   * @return the formatted time string
   */
  public static String formatTime(long millis, String format) {
    long v = millis;
    String sign = "+";
    if (millis < 0) {
      v *= (-1);
      sign = "-";
    }

    long days = v / 86400000;
    long hours = 0;
    if (v >= 3600000) {
      hours = v / 3600000;
      v -= hours * 3600000;
    }
    long minutes = 0;
    if (v >= 60000) {
      minutes = v / 60000;
      v -= (minutes * 60000);
    }
    long seconds = v / 1000;
    long f = v - seconds * 1000;
    long hours24 = hours - days * 24;

    String d = days + "";

    String hr = ((hours < 10) ? "0" + hours : hours + "");

    String hh24 = "0" + hours24;
    hh24 = hh24.substring(hh24.length() - 2);

    String hh = hh24;
    if (hours >= 24) {
      hh = d + "d" + hh24;
    }

    String mm = "0" + minutes;
    mm = mm.substring(mm.length() - 2);

    String ss = "0" + seconds;
    ss = ss.substring(ss.length() - 2);

    String f3 = "00" + f;
    f3 = f3.substring(f3.length() - 3);

    String r = format;
    r = r.replace("D", d);
    r = r.replace("sn", sign);
    r = r.replace("HR", hr);
    r = r.replace("HH24", hh24);
    r = r.replace("HH", hh);
    r = r.replace("mm", mm);
    r = r.replace("ss", ss);
    r = r.replace("SSS", f3);
    return r;
  }

  /**
   * Converts a decimal hours into a clock-like string.
   *
   * @param hours
   *          hours (e.g., -8.0, 9.5)
   * @return clock-like string like "-0800", "0930"
   */
  public static String hoursToClockString(float hours) {
    return hoursToClockString(hours, ":");
  }

  /**
   * Converts a decimal hours into a clock-like string.
   *
   * @param hours
   *          hours (e.g., -8.0, 9.5)
   * @param separator
   *          separator of the hours and the minutes
   * @return clock-like string like "-0800", "0930"
   */
  public static String hoursToClockString(float hours, String separator) {
    String s = Float.toString(hours);
    return hoursToClockString(s, separator);
  }

  /**
   * Converts a decimal hours into a clock-like string.
   *
   * @param hours
   *          a float value (e.g., "-8.0", "9.0", "+9.5")
   * @return clock-like string (e.g., "-0800", "0900", "+0930")
   */
  public static String hoursToClockString(String hours) {
    return hoursToClockString(hours, ":");
  }

  /**
   * Converts a decimal hours into a clock-like string.
   *
   * @param hours
   *          a float value (e.g., "-8.0", "9.0", "+9.5")
   * @param separator
   *          separator of the hours and the minutes
   * @return clock-like string (e.g., "-0800", "0900", "+0930")
   */
  public static String hoursToClockString(String hours, String separator) {
    String sign = "";
    Pattern pt = Pattern.compile("^[+-]", 0);
    Matcher mt = pt.matcher(hours);
    if (mt.find()) {
      sign = hours.substring(0, 1);
      hours = hours.substring(1);
    }
    String[] w = hours.split("\\.");
    int h = Integer.parseInt(w[0]);
    float fM = 0.0f;
    if (w.length >= 2) {
      fM = Float.parseFloat("0." + w[1]);
    }
    int m = (int) (60 * fM);
    String hh = ((h < 10) ? "0" + Integer.toString(h) : Integer.toString(h));
    String mm = ((m < 10) ? "0" + Integer.toString(m) : Integer.toString(m));
    String clock = sign + hh + separator + mm;
    return clock;
  }

  /**
   * Converts clock format string to milliseconds.
   *
   * @param str
   *          [+|-]HH:MI:SS.sss
   * @return milliseconds
   */
  public static long parseMillis(String str) {
    String day = "0";
    String hour;
    String min;
    String sec;
    String msec = "000";
    String wk = str;
    String[] prt;
    boolean sign = false;
    if (wk.charAt(0) == '+') {
      wk = wk.substring(1);
    } else if (wk.charAt(0) == '-') {
      sign = true;
      wk = wk.substring(1);
    }

    if (wk.indexOf("d") != -1) {
      prt = wk.split("d");
      day = prt[0];
      wk = prt[1];
    }

    if (wk.indexOf(".") != -1) {
      prt = wk.split("\\.");
      wk = prt[0];
      msec = (prt[1] + "000").substring(0, 3);
    }

    int pos = wk.indexOf(':');
    if (pos != -1) {
      hour = wk.substring(0, pos);
      wk = wk.substring(pos + 1).replaceAll(":", "");
    } else {
      hour = wk.substring(0, 2);
      wk = wk.substring(2);
    }

    wk = (wk + "00").substring(0, 4);
    min = wk.substring(0, 2);
    sec = wk.substring(2, 4);

    int days = Integer.parseInt(day);
    int hours = Integer.parseInt(hour);
    int minutes = Integer.parseInt(min);
    int seconds = Integer.parseInt(sec);
    int milliseconds = Integer.parseInt(msec);

    long ms = (days * DAY) + (hours * HOUR) + (minutes * MINUTE) + seconds * SECOND + milliseconds;
    if (sign) {
      ms *= (-1);
    }
    return ms;
  }

  /**
   * Converts a clock-like string into a decimal hours.
   *
   * @param s
   *          clock-like string (e.g., "09:30")
   * @return decimal hours (e.g., 9.5)
   */
  public static float toHours(String s) {
    s = s.replace(":", "");
    int p = s.length() - 2;
    String hh = s.substring(0, p);
    String mm = s.substring(p);
    float h = Float.parseFloat(hh);
    float m = Float.parseFloat(mm);
    return h + (m / 60);
  }

  /**
   * To string the time.
   *
   * @param millis
   *          milliseconds
   * @param format
   *          "HH:mm:ss.SSS", "HR:mm:ss.SSS", "Dd HH24:mm:ss.SSS"
   * @return the formatted time string
   */
  public static String toString(long millis, String format) {
    return (new Time(millis)).toString(format);
  }

  /**
   * To string the time with unit.<br>
   * e.g., 171959000 to "1d 23h 45m 59s"
   *
   * @param millis
   *          milliseconds
   * @return human-readable time string
   */
  public static String toStringWithUnit(long millis) {
    return toStringWithUnit(millis, 0);
  }

  /**
   * To string the time with unit.<br>
   * e.g., 171959000 to "1d 23h 45m 59s"
   *
   * @param millis
   *          milliseconds
   * @param mode
   *          resolution. 0=auto / 1=millis / 2=secs if gt 60s
   * @return human-readable time string
   */
  public static String toStringWithUnit(long millis, int mode) {
    Time t = new Time(millis);
    if (mode == 1) {
      return t.toStringWithUnit(false, true);
    }
    if ((mode == 2) || (millis >= 60000)) {
      return t.toStringWithUnit(false, false);
    }

    int ss = t.seconds;
    int sss = t.milliseconds;
    StringBuilder sb = new StringBuilder();
    if (millis < 1000) {
      sb.append(sss + "ms");
    } else {
      if (millis < 10000) {
        sss = sss - sss % 10;
      } else {
        sss = sss - sss % 100;
      }
      String msec = (sss + "").replaceAll("0+$", "");
      if (sss == 0) {
        sb.append(ss + "s");
      } else if (sss < 100) {
        sb.append(ss + ".0" + msec + "s");
      } else {
        sb.append(ss + "." + msec + "s");
      }
    }
    return sb.toString();
  }

  private static int getSignType(String s) {
    Pattern p = Pattern.compile("\\+");
    Matcher m = p.matcher(s);
    if (m.find()) {
      return 1;
    }
    p = Pattern.compile("\\-");
    m = p.matcher(s);
    if (m.find()) {
      return -1;
    }
    return 0;
  }

}
