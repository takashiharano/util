package com.libutil;

public class Time {
  public static final long DAY = 86400000;
  public static final long HOUR = 3600000;
  public static final long MINUTE = 60000;
  public static final long SECOND = 1000;

  private long millis;
  private int days;
  private int hrs;
  private int hours;
  private int minutes;
  private int seconds;
  private int milliseconds;

  /**
   * Allocates a Time object and initializes it to represent the specified number
   * of milliseconds.
   *
   * @param millis
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
    this.hrs = hh - d * 24;
    this.hours = hh;
    this.minutes = mi;
    this.seconds = ss;
    this.milliseconds = sss;
  }

  /**
   * Allocates a Time object and initializes it to represent the specified clock
   * format string.
   *
   * @param clock
   *          clock format string. "12:34", "12:34:56", "12:34:56.789"
   */
  public Time(String clock) {
    this(clock2ms(clock));
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

  public int getHrs() {
    return hrs;
  }

  public void setHrs(int hrs) {
    this.hrs = hrs;
  }

  public int getHours() {
    return hours;
  }

  public void setHours(int hours) {
    this.hours = hours;
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
   * @param h
   *          >= 24h instead of days. true: 47h 45m 59s
   * @param f
   *          to display millis. true: 1d 23h 45m 59s 123
   * @return
   */
  public String toString(boolean h, boolean f) {
    StringBuilder sb = new StringBuilder();
    if (this.millis < 0) {
      sb.append("-");
    }
    boolean d = false;
    if (!h && (this.days > 0)) {
      d = true;
      sb.append(this.days + "d ");
    }
    if (h && (this.hours > 0)) {
      d = true;
      sb.append(this.hours + "h ");
    } else if (d || (this.hrs > 0)) {
      d = true;
      sb.append(this.hrs + "h ");
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
   * Returns human-readable time string.<br>
   * 171959000 -> "1d 23h 45m 59s"
   *
   * @param millis
   *          milliseconds
   * @return time string
   */
  public static String ms2str(long millis) {
    return ms2str(millis, 0);
  }

  public static String ms2str(long millis, int mode) {
    Time t = new Time(millis);
    if (mode == 1) {
      return t.toString(false, true);
    }
    if ((mode == 2) || (millis >= 60000)) {
      return t.toString(false, false);
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

  /**
   * Converts clock format string to milliseconds.
   *
   * @param str
   *          [+|-]HH:MI:SS.sss
   * @return milliseconds
   */
  public static long clock2ms(String str) {
    String hour;
    String min;
    String sec;
    String msec = "000";
    String wk = str;
    boolean sign = false;
    if (wk.charAt(0) == '+') {
      wk = wk.substring(1);
    } else if (wk.charAt(0) == '-') {
      sign = true;
      wk = wk.substring(1);
    }

    if (wk.indexOf(".") != -1) {
      String[] prt = wk.split("\\.");
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

    int hours = Integer.parseInt(hour);
    int minutes = Integer.parseInt(min);
    int seconds = Integer.parseInt(sec);
    int milliseconds = Integer.parseInt(msec);

    long ms = (hours * HOUR) + (minutes * MINUTE) + seconds * SECOND + milliseconds;
    if (sign) {
      ms *= (-1);
    }
    return ms;
  }

}
