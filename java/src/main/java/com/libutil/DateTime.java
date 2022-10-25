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

import java.math.BigDecimal;
import java.math.RoundingMode;
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

  public static final long MINUTE = 60000;
  public static final long HOUR = 3600000;
  public static final long DAY = 86400000;

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
  private TimeZone timezone;

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

  public DateTime(long timestamp, TimeZone timezone) {
    Date date = new Date(timestamp);
    _init(date, timezone);
  }

  public DateTime(long timestamp, String timeZoneId) {
    Date date = new Date(timestamp);
    TimeZone tz = getTimeZoneFromId(timeZoneId);
    _init(date, tz);
  }

  /**
   * Allocates a DateTime object and initializes it to represent the specified
   * number of seconds since the standard base time known as "the epoch", namely
   * January 1, 1970, 00:00:00 GMT.
   *
   * @param unixtime
   *          the seconds since January 1, 1970, 00:00:00 GMT.
   */
  public DateTime(double unixtime) {
    long timestamp = secToMillis(unixtime);
    Date date = new Date(timestamp);
    _init(date, null);
  }

  public DateTime(double unixtime, TimeZone timezone) {
    long timestamp = secToMillis(unixtime);
    Date date = new Date(timestamp);
    _init(date, timezone);
  }

  public DateTime(double unixtime, String timeZoneId) {
    long timestamp = secToMillis(unixtime);
    Date date = new Date(timestamp);
    TimeZone tz = getTimeZoneFromId(timeZoneId);
    _init(date, tz);
  }

  public DateTime(Date date) {
    _init(date);
  }

  public DateTime(Date date, String timeZoneId) {
    TimeZone tz = getTimeZoneFromId(timeZoneId);
    _init(date, tz);
  }

  /**
   * Allocates a DateTime object and initializes it to represent the specified
   * date-time string.
   *
   * @param source
   *          The date time string.<br>
   *          The acceptable formats are:
   *
   *          <pre>
   * 20210102
   * 20210102T1234
   * 20210102T123456.789
   * 20210102T123456.789+0900
   * 2021-01-02
   * 2021-01-02 12:34
   * 2021-01-02 12:34:56
   * 2021-01-02 12:34:56.789
   * 2021-01-02 12:34:56.789 +09:00
   * 2021/1/2 12:34
   * 2021/1/2 12:34:56
   * 2021/1/2 12:34:56.789
   * 2021/1/2 12:34:56.789 +09:00
   *          </pre>
   */
  public DateTime(String source) {
    if (source == null) {
      _init(null, null);
      return;
    }
    String s = serializeDateTime(source);
    String[] w = splitDateTimeAndTimeZone(s);
    String tzId = w[1];
    String fmt = ((tzId == null) ? "yyyyMMddHHmmssSSS" : "yyyyMMddHHmmssSSSXX");
    SimpleDateFormat sdf = new SimpleDateFormat(fmt);
    Date date;
    try {
      date = sdf.parse(s);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    TimeZone tz = getTimeZoneFromId(tzId);
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
    TimeZone tz = getTimeZoneFromId(timeZoneId);
    int tzdiff = getTimeZoneOffsetDiff(tz);
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
    this.timezone = tz;
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
    long ts = timestamp + (86400000L * days);
    Date date = new Date(ts);
    TimeZone tz = timezone;
    String timeZoneId = tz.getID();
    return new DateTime(date, timeZoneId);
  }

  /**
   * Compares the instance and the specified DateTime object.
   *
   * @param datetime
   *          DateTime object to compare
   * @return a long value indicating the result of the comparison, as follows:<br>
   *         -1 if the instance is less than the datetime;<br>
   *         0, if the instance and the datetime are equal;<br>
   *         1 value if the instance is greater than the datetime.
   */
  public long compareTo(DateTime datetime) {
    long d = timestamp - datetime.getTimestamp();
    if (d == 0) {
      return 0;
    } else if (d < 0) {
      return -1;
    }
    return 1;
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

  public TimeZone getTimeZone() {
    return timezone;
  }

  public void setTimeZone(TimeZone timezone) {
    _init(date, timezone);
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
    if (timezone == null) {
      return "";
    }
    ZoneId zoneId = timezone.toZoneId();
    return zoneId.getId();
  }

  /**
   * Returns a long standard time name of this TimeZone suitable for presentation
   * to the user in the default locale.
   *
   * @return the name such as "Pacific Standard Time.", "日本標準時", etc.
   */
  public String getTimeZoneDisplayName() {
    if (timezone == null) {
      return "";
    }
    return timezone.getDisplayName();
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
    TimeZone tz = getTimeZoneFromId(targetTzId);
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
    if (timezone == null) {
      return false;
    }
    return timezone.useDaylightTime();
  }

  /**
   * Queries if the date of the instance is in Daylight Saving Time in this time
   * zone.
   *
   * @return true if the given date is in Daylight Saving Time, false, otherwise.
   */
  public boolean inDaylightTime() {
    if (timezone == null) {
      return false;
    }
    return timezone.inDaylightTime(date);
  }

  /**
   * Returns the date-time string of the instance in the default format.
   *
   * @return the string. e.g., "2022-04-29 12:34:56.789 +09:00"
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
    if (timezone != null) {
      sdf.setTimeZone(timezone);
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
   * Compares the two date-time.
   *
   * @param datetime1
   *          Date-time string 1
   * @param datetime2
   *          Date-time string 2
   * @return a long value indicating the result of the comparison, as follows:<br>
   *         0, if the datetime1 and datetime2 are equal;<br>
   *         a negative value if datetime1 is less than datetime2;<br>
   *         a positive value if datetime1 is greater than datetime2.
   */
  public static long compare(String datetime1, String datetime2) {
    DateTime dt1 = new DateTime(datetime1);
    DateTime dt2 = new DateTime(datetime2);
    return dt1.compareTo(dt2);
  }

  /**
   * Returns the date difference between the two specified times.
   *
   * @param t1
   *          timestamp1
   * @param t2
   *          timestamp2
   * @return the difference days
   */
  public static int diffDays(long t1, long t2) {
    return diffDays(t1, t2, null, false);
  }

  /**
   * Returns the date difference between the two specified times.
   *
   * @param t1
   *          timestamp1
   * @param t2
   *          timestamp2
   * @param timezone
   *          the time zone. (e.g., "+0900") if null, it will be treated as local
   *          time.
   * @return the difference days
   */
  public static int diffDays(long t1, long t2, String timezone) {
    return diffDays(t1, t2, timezone, false);
  }

  /**
   * Returns the date difference between the two specified times.
   *
   * @param t1
   *          timestamp1
   * @param t2
   *          timestamp2
   * @param abs
   *          If true, returns the result as an absolute value
   * @return the difference days
   */
  public static int diffDays(long t1, long t2, boolean abs) {
    return diffDays(t1, t2, null, abs);
  }

  /**
   * Returns the date difference between the two specified times.
   *
   * @param t1
   *          timestamp1
   * @param t2
   *          timestamp2
   * @param timezone
   *          the time zone. (e.g., "+0900") if null, it will be treated as local
   *          time.
   * @param abs
   *          If true, returns the result as an absolute value
   * @return the difference days
   */
  public static int diffDays(long t1, long t2, String timezone, boolean abs) {
    long d1 = getTimestampOfMidnight(t1, timezone);
    long d2 = getTimestampOfMidnight(t2, timezone);
    long d = d2 - d1;
    int sign = 1;
    if (d < 0) {
      d *= -1;
      if (!abs)
        sign = -1;
    }
    int days = (int) Math.floor(d / 86400000) * sign;
    return days;
  }

  /**
   * Returns the date difference between the two specified times. See
   * getTimestamp(String) for the acceptable date time formats.
   *
   * @param t1
   *          the date time string1
   * @param t2
   *          the date time string2
   * @return the difference days
   */
  public static int diffDays(String t1, String t2) {
    return diffDays(t1, t2, false);
  }

  /**
   * Returns the date difference between the two specified times.<br>
   * See getTimestamp(String) for the acceptable date time formats.
   *
   * @param t1
   *          the date time string1
   * @param t2
   *          the date time string2
   * @param abs
   *          If true, returns the result as an absolute value
   * @return the difference days
   */
  public static int diffDays(String t1, String t2, boolean abs) {
    long d1 = getTimestampOfMidnight(t1);
    long d2 = getTimestampOfMidnight(t2);
    long d = d2 - d1;
    int sign = 1;
    if (d < 0) {
      d *= -1;
      if (!abs)
        sign = -1;
    }
    int days = (int) Math.floor(d / 86400000) * sign;
    return days;
  }

  /**
   * Returns the current date-time object.
   *
   * @return the current date-time object
   */
  public static DateTime getCurrentDateTime() {
    return new DateTime();
  }

  /**
   * Returns the current date-time string in the specified format.
   *
   * @param format
   *          the pattern describing the date and time format
   * @return the current date-time string.
   */
  public static String getCurrentDateTimeString(String format) {
    DateTime dt = new DateTime();
    return dt.toString(format);
  }

  /**
   * Converts a date time string to UnixMillis.
   *
   * @param datetime
   *          The date time string.<br>
   *          The acceptable formats are:
   *
   *          <pre>
   * 20210102
   * 20210102T1234
   * 20210102T123456.789
   * 20210102T123456.789+0900
   * 2021-01-02
   * 2021-01-02 12:34
   * 2021-01-02 12:34:56
   * 2021-01-02 12:34:56.789
   * 2021-01-02 12:34:56.789 +09:00
   * 2021/1/2 12:34
   * 2021/1/2 12:34:56
   * 2021/1/2 12:34:56.789
   * 2021/1/2 12:34:56.789 +09:00
   *          </pre>
   *
   * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
   */
  public static long getTimestamp(String datetime) {
    return new DateTime(datetime).getTimestamp();
  }

  /**
   * Returns today's timestamp for the specified time.
   *
   * @param time
   *          the time
   * @return the timestamp
   */
  public static long getTimestampOfDay(String time) {
    return getTimestampOfDay(time, 0);
  }

  /**
   * Returns the timestamp of the day for the specified time.
   *
   * @param time
   *          the time
   * @param offset
   *          offset (-1=yesterday / 0=today / 1=tomorrow)
   * @return the timestamp
   */
  public static long getTimestampOfDay(String time, int offset) {
    DateTime dt = new DateTime();
    String date = dt.toString("yyyyMMdd");
    String s = date + "T" + time;
    long timestamp = new DateTime(s).getTimestamp();
    timestamp += (offset * DAY);
    return timestamp;
  }

  /**
   * Returns the midnight timestamp for the given moment.
   *
   * @param moment
   *          the moment (e.g., "20220120T1234546.789",
   *          "20220120T1234546.789+0900")
   * @return the midnight timestamp
   */
  public static long getTimestampOfMidnight(String moment) {
    DateTime dt = new DateTime(moment);
    int year = dt.getYear();
    int month = dt.getMonth();
    int day = dt.getDay();
    String timeZoneId = dt.getTimeZoneId();
    DateTime dt0 = new DateTime(year, month, day, timeZoneId);
    long midnight = dt0.getTimestamp();
    return midnight;
  }

  /**
   * Returns the midnight timestamp for the given moment.
   *
   * @param timestamp
   *          the moment
   * @return the midnight timestamp
   */
  public static long getTimestampOfMidnight(long timestamp) {
    return getTimestampOfMidnight(timestamp, null);
  }

  /**
   * Returns the midnight timestamp for the given moment.
   *
   * @param timestamp
   *          the moment
   * @param timezone
   *          the time zone. (e.g., "+0900") if null, treated as the local time.
   * @return the midnight timestamp
   */
  public static long getTimestampOfMidnight(long timestamp, String timezone) {
    DateTime dt = new DateTime(timestamp, timezone);
    int year = dt.getYear();
    int month = dt.getMonth();
    int day = dt.getDay();
    DateTime dt0 = new DateTime(year, month, day, timezone);
    long midnight = dt0.getTimestamp();
    return midnight;
  }

  /**
   * Returns the TimeZone for the given ID.
   *
   * @param id
   *          the ID for a TimeZone.
   * @return the specified TimeZone, or the local TimeZone if the given ID is
   *         null, otherwise (the ID cannot be understood) the GMT zone.
   */
  public static TimeZone getTimeZoneFromId(String id) {
    TimeZone timezone;
    if (id == null) {
      return TimeZone.getDefault();
    }

    Pattern p = Pattern.compile("^[+-].+$", 0);
    Matcher m = p.matcher(id);
    if (m.find()) {
      id = "GMT" + id;
    }
    timezone = TimeZone.getTimeZone(id);
    return timezone;
  }

  /**
   * Returns the difference in time zone offset of time zone id with respect to
   * local time in milliseconds.
   *
   * @param timeZoneId
   *          time zone id to compare
   * @return the difference in milliseconds
   */
  public static int getTimeZoneOffsetDiff(String timeZoneId) {
    TimeZone tz0 = TimeZone.getDefault();
    TimeZone tz1 = TimeZone.getTimeZone(timeZoneId);
    return getTimeZoneOffsetDiff(tz0, tz1);
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
  public static int getTimeZoneOffsetDiff(String timeZoneId0, String timeZoneId1) {
    TimeZone tz0 = TimeZone.getTimeZone(timeZoneId0);
    TimeZone tz1 = TimeZone.getTimeZone(timeZoneId1);
    return getTimeZoneOffsetDiff(tz0, tz1);
  }

  /**
   * Returns the difference in time zone offset of tz1 with respect to local time
   * in milliseconds.
   *
   * @param tz
   *          time zone to compare
   * @return the difference in milliseconds
   */
  public static int getTimeZoneOffsetDiff(TimeZone tz) {
    TimeZone tz0 = TimeZone.getDefault();
    return getTimeZoneOffsetDiff(tz0, tz);
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
  public static int getTimeZoneOffsetDiff(TimeZone tz0, TimeZone tz1) {
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
   * Normalizes the time zone offset string.
   *
   * @param s
   *          "-0800", "Z", "+00:00", "+5.5", "+9"
   * @return the time zone offset string like "+0900"
   */
  public static String normalizeTimeZone(String s) {
    if (s == null) {
      return null;
    }
    if (s.equals("Z")) {
      return "+0000";
    }
    s = s.replace(":", "");

    Pattern p = Pattern.compile("^[+-].+", 0);
    Matcher m = p.matcher(s);
    if (!m.find()) {
      return null;
    }

    String sn = s.substring(0, 1);
    s = s.substring(1);

    p = Pattern.compile("\\.", 0);
    m = p.matcher(s);
    if (m.find()) {
      s = Time.hoursToClockString(s, "");
    }

    int len = s.length();
    if (len == 1) {
      s = "0" + s + "00";
    } else if (len == 2) {
      s = s + "00";
    } else if (len == 3) {
      s = "0" + s;
    }

    String tz = sn + s;
    if (tz.equals("-0000")) {
      tz = "+0000";
    }

    return tz;
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
      throw new RuntimeException("PARSE_ERROR: " + src, e);
    }
  }

  private static String _serializeDateTime(String src) {
    String[] dttz = splitDateTimeAndTimeZone(src);
    String sdt = dttz[0];
    String tzId = dttz[1];

    String w = sdt;
    w = w.trim();
    w = w.replaceAll("\\s{2,}", " ");
    w = w.replaceAll("T", " ");

    Pattern p = Pattern.compile("[-/]");
    Matcher m = p.matcher(w);
    if (!m.find()) {
      return __serializeDateTime(w, tzId);
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
    if (mm.equals("00")) {
      mm = "01";
    }
    if (dd.equals("00")) {
      dd = "01";
    }
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
    return __serializeDateTime(date + time, tzId);
  }

  private static String __serializeDateTime(String s, String tz) {
    s = s.replaceAll("[-\\s:\\.]", "");
    s = (s + "0000000000000").substring(0, 17);
    String w = s.substring(4, 6);
    if (w.equals("00")) {
      s = s.substring(0, 4) + "01" + s.substring(6);
    }
    w = s.substring(6, 8);
    if (w.equals("00")) {
      s = s.substring(0, 6) + "01" + s.substring(8);
    }
    if (tz != null) {
      s += normalizeTimeZone(tz);
    }
    return s;
  }

  /**
   * Splits the date-time and time zone.
   *
   * @param s
   *          the source string
   * @return [date-time, time-zone]
   */
  public static String[] splitDateTimeAndTimeZone(String s) {
    String w = s;
    String tz = null;

    int tzPos = getTzPos(w);
    if (tzPos != -1) {
      tz = w.substring(tzPos);
      w = w.substring(0, tzPos);
    }

    String[] ret = { w, tz };
    return ret;
  }

  private static int getTzPos(String s) {
    int p = s.indexOf("Z");
    if (p != -1) {
      return p;
    }
    int cnt = StrUtil.countPattern(s, "\\+");
    if (cnt == 1) {
      return s.lastIndexOf("+");
    }
    cnt = StrUtil.countPattern(s, "-");
    if (cnt == 2) {
      return -1;
    }
    return s.lastIndexOf("-");
  };

  /**
   * Converts seconds to milliseconds.
   *
   * @param sec
   *          seconds
   * @return milliseconds
   */
  public static long secToMillis(double sec) {
    BigDecimal bd = new BigDecimal(sec);
    bd = bd.setScale(3, RoundingMode.DOWN);
    double d = bd.doubleValue();
    double v = d * 1000;
    long unixmillis = (long) v;
    return unixmillis;
  }

  /**
   * Parses text from the beginning of the given string to produce a date.
   *
   * @param str
   *          A String whose beginning should be parsed
   * @param pattern
   *          the pattern describing the date and time format like "yyyy-MM-dd"
   * @return A Date parsed from the string
   * @throws ParseException
   *           If failed to parse
   */
  public static Date toDate(String str, String pattern) throws ParseException {
    return toDate(str, pattern, false);
  }

  /**
   * Parses text from the beginning of the given string to produce a date.
   *
   * @param str
   *          A String whose beginning should be parsed
   * @param pattern
   *          the pattern describing the date and time format like "yyyy-MM-dd"
   * @param lenient
   *          when true, parsing is lenient
   * @return A Date parsed from the string
   * @throws ParseException
   *           If failed to parse
   */
  public static Date toDate(String str, String pattern, boolean lenient) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    sdf.setLenient(lenient);
    Date date = sdf.parse(str);
    return date;
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
  public static String toString(long timestamp, String format) {
    DateTime dt = new DateTime(timestamp);
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
  public static String toString(Date date, String format) {
    DateTime dt = new DateTime(date);
    return dt.toString(format);
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
  public static String toString(String datetime, String format) {
    DateTime dt;
    dt = new DateTime(datetime);
    return dt.toString(format);
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
