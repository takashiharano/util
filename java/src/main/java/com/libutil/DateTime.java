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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class DateTime represents a specific instant in time, with millisecond
 * precision.
 */
public class DateTime {

  public static final String DATE_FORMAT_ISO8601 = "yyyyMMdd'T'HHmmss.SSS";
  public static final String DATE_FORMAT_ISO8601EX = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  public static final String DATE_FORMAT_ISO8601TZ = "yyyyMMdd'T'HHmmss.SSSXX"; // "20191201T123456.987+0900"
  public static final String DATE_FORMAT_ISO8601EXTZ = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // "2019-12-01T12:34:56.987+09:00"
  public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS XXX";

  private Date date;
  private long timestamp;
  private int year;
  private int month;
  private int day;
  private int weekDay;
  private int hour;
  private int minute;
  private int second;
  private int millisecond;
  private TimeZone timeZone;

  /**
   * Allocates a DateTime object and initializes it so that it represents the time
   * at which it was allocated, measured to the nearest millisecond.
   */
  public DateTime() {
    this(new Date());
  }

  /**
   * Allocates a DateTime object and initializes it to represent the specified
   * number of milliseconds since the standard base time known as "the epoch",
   * namely January 1, 1970, 00:00:00 GMT.
   *
   * @param timestamp
   *          the milliseconds since January 1, 1970, 00:00:00 GMT.
   */
  public DateTime(long timestamp) {
    Date date = new Date(timestamp);
    _init(date, null);
  }

  public DateTime(long timestamp, TimeZone timeZone) {
    Date date = new Date(timestamp);
    _init(date, timeZone);
  }

  public DateTime(Date date) {
    _init(date);
  }

  public DateTime(Date date, String timeZoneId) {
    TimeZone tz = getTimezoneFromId(timeZoneId);
    _init(date, tz);
  }

  public DateTime(String source) {
    if (source == null) {
      _init(null, null);
      return;
    }
    String[] w = splitDateTimeAndTimezone(source);
    String sdt = w[0];
    String tzId = w[1];
    String s = serializeDateTime(sdt);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    Date date;
    try {
      date = sdf.parse(s);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    if (tzId != null) {
      if (tzId.equals("Z")) {
        tzId = "+0000";
      }
      tzId = "GMT" + tzId;
      int tzdiff = getTimezoneOffsetDiff(tzId);
      long timestamp = date.getTime() - tzdiff;
      date = new Date(timestamp);
    }
    TimeZone tz = getTimezoneFromId(tzId);
    _init(date, tz);
  }

  public DateTime(String source, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date date;
    try {
      date = sdf.parse(source);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    _init(date);
  }

  public DateTime(int year, int month, int day) {
    this(year, month, day, 0, 0, 0, 0, null);
  }

  public DateTime(int year, int month, int day, String timeZoneId) {
    this(year, month, day, 0, 0, 0, 0, timeZoneId);
  }

  public DateTime(int year, int month, int day, int hour, int minute) {
    this(year, month, day, hour, minute, 0, 0, null);
  }

  public DateTime(int year, int month, int day, int hour, int minute, String timeZoneId) {
    this(year, month, day, hour, minute, 0, 0, timeZoneId);
  }

  public DateTime(int year, int month, int day, int hour, int minute, int second) {
    this(year, month, day, hour, minute, second, 0, null);
  }

  public DateTime(int year, int month, int day, int hour, int minute, int second, String timeZoneId) {
    this(year, month, day, hour, minute, second, 0, timeZoneId);
  }

  public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond) {
    this(year, month, day, hour, minute, second, millisecond, null);
  }

  /**
   * Allocates a DateTime object and initializes with the given parameters.
   *
   * @param year
   *          Year
   * @param month
   *          Month (1-12)
   * @param day
   *          Day (1-31)
   * @param hour
   *          Hour (0-23)
   * @param minute
   *          Minute (0-59)
   * @param second
   *          Second (0-59)
   * @param millisecond
   *          Millisecond (0-999)
   * @param timeZoneId
   *          the ID for a TimeZone, such as "PST", "America/Los_Angeles",
   *          "GMT-8:00"
   */
  public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, String timeZoneId) {
    String yyyy = year + "";
    String mm = intToStr2(month);
    String dd = intToStr2(day);
    String hh = intToStr2(hour);
    String mi = intToStr2(minute);
    String ss = intToStr2(second);
    String ms = intToStr3(millisecond);
    String dtString = yyyy + mm + dd + hh + mi + ss + ms;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    Date date;
    try {
      date = sdf.parse(dtString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    TimeZone tz = getTimezoneFromId(timeZoneId);
    int tzdiff = getTimezoneOffsetDiff(tz);
    long ts = date.getTime() - tzdiff;
    date = new Date(ts);
    _init(date, tz);
  }

  private void _init(Date date) {
    _init(date, null);
  }

  private void _init(Date date, TimeZone tz) {
    if (date == null) {
      date = new Date();
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    if (tz == null) {
      tz = TimeZone.getDefault();
    }
    calendar.setTimeZone(tz);
    this.timeZone = tz;
    this.date = date;
    this.timestamp = date.getTime();
    this.year = calendar.get(Calendar.YEAR);
    this.month = calendar.get(Calendar.MONTH) + 1;
    this.day = calendar.get(Calendar.DATE);
    this.weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    this.hour = calendar.get(Calendar.HOUR_OF_DAY);
    this.minute = calendar.get(Calendar.MINUTE);
    this.second = calendar.get(Calendar.SECOND);
    this.millisecond = calendar.get(Calendar.MILLISECOND);
  }

  // --------------------------------------------------------------------------

  /**
   * Returns calendrical calculation result.
   *
   * @param days
   *          offset
   * @return the calculated DateTime object
   */
  public DateTime addDays(int days) {
    long ts = timestamp + (86400000 * days);
    Date date = new Date(ts);
    TimeZone tz = timeZone;
    String timeZoneId = tz.getID();
    return new DateTime(date, timeZoneId);
  }

  /**
   * Compares the instance and the specified DateTime object.
   *
   * @param datetime
   *          DateTime object to compare
   * @return a long value indicating the result of the comparison, as follows:<br>
   *         0, if the instance and the datetime are equal;<br>
   *         a negative value if the instance is less than the datetime;<br>
   *         a positive value if the instance is greater than the datetime.
   */
  public long compare(DateTime datetime) {
    return timestamp - datetime.getTimestamp();
  }

  /**
   * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
   * represented by this Date object.
   *
   * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
   *         represented by this date.
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Returns the year of this date.
   *
   * @return year
   */
  public int getYear() {
    return year;
  }

  /**
   * Returns the month of this date.
   *
   * @return month (1-12)
   */
  public int getMonth() {
    return month;
  }

  /**
   * Returns the day of the month.
   *
   * @return day of month (1-31)
   */
  public int getDay() {
    return day;
  }

  /**
   * Returns the day of the week.<br>
   *
   * @return the day of week<br>
   *         0 SUNDAY<br>
   *         1 MONDAY<br>
   *         2 TUESDAY<br>
   *         3 WEDNESDAY<br>
   *         4 THURSDAY<br>
   *         5 FRIDAY<br>
   *         6 SATURDAY
   */
  public int getWeekDay() {
    return weekDay;
  }

  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }

  public int getSecond() {
    return second;
  }

  public int getMillisecond() {
    return millisecond;
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(TimeZone timeZone) {
    _init(date, timeZone);
  }

  /**
   * Sets time zone by id.
   *
   * @param timeZoneId
   *          the ID for a TimeZone, such as "PST", "America/Los_Angeles",
   *          "GMT-8:00"
   */
  public void setTimeZone(String timeZoneId) {
    TimeZone tz = TimeZone.getTimeZone(timeZoneId);
    _init(date, tz);
  }

  /**
   * Gets the ID of this time zone.
   *
   * @return the ID of this time zone such as "America/Los_Angeles".
   */
  public String getTimeZoneId() {
    if (timeZone == null) {
      return "";
    }
    ZoneId zoneId = timeZone.toZoneId();
    return zoneId.getId();
  }

  /**
   * Returns a long standard time name of this TimeZone suitable for presentation
   * to the user in the default locale.<br>
   *
   * @return the name such as "Pacific Standard Time.", "日本標準時", etc.
   */
  public String getTimeZoneDisplayName() {
    if (timeZone == null) {
      return "";
    }
    return timeZone.getDisplayName();
  }

  /**
   * Returns newly-allocated DateTime object in the local time zone ID.<br>
   * The time stamp remains preserved.
   *
   * @return A newly-allocated DateTime object
   */
  public DateTime getLocalDateTime() {
    return getLocalDateTime((TimeZone) null);
  }

  /**
   * Returns newly-allocated DateTime object in the specified time zone ID.<br>
   * The time stamp remains preserved.
   *
   * @param targetTzId
   *          time zone ID
   * @return A newly-allocated DateTime object
   */
  public DateTime getLocalDateTime(String targetTzId) {
    TimeZone tz = getTimezoneFromId(targetTzId);
    return getLocalDateTime(tz);
  }

  /**
   * Returns newly-allocated DateTime object in the specified time zone.<br>
   * The time stamp remains preserved.
   *
   * @param targetTz
   *          time zone
   * @return A newly-allocated DateTime object
   */
  public DateTime getLocalDateTime(TimeZone targetTz) {
    return new DateTime(timestamp, targetTz);
  }

  /**
   * Queries if this TimeZone uses Daylight Saving Time.
   *
   * @return true if this TimeZone uses Daylight Saving Time, false, otherwise.
   */
  public boolean useDaylightTime() {
    if (timeZone == null) {
      return false;
    }
    return timeZone.useDaylightTime();
  }

  /**
   * Queries if the date of the instance is in Daylight Saving Time in this time
   * zone.
   *
   * @return true if the given date is in Daylight Saving Time, false, otherwise.
   */
  public boolean inDaylightTime() {
    if (timeZone == null) {
      return false;
    }
    return timeZone.inDaylightTime(date);
  }

  /**
   * Returns the date-time string of the instance in the default format
   * "yyyy-MM-dd HH:mm:ss.SSS XXX".
   */
  public String toString() {
    return toString(DEFAULT_FORMAT);
  }

  /**
   * Returns the date-time string of the instance in the specified format.
   *
   * @param format
   *          "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" to
   *          "2019-07-13T12:34:56.987+09:00"<br>
   *          "yyyyMMdd'T'HHmmssSSSXX" to "20190713T123456.987+0900"
   * @return the date-time string
   */
  public String toString(String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    if (timeZone != null) {
      sdf.setTimeZone(timeZone);
    }
    return sdf.format(date);
  }

  /**
   * Returns if the year is leap year.
   *
   * @return true if the year is leap year
   */
  public boolean isLeapYear() {
    return isLeapYear(year);
  }

  // --------------------------------------------------------------------------

  /**
   * Returns calendrical calculation result.
   *
   * @param date
   *          a string of the origin date
   * @param days
   *          offset
   * @param format
   *          date-time format for result
   * @return the calculated date-time string
   */
  public static String addDate(String date, int days, String format) {
    String s = serializeDateTime(date);
    DateTime dt0;
    dt0 = new DateTime(s, "yyyyMMddHHmmssSSS");
    DateTime dt1 = dt0.addDays(days);
    return dt1.toString(format);
  }

  /**
   * Compares the two date-time dt1 and dt2.
   *
   * @param dt1
   *          Date-time string 1
   * @param dt2
   *          Date-time string 2
   * @return a long value indicating the result of the comparison, as follows:<br>
   *         0, if the dt1 and dt2 are equal;<br>
   *         a negative value if dt1 is less than dt2;<br>
   *         a positive value if dt1 is greater than dt2.
   */
  public static long compareDateTime(String dt1, String dt2) {
    DateTime datetime1 = new DateTime(dt1);
    DateTime datetime2 = new DateTime(dt2);
    return datetime1.compare(datetime2);
  }

  /**
   * Convert milliseconds to a time string.<br>
   *
   * @param millis
   *          milliseconds to format
   * @return the formatted time string
   */
  public static String formatTime(long millis) {
    return formatTime(millis, "HH:mm:ss.SSS");
  }

  /**
   * Convert milliseconds to a time string.<br>
   * <br>
   * e.g.,<br>
   * (1234, "HH:mm:ss.SSS") to "00:00:01.234"<br>
   * (171954123, "Dd HH:mm:ss.SSS") to "1d 23:45:54.123"<br>
   *
   * @param millis
   *          milliseconds to format
   * @param format
   *          "HH:mm:ss.SSS"
   * @return the formatted time string
   */
  public static String formatTime(long millis, String format) {
    long v = millis;
    String sn = "+";
    if (millis < 0) {
      v *= (-1);
      sn = "-";
    }

    long d = v / 86400000;
    long hr = 0;
    if (v >= 3600000) {
      hr = v / 3600000;
      v -= hr * 3600000;
    }
    long m = 0;
    if (v >= 60000) {
      m = v / 60000;
      v -= (m * 60000);
    }
    long s = v / 1000;
    long f = v - s * 1000;
    long h = hr - d * 24;

    String dd = d + "";

    String hh = "00" + h;
    hh = hh.substring(hh.length() - 2);
    String hrs = ((hr < 10) ? "0" + hr : hr + "");

    String mm = "00" + m;
    mm = mm.substring(mm.length() - 2);

    String ss = "00" + s;
    ss = ss.substring(ss.length() - 2);

    String f3 = "000" + f;
    f3 = f3.substring(f3.length() - 3);

    String r = format;
    r = r.replace("D", dd);
    r = r.replace("sn", sn);
    r = r.replace("HR", hrs);
    r = r.replace("HH", hh);
    r = r.replace("mm", mm);
    r = r.replace("ss", ss);
    r = r.replace("SSS", f3);
    return r;
  }

  /**
   * Returns the date-time string in the specified format.
   *
   * @param datetime
   *          the date-time string
   * @param format
   *          the pattern describing the date and time format
   *          "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" to
   *          "2021-07-01T12:34:56.987+09:00"<br>
   *          "yyyyMMdd'T'HHmmssSSSXX" to<br>
   *          "20210701T123456.987+0900"
   * @return the formatted date-time string
   */
  public static String getString(String datetime, String format) {
    DateTime dt;
    dt = new DateTime(datetime);
    return dt.toString(format);
  }

  /**
   * Returns the date-time string from Date object.
   *
   * @param date
   *          the date
   * @param format
   *          "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" to
   *          "2019-07-13T12:34:56.987+09:00"<br>
   *          "yyyyMMdd'T'HHmmssSSSXX" to "20190713T123456.987+0900"
   * @return the date-time string
   */
  public static String getString(Date date, String format) {
    DateTime dt = new DateTime(date);
    return dt.toString(format);
  }

  /**
   * Returns the date-time string represented by the time-stamp in the specified
   * format.
   *
   * @param timestamp
   *          the milliseconds since January 1, 1970, 00:00:00 GMT.
   * @param format
   *          the pattern describing the date and time format
   *          "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" to
   *          "2020-07-01T12:34:56.987+09:00"<br>
   *          "yyyyMMdd'T'HHmmssSSSXX" to "20190701T123456.987+0900"
   * @return the formatted date-time string
   */
  public static String getString(long timestamp, String format) {
    DateTime dt = new DateTime(timestamp);
    return dt.toString(format);
  }

  /**
   * Returns the TimeZone for the given ID.
   *
   * @param id
   *          the ID for a TimeZone.
   * @return the specified TimeZone, or the local TimeZone if the given ID is
   *         null, otherwise (the ID cannot be understood) the GMT zone.
   */
  public static TimeZone getTimezoneFromId(String id) {
    TimeZone timeZone;
    if (id == null) {
      return TimeZone.getDefault();
    }

    Pattern p = Pattern.compile("^[+-].+$", 0);
    Matcher m = p.matcher(id);
    if (m.find()) {
      id = "GMT" + id;
    }
    timeZone = TimeZone.getTimeZone(id);
    return timeZone;
  }

  /**
   * Returns the difference in time zone offset of time zone id with respect to
   * local time in milliseconds.
   *
   * @param timeZoneId
   *          time zone id to compare
   * @return the difference in milliseconds
   */
  public static int getTimezoneOffsetDiff(String timeZoneId) {
    TimeZone tz0 = TimeZone.getDefault();
    TimeZone tz1 = TimeZone.getTimeZone(timeZoneId);
    return getTimezoneOffsetDiff(tz0, tz1);
  }

  /**
   * Returns the difference in time zone offset of tz1 with respect to tz0 in
   * milliseconds.
   *
   * @param timeZoneId0
   *          origin time zone id
   * @param timeZoneId1
   *          time zone id to compare
   * @return the difference in milliseconds
   */
  public static int getTimezoneOffsetDiff(String timeZoneId0, String timeZoneId1) {
    TimeZone tz0 = TimeZone.getTimeZone(timeZoneId0);
    TimeZone tz1 = TimeZone.getTimeZone(timeZoneId1);
    return getTimezoneOffsetDiff(tz0, tz1);
  }

  /**
   * Returns the difference in time zone offset of tz1 with respect to local time
   * in milliseconds.
   *
   * @param tz
   *          time zone to compare
   * @return the difference in milliseconds
   */
  public static int getTimezoneOffsetDiff(TimeZone tz) {
    TimeZone tz0 = TimeZone.getDefault();
    return getTimezoneOffsetDiff(tz0, tz);
  }

  /**
   * Returns the difference in time zone offset of tz1 with respect to tz0 in
   * milliseconds.
   *
   * @param tz0
   *          origin time zone
   * @param tz1
   *          time zone to compare
   * @return the difference in milliseconds
   */
  public static int getTimezoneOffsetDiff(TimeZone tz0, TimeZone tz1) {
    int offset0 = tz0.getRawOffset();
    int offset1 = tz1.getRawOffset();
    return offset1 - offset0;
  }

  /**
   * Returns if the year is leap year.
   *
   * @param year
   *          Year
   * @return true if the year is leap year
   */
  public static boolean isLeapYear(int year) {
    return ((((year % 4) == 0) && ((year % 100) != 0)) || (year % 400 == 0));
  }

  /**
   * Returns the current time stamp.
   *
   * @return the difference, measured in milliseconds, between the current time
   *         and midnight, January 1, 1970 UTC.
   */
  public static long now() {
    return System.currentTimeMillis();
  }

  /**
   * Returns the current date-time string in the specified format.
   *
   * @param format
   *          the pattern describing the date and time format
   * @return the current date-time string.
   */
  public static String now(String format) {
    DateTime dt = new DateTime();
    return dt.toString(format);
  }

  /**
   * Parses text from the beginning of the given string to produce a date.
   *
   * @param pattern
   *          the pattern describing the date and time format like "yyyy-MM-dd"
   * @param str
   *          A String whose beginning should be parsed
   * @return A Date parsed from the string
   * @throws ParseException
   *           If failed to parse
   */
  public static Date parseDate(String pattern, String str) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    Date date = sdf.parse(str);
    return date;
  }

  /**
   * Format the date-time string in YYYYMMDDHHMISSfff format<br>
   * <br>
   * 20200920 to 20200920000000000000<br>
   * 20200920T1234 to 20200920123400000000<br>
   * 20200920T123456.789 to 20200920123456789000<br>
   * 20200920T123456.789123 to 20200920123456789123<br>
   * 2020-09-20 12:34:56.789 to 20200920123456789000<br>
   * 2020-09-20 12:34:56.789123 to 20200920123456789123<br>
   * 2020/9/3 12:34:56.789 to 20200903123456789000<br>
   * 2020/9/3 12:34:56.789123 to 20200903123456789123
   *
   * @param src
   *          date-time string to format
   * @return YYYYMMDDHHMISSfff
   */
  public static String serializeDateTime(String src) {
    try {
      return _serializeDateTime(src);
    } catch (Exception e) {
      throw new RuntimeException("PARSE_ERROR: " + src);
    }
  }

  private static String _serializeDateTime(String src) {
    String w = src;
    w = w.trim();
    w = w.replaceAll("\\s{2,}", " ");
    w = w.replaceAll("T", " ");

    Pattern p = Pattern.compile("[-/:]");
    Matcher m = p.matcher(w);
    if (!m.find()) {
      return __serializeDateTime(w);
    }

    String[] prt = w.split(" ");
    String date = prt[0];
    String time = "";
    if (prt.length >= 2) {
      time = prt[1];
    }
    date = date.replace("/", "-");

    prt = date.split("-");
    String yyyy = prt[0];
    String mm = zeroPadding(prt[1]);
    String dd = zeroPadding(prt[2]);
    date = yyyy + mm + dd;

    prt = time.split("\\.");
    String f = "";
    if (prt.length >= 2) {
      f = prt[1];
      time = prt[0];
    }

    prt = time.split(":");
    String hh = zeroPadding(prt[0]);

    String mi = "00";
    if (prt.length >= 2) {
      mi = zeroPadding(prt[1]);
    }

    String ss = "00";
    if (prt.length >= 3) {
      ss = zeroPadding(prt[2]);
    }

    time = hh + mi + ss + f;
    return __serializeDateTime(date + time);
  }

  private static String __serializeDateTime(String s) {
    s = s.replaceAll("[-\\s:\\.]", "");
    s = (s + "000000000").substring(0, 17);
    return s;
  }

  private static String[] splitDateTimeAndTimezone(String s) {
    String w = s;
    String tz = null;
    if (w.length() > 10) {
      int zPos = getTzPos(w);
      if (zPos != -1) {
        tz = w.substring(zPos);
        w = w.substring(0, zPos);
      }
    }
    String[] ret = { w, tz };
    return ret;
  }

  private static int getTzPos(String s) {
    int p = s.indexOf("Z");
    if (p != -1) {
      return p;
    }
    int tzSnCnt = countMatcher(s, "\\+");
    if (tzSnCnt == 1) {
      return s.indexOf("+");
    }
    tzSnCnt = countMatcher(s, "-");
    if (tzSnCnt > 0) {
      return s.indexOf("-", s.length() - 6);
    }
    return -1;
  };

  private static int countMatcher(String target, String regex) {
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(target);
    int count = 0;
    while (m.find()) {
      count++;
    }
    return count;
  }

  private static String intToStr2(int v) {
    String s = v + "";
    if (v < 10) {
      s = "0" + v;
    }
    return s;
  }

  private static String intToStr3(int v) {
    String s = v + "";
    if (v < 10) {
      s = "00" + v;
    } else if (v < 100) {
      s = "0" + v;
    }
    return s;
  }

  private static String zeroPadding(String s) {
    s = ("00" + s);
    s = s.substring(s.length() - 2);
    return s;
  }

}
