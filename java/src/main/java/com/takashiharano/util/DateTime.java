package com.takashiharano.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
    this(new Date(timestamp));
  }

  public DateTime(Date date) {
    _init(date);
  }

  public DateTime(String source) throws ParseException {
    this(source, DEFAULT_FORMAT);
  }

  public DateTime(String source, String format) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date date = sdf.parse(source);
    _init(date);
  }

  public DateTime(int year, int month, int day) throws ParseException {
    this(year, month, day, 0, 0, 0, 0, null);
  }

  public DateTime(int year, int month, int day, String timeZoneId) throws ParseException {
    this(year, month, day, 0, 0, 0, 0, timeZoneId);
  }

  public DateTime(int year, int month, int day, int hour, int minute) throws ParseException {
    this(year, month, day, hour, minute, 0, 0, null);
  }

  public DateTime(int year, int month, int day, int hour, int minute, String timeZoneId) throws ParseException {
    this(year, month, day, hour, minute, 0, 0, timeZoneId);
  }

  public DateTime(int year, int month, int day, int hour, int minute, int second) throws ParseException {
    this(year, month, day, hour, minute, second, 0, null);
  }

  public DateTime(int year, int month, int day, int hour, int minute, int second, String timeZoneId)
      throws ParseException {
    this(year, month, day, hour, minute, second, 0, timeZoneId);
  }

  public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond)
      throws ParseException {
    this(year, month, day, hour, minute, second, millisecond, null);
  }

  /**
   * Allocates a DateTime object and initializes with the given parameters.
   *
   * @param year
   * @param month
   * @param day
   * @param hour
   * @param minute
   * @param second
   * @param millisecond
   * @param timeZoneId
   *          the ID for a TimeZone, such as "PST", "America/Los_Angeles",
   *          "GMT-8:00"
   * @throws ParseException
   */
  public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, String timeZoneId)
      throws ParseException {
    Calendar calendar = Calendar.getInstance();
    if (timeZoneId != null) {
      TimeZone tz = TimeZone.getTimeZone(timeZoneId);
      calendar.setTimeZone(tz);
    }
    calendar.set(year, --month, day, hour, minute, second);
    calendar.set(Calendar.MILLISECOND, millisecond);
    calendar.getTimeInMillis();
    long timestamp = calendar.getTimeInMillis();
    Date date = new Date(timestamp);
    _init(date, timeZoneId);
  }

  private void _init(Date date) {
    _init(date, null);
  }

  private void _init(Date date, String timeZoneId) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    TimeZone tz;
    if (timeZoneId == null) {
      tz = TimeZone.getDefault();
    } else {
      tz = TimeZone.getTimeZone(timeZoneId);
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
   * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
   * represented by this Date object.
   *
   * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
   *         represented by this date.
   */
  public long getTimeStamp() {
    return timestamp;
  }

  /**
   * Returns year
   *
   * @return year
   */
  public int getYear() {
    return year;
  }

  /**
   * Returns month
   *
   * @return month (1-12)
   */
  public int getMonth() {
    return month;
  }

  /**
   * Returns day of month.
   *
   * @return day of month (1-31)
   */
  public int getDay() {
    return day;
  }

  /**
   * Returns day of week.<br>
   *
   * @return day of week<br>
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
    String timeZoneId = timeZone.getID();
    _init(date, timeZoneId);
  }

  /**
   * Sets time zone by id.
   *
   * @param timeZoneId
   *          the ID for a TimeZone, such as "PST", "America/Los_Angeles",
   *          "GMT-8:00"
   */
  public void setTimeZone(String timeZoneId) {
    _init(date, timeZoneId);
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
   * Returns the Date Time String of the instance in the default format.
   */
  public String toString() {
    return toString(DEFAULT_FORMAT);
  }

  /**
   * Returns the Date Time String of the instance in the specified format.
   *
   * @param format
   *          "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ->
   *          "2019-07-13T12:34:56.987+09:00"<br>
   *          "yyyyMMdd'T'HHmmssSSSXX" -> "20190713T123456.987+0900"
   * @return
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
   * Returns current time stamp.
   *
   * @return the difference, measured in milliseconds, between the current time
   *         and midnight, January 1, 1970 UTC.
   */
  public static long now() {
    return System.currentTimeMillis();
  }

  /**
   * Returns current Date Time String in the specified format.
   *
   * @param format
   *          the pattern describing the date and time format
   *          "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ->
   *          "2020-07-01T12:34:56.987+09:00"<br>
   *          "yyyyMMdd'T'HHmmssSSSXX" -> "20190701T123456.987+0900"
   * @return the formatted date-time string
   */
  public static String formatDateTime(String format) {
    DateTime dt = new DateTime();
    return dt.toString(format);
  }

  /**
   * Returns Date Time String from Date object.
   *
   * @param date
   * @param format
   *          "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ->
   *          "2019-07-13T12:34:56.987+09:00"<br>
   *          "yyyyMMdd'T'HHmmssSSSXX" -> "20190713T123456.987+0900"
   * @return
   */
  public static String formatDateTime(Date date, String format) {
    DateTime dt = new DateTime(date);
    return dt.toString(format);
  }

  /**
   * Returns current Date Time String in the specified format. * @param millis
   *
   * @param timestamp
   *          the milliseconds since January 1, 1970, 00:00:00 GMT.
   * @param format
   *          the pattern describing the date and time format
   *          "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ->
   *          "2020-07-01T12:34:56.987+09:00"<br>
   *          "yyyyMMdd'T'HHmmssSSSXX" -> "20190701T123456.987+0900"
   * @return the formatted date-time string
   */
  public static String formatDateTime(long timestamp, String format) {
    DateTime dt = new DateTime(timestamp);
    return dt.toString(format);
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
   * e.g., (1234, "HH:mm:ss.SSS") -> "00:00:01.234"
   *
   * @param millis
   *          milliseconds to format
   * @param format
   *          "HH:mm:ss.SSS"
   * @return the formatted time string
   */
  public static String formatTime(long millis, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    TimeZone tz = TimeZone.getTimeZone("UTC");
    sdf.setTimeZone(tz);
    String time = sdf.format(millis);
    return time;
  }

  /**
   * Returns if the year is leap year.
   *
   * @param year
   * @return true if the year is leap year
   */
  public static boolean isLeapYear(int year) {
    return ((((year % 4) == 0) && ((year % 100) != 0)) || (year % 400 == 0));
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
   */
  public static Date parseDate(String pattern, String str) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    Date date = sdf.parse(str);
    return date;
  }

}
