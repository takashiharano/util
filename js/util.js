/*!
 * util.js
 * Copyright 2019 Takashi Harano
 * Released under the MIT license
 * https://libutil.com/
 */
var util = util || {};
util.v = '202402122023';

util.SYSTEM_ZINDEX_BASE = 0x7ffffff0;
util.DFLT_FADE_SPEED = 500;
util.LS_AVAILABLE = false;
util.mouseX = 0;
util.mouseY = 0;

//---------------------------------------------------------
// Date & Time
//---------------------------------------------------------
util.MINUTE = 60000;
util.HOUR = 3600000;
util.DAY = 86400000;

util.MINUTE_SEC = 60;
util.HOUR_SEC = 3600;
util.DAY_SEC = 86400;

util.SUNDAY = 0;
util.MONDAY = 1;
util.TUESDAY = 2;
util.WEDNESDAY = 3;
util.THURSDAY = 4;
util.FRIDAY = 5;
util.SATURDAY = 6;
util.WDAYS = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];
util.MONTH = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'];

/**
 * DateTime class
 * src:
 *  timestamp (millis from 1970-01-01T00:00:00Z) / Date-Time-String / Date object
 * tzOffset: (OPT)
 *  -480='-0800' / 0='+0000'='Z' / 540='+0900'
 */
util.DateTime = function(src, tzOffset) {
  var dt, st;
  if (!src && (src !== 0)) {
    dt = new Date();
  } else if (typeof src == 'string') {
    if (tzOffset != undefined) {
      src += tzOffset;
      tzOffset = undefined;
    }
    st = util.str2datestruct(src);
    dt = new Date(st.year, st.month - 1, st.day, st.hour, st.minute, st.second);
  } else if (src instanceof Date) {
    dt = src;
  } else {
    if (util.isFloat(src)) src *= 1000;
    dt = new Date(src);
  }
  var timestamp = dt.getTime();

  if (tzOffset == undefined) {
    tzOffset = ((st && st.tz) ? st.tz : util.getLocalTZ());
  } else {
    var os = tzOffset;
    if (typeof os == 'string') os = util.getOffsetFromLocalTz(os);
    dt = new Date(timestamp + os);
    timestamp = dt.getTime();
  }

  this.tz = '';
  if (st) {
    timestamp += st.millisecond;
    if (st.tz != '') {
      var tzdf = util.getOffsetFromLocalTz(st.tz);
      timestamp -= tzdf;
    }
    this.tz = st.tz;
  }

  var tzOffsetMin = tzOffset;
  if (typeof tzOffset == 'string') tzOffsetMin = util.tz2ms(tzOffset) / 60000;
  var year = dt.getFullYear();
  var month = dt.getMonth() + 1;
  var day = dt.getDate();
  var hour = dt.getHours();
  var minute = dt.getMinutes();
  var second = dt.getSeconds();
  var millisecond = (st ? st.millisecond : dt.getMilliseconds());

  this.timestamp = timestamp;
  this.year = year;
  this.month = month;
  this.day = day;
  this.hour = hour;
  this.minute = minute;
  this.second = second;
  this.millisecond = millisecond;
  this.tzOffset = tzOffset;
  this.tzOffsetMin = tzOffsetMin;
  this.wday = dt.getDay(); // Sunday=0 - Saturday=6
  this.WDAYS = util.WDAYS;

  this.yyyy = year + '';
  this.mm = util.lpad(month, '0', 2);
  this.dd = util.lpad(day, '0', 2);
  this.hh = util.lpad(hour, '0', 2);
  this.mi = util.lpad(minute, '0', 2);
  this.ss = util.lpad(second, '0', 2);
  this.sss = util.lpad(millisecond, '0', 3);
};
util.DateTime.prototype = {
  setWdays: function(wdays) {
    this.WDAYS = wdays;
  },
  // -> '+0900' / ext=true: '+09:00'
  getTZ: function(ext) {
    return util.formatTZ(this.tzOffsetMin, ext);
  },
  toString: function(fmt) {
    if (!fmt) fmt = '%YYYY-%MM-%DD %HH:%mm:%SS.%sss %Z';
    var yyyy = this.year + '';
    var yy = yyyy.substr(2);
    var M = this.month + '';
    var MM = ('0' + M).slice(-2);
    var mName = util.MONTH[this.month - 1];
    var d = this.day;
    var dd = ('0' + d).slice(-2);
    var h = '' + this.hour;
    var hh = ('0' + h).slice(-2);
    var m = '' + this.minute;
    var mm = ('0' + m).slice(-2);
    var S = '' + this.second;
    var SS = ('0' + S).slice(-2);
    var sss = ('00' + this.millisecond).slice(-3);
    var s = util.trimTrailingZeros(sss);
    var ampm = 'AM';
    var h12 = '' + this.hour;
    if (this.hour >= 12) {
      ampm = 'PM';
      h12 = (this.hour - 12) + '';
    }
    var hh12 = ('0' + h12).slice(-2);
    var r = fmt;
    r = r.replace(/%YYYY/g, yyyy);
    r = r.replace(/%YY/g, yy);
    r = r.replace(/%MMM/g, mName);
    r = r.replace(/%MM/g, MM);
    r = r.replace(/%M/g, M);
    r = r.replace(/%DD/g, dd);
    r = r.replace(/%D/g, d);
    r = r.replace(/%W/g, this.WDAYS[this.wday]);
    r = r.replace(/%AMPM/g, ampm);
    r = r.replace(/%H12/g, h12);
    r = r.replace(/%HH12/g, hh12);
    r = r.replace(/%HH/g, hh);
    r = r.replace(/%H/g, h);
    r = r.replace(/%mm/g, mm);
    r = r.replace(/%m/g, m);
    r = r.replace(/%SS/g, SS);
    r = r.replace(/%S/g, S);
    r = r.replace(/%sss/g, sss);
    r = r.replace(/%s/g, s);
    r = r.replace(/%z/g, this.getTZ());
    r = r.replace(/%Z/g, this.getTZ(true));
    r = r.replace(/%N/g, util.getLocalTzName());
    r = r.replace(/\\/g, '');
    return r;
  }
};

util.str2datestruct = function(s) {
  var w = util.serializeDateTime(s);
  var a = util.splitDateTimeAndTZ(w);
  w = a[0];
  var tz = a[1];
  var yyyy = w.substr(0, 4) | 0;
  var mm = w.substr(4, 2) | 0;
  var dd = w.substr(6, 2) | 0;
  var hh = w.substr(8, 2) | 0;
  var mi = w.substr(10, 2) | 0;
  var ss = w.substr(12, 2) | 0;
  var sss = w.substr(14, 3) | 0;
  var st = {
    year: yyyy,
    month: mm,
    day: dd,
    hour: hh,
    minute: mi,
    second: ss,
    millisecond: sss,
    tz: tz
  };
  return st;
};
util.splitDateTimeAndTZ = function(s) {
  var w = s;
  var tz = '';
  var tzPos = util._getTzPos(w);
  if (tzPos != -1) {
    tz = w.substr(tzPos);
    w = w.substr(0, tzPos);
  }
  return [w, tz];
};
util._getTzPos = function(s) {
  var p = s.indexOf('Z');
  if (p != -1) return p;
  var cnt = util.countStr(s, '+');
  if (cnt == 1) return s.lastIndexOf('+');
  cnt = util.countStr(s, '-');
  if (cnt == 2) return -1;
  return s.lastIndexOf('-');
};

/**
 * Format the date and time string in YYYYMMDDHHMISSsss format
 * 20200920                -> 20200920000000000
 * 20200920T1234           -> 20200920123400000
 * 20200920T123456.789     -> 20200920123456789
 * 2020-09-20 12:34:56.789 -> 20200920123456789
 * 2020/9/3 12:34:56.789   -> 20200903123456789
 */
util.serializeDateTime = function(s) {
  var a = util.splitDateTimeAndTZ(s);
  var w = a[0];
  var tz = a[1];
  w = w.trim().replace(/\s{2,}/g, ' ').replace(/T/, ' ').replace(/,/, '.');
  if (!w.match(/[-/:]/)) return util._serializeDateTime(w, tz);
  var prt = w.split(' ');
  var date = prt[0];
  var time = (prt[1] ? prt[1] : '');
  date = date.replace(/\//g, '-');
  prt = date.split('-');
  var y = prt[0];
  var m = util.lpad(prt[1], '0', 2);
  var d = util.lpad(prt[2], '0', 2);
  date = y + m + d;
  prt = time.split('.');
  var ms = '';
  if (prt[1]) {
    ms = prt[1];
    time = prt[0];
  }
  prt = time.split(':');
  var hh = prt[0] | 0;
  var mi = prt[1] | 0;
  var ss = prt[2] | 0;
  hh = util.lpad(hh, '0', 2);
  mi = util.lpad(mi, '0', 2);
  ss = util.lpad(ss, '0', 2);
  time = hh + mi + ss + ms;
  return util._serializeDateTime(date + time, tz);
};
util._serializeDateTime = function(s, tz) {
  s = s.replace(/-/g, '').replace(/\s/g, '').replace(/:/g, '').replace(/\./g, '');
  tz = util.normalizeTZ(tz);
  return (s + '000000000').substr(0, 17) + tz;
};

util.now = function() {
  return Date.now();
};

/**
 * 20200920T123456
 * 20200920T123456+0900
 * 20200920T123456.789
 * 20200920T123456.789+0900
 * 2020-09-20T12:34:56
 * 2020-09-20T12:34:56+09:00
 * 2020-09-20T12:34:56.789
 * 2020-09-20T12:34:56.789+09:00
 * 2020-09-20 12:34:56
 * 2020-09-20 12:34:56 +09:00
 * 2020-09-20 12:34:56.789
 * 2020-09-20 12:34:56.789 +09:00
 * 2020/09/20 12:34:56.789 +09:00
 * -> millis from 19700101T0000Z
 */
util.getTimestamp = function(s) {
  return new util.DateTime(s).timestamp;
};

/**
 * Returns DateTime object
 * dt: timestamp / Date object
 */
util.getDateTime = function(dt, ofst) {
  return new util.DateTime(dt, ofst);
};

/**
 * Returns Date-Time string
 * t: timestamp / Date object
 * fmt: '%YYYY-%MM-%DD %HH:%mm:%SS.%sss %Z'
 * tz: '+0000'
 */
util.getDateTimeString = function(a1, a2, a3) {
  var t = a1;
  var fmt = a2;
  var tz = a3;
  if ((typeof t == 'string') && (t.match(/%/))) {
    fmt = t;
    t = null;
    tz = a2;
  }
  return (new util.DateTime(t, tz)).toString(fmt);
};

/**
 * '12:34:56.987' -> DateTime object
 * offset: -1 -> Yesterday
 *          0 -> Today (default)
 *          1 -> Tomorrow
 */
util.getDateTimeFromTime = function(timeString, offset) {
  var t = util.getTimestampOfDay(timeString, offset);
  return util.getDateTime(t);
};

/**
 * SUN=0 .. SAT=6
 * dt: '20231001' -> 0
 *     '20231002' -> 1
 *     '20231007' -> 6
 */
util.getWday = function(dt) {
  return util.getDateTime(dt).wday;
};

/**
 * dt: '20231001' -> 31
 *     '20231101' -> 30
 */
util.getLastDayOfMonth = function(dt) {
  var E = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
  var v = util.getDateTime(dt);
  var m = v.month;
  var d = E[m - 1];
  if ((m == 2) && util.isLeapYear(v.year)) d = 29;
  return d;
};

util.isLeapYear = function(y) {
  return ((((y % 4) == 0) && ((y % 100) != 0)) || (y % 400 == 0));
};

/**
 * '12:34:56.987' -> timestamp (millis from 1970-01-01T00:00:00Z)
 * offset: -1 -> Yesterday
 *          0 -> Today (default)
 *          1 -> Tomorrow
 */
util.getTimestampOfDay = function(timeString, offset) {
  var tm = timeString.replace(/:/g, '').replace(/\./, '');
  var hh = tm.substr(0, 2);
  var mi = tm.substr(2, 2);
  var ss = tm.substr(4, 2);
  var sss = tm.substr(6, 3);
  var d = util.getDateTime();
  var d1 = new Date(d.year, d.month - 1, d.day, hh, mi, ss, sss);
  var ts = d1.getTime();
  if (offset != undefined) ts += (offset * util.DAY);
  return ts;
};

/**
 * Returns the midnight timestamp of the date-time.
 * dt: date-time string or timestamp in millis
 * offset: TZ offset. '-1200' to '+1400' (abs) / -12 to 14 (rel)
 * 1628679929040 -> 1628640000000 (offset='+0000')
 */
util.getMidnightTimestamp = function(dt, offset) {
  var ms = dt;
  var os = 0;
  if (dt == undefined) {
    dt = util.getDateTime();
  } else if (typeof dt == 'string') {
    dt = util.getDateTime(dt);
    ms = dt.timestamp;
    if ((offset == undefined) && dt.tz) os = util.getOffsetFromLocalTz(dt.tz);
  }
  if (offset != undefined) {
    os = ((typeof offset == 'string') ? util.getOffsetFromLocalTz(offset) : offset * 3600000);
  }
  var t = ((typeof dt == 'number') ? util.getDateTime(ms) : dt);
  var d = new Date(t.year, t.month - 1, t.day);
  var r = new util.DateTime(d).timestamp;
  return r - os;
};

/**
 * Returns time zone offset string from hours/minutes
 * -8         -> -0800
 *  9         -> +0900
 *  9, true   -> +09:00
 * -480       -> -0800
 *  540       -> +0900
 *  540, true -> +09:00
 */
util.formatTZ = function(v, e) {
  var s = '+';
  v = parseFloat(v);
  if (v < 0) {
    s = '-';
    v *= -1;
  }
  var f = ((v <= 24) ? util.formatTzH : util.formatTzM);
  var str = s + f(v);
  if (e) str = str.substr(0, 3) + ':' + str.substr(3, 2);
  return str;
};
util.formatTzH = function(v) {
  var w = ('' + v).split('.');
  var h = +w[0];
  var m = +('0.' + (w[1] | 0)) * 60;
  return ('0' + h).slice(-2) + ('0' + m).slice(-2);
};
util.formatTzM = function(v) {
  var h = (v / 60) | 0;
  var m = v - h * 60;
  return ('0' + h).slice(-2) + ('0' + m).slice(-2);
};

/**
 * '+00:00', '+0000', 'Z' -> '+0000'
 */
util.normalizeTZ = function(s) {
  if (!s) return s;
  if (s == 'Z') return '+0000';
  s = s.replace(/:/, '');
  if (!s.match(/^[+-].+/)) return null;
  var sn = s.substr(0, 1);
  s = s.substr(1);
  if (s.match(/\./)) s = util.hours2clock(s, '');
  var len = s.length;
  if (len == 1) {
    s = '0' + s + '00';
  } else if (len == 2) {
    s = s + '00';
  } else if (len == 3) {
    s = '0' + s;
  }
  var tz = sn + s;
  if (tz == '-0000') tz = '+0000';
  return tz;
};

/**
 * '09:30' -> 9.5
 */
util.clock2hours = function(s) {
  s = s.replace(/:/, '');
  var p = s.length - 2;
  var h = s.substr(0, p) | 0;
  var m = s.substr(p, 2) | 0;
  return h + (m / 60);
};

/**
 * 9.5, ':' -> '09:30'
 */
util.hours2clock = function(s, sep) {
  if (sep == undefined) sep = ':';
  s += '';
  var sign = '';
  if (s.match(/^[+-]/)) {
    sign = s.substr(0, 1);
    s = s.substr(1);
  }
  var w = s.split('.');
  var h = w[0] | 0;
  var fM = 0;
  if (w.length >= 2) fM = parseFloat('0.' + w[1]);
  var m = (60 * fM) | 0;
  var hh = ((h < 10) ? '0' + h : h);
  var mm = ((m < 10) ? '0' + m : m);
  return (sign + hh + sep + mm);
};

/**
 * Returns local time zone offset string
 * +0900
 * ext=true: +09:00
 */
util.getLocalTZ = function(ext) {
  return util.formatTZ(new Date().getTimezoneOffset() * (-1), ext);
};

/**
 * Returns local time zone offset value
 */
util.getLocalTzVal = function() {
  return util.clock2hours(util.getLocalTZ());
};

/**
 * Returns TZ database name
 * i.e., America/Los_Angeles
 */
util.getLocalTzName = function() {
  var n = Intl.DateTimeFormat().resolvedOptions().timeZone;
  if (!n) n = '';
  return n;
};

/**
 * Returns local TZ offset in minutes
 * at +0900 ->  540
 * at -0800 -> -480
 */
util.getLocalTzOffsetMin = function() {
  return new Date().getTimezoneOffset() * -1;
};

/**
 * '+0100'  ->  3600000
 * '+01:00' ->  3600000
 * '-0100'  -> -3600000
 * '-01:00' -> -3600000
 */
util.tz2ms = function(tz) {
  if (tz == 'Z') tz = '+0000';
  return util.clock2ms(tz);
};

/**
 * (at +0900)
 * '+0800' or    8 -> -3600000
 * '+1030' or 10.5 ->  5400000
 */
util.getOffsetFromLocalTz = function(tz) {
  if (tz == undefined) return 0;
  var ms;
  if (typeof tz == 'string') {
    ms = util.tz2ms(tz);
  } else {
    ms = tz * 3600000;
  }
  var os = new Date().getTimezoneOffset() * 60000;
  return os + ms;
};

/**
 * t0: YYYY-MM-DD HH:MI:SS.sss
 * t1: YYYY-MM-DD HH:MI:SS.sss
 * return t1 - t0 in millis
 */
util.difftime = function(t0, t1) {
  t0 = util.getTimestamp(t0);
  t1 = (t1 == undefined ? Date.now() : util.getTimestamp(t1));
  return t1 - t0;
};

/**
 * ms1: baseline timestamp
 * ms2: comparison timestamp
 * offset: timezone offset. -12 or '-1200' to 14 or '+1400'
 * abs: absolute(opt)
 * 1581217200000, 1581066000000 -> -1 (abs=true: 1)
 * 1581217200000, 1581217200000 ->  0
 * 1581217200000, 1581318000000 ->  1
 */
util.diffDays = function(ms1, ms2, offset, abs) {
  var sign = 1;
  ms1 = util.getMidnightTimestamp(ms1, offset);
  ms2 = util.getMidnightTimestamp(ms2, offset);
  var d = ms2 - ms1;
  if (d < 0) {
    d *= -1;
    if (!abs) sign = -1;
  }
  return Math.floor(d / 86400000) * sign;
};

/**
 * Calc Estimated Time of Accomplishment
 * t0: origin unix millis
 * total: total value
 * val: current value
 * t1: calculation time point
 */
util.calcETA = function(t0, total, val, t1) {
  if (t1 == undefined) t1 = Date.now();
  var dt = t1 - t0;
  var avg = dt / val;
  var tt = Math.ceil(avg * total);
  var rt = Math.ceil(avg * (total - val));
  var r = {
    completion: t1 + rt,
    remaining: rt,
    totaltime: tt,
    elapsed: dt,
    average: Math.ceil(dt / val)
  };
  return r;
};

//---------------------------------------------------------
/**
 * Time class
 * t:
 *   millis or '[+|-]12:34:56.789'
 */
util.Time = function(t) {
  if (typeof t == 'string') t = util.clock2ms(t);
  var tm = util.ms2struct(t);
  this.millis = t;
  this.days = tm.days;
  this.hours = tm.hours;
  this.hours24 = tm.hours24;
  this.minutes = tm.minutes;
  this.seconds = tm.seconds;
  this.milliseconds = tm.milliseconds;
};
util.Time.prototype = {
  /**
   * fmt
   *  '%HH:%mm:%SS.%sss'
   *  '%dd %HH:%mm:%SS.%sss'
   *  '%HR:%mm:%SS.%sss'
   *  '%HRh %m\\m %Ss %sms'
   */
  toString: function(fmt) {
    var ctx = this;
    if (!fmt) fmt = '%HH:%mm:%SS.%sss';
    var vH = ctx.hours;
    var vM = ctx.minutes;
    var vS = ctx.seconds;
    var vMS = ctx.milliseconds;
    if (!fmt.match(/%H/)) vM += vH * 60;
    if (!fmt.match(/%m/)) vS += vM * 60;
    if (!fmt.match(/%S/)) vMS += vS * 1000;
    var d = '' + ctx.days;
    var hr = '' + vH;
    var h = '' + ctx.hours24;
    var m = '' + vM;
    var mm = m;
    var S = '' + vS;
    var SS = S;
    var s = vMS;
    var hh = ('0' + h).slice(-2);
    if (vM < 10) mm = '0' + m;
    if (vS < 10) SS = '0' + S;
    var sss = ('00' + s).slice(-3);
    if (!fmt.match(/%d/) && (d > 0)) {
      h = d + 'd' + h;
      hh = d + 'd' + hh;
    }
    var r = fmt;
    r = r.replace(/%d/g, d);
    r = r.replace(/%HR/g, hr);
    r = r.replace(/%HH/g, hh);
    r = r.replace(/%H/g, h);
    r = r.replace(/%mm/g, mm);
    r = r.replace(/%m/g, m);
    r = r.replace(/%SS/g, SS);
    r = r.replace(/%S/g, S);
    r = r.replace(/%sss/g, sss);
    r = r.replace(/%s/g, s);
    r = r.replace(/\\/g, '');
    if (ctx.millis < 0) r = '-' + r;
    return r;
  },

  /**
   * Returns a human-readable string representation of the object.
   *
   * 1d 23h 45m 59s
   * h:
   *   >= 24h instead of days
   *   true: 47h 45m 59s
   * f:
   *   to display millis
   *   true: 1d 23h 45m 59s 123
   */
  toReadableString: function(h, f, z) {
    var ctx = this;
    var r = (ctx.millis < 0 ? '-' : '');
    var d = 0;
    if (!h && (ctx.days > 0)) {
      d = 1;
      r += ctx.days + 'd ';
    }
    if (h && (ctx.hours > 0)) {
      d = 1;
      r += ((z && r) ? (('0' + ctx.hours).substr(-2)) : ctx.hours) + 'h ';
    } else if (d || (ctx.hours24 > 0)) {
      d = 1;
      r += ((z && r) ? (('0' + ctx.hours24).substr(-2)) : ctx.hours24) + 'h ';
    }
    if (d || (ctx.minutes > 0)) {
      d = 1;
      r += ((z && r) ? (('0' + ctx.minutes).substr(-2)) : ctx.minutes) + 'm ';
    }
    if (f) {
      if (Math.abs(ctx.millis) >= 1000) {
        if (ctx.milliseconds == 0) {
          r += ((z && r) ? (('0' + ctx.seconds).substr(-2)) : ctx.seconds) + 's';
        } else {
          r += ((z && r) ? (('0' + ctx.seconds).substr(-2)) : ctx.seconds) + 's ' + (z ? ('00' + ctx.milliseconds).substr(-3) : ctx.milliseconds) + 'ms';
        }
      } else {
        r += ctx.milliseconds + 'ms';
      }
    } else {
      r += ((z && r) ? (('0' + ctx.seconds).substr(-2)) : ctx.seconds) + 's';
    }
    return r;
  }
};

/**
 * Milliseconds to string.
 *
 * fmt
 *  '%HH:%mm:%SS.%sss' = '1d12:34:56.789'
 */
util.ms2str = function(ms, fmt) {
  return (new util.Time(ms)).toString(fmt);
};

/**
 * Millis to a string (171959000 -> '1d 23h 45m 59s')
 *
 * mode:
 *   0: auto
 *   1: s
 *   2: ms
 */
util.msToReadableString = function(ms, mode, unsigned, zero) {
  var t = new util.Time(ms);
  var r = '';
  var sn = 0;
  if (ms < 0) {
    sn = 1;
    ms *= (-1);
  }
  if (mode == 2) {
    r = t.toReadableString(false, true, zero);
    if (unsigned) r = r.replace('-', '');
    return r;
  }
  if ((mode == 1) || (ms >= 60000)) {
    r = t.toReadableString(false, false, zero);
    if (unsigned) r = r.replace('-', '');
    return r;
  }
  var ss = t.seconds;
  var sss = t.milliseconds;
  if (ms < 1000) {
    r += sss + 'ms';
  } else {
    if (ms < 10000) {
      sss = sss - sss % 10;
    } else {
      sss = sss - sss % 100;
    }
    var msec = (sss + '').replace(/0+$/, '');
    if (sss == 0) {
      r += ss + 's';
    } else if (sss < 100) {
      r += ss + '.0' + msec + 's';
    } else {
      r += ss + '.' + msec + 's';
    }
  }
  if (!unsigned) r = (sn ? '-' : '') + r;
  return r;
};

/**
 * millis to struct
 * -> {'millis': millis, 'days': 1, 'hours': 24, 'hours24': 0, 'minutes': 34, 'seconds': 56, 'milliseconds': 123}
 */
util.ms2struct = function(millis) {
  var wk = millis;
  if (millis < 0) wk *= (-1);
  var d = (wk / 86400000) | 0;
  var hh = 0;
  if (wk >= 3600000) {
    hh = (wk / 3600000) | 0;
    wk -= (hh * 3600000);
  }
  var mi = 0;
  if (wk >= 60000) {
    mi = (wk / 60000) | 0;
    wk -= (mi * 60000);
  }
  var ss = (wk / 1000) | 0;
  var sss = wk - (ss * 1000);
  var tm = {
    millis: millis,
    days: d,
    hours: hh,
    hours24: hh - d * 24,
    minutes: mi,
    seconds: ss,
    milliseconds: sss
  };
  return tm;
};

/**
 *  123456.789
 * '123456.789'
 * -> 123456789
 */
util.sec2ms = function(sec) {
  return parseFloat(sec) * 1000;
};

/**
 *  1200
 * '1200'
 * -> 1.2
 * -> '1.200' (toString=true)
 */
util.ms2sec = function(ms, toString) {
  ms += '';
  var len = ms.length;
  var s;
  if (len <= 3) {
    s = '0.' + ('00' + ms).slice(-3);
  } else {
    s = ms.substr(0, len - 3) + '.' + ms.substr(len - 3);
  }
  if (!toString) s = parseFloat(s);
  return s;
};

//---------------------------------------------------------
// Time Counter
//---------------------------------------------------------
util.timecounter = {};
util.timecounter.id = 0;
util.timecounter.objs = {};
/**
 * Start displaying the time delta
 *
 * opt = {
 *  interval: 500,
 *  mode: 1,
 *  unsigned: false,
 *  cb: null
 * }
 */
util.timecounter.start = function(el, t0, opt) {
  if (!opt) opt = {};
  var o = util.timecounter.getObj(el);
  if (o) {
    if (t0 !== undefined) o.t0 = t0;
    if (opt.interval != undefined) o.interval = opt.interval;
    if (opt.mode != undefined) o.mode = opt.mode;
    if (opt.unsigned != undefined) o.unsigned = opt.unsigned;
    if (opt.cb != undefined) o.cb = opt.cb;
  } else {
    o = new util.TimeCounter(el, t0, opt);
    util.timecounter.objs[o.id] = o;
  }
  o.start();
};

/**
 * Stop displaying the time difference
 */
util.timecounter.stop = function(el, t0, opt) {
  var v = 0;
  var o = util.timecounter.getObj(el);
  if (!o) {
    o = new util.TimeCounter(el, t0, opt);
    util.timecounter.objs[o.id] = o;
  }
  if (t0 != undefined) o.t0 = t0;
  v = o.update(o);
  o.stop();
  delete util.timecounter.objs[o.id];
  return v;
};

util.timecounter.restart = function(el) {
  var v = 0;
  var o = util.timecounter.getObj(el);
  if (o) {
    v = o.update(o);
    o.t0 = Date.now();
  }
  util.timecounter.start(el);
  return v;
};

/**
 * Returns a string of time difference
 *
 * t0: from in millis / Date-Time-String
 * t1: to in millis / Date-Time-String (default=current time)
 *
 * t1:1600000083000 - t0:1600000000000 = 83000 -> '1m 23s'
 * t1:'2020-09-20 20:01:23' - t0:'2020-09-20 20:00:00' = 83000 -> '1m 23s'

 * unsigned: true='1m 23s' / false='1m 23s' | '-1m 23s'
 */
util.timecounter.delta = function(t0, t1, mode, unsigned, zero) {
  var ms = util.difftime(t0, t1);
  return util.msToReadableString(ms, mode, unsigned, zero);
};

/**
 * Returns the time difference value in millis
 */
util.timecounter.value = function(el) {
  var v = 0;
  var o = util.timecounter.getObj(el);
  if (o) v = o.update(o);
  return v;
};

/**
 * Returns a string of the time difference like '1m 23s'
 */
util.timecounter.getText = function(el) {
  var v = 0;
  var s = '';
  var o = util.timecounter.getObj(el);
  if (o) {
    v = o.update(o);
    s = util.msToReadableString(v, o.mode, o.unsigned, o.zero);
  }
  return s;
};

util.timecounter.getObj = function(el) {
  return util.getElRelObj(util.timecounter.objs, el);
};

util.timecounter.ids = function() {
  return util.objKeys(util.timecounter.objs);
};

/**
 * TimeCounter Class
 */
util.TimeCounter = function(el, t0, opt) {
  opt = util.copyDefaultProps(util.TimeCounter.DFLT_OPT, opt);
  this.el = el;
  this.t0 = (t0 == undefined ? Date.now() : t0);
  this.interval = opt.interval;
  this.mode = opt.mode;
  this.unsigned = opt.unsigned;
  this.zero = opt.zero;
  this.cb = opt.cb;
  this.id = '_timecounter-' + util.timecounter.id++;
};
util.TimeCounter.prototype = {
  start: function() {
    var ctx = this;
    util.IntervalProc.stop(ctx.id);
    util.IntervalProc.start(ctx.id, ctx.update, ctx.interval, ctx);
  },
  update: function(ctx) {
    var v = Date.now() - ctx.t0;
    var el = util.getElement(ctx.el);
    if (el) el.innerHTML = util.msToReadableString(v, ctx.mode, ctx.unsigned, ctx.zero);
    if (ctx.cb) ctx.cb(v);
    return v;
  },
  stop: function() {
    var ctx = this;
    util.IntervalProc.stop(ctx.id);
    util.IntervalProc.remove(ctx.id);
  }
};
util.TimeCounter.DFLT_OPT = {interval: 500, mode: 1, unsigned: false, cb: null};

//---------------------------------------------------------
// Clock
// util.clock('#clock1', opt);
//---------------------------------------------------------
util.clock = function(el, opt) {
  var o = util.clock.getObj(el);
  if (!o) {
    o = new util.Clock(el, opt);
    util.clock.objs[o.id] = o;
  }
  o.start(o);
  return o;
};
util.clock.id = 0;
util.clock.objs = {};
util.clock.getObj = function(el) {
  return util.getElRelObj(util.clock.objs, el);
};
util.clock.start = function(el) {
  var o = util.clock.getObj(el);
  if (o) o.start(o);
};
util.clock.stop = function(el) {
  var o = util.clock.getObj(el);
  if (o) o.stop(o);
};

/**
 * Clock class
 */
util.Clock = function(el, opt) {
  if (typeof opt == 'string') {
    opt = {fmt: opt};
  } else if (typeof a1 == 'number') {
    opt = {offset: opt};
  }
  opt = util.copyDefaultProps(util.Clock.DFLT_OPT, opt);
  this.el = el;
  this.opt = opt;
  this.interval = opt.interval;
  this.offset = opt.offset;
  this.tz = opt.tz;
  this.fmt = opt.fmt;
  this.tmId = 0;
  this.id = util.clock.id++;
};
util.Clock.prototype = {
  start: function(ctx) {
    ctx.stop(ctx);
    ctx.update(ctx);
  },
  update: function(ctx) {
    var el = util.getElement(ctx.el);
    if (el) {
      var t = Date.now();
      t += ctx.offset;
      el.innerHTML = new util.DateTime(t, ctx.tz).toString(ctx.fmt);
    }
    ctx.tmId = setTimeout(ctx.update, ctx.opt.interval, ctx);
  },
  stop: function(ctx) {
    if (ctx.tmId > 0) {
      clearTimeout(ctx.tmId);
      ctx.tmId = 0;
    }
  }
};
util.Clock.DFLT_OPT = {interval: 500, fmt: '%YYYY-%MM-%DD %W %HH:%mm:%SS', offset: 0, tz: util.getLocalTZ()};

//---------------------------------------------------------
// Time calculation
//---------------------------------------------------------
/**
 * ClockTime Class
 */
util.ClockTime = function(millis) {
  var clockMillis = millis;
  var days = 0;
  if (clockMillis < 0) {
    var wk = clockMillis * (-1);
    days = (wk / util.DAY) | 0;
    days = days + ((wk % util.DAY == 0) ? 0 : 1);
    wk = util.DAY - (wk - days * util.DAY);
    clockMillis = wk * (-1);
  } else if (clockMillis >= util.DAY) {
    days = (clockMillis / util.DAY) | 0;
    clockMillis -= days * util.DAY;
  }
  this.millis = millis;
  this.clockMillis = clockMillis;
  this.days = days;
  this.tm = util.ms2struct(millis);
  this.clockTm = util.ms2struct(clockMillis); // -00:01=23:59
};
util.ClockTime.prototype = {
  // %HH:%mm               '25:00'
  // %HH:%mm:%SS           '25:00:00'
  // %HH:%mm:%SS.%sss      '25:00:00.000'
  // %HH:%mm:%SS.%sss (%d) '01:00:00.000 (+1 Day)'
  toString: function(fmt) {
    if (!fmt) fmt = '%HH:%mm:%SS.%sss';
    var byTheDay = fmt.match(/%d/) != null;
    var h = this.toHrString(byTheDay);
    var m = this.toMinString(byTheDay);
    var S = this.toSecString(byTheDay);
    var s = this.toMilliSecString(byTheDay);
    var hh = (h < 10 ? ('0' + h) : h);
    var mm = (m < 10 ? ('0' + m) : m);
    var SS = (S < 10 ? ('0' + S) : S);
    var sss = ('00' + s).slice(-3);
    if ((this.millis < 0) && !byTheDay) {
      h = '-' + h;
      hh = '-' + hh;
    }
    var r = fmt;
    r = r.replace(/%HH/g, hh);
    r = r.replace(/%H/g, h);
    r = r.replace(/%mm/g, mm);
    r = r.replace(/%m/g, m);
    r = r.replace(/%SS/g, SS);
    r = r.replace(/%S/g, S);
    r = r.replace(/%sss/g, sss);
    r = r.replace(/%s/g, s);
    if (byTheDay) {
      var d = this.toDaysString();
      var dd = this.toDaysString(1);
      r = r.replace(/%dd/, dd);
      r = r.replace(/%d/, d);
    }
    r = r.replace(/\\/g, '');
    return r;
  },
  toDaysString: function(f) {
    var days = ((this.millis < 0) ? '-' : '+') + this.days;
    if (f) days += ' ' + util.plural('Day', this.days, true);
    return days;
  },
  toHrString: function(byTheDay) {
    if (byTheDay === undefined) byTheDay = false;
    return (byTheDay ? this.clockTm['hours24'] : this.tm['hours']) + '';
  },
  toMinString: function(byTheDay) {
    var st = (byTheDay ? this.clockTm : this.tm);
    return '' + st['minutes'];
  },
  toSecString: function(byTheDay) {
    var st = (byTheDay ? this.clockTm : this.tm);
    return '' + st['seconds'];
  },
  toMilliSecString: function(byTheDay) {
    var st = (byTheDay ? this.clockTm : this.tm);
    return '' + st['milliseconds'];
  }
};

/**
 * Add time
 * '12:00' + '01:30' -> '13:30'
 * '12:00' + '13:00' -> '25:00' / '01:00 (+1 Day)'
 * fmt:
 *  '%HH:%mm:%SS.%sss (%d)' -> '12:34:56.789 (+1 Day)'
 */
util.addTime = function(t1, t2, fmt) {
  if (!fmt) fmt = '%HH:%mm';
  var ms1 = util.clock2ms(t1);
  var ms2 = util.clock2ms(t2);
  var c = new util.ClockTime(ms1 + ms2);
  return c.toString(fmt);
};

/**
 * Sub time
 * '12:00' - '01:30' -> '10:30'
 * '12:00' - '13:00' -> '-01:00' / '23:00 (-1 Day)'
 * fmt:
 *  '%HH:%mm:%SS.%sss (%d)' -> '12:34:56.789 (-1 Day)'
 */
util.subTime = function(t1, t2, fmt) {
  if (!fmt) fmt = '%HH:%mm';
  var ms1 = util.clock2ms(t1);
  var ms2 = util.clock2ms(t2);
  var c = new util.ClockTime(ms1 - ms2);
  return c.toString(fmt);
};

/**
 * Multiply time
 * '01:30' * 2 -> '03:00'
 * '12:00' * 3 -> '36:00' / '12:00 (+1 Day)'
 * fmt:
 *  '%HH:%mm:%SS.%sss (%d)' -> '12:34:56.789 (+1 Day)'
 */
util.multiTime = function(t, v, fmt) {
  if (!fmt) fmt = '%HH:%mm';
  var ms = util.clock2ms(t);
  var c = new util.ClockTime(ms * v);
  return c.toString(fmt);
};

/**
 * Divide time
 * '03:00' / 2 -> '01:30'
 * '72:00' / 3 -> '24:00' / '00:00 (+1 Day)'
 * fmt:
 *  '%HH:%mm:%SS.%sss (%d)' -> '12:34:56.789 (+1 Day)'
 */
util.divTime = function(t, v, fmt) {
  if (!fmt) fmt = '%HH:%mm';
  var ms = util.clock2ms(t);
  var c = new util.ClockTime(ms / v);
  return c.toString(fmt);
};

// '09:00', '10:00' -> -1
// '10:00', '10:00' -> 0
// '10:00', '09:00' -> 1
util.timecmp = function(t1, t2) {
  var ms1 = util.clock2ms(t1);
  var ms2 = util.clock2ms(t2);
  var d = ms1 - ms2;
  if (d == 0) {
    return 0;
  } else if (d < 0) {
    return -1;
  }
  return 1;
};

// str: '[+|-][Dd]HH:MI:SS.sss'
// '01:00'         ->   3600000
// '01:00:30'      ->   3630000
// '01:00:30.123'  ->   3630123
// '0100'          ->   3600000
// '010030'        ->   3630000
// '010030.123'    ->   3630123
// '100:30:45.789' -> 361845789
// '+01:00'        ->   3600000
// '-01:00'        ->  -3600000
// '1d23:45:56.789' -> 171956789
util.clock2ms = function(str) {
  var prt;
  var d;
  var hour;
  var msec;
  var wk = str;
  var sn = false;
  if (wk.charAt(0) == '+') {
    wk = wk.substr(1);
  } else if (wk.charAt(0) == '-') {
    sn = true;
    wk = wk.substr(1);
  }

  if (wk.match(/d/)) {
    prt = wk.split('d');
    d = prt[0];
    wk = prt[1];
  }

  if (wk.match(/\./)) {
    prt = wk.split('.');
    wk = prt[0];
    msec = (prt[1] + '000').substr(0, 3);
  }

  var pos = wk.indexOf(':');
  if (pos != -1) {
    hour = wk.substr(0, pos);
    wk = wk.substr(pos + 1).replace(/:/g, '');
  } else {
    hour = wk.substr(0, 2);
    wk = wk.substr(2);
  }
  wk = (wk + '00').substr(0, 4);

  d |= 0;
  hour |= 0;
  var min = wk.substr(0, 2) | 0;
  var sec = wk.substr(2, 2) | 0;
  msec |= 0;
  var ms = (d * util.DAY) + (hour * util.HOUR) + (min * util.MINUTE) + sec * 1000 + msec;
  if (sn) ms *= (-1);
  return ms;
};

//---------------------------------------------------------
// ['0000', '1200', '1530']
// ['T0000', 'T1200', 'T1530']
// ['00:00', '12:00', '15:30']
// -> {time: '1200', datetime: DateTime object}
util.calcNextTime = function(times, dfltS) {
  var now = Date.now();
  var tList = [];
  for (var i = 0; i < times.length; i++) {
    var t = times[i].replace(/T/, '').replace(/:/g, '');
    tList.push(t);
  }
  tList.sort();
  var ret = {time: null, datetime: null};
  for (i = 0; i < tList.length; i++) {
    t = tList[i];
    var h = t.substr(0, 2);
    var m = t.substr(2, 2);
    var s = t.substr(4, 2);
    if (s == '') s = ((dfltS == undefined) ? '59' : dfltS);
    var tmstr = h + m + s + '.999';
    var tgt = util.getDateTimeFromTime(tmstr);
    if (now <= tgt.timestamp) {
      ret.time = tList[i];
      ret.datetime = tgt;
      return ret;
    }
  }
  ret.time = tList[0];
  ret.datetime = util.getDateTimeFromTime(tList[0], 1);
  return ret;
};

//---------------------------------------------------------
/**
 * 0.12345, 3 -> 0.123
 * 0.12345, 4 -> 0.1235
 * 12345, -1  -> 12350
 * 12345, -2-  > 12300
 */
util.round = function(num, scale) {
  return util._shift(Math.round(util._shift(num, scale, false)), scale, true);
};

util.floor = function(num, scale) {
  return util._shift(Math.floor(util._shift(num, scale, false)), scale, true);
};

util.ceil = function(num, scale) {
  return util._shift(Math.ceil(util._shift(num, scale, false)), scale, true);
};

util._shift = function(num, scale, reverseShift) {
  if (scale == undefined) scale = 0;
  if (reverseShift) scale = -scale;
  var a = ('' + num).split('e');
  return +(a[0] + 'e' + (a[1] ? (+a[1] + scale) : scale));
};

// 123   , 1 -> '123.0'
// 123.4 , 1 -> '123.4'
// 123.45, 1 -> '123.5'
// type: 0=floor / 1=round / 2=ceil
// zero: true=0 / false=0.0
util.alignDecimal = function(v, scale, type, zero) {
  var F = [util.floor, util.round, util.ceil];
  var f = F[type | 0];
  if (!f) f = F[0];
  v = f(v, scale);
  if (zero && v == 0) return 0;
  return util.alignDecimalZero(v, scale);
};

// 123  , 1 -> '123.0'
// 123  , 2 -> '123.00'
// 123.4, 1 -> '123.4'
// 123.4, 2 -> '123.40'
util.alignDecimalZero = function(v, scale) {
  var r = v + '';
  if (scale == undefined) scale = 1;
  if (scale <= 0) return r;
  var w = r.split('.');
  var i = w[0];
  var d = (w[1] == undefined ? '' : w[1]);
  d = util.rpad(d, '0', scale);
  return (i + '.' + d);
};

util.parseInt = function(v, d) {
  if (d == undefined) d = 0;
  var r = parseInt(v);
  if (isNaN(r)) r = d;
  return r;
};

util.parseFloat = function(v, d) {
  if (d == undefined) d = 0;
  var r = parseFloat(v);
  if (isNaN(r)) r = d;
  return r;
};

/**
 * '1'
 * '1.0'
 * -> true
 * '1.2'
 * 'a'
 * -> false
 */
util.isInteger = function(v, strict) {
  if (strict && (typeof v != 'number')) return false;
  v = ('' + v).trim();
  if (!v) return false;
  var a = v.split('.');
  if (!a[0].match(/^[+-]?\d+$/)) return false;
  if (!a[1]) return true;
  return (a[1].match(/^0+$/) ? true : false);
};

/**
 * '1.2' -> true
 * '1', 'a' -> false
 */
util.isFloat = function(s) {
  if (!s) return false;
  return ((s += '').trim().match(/^[+-]?\d+\.\d+$/) ? true : false);
};

/**
 * '1', '1.2' -> true
 * 'a' -> false
 */
util.isNumeric = function(s) {
  return (util.isInteger(s) || util.isFloat(s));
};

/**
 * 360 -> 0
 * 361 -> 1
 * -1  -> 359
 */
util.roundAngle = function(v) {
  if (v < 0) v = 360 + (v % 360);
  if (v >= 360) v = v % 360;
  return v;
};

util.isNumber = function(ch) {
  var c = ch.charCodeAt(0);
  return ((c >= 0x30) && (c <= 0x39));
};
util.isAlphabet = function(ch) {
  var c = ch.charCodeAt(0);
  return (((c >= 0x41) && (c <= 0x5A)) || ((c >= 0x61) && (c <= 0x7A)));
};
util.isUpperCase = function(ch) {
  var c = ch.charCodeAt(0);
  return ((c >= 0x41) && (c <= 0x5A));
};
util.isLowerCase = function(ch) {
  var c = ch.charCodeAt(0);
  return ((c >= 0x61) && (c <= 0x7A));
};

//---------------------------------------------------------
/**
 * 0-2147483647
 */
util.random = function(min, max) {
  min = parseInt(min);
  max = parseInt(max);
  if (isNaN(min)) {
    min = 0;
    max = 0x7fffffff;
  } else if (isNaN(max)) {
    max = min;
    min = 0;
  }
  return parseInt(Math.random() * (max - min + 1)) + min;
};

/**
 * randomString(len)
 * randomString(tbl)
 * randomString(tbl, len)
 * randomString(tbl, minLen, maxLen)
 */
util.randomString = function(a1, a2, a3) {
  var DFLT_LEN = 8;
  var min = -1;
  var max = -1;
  var tbl;
  if ((typeof a1 == 'string') || (a1 instanceof Array)) {
    tbl = a1;
  } else if (typeof a1 == 'number') {
    min = a1;
  }
  if (typeof a2 == 'number') {
    if (min == -1) {
      min = a2;
    } else {
      max = a2;
    }
  }
  if (typeof a3 == 'number') max = a3;
  if (!tbl) tbl = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  if (typeof tbl == 'string') tbl = tbl.split('');
  if (min == -1) min = DFLT_LEN;
  if (max == -1) max = min;
  var s = '';
  var len = util.random(min, max);
  if (tbl.length > 0) {
    for (var i = 0; i < len; i++) {
      s += tbl[Math.floor(Math.random() * tbl.length)];
    }
  }
  return s;
};

//---------------------------------------------------------
util.fromJSON = function(j, r) {
  if (!j) return j;
  return JSON.parse(j, r);
};

util.toJSON = function(o, r, s) {
  return JSON.stringify(o, r, s);
};

util.copyObject = function(src, dst) {
  if (src instanceof Array) {
    if (!dst) dst = [];
    for (var i = 0; i < src.length; i++) {
      var v = src[i];
      if (v instanceof Object) {
        dst.push(util.copyObject(v));
      } else {
        dst.push(v);
      }
    }
  } else {
    if (!dst) dst = {};
    for (var k in src) {
      if (src[k] instanceof Object) {
        dst[k] = {};
        util.copyObject(src[k], dst[k]);
      } else {
        dst[k] = src[k];
      }
    }
  }
  return dst;
};

util.copyDefaultProps = function(dflt, tgt) {
  if (!tgt) tgt = {};
  for (var k in dflt) {
    if (!(k in tgt)) {
      tgt[k] = dflt[k];
    } else if (tgt[k] instanceof Object) {
      util.copyDefaultProps(dflt[k], tgt[k]);
    }
  }
  return tgt;
};

util.loadObject = function(key) {
  if (util.LS_AVAILABLE) {
    return JSON.parse(localStorage.getItem(key));
  }
  return null;
};

util.saveObject = function(key, obj) {
  if (util.LS_AVAILABLE) localStorage.setItem(key, JSON.stringify(obj));
};

util.clearObject = function(key) {
  if (util.LS_AVAILABLE) localStorage.removeItem(key);
};

util.objtype = function(o) {
  return Object.prototype.toString.call(o);
};

/**
 * s='ABCDEF'
 * n=2: ['AB', 'CD', 'EF']
 * n=3: ['ABC', 'DEF']
 */
util.divideString = function(s, n) {
  if ((s == undefined) || (s == null)) return s;
  if ((n <= 0) || (s == '')) return [s];
  var a = [];
  for (var i = 0; i < s.length / n; i++) {
    a.push(s.substr(i * n, n));
  }
  return a;
};

util.str2chars = function(s) {
  return s.match(/[\uD800-\uDBFF][\uDC00-\uDFFF]|[\s\S]/g) || [];
};

/**
 * flg: 0 = ALL
 *      1 = Omit empty lines
 *      2 = Omit comments (starts with "#")
 */
util.text2list = function(s, flg) {
  s = util.convertNewLine(s, '\n');
  var a = s.split('\n');
  var lastIdx = a.length - 1;
  if (a[lastIdx] == '') a.splice(lastIdx, 1);
  if (flg) {
    var w = [];
    for (var i = 0; i < a.length; i++) {
      var v = a[i];
      var t = v.trim();
      if ((t == '') || ((flg == 2) && (t.match(/^#/)))) continue;
      w.push(v);
    }
    a = w;
  }
  return a;
};

/**
 * List to Text w/ newline
 */
util.list2text = function(l, nl) {
  if (nl == undefined) nl = '\n';
  var s = '';
  for (var i = 0; i < l.length; i++) {
    s += l[i] + nl;
  }
  return s;
};

util.isAscii = function(s) {
  if (typeof s != 'string') return false;
  return /^[\x20-\x7F\t\r\n]*$/.test(s);
};

/**
 * startsWith(string, pattern, position)
 * startsWith(string, pattern, case-insensitive)
 * startsWith(string, pattern, position, case-insensitive)
 */
util.startsWith = function(s, p, a3, a4) {
  if (s == null) return false;
  var a = [a3, a4];
  var o = 0;
  var i = 0;
  if (typeof a3 == 'number') {
    o = a3;
    i++;
  }
  var ci = a[i];
  if (o) s = s.substr(o);
  if ((s == '') && (p == '')) return true;
  if (p == '') return false;
  var f = (ci ? 'i' : '');
  var re = new RegExp('^' + p, f);
  return s.match(re) != null;
};

/**
 * endsWith(string, pattern, length)
 * endsWith(string, pattern, case-insensitive)
 * endsWith(string, pattern, length, case-insensitive)
 */
util.endsWith = function(s, p, a3, a4) {
  if (s == null) return false;
  var a = [a3, a4];
  var l = 0;
  var i = 0;
  if (typeof a3 == 'number') {
    l = a3;
    i++;
  }
  var ci = a[i];
  if (l) s = s.substr(0, l);
  if ((s == '') && (p == '')) return true;
  if (p == '') return false;
  var f = (ci ? 'i' : '');
  var re = new RegExp(p + '$', f);
  return s.match(re) != null;
};

util.repeatCh = function(c, n) {
  var s = '';
  for (var i = 0; i < n; i++) s += c;
  return s;
};

util.insertCh = function(s, ch, n) {
  if (n == 0) return s;
  var w = '';
  var p = 0;
  while (p < s.length) {
    var a = s.substr(p, n);
    w += a;
    p += n;
    if (p < s.length) w += ch;
  }
  return w;
};

/**
 * lpad(str, '0', 5)
 * 'ABC'   -> '00ABC'
 * 'ABCEF' -> 'ABCEF'
 * adj:
 * 'ABCEFG' -> false='ABCEFG' / true='ABCEF'
 */
util.lpad = function(str, pad, len, adj) {
  var r = str + '';
  var d = len - r.length;
  if (d <= 0) return r;
  var pd = util.repeatCh(pad, d);
  r = pd + r;
  if (adj) r = r.substr(0, len);
  return r;
};
/**
 * rpad('str, '0', 5)
 * 'ABC'   -> 'ABC00'
 * 'ABCEF' -> 'ABCEF'
 * adj:
 * 'ABCEFG' -> false='ABCEFG' / true='ABCEF'
 */
util.rpad = function(str, pad, len, adj) {
  var r = str + '';
  var d = len - r.length;
  if (d <= 0) return r;
  var pd = util.repeatCh(pad, d);
  r += pd;
  if (adj) r = r.substr(0, len);
  return r;
};

/**
 * ABCDEFGHIJKLMNOPQRSTUVWXYZ
 * -> ABCDEFG..TUVWXYZ
 */
util.snip = function(s, n1, n2, c) {
  if (n1 == undefined) n1 = 7;
  if (n2 == undefined) n2 = 7;
  if (c == undefined) c = '..';
  if (s.length >= (n1 + n2 + c.length)) s = s.substr(0, n1) + c + s.substr(s.length - n2, n2);
  return s;
};

/**
 * abc -> Abc
 */
util.capitalize = function(s) {
  return ((s && (typeof s == 'string')) ? s.charAt(0).toUpperCase() + s.slice(1).toLowerCase() : s);
};

util.null2empty = function(s) {
  if ((s == null) || (s == undefined)) s = '';
  return s;
};

util.countStr = function(s, p) {
  var i = 0;
  if (util.objtype(p) == '[object RegExp]') {
    var m = s.match(p);
    if (m) i = m.length;
  } else {
    var pos = s.indexOf(p);
    while ((p != '') && (pos != -1)) {
      i++;
      pos = s.indexOf(p, pos + p.length);
    }
  }
  return i;
};

util.lenB = function(s) {
  return (new Blob([s], {type: 'text/plain'})).size;
};

util.split = function(v, s, l) {
  l |= 0;
  var r = [];
  var p = 0;
  var c = 0;
  while (true) {
    var i = v.indexOf(s, p);
    if ((l > 0) && (c >= l - 1) || (i == -1)) {
      r.push(v.substr(p));
      break;
    } else {
      r.push(v.substring(p, i));
      p = i + s.length;
      c++;
    }
  }
  return r;
};

util.convertNewLine = function(s, nl) {
  return s.replace(/\r\n|\r/g, '\n').replace(/\n/g, nl);
};

util.toHalfWidth = function(s) {
  var h = s.replace(/\u3000/g, ' ').replace(/\u201D/g, '"').replace(/\u2019/g, '\'').replace(/\u2018/g, '`').replace(/\uFFE5/g, '\\');
  h = h.replace(/[\uFF01-\uFF5E]/g, util.shift2half);
  return h;
};
util.shift2half = function(w) {
  return String.fromCharCode(w.charCodeAt(0) - 65248);
};

util.toFullWidth = function(s) {
  var f = s.replace(/ /g, '\u3000').replace(/"/g, '\u201D').replace(/'/g, '\u2019').replace(/`/g, '\u2018').replace(/\\/g, '\uFFE5');
  f = f.replace(/[!-~]/g, util.shift2full);
  return f;
};
util.shift2full = function(w) {
  return String.fromCharCode(w.charCodeAt(0) + 65248);
};

util.toSingleSP = function(s) {
  if (!s) return s;
  return s.replace(/ {2,}/g, ' ');
};

util.alignFields = function(s, dlmt, n) {
  var a = util.text2list(s);
  if (!n) n = 1;
  var d = ' ';
  var c = [];
  for (var i = 0; i < a.length; i++) {
    var l = a[i].split(dlmt);
    for (var j = 0; j < l.length; j++) {
      var b = util.lenW(l[j]);
      if ((c[j] | 0) < b) c[j] = b;
    }
  }
  var r = '';
  for (i = 0; i < a.length; i++) {
    l = a[i].split(dlmt);
    for (j = 0; j < l.length - 1; j++) {
      r += util.rpad(l[j], d, c[j] + n);
    }
    r += l[j] + '\n';
  }
  return r;
};
util.lenW = function(s) {
  var n = 0;
  for (var i = 0; i < s.length; i++) {
    var p = String.prototype.codePointAt ? s.codePointAt(i) : s.charCodeAt(i);
    n += ((p <= 0x7F) || ((p >= 0xFF61) && (p <= 0xFF9F))) ? 1 : 2;
    if (p >= 0x10000) i++;
  }
  return n;
};

util.getUnicodePoints = function(str) {
  var cd = '';
  var chs = util.str2chars(str);
  for (var i = 0; i < chs.length; i++) {
    var p = util.getCodePoint(chs[i], true);
    if (i > 0) cd += ' ';
    cd += 'U+' + util.formatHex(p, true, 4);
  }
  return cd;
};

util.getCodePoint = function(c, hex) {
  var p;
  if (String.prototype.codePointAt) {
    p = c.codePointAt(0);
  } else {
    p = c.charCodeAt(0);
  }
  if (hex) p = util.toHex(p, true, 0, '');
  return p;
};

util.countLineBreak = function(s) {
  return (s.match(/\n/g) || []).length;
};
util.clipTextLine = function(s, p) {
  var n = s.indexOf('\n', p);
  if (n > 0) s = s.substr(0, n);
  return s.replace(/.*\n/g, '');
};

util.toBin = function(v, uc, d, pFix) {
  var bin = parseInt(v).toString(2);
  return util.formatBin(bin, uc, d, pFix);
};
util.formatBin = function(bin, d, pFix) {
  if ((d) && (bin.length < d)) {
    bin = (util.repeatCh('0', d) + bin).slice(d * (-1));
  }
  if (pFix) bin = pFix + bin;
  return bin;
};

util.toHex = function(v, uc, d, pFix) {
  var hex = parseInt(v).toString(16);
  return util.formatHex(hex, uc, d, pFix);
};
util.formatHex = function(hex, uc, d, pFix) {
  if (uc) hex = hex.toUpperCase();
  if ((d) && (hex.length < d)) {
    hex = (util.repeatCh('0', d) + hex).slice(d * (-1));
  }
  if (pFix) hex = pFix + hex;
  return hex;
};

/**
 * -1234.98765 -> '-1,234.98765'
 */
util.formatNumber = function(v) {
  v += '';
  if (!v.match(/\d+/)) return v;
  var d = v.replace(/.*?(\d+).*/, '$1');
  var f = util.separateDigits(d);
  var re = new RegExp(d);
  return v.replace(re, f);
};
util.separateDigits = function(v) {
  var len = v.length;
  var r = '';
  for (var i = 0; i < len; i++) {
    if ((i != 0) && ((len - i) % 3 == 0)) r += ',';
    r += v.charAt(i);
  }
  return r;
};

/**
 * '-0102.3040' -> '-102.304'
 */
util.trimZeros = function(v) {
  v = (v + '').trim();
  var p = v.split('.');
  var i = p[0];
  var d = ((p.length > 1) ? p[1] : '');
  i = util.trimLeadingZeros(i);
  d = util.trimTrailingZeros(d);
  var r = i;
  if (d != '') r += '.' + d;
  return r;
};
util.trimLeadingZeros = function(s) {
  if (!s) return s;
  return (s + '').replace(/^([^0]*)0+(.+)$/, '$1$2');
};
util.trimTrailingZeros = function(s) {
  if (!s) return s;
  return (s + '').replace(/(.+?)0*$/, '$1');
};

/**
 * 1 -> '1'
 * 1024 -> '1K'
 * 1048576 -> '1M'
 * scale: 1=1.2K / 2=1.25K
 * sep=true: '1,023K'
 * sp=true: '1 K'
 */
util.convByte = function(v, scale, sep, sp) {
  var U = ['', 'K', 'M', 'G', 'T', 'P'];
  var b = v;
  var u = '';
  for (var i = 5; i >= 1; i--) {
    var c = Math.pow(1024, i);
    var w = v / c;
    if ((v >= c) || (w > 0.9756)) {
      b = w;
      u = U[i];
      break;
    }
  }
  if (scale == undefined) scale = 1;
  var r = util.floor(b, scale);
  if (sep) r = util.formatNumber(r);
  w = (r + '').split('.');
  if ((scale > 0) && (r != 0)) {
    var z = util.repeatCh('0', scale);
    if (w.length > 1) {
      r = w[0] + '.' + ((w[1] += z).substr(0, scale));
    } else {
      r += '.' + z;
    }
  }
  if (sp && u) r += ' ';
  return r + u;
};

util.plural = function(s, n, f) {
  if (n == 1) return s;
  if (f) return s + 's';
  if (s.match(/s$/i) || s.match(/ch$/i) || s.match(/sh$/i) || s.match(/x$/i) || s.match(/o$/i)) return s + 'es';
  if (s.match(/y$/)) return s.replace(/y$/, 'ies');
  if (s.match(/Y$/)) return s.replace(/Y$/, 'IES');
  if (s.match(/f$/)) return s.replace(/f$/, 'ves');
  if (s.match(/F$/)) return s.replace(/F$/, 'VES');
  if (s.match(/fe$/)) return s.replace(/fe$/, 'ves');
  if (s.match(/FE$/)) return s.replace(/FE$/, 'VES');
  return s + 's';
};

util.haveBeen = function(subject, pred, n) {
  if (n == null) return 'The ' + subject + ' has been ' + pred + '.';
  var s = util.plural(subject, n) + ' ' + (n == 1 ? 'has' : 'have') + ' been ' + pred + '.';
  return (n == 0 ? 'No' : n) + ' ' + s;
};

util.copy = function(s) {
  var b = document.body;
  var ta = document.createElement('textarea');
  ta.style.position = 'fixed';
  ta.style.left = '-9999';
  ta.value = s;
  b.appendChild(ta);
  ta.select();
  var r = document.execCommand('copy');
  b.removeChild(ta);
  return r;
};

util.A2Z = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

/**
 *   1  -> 'A'
 *   2  -> 'B'
 *  26  -> 'Z'
 * 'A'  ->  1
 * 'B'  ->  2
 * 'Z'  -> 26
 */
util.xlscol = function(c, o) {
  if (o != undefined) return util.xlscolShift(c, o);
  var f = (isNaN(c) ? util.xlscolA2N : util.xlscolN2A);
  return f(c);
};
util.xlscolA2N = function(c) {
  return util.strpIndex(util.A2Z, c.trim().toUpperCase());
};
util.xlscolN2A = function(n) {
  var a = util.strp(util.A2Z, n);
  if (n <= 0) a = '';
  return a;
};
util.xlscolShift = function(c, o) {
  var n = util.xlscolA2N(c);
  if (n == 0) return '';
  return util.xlscolN2A(n + o);
};

/**
 * String permutation.
 * strp('ABC', 1) -> 'A'
 * strp('ABC', 2) -> 'B'
 * strp('ABC', 4) -> 'AA'
 */
util.strp = function(tbl, idx) {
  if (typeof tbl == 'string') tbl = tbl.split('');
  var len = tbl.length;
  var a = [-1];
  for (var i = 0; i < idx; i++) {
    var j = 0;
    var cb = 1;
    while (j < a.length) {
      if (cb) {
        a[j]++;
        if (a[j] > len - 1) {
          a[j] = 0;
          if (a.length <= j + 1) a[j + 1] = -1;
        } else {
          cb = 0;
        }
      }
      j++;
    }
  }
  var s = '';
  for (i = a.length - 1; i >= 0; i--) {
    s += tbl[a[i]];
  }
  return s;
};

/**
 * The index of the string permutation.
 * strpIndex('ABC', 'A')  -> 1
 * strpIndex('ABC', 'B')  -> 2
 * strpIndex('ABC', 'AA') -> 4
 */
util.strpIndex = function(tbl, ptn) {
  if (typeof tbl == 'string') tbl = tbl.split('');
  var len = ptn.length;
  var rdx = tbl.length;
  var idx = 0;
  for (var i = 0; i < len; i++) {
    var d = len - i - 1;
    var c = ptn.substr(d, 1);
    var v = tbl.indexOf(c);
    if (v == -1) return 0;
    v++;
    var n = v * Math.pow(rdx, i);
    idx += n;
  }
  return idx;
};

/**
 * Total count of the permutation pattern.
 * strpTotal('ABC', 1) -> 3
 * strpTotal('ABC', 2) -> 12
 */
util.strpTotal = function(tbl, d) {
  if (typeof tbl == 'string') tbl = tbl.split('');
  var c = tbl.length;
  var n = 0;
  for (var i = 1; i <= d; i++) {
    n += Math.pow(c, i);
  }
  return n;
};

//---------------------------------------------------------
// Array
//---------------------------------------------------------
util.arr = {};
/**
 * ['A', 'B', 'C', '1', 1], 'A' -> 0
 * ['A', 'B', 'C', '1', 1], 1, false -> 3
 * ['A', 'B', 'C', '1', 1], 1, true -> 4
 */
util.arr.pos = function(a, v, f) {
  var r = -1;
  for (var i = 0; i < a.length; i++) {
    if ((!f && (a[i] == v)) || (f && (a[i] === v))) {
      r = i;
      break;
    }
  }
  return r;
};

/**
 * ['A', 'B', 'A'], 'A' -> 2
 * ['A', 'B', 'A'], 'Z' -> 0
 */
util.arr.count = function(a, v, f) {
  var c = 0;
  for (var i = 0; i < a.length; i++) {
    if ((!f && (a[i] == v)) || (f && (a[i] === v))) c++;
  }
  return c;
};

/**
 * ['A', 'B', 'C', 'C', 'A', 'A']
 * -> {'A': 3, 'B': 2, 'C': 1}
 */
util.arr.countByValue = function(arr) {
  var o = {};
  for (var i = 0; i < arr.length; i++) {
    var v = arr[i];
    if (o[v] == undefined) o[v] = 0;
    o[v]++;
  }
  return o;
};

/**
 * ['A', 'B', 'C'], 'B' -> ['A', 'C']
 */
util.arr.del = function(arr, v) {
  for (var i = 0; i < arr.length; i++) {
    if (arr[i] == v) arr.splice(i--, 1);
  }
};

/**
 * ['A', 'B', 'C'], 'A' -> true
 *
 * ['A', 'B', 'C'], 'Z' -> false
 */
util.arr.hasValue = function(a, v, f) {
  return (util.arr.pos(a, v, f) >= 0);
};

/**
 * ['A', 'B', 'C'], 'A' -> 'B'
 */
util.arr.next = function(a, v) {
  var r = a[0];
  for (var i = 0; i < a.length; i++) {
    if ((a[i] == v) && (i < (a.length - 1))) {
      r = a[i + 1];break;
    }
  }
  return r;
};

/**
 * ['A', 'B', 'C'], 'A' -> 'C'
 */
util.arr.prev = function(a, v) {
  var r = a[a.length - 1];
  for (var i = 0; i < a.length; i++) {
    if ((a[i] == v) && (i > 0)) {
      r = a[i - 1];break;
    }
  }
  return r;
};

/**
 * ['A', 'B', 'C', 'B', 'A', 'A'] -> ['A', 'B', 'C']
 */
util.list2set = function(arr, srt) {
  var o = util.arr.countByValue(arr);
  var v = [];
  for (var k in o) {
    v.push({key: k, cnt: o[k]});
  }
  if (srt == 'asc|count') {
    v = util.sortObjectList(v, 'cnt');
  } else if (srt == 'desc|count') {
    v = util.sortObjectList(v, 'cnt', true);
  }
  var r = [];
  for (var i = 0; i < v.length; i++) {
    r.push(v[i].key);
  }
  if (srt == 'asc|val') {
    r.sort();
  } else if (srt == 'desc|val') {
    r.sort().reverse();
  }
  return r;
};

//---------------------------------------------------------
util.addListItem = function(list, item) {
  if (list && !util.arr.hasValue(list, item)) list.push(item);
};

util.removeListItem = function(list, item) {
  if (list) util.arr.del(list, item);
};

/**
 * [{id: 'A', cnt: 2}, {id: 'C', cnt: 1}, {id: 'B', cnt: 3}]
 * 'id' -> [{id: 'A', cnt: 2}, {id: 'B', cnt: 3}, {id: 'C', cnt: 1}]
 */
util.sortObjectList = function(list, key, desc, asNum) {
  list.sort(function(a, b) {return util._cmpFn(a, b, key, desc, asNum);});
  return list;
};
util._cmpFn = function(a, b, key, desc, asNum) {
  var k = key.split('.');
  for (var i = 0; i < k.length; i++) {
    a = a[k[i]];
    b = b[k[i]];
  }
  return util._cmp(a, b, desc, asNum);
};
util._cmp = function(a, b, desc, asNum) {
  if (a == undefined) a = '';
  if (b == undefined) b = '';
  if (a === true) a = 1;
  if (b === true) b = 1;
  if (a === false) a = 0;
  if (b === false) b = 0;
  if (asNum) {
    if (!isNaN(a) && (a !== '')) a = parseFloat(a);
    if (!isNaN(b) && (b !== '')) b = parseFloat(b);
  }
  if (a == b) return 0;
  return (desc ? (a < b ? 1 : -1) : (a > b ? 1 : -1));
};

/**
 * {a: 50, b: 100, c: 20} -> {c: 20, a: 50, b: 100}
 */
util.sortObjectKeyByValue = function(o, desc, asNum) {
  var lst = [];
  for (var k in o) {
    lst.push({k: k, v: o[k]});
  }
  lst.sort(function(a, b) {return util._cmp(a.v, b.v, desc, asNum);});
  var r = {};
  for (var i = 0; i < lst.length; i++) {
    var w = lst[i];
    r[w.k] = w.v;
  }
  return r;
};

/**
 * ['A', 'B', 'C', 'B', 'A', 'A']
 * -> {'A': 3, 'B': 2, 'C': 1}
 *
 * [{k1: 'A', k2: {k3: 'X'}}, {k1: 'B', k2: {k3: 'Z'}}, {k1: 'C', k2: {k3: 'Y'}}, {k1: 'C', k2: {k3: 'Z'}}, {k1: 'A', k2: {k3: 'Z'}}, {k1: 'A', k2: {k3: 'X'}}]
 * 'k1' -> {'A': 3, 'B': 1, 'C': 2}
 * 'k2.k3' -> {'X': 2, 'Z': 3, 'Y': 1}
 */
util.countByValue = function(a, k) {
  return (k ? util.countObjectByValue(a, k) : util.arr.countByValue(a));
};
util.countObjectByValue = function(a, k) {
  var r = {};
  var w = k.split('.');
  for (var i = 0; i < a.length; i++) {
    var v = a[i];
    for (var j = 0; j < w.length; j++) {
      if (!v) break;
      v = v[w[j]];
    }
    if (v != null) {
      if (r[v] == undefined) r[v] = 0;
      r[v]++;
    }
  }
  return r;
};

//---------------------------------------------------------
// HTTP
//---------------------------------------------------------
/**
 *  var param = {
 *    key1: val1,
 *    key2: val2
 *  };
 *
 *  var req = {
 *    url: 'xxx',
 *    method: 'POST',
 *    data: param,
 *    responseType: 'json',
 *    timeout: 0,
 *    cb: callback,
 *    onsuccess: callback,
 *    onerror: callback
 *  };
 *
 *  util.http(req);
 *
 *  callback = function(xhr, res, req) {
 *    if (xhr.status != 200) {
 *      return;
 *    }
 *  };
 */
util.http = function(req) {
  var trc = util.http.trace;
  var trcid = util.randomString(util.http.TRC_ID_CHARS, util.http.TRC_ID_LEN);
  req.trcid = trcid;
  if (util.http.conn == 0) {
    util.http.onStart();
  }
  if (!util.http.onSend(req)) {
    if (util.http.conn == 0) {
      util.http.onStop();
    }
    return;
  }
  util.http.conn++;
  if (!req.method) req.method = 'GET';
  req.method = req.method.toUpperCase();
  var data = (((req.data != undefined) && (req.data != '')) ? req.data : null);
  if (trc) {
    if (!data) data = {};
    if (typeof data == 'string') {
      data += '&_trcid=' + trcid;
    } else {
      data._trcid = trcid;
    }
  }
  if (data instanceof Object) data = util.http.buildQueryString(data);
  var url = req.url;
  if (data && (req.method == 'GET')) {
    url += (url.match(/\?/) ? '&' : '?') + data;
    data = null;
  }
  if (req.async == undefined) req.async = true;
  var xhr = new XMLHttpRequest();
  xhr.onreadystatechange = function() {
    if (xhr.readyState == XMLHttpRequest.DONE) {
      util.http.onDone(xhr, req);
    }
  };
  xhr.open(req.method, url, req.async, req.user, req.pass);
  if (req.timeout != undefined) xhr.timeout = req.timeout;
  var contentType = 'application/x-www-form-urlencoded';
  if (req.contentType) {
    contentType = req.contentType;
  }
  xhr.setRequestHeader('Content-Type', contentType);
  if (!req.cache) {
    xhr.setRequestHeader('If-Modified-Since', 'Thu, 01 Jun 1970 00:00:00 GMT');
  }
  for (var k in req.headers) {
    xhr.setRequestHeader(k, req.headers[k]);
  }
  if (req.withCredentials) {
    xhr.withCredentials = true;
  }
  if (util.http.logging) {
    var m = '[' + trcid + '] => ' + req.method + ' ' + util.escHtml(url);
    if (data) m += ' : ' + data.substr(0, util.http.MAX_LOG_LEN);
    util._log.v(m);
  }
  if (util.debug) $dbg[trcid] = {req: req};
  if (util.http.online) xhr.send(data);
  util.http.onSent(req);
  if (!util.http.online) {
    var o = {xhr: xhr, req: req};
    setTimeout(util.http.pseudoDone, 0, o);
  }
};
util.http.onDone = function(xhr, req) {
  var res = xhr.responseText;
  var st = xhr.status;
  if (util.debug) $dbg[req.trcid].res = res;
  if (util.http.logging) {
    var m = res;
    if (st == 0) {
      m = 'ERROR =>X';
    } else {
      if (m) {
        if (m.length > util.http.LOG_LIMIT) {
          m = '[size=' + m.length + ']';
        } else if (m.length > util.http.MAX_LOG_LEN) {
          m = m.substr(0, util.http.MAX_LOG_LEN) + '... (size=' + m.length + ')';
        }
      }
      m = '<= [' + st + '] ' + util.escHtml(m);
    }
    util._log.v('[' + req.trcid + '] ' + m);
  }
  if (util.http.onReceive(xhr, res, req)) {
    if (st == 200) {
      if (util.http.isJSONable(xhr, req)) {
        try {
          res = util.fromJSON(res);
        } catch (e) {
          if (util.http.logging) {
            util._log.e('[' + req.trcid + '] JSON PARSE ERROR');
          }
          res = null;
        }
        if (util.debug) $dbg[req.trcid].res = res;
      }
    }
    if (req.cb) req.cb(xhr, res, req);
    if (((st >= 200) && (st < 300)) || (st == 304)) {
      if (req.onsuccess) req.onsuccess(xhr, res, req);
    } else {
      util.http.onError(xhr, res, req);
      if (req.onerror) req.onerror(xhr, res, req);
    }
  }
  util.http.conn--;
  if (util.http.conn == 0) {
    util.http.onStop();
  }
};
util.http.pseudoDone = function(o) {
  o.xhr.status = 0;
  util.http.onDone(o.xhr, o.req);
};
util.http.buildQueryString = function(p) {
  var s = '';
  var cnt = 0;
  for (var k in p) {
    if (cnt > 0) s += '&';
    s += k + '=' + encodeURIComponent(p[k]);
    cnt++;
  }
  return s;
};
util.http.isJSONable = function(xhr, req) {
  var ct = xhr.getResponseHeader('Content-Type');
  if (ct) ct = ct.split(';')[0];
  if ((req.responseType == 'json') || ((!req.responseType) && (ct == 'application/json'))) {
    return true;
  }
  return false;
};

/**
 * addListener('start|send|sent|receive|stop|error', fn);
 *  callback args:
 *   start: ()
 *   send: (req)
 *   sent: (req)
 *   receive: (xhr, res, req)
 *   stop: ()
 *   error: (xhr, res, req)
 */
util.http.addListener = function(type, fn) {
  util.addListItem(util.http.listeners[type], fn);
};
util.http.onStart = function() {
  util.http.t0 = Date.now();
  util.http.sartInd();
  util.http.callListeners('start');
};
util.http.onSend = function(req) {
  return util.http.callListeners('send', req);
};
util.http.onSent = function(req) {
  return util.http.callListeners('sent', req);
};
util.http.onReceive = function(xhr, res, req) {
  return util.http.callListeners('receive', xhr, res, req);
};
util.http.onStop = function() {
  util.http.callListeners('stop');
  util.http.stopInd();
  util.http.t0 = 0;
};
util.http.onError = function(xhr, res, req) {
  util.http.callListeners('error', xhr, res, req);
};
util.http.callListeners = function(type, a1, a2, a3) {
  var listeners = util.http.listeners[type];
  for (var i = 0; i < listeners.length; i++) {
    var f = listeners[i];
    if (f(a1, a2, a3) === false) return false;
  }
  return true;
};
util.http.sartInd = function() {
  if (window.dbg) window.dbg.led.on(0);
};
util.http.stopInd = function() {
  var t = 100;
  var t1 = Date.now();
  var d = t1 - util.http.t0;
  if (d < t) {
    setTimeout(util.http._stopInd, (t - d));
  } else {
    util.http._stopInd();
  }
};
util.http._stopInd = function() {
  if (window.dbg && (util.http.conn == 0)) window.dbg.led.off(0);
};
util.http.countConnections = function() {
  return util.http.conn;
};
util.http.TRC_ID_CHARS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
util.http.TRC_ID_LEN = 4;
util.http.LOG_LIMIT = 3145728;
util.http.MAX_LOG_LEN = 4096;
util.http.online = true;
util.http.logging = window.dbg ? true : false;
util.http.trace = false;
util.http.conn = 0;
util.http.t0 = 0;
util.http.listeners = {
  start: [],
  send: [],
  sent: [],
  receive: [],
  stop: [],
  error: []
};

//---------------------------------------------------------
// Element
//---------------------------------------------------------
var $el = function(tgt, idx) {
  var el = util.getElement(tgt, idx);
  if (el) {
    for (var k in $el.fn) {
      if (el[k] == undefined) el[k] = $el.fn[k];
    }
    el.notFound = false;
  } else {
    el = {style: {}};
    for (k in $el.fn) {
      el[k] = util.nop;
    }
    el.exists = function() {return false;};
    el.notFound = true;
  }
  return el;
};
$el.fn = {
  html: function(html, speed) {
    if (html == undefined) return this.innerHTML;
    if (speed == undefined) speed = 0;
    util.callFn4El(util.writeHTML, this, html, speed);
  },
  text: function(text, speed) {
    if (text == undefined) return this.innerText;
    var html = util.escHtml(text);
    if (speed == undefined) speed = 0;
    util.callFn4El(util.writeHTML, this, html, speed);
  },
  clear: function(speed) { // Not available for <pre> on IE11
    util.callFn4El(util.elClear, this, speed);
  },
  disable: function() {
    util.callFn4El(util.toDisable, this);
  },
  enable: function() {
    util.callFn4El(util.toEnable, this);
  },
  textseq: function(text, opt) {
    return util.callFn4El(util.textseq, this, text, opt);
  },
  startTextSeq: function() {
    util.callFn4El(util.textseq.start, this);
  },
  stopTextSeq: function() {
    util.callFn4El(util.textseq.stop, this);
  },
  setStyle: function(n, v) {
    util.setStyle(this, n, v);
  },
  addClass: function(n) {
    util.addClass(this, n);
  },
  removeClass: function(n) {
    util.removeClass(this, n);
  },
  toggleClass: function(n) {
    util.toggleClass(this, n);
  },
  hasClass: function(n) {
    return util.hasClass(this, n);
  },
  hasFocus: function() {
    return util.hasFocus(this);
  },
  blink: function(a) {
    if (a === false) {
      util.removeClass(this, 'blink');
      util.removeClass(this, 'blink1');
      util.removeClass(this, 'blink2');
      util.removeClass(this, 'blink3');
    } else {
      if (a == undefined) a = '';
      util.addClass(this, 'blink' + a);
    }
  },
  hide: function() {
    util.callFn4El(util.elHide, this);
  },
  show: function() {
    util.callFn4El(util.elShow, this);
  },
  fadeIn: function(speed, cb, arg) {
    util.fadeIn(this, speed, cb, arg);
  },
  fadeOut: function(speed, cb, arg) {
    util.fadeOut(this, speed, cb, arg);
  },
  getRect: function() {
    return this.getBoundingClientRect();
  },
  loading: function(opt, force) {
    if (opt === false) {
      util.loader.hide(this, force);
    } else {
      util.loader.show(this, opt);
    }
  },
  center: function() {
    util.callFn4El(util.center, this);
  },
  position: function(x, y) {
    util.callFn4El(util.setPosition, this, x, y);
  },
  scrollX: function(x) {
    return util.callFn4El(util.scrollX, this, x);
  },
  scrollY: function(y) {
    return util.callFn4El(util.scrollY, this, y);
  },
  scrollToTop: function() {
    return util.callFn4El(util.scrollToTop, this);
  },
  scrollToRight: function() {
    return util.callFn4El(util.scrollToRight, this);
  },
  scrollToBottom: function() {
    return util.callFn4El(util.scrollToBottom, this);
  },
  scrollToLeft: function() {
    return util.callFn4El(util.scrollToLeft, this);
  },
  prev: function() {
    return util.prevElement(this);
  },
  next: function() {
    return util.nextElement(this);
  },
  exists: function() {
    return true;
  }
};

util.getElement = function(tgt, idx) {
  var el = tgt;
  if (typeof tgt == 'string') {
    el = document.querySelectorAll(tgt);
    if (tgt.charAt(0) == '#') idx = 0;
    if (idx != undefined) el = el.item(idx);
  }
  return el;
};

util.callFn4El = function(f, el, a1, a2) {
  var r;
  if (el.toString() == '[object NodeList]') {
    for (var i = 0; i < el.length; i++) {
      r = f(el[i], a1, a2);
    }
  } else {
    r = f(el, a1, a2);
  }
  return r;
};

util.toEnable = function(el) {
  el.disabled = false;
};
util.toDisable = function(el) {
  el.disabled = true;
};

util.elShow = function(el) {
  if (el.style.display != 'none') return;
  var v = el.displayBak;
  if (v == undefined) v = '';
  el.style.display = v;
};
util.elHide = function(el) {
  var v = el.style.display;
  if (v == 'none') return;
  el.displayBak = v;
  el.style.display = 'none';
};

util.elClear = function(el, speed) {
  if (util.isTextInput(el)) {
    el.value = '';
  } else {
    if (speed == undefined) speed = 0;
    util.clearHTML(el, speed);
  }
};

util.addClass = function(el, n) {
  el = util.getElement(el);
  util.callFn4El(util._addClass, el, n);
};
util._addClass = function(el, n) {
  el.classList.add(n);
};

util.removeClass = function(el, n) {
  el = util.getElement(el);
  util.callFn4El(util._removeClass, el, n);
};
util._removeClass = function(el, n) {
  el.classList.remove(n);
};

util.toggleClass = function(el, n) {
  el = util.getElement(el);
  util.callFn4El(util._toggleClass, el, n);
};
util._toggleClass = function(el, n) {
  el.classList.toggle(n);
};

util.hasClass = function(el, n) {
  el = util.getElement(el);
  return el.classList.contains(n);
};

util.hasFocus = function(el, idx) {
  if (el.toString() != '[object NodeList]') return util.getElement(el, idx) == document.activeElement;
  for (var i = 0; i < el.length; i++) {
    if (util.getElement(el[i]) == document.activeElement) return true;
  }
  return false;
};

util.hasParent = function(el, parent) {
  el = util.getElement(el);
  parent = util.getElement(parent);
  if (!el || !parent) return false;
  do {
    if (parent.toString() == '[object NodeList]') {
      for (var i = 0; i < parent.length; i++) {
        var p = parent[i];
        if (el == p) return true;
      }
    } else {
      if (el == parent) return true;
    }
    el = el.parentNode;
  } while (el);
  return false;
};

util.hasChild = function(el, child) {
  el = util.getElement(el);
  child = util.getElement(child);
  var children = el.childNodes;
  for (var i = 0; i < children.length; i++) {
    var c = children[i];
    if (c == child) {
      return true;
    } else if (c.childNodes.length > 0) {
      if (util.hasChild(c, child)) return true;
    }
  }
  return false;
};

util.prevElement = function(node) {
  var el = node.previousElementSibling;
  if (el) {
    if (el.childElementCount > 0) {
      var lastChild = el.lastElementChild;
      while (lastChild.childElementCount > 0) {
        lastChild = lastChild.lastElementChild;
      }
      el = lastChild;
    }
  } else {
    el = node.parentNode;
  }
  return el;
};

util.nextElement = function(node) {
  var el = node.firstElementChild;
  if (!el) {
    el = node.nextElementSibling;
    if (el == null) {
      var parent = node.parentNode;
      if (parent) {
        do {
          el = parent.nextElementSibling;
          if (el) break;
          parent = parent.parentNode;
        } while (parent);
      }
    }
  }
  return el;
};

util.center = function(el) {
  if (!el) return;
  var cliW = util.getClientWidth();
  var cliH = util.getClientHeight();
  var rect = el.getBoundingClientRect();
  var w = rect.width;
  var h = rect.height;
  var x = cliW / 2 - w / 2;
  var y = cliH / 2 - h / 2;
  if (x < 0) x = 0;
  if (y < 0) y = 0;
  util.setPosition(el, x, y);
};

util.setPosition = function(el, x, y) {
  var style = {left: x + 'px', top: y + 'px'};
  util.setStyle(el, style);
};

util.isTextInput = function(el) {
  if (el.tagName == 'TEXTAREA') return true;
  if (el.tagName == 'INPUT') {
    if ((el.type == 'text') || (el.type == 'password')) return true;
  }
  return false;
};

util.addSelectOption = function(select, label, val, selected) {
  select = util.getElement(select);
  var opt = document.createElement('option');
  opt.value = (val === undefined ? label : val);
  opt.innerText = label;
  if (selected) opt.selected = true;
  select.appendChild(opt);
  return opt;
};

util.getClientWidth = function() {
  return document.documentElement.clientWidth;
};

util.getClientHeight = function() {
  return document.documentElement.clientHeight;
};

util.getZoomRatio = function() {
  return Math.round(window.devicePixelRatio * 100);
};

util.scrollX = function(el, x) {
  el = util.getElement(el);
  if (!el) return;
  if (x == undefined) return el.scrollLeft;
  x += '';
  if ((x.charAt(0) == '+') || (x.charAt(0) == '-')) {
    x = el.scrollLeft + (x | 0);
  } else if (x == 'left') {
    x = 0;
  } else if (x == 'center') {
    el.scrollLeft = el.scrollWidth;
    x = el.scrollLeft / 2;
  } else if (x == 'right') {
    x = el.scrollWidth;
  }
  el.scrollLeft = x;
  return el.scrollLeft;
};
util.scrollY = function(el, y) {
  el = util.getElement(el);
  if (!el) return;
  if (y == undefined) return el.scrollTop;
  y += '';
  if ((y.charAt(0) == '+') || (y.charAt(0) == '-')) {
    y = el.scrollTop + (y | 0);
  } else if (y == 'top') {
    y = 0;
  } else if (y == 'middle') {
    el.scrollTop = el.scrollHeight;
    y = el.scrollTop / 2;
  } else if (y == 'bottom') {
    y = el.scrollHeight;
  }
  el.scrollTop = y;
  return el.scrollTop;
};
util.scrollToTop = function(el) {
  el.scrollTop = 0;
};
util.scrollToRight = function(el) {
  el.scrollLeft = el.scrollWidth;
};
util.scrollToBottom = function(el) {
  el.scrollTop = el.scrollHeight;
};
util.scrollToLeft = function(el) {
  el.scrollLeft = 0;
};

util.escHtml = function(s) {
  return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
};

util.html2text = function(s) {
  if (!s) return '';
  var p = document.createElement('pre');
  p.innerHTML = s;
  return p.innerText;
};

util.getCtxIdx4El = function(ctxs, el) {
  for (var i = 0; i < ctxs.length; i++) {
    var ctx = ctxs[i];
    if (ctx.el == el) return i;
  }
  return -1;
};
util.getCtx4El = function(ctxs, el) {
  var ctx = null;
  var i = util.getCtxIdx4El(ctxs, el);
  if (i >= 0) ctx = ctxs[i];
  return ctx;
};

//---------------------------------------------------------
util.overlay = {};
util.overlay.DFLT_FADE_SPEED = 250;
util.overlay.DFLT_SHOW_OPT = {pos: 'nw', adjX: 0, adjY: 0, fade: 0};
util.overlay.DFLT_HIDE_OPT = {fade: 0};
/**
 * tgt: must be style.position != 'static'
 * opt: {pos: 'nw|n|ne|e|se|s|sw|w', adjX: 0, adjY: 0, fade: 0}
 */
util.overlay.show = function(tgt, el, opt) {
  tgt = util.getElement(tgt);
  el = util.getElement(el);
  if (!tgt || !el || tgt.contains(el)) return;
  opt = util.copyDefaultProps(util.overlay.DFLT_SHOW_OPT, opt);
  el.style.opacity = 0;
  el.style.position = 'absolute';
  if (opt.fade > 0) util.addClass(el, 'fadeout');
  tgt.appendChild(el);
  setTimeout(util.overlay._show, 0, tgt, el, opt);
};
util.overlay._show = function(tgt, el, opt) {
  el.style.opacity = 1;
  var tgtSP = util.getElSizePos(tgt);
  var elSP = util.getElSizePos(el);
  var x = 0;
  var y = 0;
  switch (opt.pos) {
    case 'n':
      x = tgtSP.wC - elSP.wC;
      break;
    case 'ne':
      x = tgtSP.w - elSP.w;
      break;
    case 'e':
      y = tgtSP.hC - elSP.hC;
      x = tgtSP.w - elSP.w;
      break;
    case 'se':
      y = tgtSP.h - elSP.h;
      x = tgtSP.w - elSP.w;
      break;
    case 's':
      y = tgtSP.h - elSP.h;
      x = tgtSP.wC - elSP.wC;
      break;
    case 'sw':
      y = tgtSP.h - elSP.h;
      break;
    case 'w':
      y = tgtSP.hC - elSP.hC;
      break;
    case 'c':
      y = tgtSP.hC - elSP.hC;
      x = tgtSP.wC - elSP.wC;
  }
  el.style.top = (y + opt.adjY) + 'px';
  el.style.left = (x + opt.adjX) + 'px';
  if (opt.fade > 0) {
    setTimeout(util.overlay.__show, 0, el, opt.fade);
  } else {
    util.clearFade(el);
  }
};
util.overlay.__show = function(el, speed) {
 util.fadeIn(el, speed);
};
util.overlay.hide = function(tgt, el, opt) {
  tgt = util.getElement(tgt);
  el = util.getElement(el);
  if (!tgt || !el || !tgt.contains(el)) return;
  opt = util.copyDefaultProps(util.overlay.DFLT_HIDE_OPT, opt);
  var o = {el: el, tgt: tgt};
  if (opt.fade > 0) {
    util.fadeOut(el, opt.fade, util.overlay._hide, o);
  } else {
    util.overlay._hide(o);
  }
};
util.overlay._hide = function(o) {
  o.tgt.removeChild(o.el);
};
util.getElSizePos = function(el) {
  var r = el.getBoundingClientRect();
  var rT = Math.round(r.top);
  var rL = Math.round(r.left);
  var rR = Math.round(r.right);
  var rB = Math.round(r.bottom);
  var o = {x1: rL, y1: rT, x2: rR, y2: rB, w: ((rR - rL) + 1), h: ((rB - rT) + 1)};
  o.wC = o.w / 2;
  o.hC = o.h / 2;
  o.xC = o.x1 + o.wC;
  o.yC = o.y1 + o.hC;
  return o;
};

// f = function() {/*
// ABC
// 123
// */};
//
// -> fn2text(f) = 'ABC\n123'
// s, e: start/end offset
//
util.fn2text = function(f, s, e) {
  var b = f.toString().replace(/\r\n/g, '\n');
  var a = b.split('\n');
  var t = '';
  if ((s == undefined) || (s < 0)) s = 1;
  if ((e == undefined) || (e < 0)) e = 1;
  for (var i = s; i < a.length - e; i++) {
    t += a[i] + '\n';
  }
  return t;
};

/**
 * http://xxx/ -> <a href="http://xxx/">http://xxx/</a>
 */
util.linkUrls = function(s, attr) {
  if (attr == undefined) attr = 'target="_blank" rel="noopener"';
  var t = '<a href="$1"';
  if (attr) t += ' ' + attr;
  t += '>$1</a>';
  return s.replace(/(https?:\/\/[!#$%&'*+,/:;=?@[\]0-9A-Za-z\-._~]+)/g, t);
};

//---------------------------------------------------------
/**
 * R10, G10, B10 or 'R10,G10,B10' -> '#R16G16B16'
 */
util.rgb10to16 = function(r, g, b) {
  return util.color.rgb(r, g, b);
};

/**
 * '#R16G16B16' -> 'R10,G10,B10'
 */
util.rgb16to10 = function(v) {
  var o = util.color.rgb(v);
  return o.r + ',' + o.g + ',' + o.b;
};

util.color = {};
/**
 * R, G, B <-> #RGB
 */
util.color.rgb = function(r, g, b) {
  if (typeof r == 'string') {
    if (r.match(/,/)) {
      var v = r.split(',');
      r = v[0];
      g = v[1];
      b = v[2];
    } else {
      return util.color.rgb16to10(r);
    }
  }
  var rgb = util.color.rgb10to16(r, g, b);
  return '#' + rgb.r + rgb.g + rgb.b;
};

/**
 * R, G, B -> {h, s, v}
 * r: R or #RGB
 */
util.color.rgb2hsv = function(r, g, b) {
  if (typeof r == 'string') {
    var rgb = util.color.rgb16to10(r);
    r = rgb.r;
    g = rgb.g;
    b = rgb.b;
  }
  var hsv = {
    h: util.color.getH(r, g, b),
    s: util.color.getS(r, g, b),
    v: util.color.getV(r, g, b)
  };
  return hsv;
};

/**
 * H, S, V -> {r, g, b}
 */
util.color.hsv2rgb = function(h, s, v) {
  var r, g, b;
  var max = v;
  var min = max - ((s / 255) * max);
  if ((h >= 0) && (h < 60)) {
    r = max;
    g = (h / 60) * (max - min) + min;
    b = min;
  } else if ((h >= 60) && (h < 120)) {
    r = ((120 - h) / 60) * (max - min) + min;
    g = max;
    b = min;
  } else if ((h >= 120) && (h < 180)) {
    r = min;
    g = max;
    b = ((h - 120) / 60) * (max - min) + min;
  } else if ((h >= 180) && (h < 240)) {
    r = min;
    g = ((240 - h) / 60) * (max - min) + min;
    b = max;
  } else if ((h >= 240) && (h < 300)) {
    r = ((h - 240) / 60) * (max - min) + min;
    g = min;
    b = max;
  } else {
    r = max;
    g = min;
    b = ((360 - h) / 60) * (max - min) + min;
  }
  return {r: Math.round(r), g: Math.round(g), b: Math.round(b)};
};

/**
 * rgb16: #rgb
 * brightness: -100-0-100
 * hue: -100-0-100
 */
util.color.adjust = function(rgb16, brightness, hue) {
  hue |= 0;
  var hsv = util.color.rgb2hsv(rgb16);
  var h = hsv.h + ((hue / 100) * 255);
  var s = hsv.s;
  var v = hsv.v;
  if (brightness > 0) {
    s = s + (((brightness * -1) / 100) * 255);
  } else {
    v = v + ((brightness / 100) * 255);
  }
  if (h < 0) {
    h += 360;
  } else if (h >= 360) {
    h -= 360;
  }
  if (s < 0) {
    s = 0;
  } else if (s > 255) {
    s = 255;
  }
  if (v < 0) {
    v = 0;
  } else if (v > 255) {
    v = 255;
  }
  var rgb = util.color.hsv2rgb(h, s, v);
  return util.color.rgb(rgb.r, rgb.g, rgb.b);
};

util.color.rgb10to16 = function(r, g, b) {
  var r16 = ('0' + parseInt(r).toString(16)).slice(-2);
  var g16 = ('0' + parseInt(g).toString(16)).slice(-2);
  var b16 = ('0' + parseInt(b).toString(16)).slice(-2);
  var r0 = r16.charAt(0);
  var r1 = r16.charAt(1);
  var g0 = g16.charAt(0);
  var g1 = g16.charAt(1);
  var b0 = b16.charAt(0);
  var b1 = b16.charAt(1);
  if ((r0 == r1) && (g0 == g1) && (b0 == b1)) {
    r16 = r0;
    g16 = g0;
    b16 = b0;
  }
  return {r: r16, g: g16, b: b16};
};
util.color.rgb16to10 = function(rgb16) {
  var r16, g16, b16, r10, g10, b10;
  rgb16 = rgb16.replace(/#/, '').replace(/\s/g, '');
  if (rgb16.length == 6) {
    r16 = rgb16.substr(0, 2);
    g16 = rgb16.substr(2, 2);
    b16 = rgb16.substr(4, 2);
  } else if (rgb16.length == 3) {
    r16 = rgb16.substr(0, 1);
    g16 = rgb16.substr(1, 1);
    b16 = rgb16.substr(2, 1);
    r16 += r16;
    g16 += g16;
    b16 += b16;
  }
  r10 = parseInt(r16, 16);
  g10 = parseInt(g16, 16);
  b10 = parseInt(b16, 16);
  return {r: r10, g: g10, b: b10};
};
// Hue 0-360
util.color.getH = function(r, g, b) {
  if ((r == g) && (g == b)) return 0;
  var a = util.color.sortRGB(r, g, b);
  var min = a[0];
  var max = a[2];
  var h;
  if (max == r) {
    h = 60 * ((g - b) / (max - min));
  } else if (max == g) {
    h = 60 * ((b - r) / (max - min)) + 120;
  } else {
    h = 60 * ((r - g) / (max - min)) + 240;
  }
  if (h < 0) h += 360;
  return Math.round(h);
};
// Saturation/Chroma 0-255
util.color.getS = function(r, g, b) {
  var a = util.color.sortRGB(r, g, b);
  var min = a[0];
  var max = a[2];
  var s = (max - min) / max;
  return Math.round(s * 255);
};
// Value/Brightness 0-255
util.color.getV = function(r, g, b) {
  return util.color.sortRGB(r, g, b)[2];
};
util.color.sortRGB = function(r, g, b) {
  return [r, g, b].sort(function(v1, v2) {return v1 - v2});
};

//---------------------------------------------------------
util.textarea = {};
/**
 * addStatusInfo('#textarea-id', '#infoarea-id')
 */
util.textarea.addStatusInfo = function(textarea, infoarea) {
  textarea = util.getElement(textarea);
  if (!textarea) return;
  infoarea = util.getElement(infoarea);
  if (!infoarea) return;
  textarea.infoarea = infoarea;
  util.textarea._adqdListener(textarea);
};
/**
 * updateTextAreaInfo('#textarea-id')
 */
util.updateTextAreaInfo = function(textarea) {
  textarea = util.getElement(textarea);
  if (!textarea) return;
  var txt = textarea.value;
  var len = txt.length;
  var lenB = util.lenB(txt);
  var lfCnt = util.countLineBreak(txt);
  var chrs = len - lfCnt;
  var tl = (len == 0 ? 0 : lfCnt + 1);
  var st = textarea.selectionStart;
  var ed = textarea.selectionEnd;
  var sl = ed - st;
  var ch = util.str2chars(txt)[st] || '';
  var u10 = util.getCodePoint(ch);
  var u16 = util.getUnicodePoints(ch, true);
  var CTCH = {0: 'NUL', 9: 'TAB', 10: 'LF', 11: 'ESC', 32: 'SP', 127: 'DEL', 12288: 'emSP'};
  if (isNaN(u10)) {
    ch = '[END]';
    u16 = 'U+----';
  } else if (CTCH[u10]) {
    ch = CTCH[u10];
  }
  if (textarea.infoarea) {
    var cp = ch + '&nbsp;' + u16 + (u10 ? '(' + u10 + ')' : '');
    var t = txt.substr(0, st);
    var l = (t.match(/\n/g) || []).length + 1;
    var c = t.replace(/.*\n/g, '').length + 1;
    var tc = util.clipTextLine(txt, st).length;
    var slT = txt.substring(st, ed);
    var slL = util.countLineBreak(slT) + 1;
    var slct = (sl ? ' SEL:' + ('LEN=' + sl + '/L=' + slL) : '');
    var s = cp;
    s += ' ' + l + ':' + c + ' ';
    s += ' C=' + tc + ' L=' + tl;
    s += ' LEN=' + len;
    s += ' CHARS=' + chrs;
    s += ' bytes=' + lenB;
    textarea.infoarea.innerHTML = s + slct;
  }
  var listener = textarea.listener;
  if (listener) {
    var data = {
      codePoint: u10,
      cp16: u16,
      ch: ch,
      len: len,
      lenB: lenB,
      start: st,
      end: ed,
      selected: sl
    };
    listener(data);
  }
};
util.textarea._adqdListener = function(tgt) {
  tgt.addEventListener('input', util.textarea.onInput);
  tgt.addEventListener('change', util.textarea.onInput);
  tgt.addEventListener('keydown', util.textarea.onInput);
  tgt.addEventListener('keyup', util.textarea.onInput);
  tgt.addEventListener('click', util.textarea.onInput);
};
util.textarea.onInput = function(e) {
  util.updateTextAreaInfo(e.target);
};
util.textarea.addListener = function(tgt, f) {
  var el = util.getElement(tgt);
  if (el) {
    el.listener = f;
    util.textarea._adqdListener(el);
  }
};

//---------------------------------------------------------
// Write HTML
//---------------------------------------------------------
/**
 * Write HTML and Fade in
 * util.writeHTML('#id', 'text');
 * util.writeHTML('#id', 'text', 300);
 *
 * Clear and Fade out
 * util.writeHTML('#id', '');
 * util.writeHTML('#id', '', 200);
 */
util.writeHTML = function(tgt, html, speed) {
  var el = tgt;
  if (typeof tgt == 'string') el = document.querySelector(tgt);
  if (!el) return;
  if (speed == 0) {
    el.innerHTML = html;
    return;
  }
  if (html == '') {
    util.clearHTML(el, speed);
  } else {
    el.innerHTML = '';
    var cbData = {el: el, html: html, speed: speed};
    util.fadeOut(el, 0, util._writeHTML, cbData);
  }
};
util._writeHTML = function(cbData) {
  var DFLT_SPEED = 250;
  var speed = cbData.speed;
  if ((speed == undefined) || (speed < 0)) speed = DFLT_SPEED;
  cbData.el.innerHTML = cbData.html;
  setTimeout(util.__writeHTML, 10, cbData);
};
util.__writeHTML = function(cbData) {
  util.fadeIn(cbData.el, cbData.speed);
};

/**
 * Fade out and clear
 */
util.clearHTML = function(tgt, speed) {
  var DFLT_SPEED = 200;
  if ((speed == undefined) || (speed < 0)) speed = DFLT_SPEED;
  util.fadeOut(tgt, speed, util._clearHTML, tgt);
};
util._clearHTML = function(el) {
  el.innerHTML = '';
  util.removeClass(el, 'fadeout');
};

//---------------------------------------------------------
// Text Sequencer
//---------------------------------------------------------
/**
 * var ctx = util.textseq(el, text, opt);
 * opt: see DFLT_OPT
 */
util.textseq = function(el, text, opt) {
  el = util.getElement(el);
  if (!opt) opt = {};
  var cursor = opt.cursor;
  if (typeof opt.cursor == 'number') delete opt.cursor;
  util.copyDefaultProps(util.textseq.DFLT_OPT, opt);
  if (typeof cursor == 'number') opt.cursor.n = cursor;
  if (text instanceof Array) {
    el.$$textseqCtx = {textList: text, idx: 0, opt: opt};
    text = text[0];
  }
  return util.textseq1(el, text, opt);
};
util.textseq.DFLT_OPT = {
  speed: 20,
  step: 1,
  start: 0,
  len: -1,
  reverse: false,
  cursor: {n: 0, speed: 100, repeat: false},
  pause: 1000, // for text array
  onprogress: null, // <callback-function(ctx, chunk)>
  oncomplete: null // <callback-function(ctx)>
};
util.textseq1 = function(el, text, opt) {
  var ctx = util.getCtx4El(util.textseq.ctxs, el);
  if (ctx) util.textseq._stop(ctx);
  ctx = util.textseq.createCtx(el, text, opt);
  var i = util.getCtxIdx4El(util.textseq.ctxs, el);
  if (i < 0) {
    util.textseq.ctxs.push(ctx);
  } else {
    util.textseq.ctxs[i] = ctx;
  }
  var f = (ctx.cursor.n && ctx.cursor.speed ? util.textseq.blinkCursor : (ctx.reverse ? util.textseq._reverse : util.textseq._textseq));
  ctx.tmrId = setTimeout(f, 0, ctx);
  return ctx;
};
util.textseq._textseq = function(ctx) {
  var speed = util.textseq.getSpeed(ctx);
  var step = ctx.step;
  ctx.tmrId = 0;
  var text = util.textseq.getText(ctx);
  util.textseq.print(ctx, text);
  util.textseq.onprogress(ctx, ctx.pos, ctx.prevPos, ctx.cutLen);
  if (ctx.pos < ctx.text.length) {
    speed = util.textseq.getSpeed(ctx);
    ctx.tmrId = setTimeout(util.textseq._textseq, speed, ctx);
  } else {
    util.textseq.print(ctx, ctx.orgTxt);
    util.textseq.oncomplete(ctx);
  }
  if ((speed == 0) || (step == 0)) {
    ctx.pos = ctx.text.length;
    ctx.cutLen = ctx.pos;
  } else {
    ctx.prevPos = ctx.pos;
    ctx.pos += step;
    ctx.cutLen = step;
    if (ctx.pos > ctx.end) ctx.pos = ctx.text.length;
  }
};
util.textseq._reverse = function(ctx) {
  var speed = util.textseq.getSpeed(ctx);
  var step = ctx.step;
  ctx.tmrId = 0;
  var text = util.textseq.getText(ctx);
  util.textseq.print(ctx, text);
  util.textseq.onprogress(ctx, ctx.pos, ctx.prevPos, ctx.cutLen);
  if (ctx.pos == ctx.end) {
    util.textseq.oncomplete(ctx);
  } else if (ctx.pos >= 0) {
    speed = util.textseq.getSpeed(ctx);
    ctx.tmrId = setTimeout(util.textseq._reverse, speed, ctx);
  } else {
    util.textseq.print(ctx, '');
    util.textseq.oncomplete(ctx);
  }
  if ((speed == 0) || (step == 0)) {
    ctx.pos = -1;
    ctx.cutLen = ctx.text.length;
  } else {
    ctx.prevPos = ctx.pos;
    ctx.pos -= step;
    if (ctx.pos < ctx.end) ctx.pos = ctx.end;
  }
};
util.textseq.getText = function(ctx) {
  var len = ctx.pos;
  if (ctx.reverse) len++;
  return ctx.text.substr(0, len);
};
util.textseq.start = function(el) {
  var ctx = util.getCtx4El(util.textseq.ctxs, el);
  if (!ctx) return;
  util.textseq._stop(ctx);
  var f = ctx.reverse ? util.textseq._reverse : util.textseq._textseq;
  f(ctx);
};
util.textseq.stop = function(el) {
  var i = util.getCtxIdx4El(util.textseq.ctxs, el);
  if (i < 0) return;
  var ctx = util.textseq.ctxs[i];
  util.textseq._stop(ctx);
};
util.textseq._stop = function(ctx) {
  if (ctx.tmrId > 0) {
    clearTimeout(ctx.tmrId);
    ctx.tmrId = 0;
  }
};
util.textseq.onprogress = function(ctx, pos, prevPos, cutLen) {
  if (!ctx.onprogress) return;
  var st;
  if (ctx.reverse) {
    if (prevPos < 0) return;
    st = prevPos - cutLen + 1;
    if (st < 0) {
      cutLen = cutLen + st;
      st = 0;
    }
  } else {
    if (prevPos == pos) return;
    st = prevPos;
    if (st < 0) return;
  }
  var chunk = ctx.text.substr(st, cutLen);
  ctx.onprogress(ctx, chunk);
};
util.textseq.oncomplete = function(ctx) {
  var el = ctx.el;
  var i = util.getCtxIdx4El(util.textseq.ctxs, el);
  util.textseq.ctxs.splice(i, 1);
  var textseqCtx = el.$$textseqCtx;
  var idx;
  if (textseqCtx) idx = textseqCtx.idx;
  if (ctx.oncomplete) ctx.oncomplete(ctx, idx);
  if (textseqCtx) {
    textseqCtx.idx++;
    if (textseqCtx.idx < textseqCtx.textList.length) {
      if (!textseqCtx.opt.cursor.repeat) textseqCtx.opt.cursor.n = 0;
      setTimeout(util.textseq1, textseqCtx.opt.pause, el, textseqCtx.textList[textseqCtx.idx], textseqCtx.opt);
    }
  }
};
util.textseq.createCtx = function(el, text, opt) {
  if (!opt) opt = {};
  var isInp = util.isTextInput(el);
  var orgTxt = text;
  if (!isInp) text = util.html2text(text);
  var txtLen = text.length;
  var len = ((opt.len < 0) ? -1 : opt.len);
  var step = opt.step;
  var cutLen = step;
  if (opt.reverse) {
    var start = ((opt.start == 0) ? txtLen - 1 : opt.start - 1);
    var end = -1;
    if (len > 0) end = start - len;
    if (end < 0) end = -1;
    var prevPos = start + cutLen;
    if (prevPos >= txtLen) prevPos = txtLen - 1;
    var pos = start;
    if (pos < 0) pos = txtLen - 1;
  } else {
    start = ((opt.start == 0) ? 0 : opt.start);
    end = txtLen;
    if (len > 0) end = start + len;
    prevPos = start;
    if (prevPos < 0) prevPos = 0;
    pos = start;
    if (pos < 0) pos = 0;
  }
  el.innerHTML = '';
  delete el.$$textseqSpan;
  if (opt.style && !isInp) {
    var span = document.createElement('span');
    util.setStyle(span, opt.style);
    el.$$textseqSpan = span;
    el.appendChild(span);
  }
  var ctx = {
    el: el,
    orgTxt: orgTxt,
    text: text,
    speed: opt.speed,
    step: step,
    start: start,
    end: end,
    isInp: isInp,
    tmrId: 0,
    prevPos: prevPos,
    pos: pos,
    cutLen: cutLen,
    reverse: opt.reverse,
    cursor: opt.cursor,
    cursorCnt: 0,
    onprogress: opt.onprogress,
    oncomplete: opt.oncomplete
  };
  return ctx;
};
util.textseq.getSpeed = function(ctx) {
  return ctx.speed;
};
util.textseq.blinkCursor = function(ctx) {
  var speed = util.textseq.getSpeed(ctx);
  var blnkNum = ctx.cursor.n * 2;
  if (ctx.cursorCnt < blnkNum) {
    ctx.cursorCnt++;
    var c = (ctx.cursorCnt % 2 == 0) ? ' ' : '_';
    var s = util.textseq.getText(ctx) + c;
    util.textseq.print(ctx, s);
    ctx.tmrId = setTimeout(util.textseq.blinkCursor, ctx.cursor.speed, ctx);
  } else {
    ctx.cursorCnt = 0;
    ctx.tmrId = setTimeout((ctx.reverse ? util.textseq._reverse : util.textseq._textseq), speed, ctx);
  }
};
util.textseq.print = function(ctx, s) {
  if (ctx.isInp) {
    ctx.el.value = s;
  } else {
    var tgt = (ctx.el.$$textseqSpan ? ctx.el.$$textseqSpan : ctx.el);
    tgt.innerHTML = s;
  }
};
util.textseq.ctxs = [];

//---------------------------------------------------------
// Styles
//---------------------------------------------------------
util.CSS = '';
util.STYLE = function() {/*
.pointable:hover {
  cursor: pointer !important;
}
.pseudo-link {
  cursor: pointer;
}
.pseudo-link:hover {
  text-decoration: underline;
}
.glow {
  text-shadow: 0 0 5px;
}
.blink {animation: blinker 1.5s step-end infinite;}
@keyframes blinker {
  50% {opacity: 0;}
  100% {opacity: 0;}
}
.blink1 {animation: blinker1 1s ease-in infinite alternate;}
@keyframes blinker1 {
  0% {opacity: 0;}
  20% {opacity: 0;}
  70% {opacity: 1;}
}
.blink2 {animation: blinker2 1s ease-in-out infinite alternate;}
@keyframes blinker2 {
  0% {opacity: 0;}
  70% {opacity: 1;}
}
.blink3 {animation: blinker3 0.5s ease-in infinite alternate;}
@keyframes blinker3 {
  0% {opacity: 0;}
  20% {opacity: 0;}
  70% {opacity: 1;}
}
.flicker {animation: flicker1 0.7s linear;animation-fill-mode:both;opacity:1 !important;}
@keyframes flicker1 {
  0% {opacity: 0;}
  10% {opacity: 1;}
  20% {opacity: 0.1;}
  25% {opacity: 1;}
  35% {opacity: 0.1;}
  40% {opacity: 1;}
  50% {opacity: 0.1;}
  80% {opacity: 1;}
}
.flicker-out {animation: flicker2 0.7s linear;animation-fill-mode:both;opacity:0 !important;}
@keyframes flicker2 {
  0% {opacity: 1;}
  10% {opacity: 0.1;}
  20% {opacity: 1;}
  25% {opacity: 0.1;}
  35% {opacity: 1;}
  40% {opacity: 0.1;}
  50% {opacity: 1;}
  80% {opacity: 0;}
}
.progdot:after {content:"..."; animation: progdot1 1.2s linear infinite;}
@keyframes progdot1 {
  10% {content: "   ";}
  20% {content: ".  ";}
  30% {content: ".. ";}
  40% {content: "...";}
}
.dialog {
  background: #fff;
  color: #000;
}
.led {
  text-shadow: 0 0 5px !important;
}
.led:before {content:"\025CF"}
.led-on {
  color: #0f0 !important;
}
.led-off {
  color: #888 !important;
}
.led-red {
  color: #f88 !important;
}
.led-orange {
  color: #f80 !important;
}
.led-blue {
  color: #4cf !important;
}
*/};

util.registerStyle = function(style) {
  util.CSS += style;
};

util.setupStyle = function() {
  util.registerStyle(util.fn2text(util.STYLE));
  util.infotip.registerStyle();
  util.registerFadeStyle();
  util.loader.registerStyle();
  var head = document.head || document.getElementsByTagName('head').item(0);
  var style = document.createElement('style');
  var firstStyle = document.getElementsByTagName('style').item(0);
  if (firstStyle) {
    head.insertBefore(style, firstStyle);
  } else {
    head.appendChild(style);
  }
  style.type = 'text/css';
  if (style.styleSheet) {
    style.styleSheet.cssText = util.CSS;
  } else {
    style.appendChild(document.createTextNode(util.CSS));
  }
};

util.setStyle = function(el, a1, a2, a3) {
  util.callFn4El(util._setStyle, el, a1, a2, a3);
};
util._setStyle = function(el, a1, a2, a3) {
  el = util.getElement(el);
  if (!el) return;
  var imp = true;
  if ((a1 === false) || (a2 === false) || (a3 === false)) imp = false;
  if (a1 instanceof Object) {
    for (var k in a1) {
      if (imp) {
        el.style.setProperty(k, a1[k], 'important');
      } else {
        el.style[k] = a1[k];
      }
    }
  } else {
    if (imp) {
      el.style.setProperty(a1, a2, 'important');
    } else {
      el.style[a1] = a2;
    }
  }
};

util.getPxVal = function(v) {
  v = v.replace('px', '');
  return parseInt(v);
};

//---------------------------------------------------------
// Infotip
//---------------------------------------------------------
util.infotip = {};
util.infotip.ST_HIDE = 0;
util.infotip.ST_FADEIN = 1;
util.infotip.ST_OPEN = 2;
util.infotip.ST_SHOW = 3;
util.infotip.ST_FADEOUT = 4;
util.DFLT_DURATION = 1500;
util.infotip.FADE_SPEED = 250;
util.infotip.obj = {
  type: 'infotip',
  st: util.infotip.ST_HIDE,
  el: {body: null, pre: null},
  duration: 0,
  timerId: 0
};
util.infotip.opt = null;

/**
 * show("message");
 * show("message", 3000);
 * show("message", 0, {pos: {x: 100, y: 200}});
 * show("message", 0, {pos: 'pointer', offset: {x: 5, y: -8}});
 * show("message", 0, {pos: 'active'});
 * show("message", 0, {style: {'font-size': '18px'}});
 */
util.infotip.show = function(msg, a2, a3) {
  var x, y, style, offset, opt;
  var duration = util.DFLT_DURATION;
  if (typeof a2 == 'number') {
    duration = a2;
    opt = a3;
  } else if (a2 instanceof Object) {
    opt = a2;
  } else if (a2 == undefined) {
    opt = a3;
  }
  if (opt) {
    if (opt.pos) {
      if (opt.pos == 'pointer') {
        x = util.mouseX;
        y = util.mouseY;
        if (!opt.offset) opt.offset = {x: 5, y: -8};
        offset = opt.offset;
      } else if (opt.pos == 'active') {
        var el = document.activeElement;
        var rect = el.getBoundingClientRect();
        x = rect.left;
        y = rect.top;
      } else if (opt.pos.x != undefined) {
        x = opt.pos.x;
      }
      if (opt.pos.y != undefined) y = opt.pos.y;
    }
    if (opt.style) style = opt.style;
  }

  var obj = util.infotip.obj;
  if (duration == undefined) duration = util.DFLT_DURATION;
  obj.duration = duration;
  util.infotip._show(obj, msg, style);

  if ((x != undefined) && (y != undefined)) {
    util.infotip._move(obj, x, y, offset);
  } else {
    util.infotip._center(obj);
  }
  util.infotip.opt = opt;
};
util.infotip._show = function(obj, msg, style) {
  if (!obj.el.body) {
    var div = document.createElement('div');
    div.className = 'infotip-wrp fadeout';
    var pre = document.createElement('pre');
    pre.className = 'infotip';
    if (style) {
      for (var p in style) {
        util.setStyle(pre, p, style[p]);
      }
    }
    div.appendChild(pre);
    obj.el.body = div;
    obj.el.pre = pre;
    document.body.appendChild(div);
  }
  msg = (msg + '').replace(/\\n/g, '\n');
  obj.el.pre.innerHTML = msg;
  document.body.appendChild(obj.el.body);
  if (obj.st == util.infotip.ST_SHOW) {
    util.infotip.setHideTimer(obj);
  } else {
    obj.st = util.infotip.ST_FADEIN;
    setTimeout(util.infotip.fadeIn, 10, obj);
  }
};

util.infotip.fadeIn = function(obj) {
  var cb = util.infotip.onFadeInCompleted;
  util.fadeIn(obj.el.body, util.infotip.FADE_SPEED, cb, obj);
  util.infotip.setHideTimer(obj);
};
util.infotip.onFadeInCompleted = function(el, obj) {
  obj.st = util.infotip.ST_SHOW;
};

util.infotip.setHideTimer = function(obj) {
  if (obj.duration > 0) {
    if (obj.timerId) clearTimeout(obj.timerId);
    obj.timerId = setTimeout(util.infotip._hide, obj.duration, obj);
  }
};

/**
 * move
 */
util.infotip.move = function(x, y, offset) {
  util.infotip._move(util.infotip.obj, x, y, offset);
};

util.infotip._move = function(obj, x, y, offset) {
  var ttBody = obj.el.body;
  if (!ttBody) return;
  var rect = ttBody.getBoundingClientRect();
  if (offset) {
    x += offset.x;
    y += offset.y;
    if (offset.y < 0) y -= rect.height;
  }
  if (y < 0) y = 0;
  var cliW = util.getClientWidth();
  if (x + rect.width > cliW) x = cliW - rect.width;
  if (x < 0) x = 0;
  ttBody.style.left = x + 'px';
  ttBody.style.top = y + 'px';
};

util.infotip.center = function() {
  util.infotip._center(util.infotip.obj);
};
util.infotip._center = function(obj) {
  var infotip = obj.el.body;
  util.center(infotip);
};

/**
 * Hide a infotip
 */
util.infotip.hide = function() {
  var obj = util.infotip.obj;
  if (obj.timerId) {
    clearTimeout(obj.timerId);
    obj.timerId = 0;
  }
  util.infotip._hide(obj);
};
util.infotip._hide = function(obj) {
  var delay = util.infotip.FADE_SPEED;
  obj.st = util.infotip.ST_FADEOUT;
  util.fadeOut(obj.el.body, delay);
  obj.timerId = setTimeout(util.infotip.onFadeOutCompleted, delay, null, obj);
};
util.infotip.onFadeOutCompleted = function(el, obj) {
  util.infotip.__hide(obj);
};
util.infotip.__hide = function(obj) {
  var div = obj.el.body;
  if ((div != null) && (div.parentNode)) document.body.removeChild(div);
  obj.el.pre = null;
  obj.el.body = null;
  if (obj.id == 'infotip') util.infotip.opt = null;
  obj.st = util.infotip.ST_HIDE;
};

util.infotip.isVisible = function() {
  return util.infotip.obj.el != null;
};

util.infotip.adjust = function() {
  if (util.infotip.isVisible()) util.infotip.center();
};

util.infotip.onMouseMove = function(x, y) {
  if (util.infotip.opt && util.infotip.opt.pos == 'pointer') {
    util.infotip.move(x, y, util.infotip.opt.offset);
  }
};

util.infotip.registerStyle = function() {
  var style = '.infotip-wrp {';
  style += '  position: fixed !important;';
  style += '  display: inline-block !important;';
  style += '  max-width: calc(100vw - 35px) !important;';
  style += '  max-height: calc(100vh - 35px) !important;';
  style += '  overflow: auto !important;';
  style += '  padding: 4px !important;';
  style += '  box-sizing: content-box !important;';
  style += '  z-index: 2147483647 !important;';
  style += '  box-shadow: 8px 8px 10px rgba(0,0,0,.3) !important;';
  style += '  border-radius: 3px !important;';
  style += '  color: #fff! important;';
  style += '  background: rgba(0,0,0,0.65) !important;';
  style += '}';
  style += '.infotip {';
  style += '  width: auto !important;';
  style += '  height: auto !important;';
  style += '  min-height: 1em !important;';
  style += '  margin: 0 !important;';
  style += '  padding: 0 !important;';
  style += '  line-height: 1.2 !important;';
  style += '  color: #fff !important;';
  style += '  font-size: 12px !important;';
  style += '  font-family: Consolas, Monaco, Menlo, monospace, sans-serif !important;';
  style += '}';
  util.registerStyle(style);
};

//---------------------------------------------------------
// Tooltip
//---------------------------------------------------------
util.tooltip = {};
util.tooltip.DELAY = 350;
util.tooltip.offset = {x: 5, y: -8};
util.tooltip.targetEl = null;
util.tooltip.obj = {
  type: 'tooltip',
  st: util.infotip.ST_HIDE,
  el: {body: null, pre: null},
  timerId: 0
};
util.tooltip.disabled = false;
util.tooltip.show = function(el, msg, x, y) {
  var obj = util.tooltip.obj;
  if (obj.st == util.infotip.ST_FADEOUT) util.tooltip.cancel(obj);
  if ((el == util.tooltip.targetEl) && (obj.st >= util.infotip.ST_OPEN)) {
    util.infotip._move(obj, x, y, util.tooltip.offset);
  } else {
    if (obj.st != util.infotip.ST_SHOW) obj.st = util.infotip.ST_OPEN;
    util.tooltip.targetEl = el;
    if (obj.el.body) {
      util.tooltip._show(msg);
    } else {
      if (obj.timerId) clearTimeout(obj.timerId);
      obj.timerId = setTimeout(util.tooltip._show, util.tooltip.DELAY, msg);
    }
  }
};
util.tooltip._show = function(msg) {
  var obj = util.tooltip.obj;
  var st = obj.st;
  if (st == util.infotip.ST_FADEOUT) {
    clearTimeout(obj.timerId);
    obj.timerId = 0;
  }
  var x = util.mouseX;
  var y = util.mouseY;
  var el = document.elementFromPoint(x, y);
  if (!el || (el != util.tooltip.targetEl)) {
    util.tooltip.cancel(obj);
    return;
  }
  util.infotip._show(obj, msg);
  util.infotip._move(obj, x, y, util.tooltip.offset);
};

util.tooltip.hide = function() {
  util.infotip._hide(util.tooltip.obj);
  util.tooltip.targetEl = null;
};

util.tooltip.cancel = function(obj) {
  util.tooltip.targetEl = null;
  if (obj.timerId) {
    clearTimeout(obj.timerId);
    obj.timerId = 0;
  }
  util.infotip.__hide(obj);
};

util.tooltip.onMouseMove = function(x, y) {
  if (util.tooltip.disabled) return;
  var el = document.elementFromPoint(x, y);
  var tpData = ((el && el.dataset) ? el.dataset.tooltip : null);
  if (tpData) {
    util.tooltip.show(el, tpData, x, y);
  } else if (util.tooltip.obj.el.body) {
    util.tooltip.hide();
  }
};

util.tooltip.setDelay = function(ms) {
  util.tooltip.DELAY = ms;
};

//---------------------------------------------------------
// Fade in / out
//---------------------------------------------------------
util.registerFadeStyle = function() {
  var style = '.fadein {opacity: 1 !important;}';
  style += '.fadeout {opacity: 0 !important;}';
  util.registerStyle(style);
};

util.fadeIn = function(el, speed, cb, arg) {
  el = util.getElement(el);
  if (!el) return;
  if ((speed == undefined) || (speed < 0)) speed = util.DFLT_FADE_SPEED;
  if (el.fadeTimerId > 0) {
    clearTimeout(el.fadeTimerId);
    el.fadeTimerId = 0;
  }
  util.addClass(el, 'fadeout');
  setTimeout(util._fadeIn, 0, el, speed, cb, arg);
};
util._fadeIn = function(el, speed, cb, arg) {
  var t = speed / 1000;
  util.setStyle(el, 'transition', 'opacity ' + t + 's ease');
  util.removeClass(el, 'fadeout');
  util.addClass(el, 'fadein');
  var dat = {cb: cb, el: el, arg: arg};
  el.fadeTimerId = setTimeout(util.__fadeIn, speed, dat);
};
util.__fadeIn = function(dat) {
  dat.el.fadeTimerId = 0;
  if (dat.cb) dat.cb(dat.el, dat.arg);
};

util.fadeOut = function(el, speed, cb, arg) {
  el = util.getElement(el);
  if (!el) return;
  if ((speed == undefined) || (speed < 0)) speed = util.DFLT_FADE_SPEED;
  if (el.fadeTimerId > 0) {
    clearTimeout(el.fadeTimerId);
    el.fadeTimerId = 0;
  }
  util.removeClass(el, 'fadein');
  setTimeout(util._fadeOut, 0, el, speed, cb, arg);
};
util._fadeOut = function(el, speed, cb, arg) {
  var t = speed / 1000;
  util.setStyle(el, 'transition', 'opacity ' + t + 's ease');
  util.removeClass(el, 'fadein');
  util.addClass(el, 'fadeout');
  var dat = {cb: cb, el: el, arg: arg};
  el.fadeTimerId = setTimeout(util.__fadeOut, speed, dat);
};
util.__fadeOut = function(dat) {
  dat.el.fadeTimerId = 0;
  if (dat.cb) dat.cb(dat.arg);
};
util.clearFade = function(el) {
  util.setStyle(el, 'transition', '');
  util.removeClass(el, 'fadein');
  util.removeClass(el, 'fadeout');
};

//---------------------------------------------------------
// Screen Fader
//---------------------------------------------------------
util.SCREEN_FADER_ZINDEX = 99999999;
util.fadeScreenEl = null;
/**
 * onReady()
 *   initScreenFader('#id')
 * onLoad()
 *   fadeScreenIn()
 */
util.initScreenFader = function(a, bg) {
  var el = util.fadeScreenEl;
  if (!el) el = util.getElement(a);
  if (!el) el = util.createFadeScreenEl(bg);
  util.fadeScreenEl = el;
  document.body.appendChild(el);
  return el;
};

util.fadeScreenIn = function(speed, cb, bg) {
  if (speed == undefined) speed = util.DFLT_FADE_SPEED;
  var el = util.initScreenFader(null, bg);
  util.fadeScreenIn.cb = cb;
  util.fadeOut(el, speed, util._fadeScreenIn);
};
util._fadeScreenIn = function() {
  document.body.removeChild(util.fadeScreenEl);
  util.fadeScreenEl = null;
  var cb = util.fadeScreenIn.cb;
  util.fadeScreenIn.cb = null;
  if (cb) cb();
};

util.fadeScreenOut = function(speed, cb, bg) {
  if (speed == undefined) speed = util.DFLT_FADE_SPEED;
  var el = util.initScreenFader(null, bg);
  util.fadeIn(el, speed, cb);
};

util.createFadeScreenEl = function(bg) {
  var el = document.createElement('div');
  if (!bg) bg = '#fff';
  var style = {
    'position': 'fixed',
    'width': '100vw',
    'height': '100vh',
    'top': '0',
    'left': '0',
    'background': bg,
    'z-index': util.SCREEN_FADER_ZINDEX
  };
  util.setStyle(el, style);
  return el;
};

//---------------------------------------------------------
// Loading indicator
//---------------------------------------------------------
util.loader = {};
util.loader.DFLTOPT = {
  delay: 500,
  size: '46px',
  color1: '#ccc',
  color2: 'rgba(204, 204, 204, 0.25)',
  weight: '4px',
  speed: '1s',
};
util.loader.cnt = 0;
util.loader.ctxs = [];
util.loader.registerStyle = function() {
  var style = '@keyframes loader-rotate {';
  style += '0% {transform: rotate(0);}';
  style += '100% {transform: rotate(360deg);}';
  style += '}';
  style += '.loading {cursor: progress !important;}';
  util.registerStyle(style);
};

util.loader.show = function(el, opt) {
  if (util.objtype(el) == '[object Object]') {
    opt = el;
    el = null;
  }
  opt = util.copyDefaultProps(util.loader.DFLTOPT, opt);
  el = util.getElement(el);
  if (!el) el = document.body;
  var ctx = util.getCtx4El(util.loader.ctxs, el);
  if (!ctx) {
    var ldrEl = util.loader.create(opt);
    ctx = {el: el, opt: opt, ldrEl: ldrEl, cnt: 0, timerId: 0};
    util.loader.ctxs.push(ctx);
    util.addClass(ldrEl, 'fadeout');
  }
  ctx.cnt++;
  util.loader.cnt++;
  if (ctx.cnt > 1) return;
  ctx.timerId = setTimeout(util.loader._show, opt.delay, ctx);
};
util.loader._show = function(ctx) {
  ctx.timerId = 0;
  util.addClass(document.body, 'loading');
  util.overlay.show(ctx.el, ctx.ldrEl);
  util.fadeIn(ctx.ldrEl, 500);
};
util.loader.create = function(opt) {
  var el = document.createElement('div');
  var s = {
    display: 'inline-block',
    position: 'fixed',
    width: opt.size,
    height: opt.size,
    border: opt.weight + ' solid ' + opt.color2,
    'border-top-color': opt.color1,
    'border-radius': '50%',
    top: '0',
    left: '0',
    right: '0',
    bottom: '0',
    margin: 'auto',
    animation: 'loader-rotate ' + opt.speed + ' linear infinite'
  };
  util.setStyle(el, s);
  return el;
};

util.loader.hide = function(el, force) {
  el = util.getElement(el);
  if (!el) el = document.body;
  var ctx = util.getCtx4El(util.loader.ctxs, el);
  if (!ctx) return;
  if (force) {
    util.loader.cnt -= ctx.cnt;
    ctx.cnt = 0;
  } else if (ctx.cnt > 0) {
    ctx.cnt--;
    util.loader.cnt--;
  }
  if (ctx.cnt > 0) return;
  if (ctx.timerId > 0) {
    clearTimeout(ctx.timerId);
    ctx.timerId = 0;
  }
  if (util.loader.cnt <= 0) {
    util.loader.cnt = 0;
    util.removeClass(document.body, 'loading');
  }
  util.fadeOut(ctx.ldrEl, 200, util.loader._hide, ctx);
};
util.loader._hide = function(ctx) {
  util.overlay.hide(ctx.el, ctx.ldrEl);
  var i = util.getCtxIdx4El(util.loader.ctxs, ctx.el);
  util.loader.ctxs.splice(i, 1);
};

//---------------------------------------------------------
// Loading Screen
// util.loadingScreen.show('Loading...');
// util.loadingScreen.show('Loading...', '<img src="img.png">');
// util.loadingScreen.hide();
//---------------------------------------------------------
util.loadingScreen = {};
util.loadingScreen.modal = null;
util.loadingScreen.baseEl = null;
util.loadingScreen.msg = null;
util.loadingScreen.show = function(m, html) {
  if (util.loadingScreen.modal) {
    var msg = util.loadingScreen.msg;
    if (m) {
      if (!msg) {
        msg = util.loadingScreen.appendMsgArea(util.loadingScreen.baseEl);
        util.loadingScreen.msg = msg;
      }
      msg.innerHTML = m;
      util.setStyle(msg, 'opacity', '1');
    } else {
      if (msg) {
        msg.innerText = '';
        util.setStyle(msg, 'opacity', '0');
      }
    }
    return false;
  }
  util.loadingScreen.create(msg, m, html);
  return true;
};
util.loadingScreen.create = function(msg, m, html) {
  var outerWrp = document.createElement('div');
  var styles = {
    'display': 'table',
    'position': 'absolute',
    'width': '100%',
    'height': '100%',
    'top': '0',
    'right': '0',
    'bottom': '0',
    'left': '0',
    'margin': 'auto'
  };
  util.setStyle(outerWrp, styles);
  var wrp = document.createElement('div');
  styles = {
    'display': 'table-cell',
    'position': 'relative',
    'width': '100%',
    'height': '100%',
    'text-align': 'center',
    'vertical-align': 'middle'
  };
  util.setStyle(wrp, styles);
  util.loadingScreen.baseEl = wrp;
  outerWrp.appendChild(wrp);
  if (html) {
    util.loadingScreen.appendHtml(wrp, html);
  } else {
    util.loadingScreen.appendLoader(wrp);
  }
  if (m && !util.loadingScreen.msg) {
    msg = util.loadingScreen.appendMsgArea(wrp);
    msg.innerHTML = m;
    util.loadingScreen.msg = msg;
  }
  styles = {background: 'rgba(0,0,0,0.3)'};
  var closeAnywhere = false;
  var modal = util.modal.show(outerWrp, closeAnywhere, styles);
  util.loadingScreen.modal = modal;
  util.setStyle(document.body, 'cursor', 'progress');
};
util.loadingScreen.hide = function() {
  if (util.loadingScreen.modal) {
    util.loadingScreen.destroy();
    return true;
  }
  return false;
};
util.loadingScreen.destroy = function() {
  util.loadingScreen.modal.hide();
  util.loadingScreen.msg = null;
  util.loadingScreen.baseEl = null;
  util.loadingScreen.modal = null;
  util.setStyle(document.body, 'cursor', '');
};
util.loadingScreen.appendLoader = function(baseEl) {
  var opt = util.copyDefaultProps(util.loader.DFLTOPT);
  var el = util.loader.create(opt);
  util.setStyle(el, 'top', '-112px');
  baseEl.appendChild(el);
};
util.loadingScreen.appendHtml = function(baseEl, html) {
  var el = document.createElement('div');
  el.innerHTML = html;
  baseEl.appendChild(el);
};
util.loadingScreen.appendMsgArea = function(baseEl) {
  var msg = util.loadingScreen.createMsgArea();
  baseEl.appendChild(msg);
  return msg;
};
util.loadingScreen.createMsgArea = function() {
  var el = document.createElement('pre');
  var styles = {
    'display': 'inline-block',
    'position': 'relative',
    'width': 'auto',
    'height': 'auto',
    'top': '0',
    'border-radius': '4px',
    'background': 'rgba(0,0,0,0.5)',
    'padding': '0.5em 1em',
    'color': '#fff',
    'font-size': '14px'
  };
  util.setStyle(el, styles);
  return el;
};

//---------------------------------------------------------
// Window
//---------------------------------------------------------
util.newWindow = function(opt) {
  return new util.Window(opt);
};
util.countWindows = function() {
  return util.Window.count();
};
util.getWindowById = function(id) {
  return util.Window.getById(id);
};
util.getWindowByName = function(n) {
  return util.Window.getByName(n);
};
util.getWindowsByGroup = function(g) {
  return util.Window.getByGroup(g);
};
util.getAllWindows = function() {
  return util.Window.getAll();
};
util.closeAllWindows = function() {
  util.Window.closeAll();
};
util.closeWindowsByGroup = function(g) {
  util.Window.closeByGroup(g);
};
util.getWindowContext = function(el) {
  el = util.getElement(el);
  if (!el) return null;
  return util.Window.getContext(el);
};

util.Window = function(opt) {
  var ctx = this;
  ctx._type_ = 'WIN';
  util.Window.registerWindow(ctx);
  ctx.id = util.Window.cnt;
  opt = util.copyDefaultProps(util.Window.DFLT_OPT, opt);
  ctx.opt = opt;
  ctx.name = opt.name;
  ctx.group = opt.group;
  ctx.scale = opt.scale;
  ctx.uiStatus = util.Window.UI_ST_POS_AUTO_ADJ;
  ctx.sizeStatus = 0;
  ctx.orgSizePos = {w: 0, h: 0, t: 0, l: 0};
  ctx.clickedPosX = 0;
  ctx.clickedPosY = 0;
  ctx.ptOfstX = 0;
  ctx.ptOfstY = 0;
  ctx.fontSize = util.Window.BASE_FONT_SIZE * ctx.scale;
  ctx.computedMinW = util.Window.WIN_MIN_W * ctx.scale;
  ctx.computedMinH = util.Window.WIN_MIN_H * ctx.scale;
  ctx.computedMaxW = 0;
  ctx.computedMaxH = 0;
  ctx.titleH = ctx.fontSize + (util.Window.WIN_TITLE_H_MRGN * 2 * ctx.scale) - util.Window.WIN_BORDER * 2;

  if (opt.kiosk) {
    ctx.uiStatus |= util.Window.UI_ST_KIOSK;
  } else {
    if (ctx.computedMinW < opt.minWidth) ctx.computedMinW = opt.minWidth;
    if (ctx.computedMinH < opt.minHeight) ctx.computedMinH = opt.minHeight;
    if (ctx.computedMaxW < opt.maxWidth) ctx.computedMaxW = opt.maxWidth;
    if (ctx.computedMaxH < opt.maxHeight) ctx.computedMaxH = opt.maxHeight;
    if (opt.draggable !== false) ctx.uiStatus |= util.Window.UI_ST_DRAGGABLE;
    if (opt.resizable !== false) ctx.uiStatus |= util.Window.UI_ST_RESIZABLE;
  }

  ctx.winSizeBtn = null;
  ctx.win = this.createWin(ctx, opt);
  ctx.win.ctx = ctx;
  document.body.appendChild(ctx.win);
  ctx.moveToInitPos();
  ctx.initWidth = ctx.win.offsetWidth - util.Window.WIN_BORDER * 2;
  ctx.initHeight = ctx.win.offsetHeight - util.Window.WIN_BORDER * 2;
  ctx.setTitle(opt.title.text);
  ctx.body.innerHTML = opt.content;
  ctx.modal = null;
  if (opt.modal) ctx.modal = util.modal.show(ctx.win);
  if (util.Window.isKiosk(ctx)) ctx.kiosk();
  util.callFn(opt.oncreate, ctx);
  if (!opt.hidden) {
    ctx.active();
    ctx.show();
  }
};
util.Window.UI_ST_KIOSK = 1;
util.Window.UI_ST_DRAGGABLE = 1 << 1;
util.Window.UI_ST_DRAGGING = 1 << 2;
util.Window.UI_ST_RESIZABLE = 1 << 3;
util.Window.UI_ST_RESIZING = 1 << 4;
util.Window.UI_ST_RESIZING_N = 1 << 5;
util.Window.UI_ST_RESIZING_E = 1 << 6;
util.Window.UI_ST_RESIZING_S = 1 << 7;
util.Window.UI_ST_RESIZING_W = 1 << 8;
util.Window.UI_ST_SHOW = 1 << 9;
util.Window.UI_ST_POS_AUTO_ADJ = 1 << 10;
util.Window.UI_ST_RESIZING_ALL = util.Window.UI_ST_RESIZING | util.Window.UI_ST_RESIZING_N | util.Window.UI_ST_RESIZING_E | util.Window.UI_ST_RESIZING_S | util.Window.UI_ST_RESIZING_W;
util.Window.SIZE_ST_NORMAL = 0;
util.Window.SIZE_ST_FULL_W = 1 << 1;
util.Window.SIZE_ST_FULL_H = 1 << 2;
util.Window.SIZE_ST_LTD_MAX_W = 1 << 3;
util.Window.SIZE_ST_LTD_MAX_H = 1 << 4;
util.Window.SIZE_ST_FULL_WH = util.Window.SIZE_ST_FULL_W | util.Window.SIZE_ST_FULL_H;
util.Window.WIN_MIN_W = 100;
util.Window.WIN_MIN_H = 25;
util.Window.WIN_SHADOW = 8;
util.Window.WIN_BORDER = 1;
util.Window.RESIZE_BOX_SIZE = 6;
util.Window.WIN_TITLE_H_MRGN = 4;
util.Window.BASE_FONT_SIZE = 12;
util.Window.BASE_ZINDEX = 10000;
util.Window.CHR_WIN_FULL = '&#x25A1;';
util.Window.CHR_WIN_RSTR = '&#x2750;';
util.Window.DFLT_OPT = {
  name: '',
  group: '',
  width: 640,
  height: 400,
  minWidth: 0,
  minHeight: 0,
  maxWidth: 0,
  maxHeight: 0,
  pos: 'auto',
  adjX: 0,
  adjY: 0,
  draggable: true,
  resizable: true,
  maximize: true,
  kiosk: false,
  closeButton: true,
  className: '',
  scale: 1,
  hidden: false,
  modal: false,
  style: {},
  title: {
    text: '',
    fontSize: 12,
    className: '',
    style: {
      background: 'linear-gradient(150deg, rgba(0,32,255,0.8),rgba(0,82,255,0.8))',
      color: '#fff'
    },
    buttons: {
      style: {
        color: '#ddd'
      }
    }
  },
  body: {
    className: '',
    style: {
      background: 'linear-gradient(150deg, rgba(255,255,255, 0.95),rgba(255,255,255,0.6))'
    }
  },
  oncreate: null,
  onshow: null,
  onhide: null,
  onactive: null,
  oninactive: null,
  onbeforeclose: null,
  onclose: null,
  onstartmove: null,
  onmove: null,
  onstopmove: null,
  onstartresize: null,
  onresize: null,
  onendresize: null,
  onsizechanged: null,
  content: ''
};
util.Window.windows = [];
util.Window.cnt = 0;
util.Window.activeWinCtx = null;
util.Window.savedFunc = null;

util.Window.prototype = {
  createWin: function(ctx, opt) {
    var win = document.createElement('div');
    win.className = 'fadeout';
    var s = {
      display: 'inline-block',
      position: 'fixed',
      'box-sizing': 'content-box',
      border: '1px solid #888',
      padding: 0,
      'box-shadow': util.Window.WIN_SHADOW + 'px ' + util.Window.WIN_SHADOW + 'px 10px rgba(0,0,0,.2)',
      'z-index': util.Window.BASE_ZINDEX
    };
    util.setStyle(win, s);
    util.setStyle(win, {width: opt.width + 'px', height: opt.height + 'px'}, false);
    util.setStyle(win, opt.style);
    if (opt.className) win.className += ' ' + opt.className;

    var t = ctx.createTitleBar(ctx, opt);
    win.appendChild(t);

    var bb = ctx.createWinBodyBase(ctx, opt);
    win.appendChild(bb);

    var b = ctx.createWinBody();
    bb.appendChild(b);
    ctx.body = b;

    if (ctx.uiStatus & util.Window.UI_ST_RESIZABLE) ctx.setupResize(ctx, win);
    if (ctx.uiStatus & util.Window.UI_ST_DRAGGABLE) ctx.setupMove(ctx, t);

    win.addEventListener('mousedown', ctx.onMouseDown, {passive: true});
    win.addEventListener('touchstart', ctx.onTouchStart, true);
    return win;
  },
  createTitleBar: function(ctx, opt) {
    var fontSize = ctx.fontSize;
    var e = document.createElement('div');
    var s = {
      display: 'table',
      position: 'relative',
      'box-sizing': 'content-box',
      width: '100%',
      height: ctx.titleH + 'px',
      padding: 0,
      border: 'none',
      'table-layout': 'fixed',
      'white-space': 'nowrap',
      'font-size': fontSize + 'px',
      cursor: 'default'
    };
    util.setStyle(e, s);
    if (opt.title.style) util.setStyle(e, opt.title.style);
    if (opt.title.className) e.className = opt.title.className;

    var t = document.createElement('span');
    s = {
      display: 'table-cell',
      position: 'relative',
      width: '70%',
      left: '4px',
      'vertical-align': 'middle',
      overflow: 'hidden',
      'text-overflow': 'ellipsis',
      'font-size': fontSize + 'px'
    };
    util.setStyle(t, s);
    e.appendChild(t);
    ctx.title = t;

    var b = ctx.createTitleBtns(ctx, opt, fontSize);
    e.appendChild(b);

    if (util.Window.canMaximize(ctx)) e.ondblclick = ctx.onTitleBarDblClick;
    return e;
  },
  onTitleBarDblClick: function(e) {
    var ctx = util.Window.getContext(e.target);
    var f = (util.Window.isExpanded(ctx) ? ctx.restoreWin : ctx.fullWin);
    f(ctx);
  },
  createTitleBtns: function(ctx, opt, fontSize) {
    var bs = document.createElement('div');
    var s = {
      display: 'table',
      position: 'absolute',
      height: '100%',
      right: '6px',
      'vertical-align': 'middle',
      'font-size': fontSize + 'px'
    };
    util.setStyle(bs, s);

    if (util.Window.canMaximize(ctx)) {
      var b = ctx.createSizeBtn(ctx, opt, fontSize);
      ctx.winSizeBtn = b;
      bs.appendChild(b);
    }

    if (opt.closeButton) {
      b = ctx.createCloseBtn(ctx, opt, fontSize);
      bs.appendChild(b);
    }
    return bs;
  },
  createSizeBtn: function(ctx, opt) {
    var b = document.createElement('span');
    var s = {
      position: 'relative',
      'vertical-align': 'middle',
      top: (-4 * ctx.scale) + 'px',
      'font-size': (18 * ctx.scale) + 'px',
      cursor: 'pointer'
    };
    util.setStyle(b, s);
    var btnStyle = opt.title.buttons.style;
    util.setStyle(b, btnStyle);

    var fn = ctx.fullWin;
    var btn = util.Window.CHR_WIN_FULL;
    if (util.Window.isExpanded(ctx)) {
      fn = ctx.restoreWin;
      btn = util.Window.CHR_WIN_RSTR;
    }
    b.innerHTML = btn;
    b.onclick = fn;
    b.className = 'win-nomove';
    b.onmouseover = new Function('util.setStyle(this, \'color\', \'#fff\');');
    b.onmouseout = new Function('util.setStyle(this, \'color\', \'' + btnStyle.color + '\');');
    return b;
  },
  updateWinCtrlBtns: function(ctx) {
    if (!ctx.winSizeBtn) return;
    var fn = ctx.fullWin;
    var btn = util.Window.CHR_WIN_FULL;
    if (util.Window.isExpanded(ctx)) {
      fn = ctx.restoreWin;
      btn = util.Window.CHR_WIN_RSTR;
    }
    ctx.winSizeBtn.innerHTML = btn;
    ctx.winSizeBtn.onclick = fn;
  },
  createCloseBtn: function(ctx, opt) {
    var b = document.createElement('span');
    var s = {
      position: 'relative',
      'vertical-align': 'middle',
      top: (-4 * ctx.scale) + 'px',
      'margin-left': (8 * ctx.scale) + 'px',
      'font-size': (18 * ctx.scale) + 'px',
      cursor: 'pointer'
    };
    util.setStyle(b, s);
    var btnStyle = opt.title.buttons.style;
    util.setStyle(b, btnStyle);
    b.innerText = 'x';
    b.className = 'win-nomove';
    b.onclick = ctx.close;
    b.onmouseover = new Function('util.setStyle(this, \'color\', \'#faa\');');
    b.onmouseout = new Function('util.setStyle(this, \'color\', \'' + btnStyle.color + '\');');
    return b;
  },

  createWinBodyBase: function(ctx, opt) {
    var e = document.createElement('div');
    var s = {
      position: 'relative',
      'box-sizing': 'content-box',
      width: '100%',
      height: 'calc(100% - ' + ctx.titleH + 'px)',
      margin: 0,
      padding: 0,
      border: 'none',
      overflow: 'auto'
    };
    util.setStyle(e, s);
    if (opt.body.style) util.setStyle(e, opt.body.style);
    if (opt.body.className) e.className = opt.body.className;
    return e;
  },
  createWinBody: function() {
    var e = document.createElement('div');
    var s = {
      position: 'relative',
      width: '100%',
      height: '100%'
    };
    util.setStyle(e, s);
    return e;
  },

  setupResize: function(ctx, win) {
    var rsN = ctx.createResizeArea(ctx, 'ns-resize', util.Window.UI_ST_RESIZING_N, '100%', '6px');
    rsN.style.top = '-3px';
    rsN.style.left = '0';
    win.appendChild(rsN);

    var rsE = ctx.createResizeArea(ctx, 'ew-resize', util.Window.UI_ST_RESIZING_E, '6px', '100%');
    rsE.style.top = '0';
    rsE.style.right = '-3px';
    win.appendChild(rsE);

    var rsS = ctx.createResizeArea(ctx, 'ns-resize', util.Window.UI_ST_RESIZING_S, '100%', '6px');
    rsS.style.bottom = '-3px';
    rsS.style.left = '0';
    win.appendChild(rsS);

    var rsSE = ctx.createResizeArea(ctx, 'nwse-resize', util.Window.UI_ST_RESIZING_S | util.Window.UI_ST_RESIZING_E, '6px', '6px');
    rsSE.style.bottom = '-3px';
    rsSE.style.right = '-3px';
    win.appendChild(rsSE);

    var rsW = ctx.createResizeArea(ctx, 'ew-resize', util.Window.UI_ST_RESIZING_W, '6px', '100%');
    rsW.style.top = '0';
    rsW.style.left = '-3px';
    win.appendChild(rsW);

    var rsNW = ctx.createResizeArea(ctx, 'nwse-resize', util.Window.UI_ST_RESIZING_N | util.Window.UI_ST_RESIZING_W, '6px', '6px');
    rsNW.style.top = '-3px';
    rsNW.style.left = '-3px';
    win.appendChild(rsNW);

    var rsNE = ctx.createResizeArea(ctx, 'nesw-resize', util.Window.UI_ST_RESIZING_N | util.Window.UI_ST_RESIZING_E, '6px', '6px');
    rsNE.style.top = '-3px';
    rsNE.style.right = '-3px';
    win.appendChild(rsNE);

    var rsSW = ctx.createResizeArea(ctx, 'nesw-resize', util.Window.UI_ST_RESIZING_S | util.Window.UI_ST_RESIZING_W, '6px', '6px');
    rsSW.style.bottom = '-3px';
    rsSW.style.left = '-3px';
    win.appendChild(rsSW);
  },

  createResizeArea: function(ctx, cursor, state, width, height) {
    var el = document.createElement('div');
    var s = {
      position: 'absolute',
      width: width,
      height: height,
      background: 'rgba(0,0,0,0)',
      cursor: cursor
    };
    util.setStyle(el, s);
    el.onmousedown = function(e) {
      util.Window.onActive(ctx);
      if (!(ctx.uiStatus & util.Window.UI_ST_RESIZABLE)) return;
      if (e.button != 0) return;
      ctx.startResize(ctx, e);
      ctx.uiStatus |= state;
      ctx.cursor = ctx.win.style.cursor;
      ctx.win.style.cursor = cursor;
    };
    return el;
  },

  setupMove: function(ctx, el) {
    el.addEventListener('mousedown', ctx.onTitleMouseDown, {passive: true});
    el.addEventListener('touchstart', ctx.onTitleTouchStart, true);
  },

  onMouseDown: function(e) {
    var ctx = util.Window.getContext(e.target);
    if (!ctx) return;
    util.Window.onActive(ctx);
  },

  onTitleMouseDown: function(e) {
    var ctx = util.Window.getContext(e.target);
    if (!ctx) return;
    var x = e.clientX;
    var y = e.clientY;
    var el = e.target;
    if (e.button != 0) return;
    if (!(ctx.uiStatus & util.Window.UI_ST_DRAGGABLE) || !util.Window.isMovable(el)) return;
    ctx.startMove(ctx, x, y);
  },
  onTitleTouchStart: function(e) {
    var ctx = util.Window.getContext(e.target);
    if (!ctx) return;
    var e0 = e.changedTouches[0];
    var x = e0.clientX;
    var y = e0.clientY;
    var el = e.target;
    if (!(ctx.uiStatus & util.Window.UI_ST_DRAGGABLE) || !util.Window.isMovable(el)) return;
    ctx.startMove(ctx, x, y);
    e.preventDefault();
  },
  startMove: function(ctx, x, y) {
    util.Window.disableTextSelect();
    ctx.uiStatus |= util.Window.UI_ST_DRAGGING;
    ctx.ptOfstY = y - ctx.win.offsetTop;
    ctx.ptOfstX = x - ctx.win.offsetLeft;
    if (!document.all) window.getSelection().removeAllRanges();
    x = util.getPxVal(ctx.win.style.left);
    y = util.getPxVal(ctx.win.style.top);
    util.callFn(ctx.opt.onstartmove, ctx, x, y);
  },
  moveWin: function(ctx, pX, pY) {
    ctx.uiStatus &= ~util.Window.UI_ST_POS_AUTO_ADJ;
    var clH = document.documentElement.clientHeight;
    if (pX < 0) pX = 0;
    if (pY < 0) pY = 0;
    if (pY > clH) pY = clH;
    var x = pX - ctx.ptOfstX;
    var y = pY - ctx.ptOfstY;
    ctx.move(x, y);
  },
  onPointerMove: function(ctx, x, y) {
    if (ctx.uiStatus & util.Window.UI_ST_DRAGGING) ctx.moveWin(ctx, x, y);
    if (ctx.uiStatus & util.Window.UI_ST_RESIZING) ctx.resizeWin(ctx, x, y);
  },
  endMove: function(ctx) {
    ctx.uiStatus &= ~util.Window.UI_ST_DRAGGING;
    util.Window.enableTextSelect();
    var x = util.getPxVal(ctx.win.style.left);
    var y = util.getPxVal(ctx.win.style.top);
    util.callFn(ctx.opt.onstartmove, ctx, x, y);
  },
  resetSize: function() {
    this.win.style.width = this.initWidth + 'px';
    this.win.style.height = this.initHeight + 'px';
  },
  fullWin: function(ctx) {
    if (!ctx) ctx = this;
    if (!util.Window.isContext(ctx)) ctx = util.Window.getContext(this);
    ctx.saveSizeAndPos(ctx);
    ctx._fullWin(ctx);
  },
  _fullWin: function(ctx) {
    var w = document.documentElement.clientWidth;
    var h = document.documentElement.clientHeight;
    if ((ctx.computedMaxW > 0) && (w > ctx.computedMaxW)) {
      ctx.sizeStatus |= util.Window.SIZE_ST_LTD_MAX_W;
      w = ctx.computedMaxW;
    }
    if ((ctx.computedMaxH > 0) && (h > ctx.computedMaxH)) {
      ctx.sizeStatus |= util.Window.SIZE_ST_LTD_MAX_H;
      h = ctx.computedMaxH;
    }
    ctx.setWinSize(ctx, w, h);

    if (util.Window.isLtdMax(ctx)) {
      if (!(ctx.sizeStatus & util.Window.SIZE_ST_LTD_MAX_W)) ctx.win.style.left = '0px';
      if (!(ctx.sizeStatus & util.Window.SIZE_ST_LTD_MAX_H)) ctx.win.style.top = '0px';
    } else {
      ctx.setWinPos(ctx, 0, 0);
      ctx.uiStatus &= ~util.Window.UI_ST_POS_AUTO_ADJ;
      ctx.sizeStatus |= util.Window.SIZE_ST_FULL_WH;
      ctx.disableDraggable(ctx);
      ctx.disableResize(ctx);
    }
    ctx.updateWinCtrlBtns(ctx);
    ctx.onSizeChanged(ctx, 'maximized');
  },
  kiosk: function(ctx) {
    if (!ctx) ctx = this;
    ctx.fullWin(ctx);
  },

  restoreWin: function(ctx) {
    if (!ctx) ctx = this;
    if (!util.Window.isContext(ctx)) ctx = util.Window.getContext(this);
    ctx._restoreWin(ctx);
  },
  _restoreWin: function(ctx) {
    var org = ctx.orgSizePos;
    ctx.enableDraggable(ctx);
    ctx.enableResize(ctx);
    ctx.setWinSize(ctx, org.w, org.h);
    if (!util.Window.isLtdMaxWH(ctx)) ctx.setWinPos(ctx, org.l, org.t);
    ctx.sizeStatus = util.Window.SIZE_ST_NORMAL;
    if (ctx.uiStatus & util.Window.UI_ST_POS_AUTO_ADJ) ctx.moveToInitPos(ctx);
    ctx.updateWinCtrlBtns(ctx);
    ctx.onSizeChanged(ctx, 'restored');
  },

  adjustWinMax: function(ctx) {
    if (ctx.sizeStatus & util.Window.SIZE_ST_FULL_W) {
      ctx.win.style.width = document.documentElement.clientWidth + 'px';
    }
    if (ctx.sizeStatus & util.Window.SIZE_ST_FULL_H) {
      ctx.win.style.height = document.documentElement.clientHeight + 'px';
    }
  },

  resetPos: function() {
    this.moveToInitPos();
  },
  resetWindow: function() {
    this.resetSize();
    this.resetPos();
    this.uiStatus |= util.Window.UI_ST_POS_AUTO_ADJ;
  },
  moveToInitPos: function() {
    var ctx = this;
    var n = util.Window.count() * 15;
    var pos = ctx.opt.pos;
    if (pos == 'auto') {
      ctx.move(n, n);
    } else if (typeof pos == 'string') {
      ctx.setWinPos2(ctx, pos);
    } else {
      ctx.move(pos.x, pos.y);
    }
  },
  setWinSize: function(ctx, w, h) {
    ctx.win.style.width = w + 'px';
    ctx.win.style.height = h + 'px';
  },
  setWinPos: function(ctx, x, y) {
    ctx.win.style.top = y + 'px';
    ctx.win.style.left = x + 'px';
  },
  setWinPos2: function(ctx, pos) {
    var opt = ctx.opt;
    var top = opt.adjY;
    var left = opt.adjX;
    var clW = document.documentElement.clientWidth;
    var clH = document.documentElement.clientHeight;
    if (clW > window.outerWidth) clW = window.outerWidth;
    if (clH > window.outerHeight) clH = window.outerHeight;
    var sp = ctx.getSelfSizePos(ctx);
    var winW = sp.w;
    var winH = sp.h;
    switch (pos) {
      case 'se':
        top = clH - winH + opt.adjY;
        left = clW - winW + opt.adjX;
        break;
      case 'ne':
        top = opt.adjY;
        left = clW - winW + opt.adjX;
        break;
      case 'c':
        top = (clH / 2) - (winH / 2) + opt.adjY;
        left = (clW / 2) - (winW / 2) + opt.adjX;
        break;
      case 'sw':
        top = clH - winH + opt.adjY;
        left = opt.adjX;
        break;
      case 'n':
        top = opt.adjY;
        left = (clW / 2) - (winW / 2);
        break;
      case 'e':
        top = (clH / 2) - (winH / 2);
        left = clW - winW + opt.adjX;
        break;
      case 's':
        top = clH - winH + opt.adjY;
        left = (clW / 2) - (winW / 2);
        break;
      case 'w':
        top = (clH / 2) - (winH / 2);
        left = opt.adjX;
    }
    ctx.setWinPos(ctx, left, top);
  },
  getSelfSizePos: function(ctx) {
    var rect = ctx.win.getBoundingClientRect();
    var resizeBoxSize = util.Window.RESIZE_BOX_SIZE;
    var sp = {};
    sp.w = ctx.win.clientWidth;
    sp.h = ctx.win.clientHeight;
    sp.x1 = rect.left - resizeBoxSize / 2;
    sp.y1 = rect.top - resizeBoxSize / 2;
    sp.x2 = sp.x1 + ctx.win.clientWidth + resizeBoxSize + util.Window.WIN_BORDER;
    sp.y2 = sp.y1 + ctx.win.clientHeight + resizeBoxSize + util.Window.WIN_BORDER;
    return sp;
  },

  saveSizeAndPos: function(ctx) {
    var o = ctx.orgSizePos;
    ctx.saveSize(ctx, o);
    ctx.savePos(ctx, o);
  },
  saveSize: function(ctx, o) {
    var shadow = util.Window.WIN_SHADOW / 2;
    o.w = (ctx.win.offsetWidth + (util.Window.WIN_BORDER * 2) - shadow);
    o.h = (ctx.win.offsetHeight + (util.Window.WIN_BORDER * 2) - shadow);
  },
  savePos: function(ctx, o) {
    o.t = ctx.win.offsetTop;
    o.l = ctx.win.offsetLeft;
  },

  enableDraggable: function(ctx) {
    ctx.uiStatus |= util.Window.UI_ST_DRAGGABLE;
  },
  disableDraggable: function(ctx) {
    ctx.uiStatus &= ~util.Window.UI_ST_DRAGGABLE;
  },

  enableResize: function(ctx) {
    ctx.uiStatus |= util.Window.UI_ST_RESIZABLE;
  },
  disableResize: function(ctx) {
    ctx.uiStatus &= ~util.Window.UI_ST_RESIZABLE;
  },

  startResize: function(ctx, e) {
    ctx.uiStatus |= util.Window.UI_ST_RESIZING;
    ctx.clickedPosX = e.clientX;
    ctx.clickedPosY = e.clientY;
    ctx.saveSizeAndPos(ctx);
    util.Window.disableTextSelect();
    var w = util.getPxVal(ctx.win.style.width);
    var h = util.getPxVal(ctx.win.style.height);
    util.callFn(ctx.opt.onstartresize, ctx, w, h);
  },

  resizeWin: function(ctx, x, y) {
    if (util.Window.isLtdMax(ctx)) {
      ctx.sizeStatus &= ~util.Window.SIZE_ST_LTD_MAX_W;
      ctx.sizeStatus &= ~util.Window.SIZE_ST_LTD_MAX_H;
      ctx.updateWinCtrlBtns(ctx);
    }
    var currentX = x;
    var currentY = y;
    var mvX, mvY, t, l, w, h;
    var clW = document.documentElement.clientWidth;
    var clH = document.documentElement.clientHeight;

    if (currentX > clW) {
      currentX = clW;
    } else if (currentX < 0) {
      currentX = 0;
    }

    if (currentY > clH) {
      currentY = clH;
    } else if (currentY < 0) {
      currentY = 0;
    }

    if (ctx.uiStatus & util.Window.UI_ST_RESIZING_N) {
      mvY = ctx.clickedPosY - currentY;
      h = ctx.orgSizePos.h + mvY;
      if (h < ctx.computedMinH) {
        t = ctx.orgSizePos.t - (ctx.computedMinH - ctx.orgSizePos.h);
        h = ctx.computedMinH;
      } else if ((ctx.computedMaxH > 0) && (h > ctx.computedMaxH)) {
        t = ctx.orgSizePos.t - (ctx.computedMaxH - ctx.orgSizePos.h);
        h = ctx.computedMaxH;
      } else {
        t = ctx.orgSizePos.t - mvY;
      }
    }

    if (ctx.uiStatus & util.Window.UI_ST_RESIZING_W) {
      mvX = ctx.clickedPosX - currentX;
      w = ctx.orgSizePos.w + mvX;
      if (w < ctx.computedMinW) {
        l = ctx.orgSizePos.l - (ctx.computedMinW - ctx.orgSizePos.w);
        w = ctx.computedMinW;
      } else if ((ctx.computedMaxW > 0) && (w > ctx.computedMaxW)) {
        l = ctx.orgSizePos.l - (ctx.computedMaxW - ctx.orgSizePos.w);
        w = ctx.computedMaxW;
      } else {
        l = ctx.orgSizePos.l - mvX;
      }
    }

    if (ctx.uiStatus & util.Window.UI_ST_RESIZING_E) {
      mvX = currentX - ctx.clickedPosX;
      w = ctx.orgSizePos.w + mvX;
      if (w < ctx.computedMinW) {
        w = ctx.computedMinW;
      } else if ((ctx.computedMaxW > 0) && (w > ctx.computedMaxW)) {
        w = ctx.computedMaxW;
      }
    }

    if (ctx.uiStatus & util.Window.UI_ST_RESIZING_S) {
      mvY = currentY - ctx.clickedPosY;
      h = ctx.orgSizePos.h + mvY;
      if (ctx.initHeight < ctx.computedMinH) {
        if (h < ctx.initHeight) h = ctx.initHeight;
      } else if (h < ctx.computedMinH) {
        h = ctx.computedMinH;
      } else if ((ctx.computedMaxH > 0) && (h > ctx.computedMaxH)) {
        h = ctx.computedMaxH;
      }
    }
    if (t != undefined) ctx.win.style.top = t + 'px';
    if (l != undefined) ctx.win.style.left = l + 'px';
    if (w != undefined) ctx.win.style.width = w + 'px';
    if (h != undefined) ctx.win.style.height = h + 'px';
    if (w == undefined) w = util.getPxVal(ctx.win.style.width);
    if (h == undefined) h = util.getPxVal(ctx.win.style.height);
    util.callFn(ctx.opt.onresize, ctx, w, h);
  },
  onResize: function(ctx) {
    if (ctx.uiStatus & util.Window.UI_ST_POS_AUTO_ADJ) {
      ctx.moveToInitPos();
    } else {
      ctx.adjustWinMax(ctx);
    }
  },
  onSizeChanged: function(ctx, st) {
    var w = util.getPxVal(ctx.win.style.width);
    var h = util.getPxVal(ctx.win.style.height);
    util.callFn(ctx.opt.onsizechanged, ctx, st);
    util.callFn(ctx.opt.onresize, ctx, w, h);
  },
  show: function() {
    var ctx = this;
    if (!(ctx.uiStatus & util.Window.UI_ST_SHOW)) {
      ctx.uiStatus |= util.Window.UI_ST_SHOW;
      util.fadeIn(ctx.win, 200);
      util.callFn(ctx.opt.onshow, ctx);
    }
  },
  hide: function() {
    var ctx = this;
    if (ctx.uiStatus & util.Window.UI_ST_SHOW) {
      ctx.uiStatus &= ~util.Window.UI_ST_SHOW;
      util.fadeOut(ctx.win, 200, ctx._hide, ctx);
    }
  },
  _hide: function(ctx) {
    util.callFn(ctx.opt.onhide, ctx);
  },
  move: function(x, y) {
    var ctx = this;
    if (x) ctx.win.style.left = x + 'px';
    if (y) ctx.win.style.top = y + 'px';
    if (x == undefined) x = util.getPxVal(ctx.win.style.left);
    if (y == undefined) y = util.getPxVal(ctx.win.style.top);
    util.callFn(ctx.opt.onmove, ctx, x, y);
  },
  size: function(w, h) {
    var ctx = this;
    if (w) {
      if (w < ctx.computedMinW) {
        w = ctx.computedMinW;
      } else if ((ctx.computedMaxW > 0) && (h > ctx.computedMaxW)) {
        w = ctx.computedMaxW;
      }
      ctx.win.style.width = w + 'px';
    }
    if (h) {
      if (h < ctx.computedMinH) {
        h = ctx.computedMinH;
      } else if ((ctx.computedMaxH > 0) && (h > ctx.computedMaxH)) {
        h = ctx.computedMaxH;
      }
      ctx.win.style.height = h + 'px';
    }
  },
  endResize: function(ctx) {
    ctx.uiStatus &= ~util.Window.UI_ST_RESIZING_ALL;
    ctx.win.style.cursor = ctx.cursor;
    util.Window.enableTextSelect(ctx);
    var w = util.getPxVal(ctx.win.style.width);
    var h = util.getPxVal(ctx.win.style.height);
    util.callFn(ctx.opt.onendresize, ctx, w, h);
  },
  getElement: function() {
    return this.win;
  },
  active: function() {
    util.Window.onActive(this);
  },
  getTitle: function() {
    return this.title.innerText;
  },
  setTitle: function(v) {
    this.title.innerText = v;
  },
  getBodyElement: function() {
    return this.body;
  },
  appendElement: function(el) {
    el = util.getElement(el);
    if (el) this.body.appendChild(el);
  },
  removeElement: function(el) {
    el = util.getElement(el);
    if (el && util.hasChild(this.body, el)) this.body.removeChild(el);
  },
  draw: function(v) {
    this.body.innerHTML = v;
  },
  getSize: function() {
    return {w: this.win.clientWidth, h: this.win.clientHeight};
  },
  getPos: function() {
    var ctx = this;
    var rect = ctx.win.getBoundingClientRect();
    var rszbox = util.Window.RESIZE_BOX_SIZE;
    var x1 = rect.left - rszbox / 2;
    var y1 = rect.top - rszbox / 2;
    var x2 = x1 + ctx.win.clientWidth + rszbox + util.Window.WIN_BORDER;
    var y2 = y1 + ctx.win.clientHeight + rszbox + util.Window.WIN_BORDER;
    return {x1: x1, y1: y1, x2: x2, y2: y2};
  },
  close: function(f) {
    var ctx = this;
    if (!util.Window.isContext(ctx)) ctx = util.Window.getContext(this);
    if ((f == true) || (util.callFn(ctx.opt.onbeforeclose, ctx) !== false)) {
      util.modal.hide(ctx.modal);
      util.fadeOut(ctx.win, 200, ctx._close, ctx);
    }
  },
  _close: function(ctx) {
    util.callFn(ctx.opt.oninactive, ctx);
    util.callFn(ctx.opt.onclose, ctx);
    try {
      document.body.removeChild(ctx.win);
    } catch (e) {}
    ctx.finalize(ctx);
  },
  finalize: function(ctx) {
    util.Window.unregisterWindow(ctx);
    ctx.win.ctx = null;
    ctx.win = null;
  }
};

util.Window.registerWindow = function(ctx) {
  util.Window.cnt++;
  util.Window.windows.push(ctx);
};
util.Window.unregisterWindow = function(ctx) {
  for (var i = 0; i < util.Window.windows.length; i++) {
    var w = util.Window.windows[i];
    if (w == ctx) {
      util.Window.windows.splice(i, 1);
      break;
    }
  }
};
util.Window.count = function() {
  return util.Window.windows.length;
};
util.Window.onActive = function(ctx) {
  if (util.Window.activeWinCtx == ctx) return;
  util.Window.activeWinCtx = ctx;
  var o = util.Window.reorder(ctx);
  if (o.length >= 2) {
    var lo = o[o.length - 2];
    util.callFn(lo.opt.oninactive, lo);
  }
  util.callFn(ctx.opt.onactive, ctx);
};
util.Window.reorder = function(topCtx) {
  var wins = [];
  for (var i = 0; i < util.Window.windows.length; i++) {
    var w = util.Window.windows[i];
    if (w != topCtx) wins.push(w);
  }
  wins.unshift(topCtx);
  var z = util.Window.BASE_ZINDEX + wins.length;
  for (i = 0; i < wins.length; i++) {
    wins[i].win.style.zIndex = z;
    z--;
  }
  util.Window.windows = wins;
  return wins;
};

util.Window.disableTextSelect = function() {
  util.Window.savedFunc = document.onselectstart;
  document.onselectstart = function() {return false;};
};
util.Window.enableTextSelect = function() {
  document.onselectstart = util.Window.savedFunc;
};
util.Window.isMovable = function(el) {
  if ((el.nodeName == 'INPUT') || (el.nodeName == 'TEXTAREA') || (util.hasClass(el, 'win-nomove'))) return false;
  return true;
};

util.Window.onMouseMove = function(x, y) {
  var ctx = util.Window.activeWinCtx;
  if (ctx) ctx.onPointerMove(ctx, x, y);
};

util.Window.onPointerUp = function() {
  var ctx = util.Window.activeWinCtx;
  if (!ctx) return;
  if (ctx.uiStatus & util.Window.UI_ST_DRAGGING) ctx.endMove(ctx);
  if (ctx.uiStatus & util.Window.UI_ST_RESIZING) ctx.endResize(ctx);
};

util.Window.onResize = function(e) {
  for (var i = 0; i < util.Window.windows.length; i++) {
    var ctx = util.Window.windows[i];
    ctx.onResize(ctx, e);
  }
};

util.Window.isContext = function(x) {
  return ((Object.prototype.toString.call(x) == '[object Object]') && (x._type_ == 'WIN'));
};
util.Window.getContext = function(el) {
  do {
    if (el.ctx) return el.ctx;
    el = el.parentNode;
  } while (el != null);
  return null;
};
util.Window.canMaximize = function(ctx) {
  return (ctx.opt.resizable && ctx.opt.maximize && !util.Window.isKiosk(ctx));
};
util.Window.isKiosk = function(ctx) {
  return ((ctx.uiStatus & util.Window.UI_ST_KIOSK) ? true : false);
};
util.Window.isExpanded = function(ctx) {
  return (((ctx.sizeStatus & util.Window.SIZE_ST_FULL_WH) || util.Window.isLtdMax(ctx)) ? true : false);
};
util.Window.isLtdMax = function(ctx) {
  return (((ctx.sizeStatus & util.Window.SIZE_ST_LTD_MAX_W) || (ctx.sizeStatus & util.Window.SIZE_ST_LTD_MAX_H)) ? true : false);
};
util.Window.isLtdMaxWH = function(ctx) {
  return (((ctx.sizeStatus & util.Window.SIZE_ST_LTD_MAX_W) && (ctx.sizeStatus & util.Window.SIZE_ST_LTD_MAX_H)) ? true : false);
};
util.Window.getById = function(id) {
  for (var i = 0; i < util.Window.windows.length; i++) {
    var w = util.Window.windows[i];
    if (w.id == id) return w;
  }
  return null;
};
util.Window.getByName = function(n) {
  for (var i = 0; i < util.Window.windows.length; i++) {
    var w = util.Window.windows[i];
    if (w.name == n) return w;
  }
  return null;
};
util.Window.getByGroup = function(g) {
  var a = [];
  for (var i = 0; i < util.Window.windows.length; i++) {
    var w = util.Window.windows[i];
    if (w.group == g) a.push(w);
  }
  return a;
};
util.Window.getAll = function() {
  var a = [];
  for (var i = 0; i < util.Window.windows.length; i++) {
    a.push(util.Window.windows[i]);
  }
  return a;
};
util.Window.closeByGroup = function(g) {
  for (var i = 0; i < util.Window.windows.length; i++) {
    var w = util.Window.windows[i];
    if (w.group == g) w.close(true);
  }
};
util.Window.closeAll = function() {
  for (var i = 0; i < util.Window.windows.length; i++) {
    util.Window.windows[i].close(true);
  }
};

//---------------------------------------------------------
// Modal
//---------------------------------------------------------
util.MODAL_ZINDEX = util.SYSTEM_ZINDEX_BASE;
util.modal = function(child, addCloseHandler) {
  this.sig = 'modal';
  var el = document.createElement('div');
  var style = util.copyObject(util.modal.DFLT_STYLE);
  if (util.modal.style) util.copyObject(util.modal.style, style);
  util.setStyle(el, style);
  el.style.opacity = '0';
  if (addCloseHandler) el.addEventListener('click', this.onClick);
  if (child) el.appendChild(child);
  el.ctx = this;
  this.el = el;
};
util.modal.prototype = {
  show: function() {
    var el = this.el;
    document.body.appendChild(el);
    util.fadeIn(el, 200);
    return this;
  },
  hide: function() {
    var el = this.el;
    var ctx = el.ctx;
    if (!ctx.closing) {
      ctx.closing = true;
      util.fadeOut(el, 200, ctx._hide, el);
    }
  },
  _hide: function(el) {
    document.body.removeChild(el);
  },
  appendChild: function(el) {
    this.el.appendChild(el);
  },
  removeChild: function(el) {
    this.el.removeChild(el);
  },
  getElement: function() {
    return this.el;
  },
  onClick: function(e) {
    var el = e.target;
    if (el.ctx && (el.ctx.sig == 'modal')) el.ctx.hide();
  }
};
util.modal.show = function(el, closeAnywhere) {
  var m = new util.modal(el, closeAnywhere).show();
  util.modal.ctxs.push(m);
  return m;
};
util.modal.hide = function(m) {
  if (m) {
    var f = 0;
    for (var i = 0; i < util.modal.ctxs.length; i++) {
      if (m == util.modal.ctxs[i]) {
        f = 1;break;
      }
    }
    if (f) util.modal.ctxs.splice(i, 1);
  } else {
    m = util.modal.ctxs.pop();
  }
  if (m) m.hide();
};
util.modal.setStyle = function(s) {
  util.modal.style = s;
};
util.modal.DFLT_STYLE = {
  position: 'fixed',
  top: '0',
  left: '0',
  'min-width': '100vw',
  'min-height': '100vh',
  width: '100%',
  height: '100%',
  background: 'rgba(0,0,0,0.6)',
  'z-index': util.MODAL_ZINDEX
};
util.modal.style = null;
util.modal.ctxs = [];

//---------------------------------------------------------
// Dialog
//---------------------------------------------------------
/**
 * content: HTML|DOM
 *
 * opt = {
 *   title: 'Title',
 *   buttons = [
 *     {label: 'Yes', cb: function1},
 *     {label: 'No', cb: function2}
 *   ],
 *   style: {
 *     body: {
 *       name: value,
 *       ...
 *     },
 *     title: {
 *       ...
 *     },
 *     content: {
 *       ...
 *     },
 *     button: {
 *       ...
 *     }
 *   },
 *   closeAnywhere: true|false
 *   data: object
 * }
 *
 * style:
 *  dialog
 *  dialog-title
 *  dialog-content
 *  dialog-button
 */
util.dialog = function(content, opt) {
  var ctx = this;
  ctx.opt = opt;
  var o = ctx.createDialogBody(ctx, content, opt);
  ctx.el = ctx.create(ctx, o.boby, opt);
  ctx.btnEls = o.btnEls;
  ctx.open = true;

  var closeAnywhere = false;
  if (opt) {
    if (opt.closeAnywhere) closeAnywhere = true;
  }

  ctx.modal = util.modal.show(ctx.el, closeAnywhere);
  setTimeout(util.dialog.focusBtn, 10);
};
util.dialog.prototype = {
  create: function(ctx, body, opt) {
    var base = document.createElement('div');
    base.className = 'dialog';
    var style = {
      'display': 'table',
      'position': 'fixed',
      'border-radius': '3px',
      'padding': util.dialog.PADDING + 'px',
      'z-index': util.SYSTEM_ZINDEX_BASE + 1
    };
    util.setStyle(base, style);

    if (opt && opt.style && opt.style.body) {
      for (var key in opt.style.body) {
        util.setStyle(base, key, opt.style.body[key]);
      }
    }
    base.appendChild(body);

    setTimeout(util.dialog.show, 0);
    base.style.opacity = 0;
    return base;
  },

  createDialogBody: function(ctx, content, opt) {
    var body = document.createElement('div');
    body.className = 'dialog-body';
    var style = {
      'display': 'table-cell',
      'vertical-align': 'middle',
      'text-align': 'center'
    };
    util.setStyle(body, style);
    var title;
    var buttons;
    if (opt) {
      title = opt.title;
      buttons = opt.buttons;
    }
    if (title) {
      var titleArea = document.createElement('div');
      titleArea.className = 'dialog-title';
      titleArea.innerHTML = title;
      style = {
        'margin-bottom': '0.5em',
        'font-weight': 'bold'
      };
      util.setStyle(titleArea, style);
      if (opt && opt.style && opt.style.title) {
        for (var key in opt.style.title) {
          util.setStyle(titleArea, key, opt.style.title[key]);
        }
      }
      body.appendChild(titleArea);
    }

    var contentArea = document.createElement('pre');
    contentArea.className = 'dialog-content';
    style = (title ? {'margin': '0'} : {'margin': '10px 0'});
    util.setStyle(contentArea, style);

    if (typeof content == 'string') {
      contentArea.innerHTML = content;
    } else {
      contentArea.appendChild(content);
    }
    if (opt && opt.style && opt.style.content) {
      for (key in opt.style.content) {
        util.setStyle(contentArea, key, opt.style.content[key]);
      }
    }
    body.appendChild(contentArea);

    var btnEls = [];
    if (buttons) {
      for (var i = 0; i < buttons.length; i++) {
        var button = buttons[i];
        var btnEl = document.createElement('button');
        style = {'margin-top': '1em', 'margin-bottom': '0'};
        if (i > 0) style['margin-left'] = '0.5em';
        util.setStyle(btnEl, style);
        if (opt && opt.style && opt.style.button) {
          for (key in opt.style.button) {
            util.setStyle(btnEl, key, opt.style.button[key]);
          }
        }
        btnEl.className = 'dialog-button dialog-button-' + i;
        btnEl.addEventListener('click', util.dialog.btnCb);
        btnEl.innerText = button.label;
        btnEl.cb = button.cb;
        btnEl.ctx = ctx;
        body.appendChild(btnEl);
        if (button.focus) util.dialog.initFocusEl = btnEl;
        btnEls.push(btnEl);
      }
    }
    if (opt.focusEl) util.dialog.initFocusEl = opt.focusEl;
    return {boby: body, btnEls: btnEls};
  },

  pressButton: function(n) {
    if (this.open) {
      var b = this.btnEls[n];
      if (b) util.dialog.btnCb({target: b});
    }
  },

  close: function(ctx) {
    ctx.modal.removeChild(ctx.el);
    util.modal.hide(ctx.modal);
    ctx.open = false;
  },

  center: function(ctx) {
    util.center(ctx.el);
  }
};
util.dialog.PADDING = 10;
util.dialog.instances = [];
util.dialog.initFocusEl = null;
util.dialog.focusBtn = function() {
  if (util.dialog.initFocusEl) {
    util.dialog.initFocusEl.focus();
    util.dialog.initFocusEl = null;
  }
};
util.dialog.adjust = function() {
  var d = util.dialog.get();
  if (d) d.center(d);
};
util.dialog.show = function() {
  var d = util.dialog.get();
  d.el.style.opacity = 1;
  util.dialog.adjust();
};

// opt = {
//   title: 'Title',
//   buttons = [
//     {label: 'Yes', focus: true, cb: cbYes},
//     ...
//   ],
//   style: {
//     styles
//   },
//   modal: {
//     closeAnywhere: true|false,
//     style: {
//       name: value,
//       ...
//     }
//   },
//   data: object
// }
util.dialog.open = function(content, opt) {
  var DEFAULT_STYLE = {
    'min-width': '180px',
    'min-height': '50px',
    'text-align': 'center'
  };
  if (!opt) opt = {};
  if (!opt.style) opt.style = {body: DEFAULT_STYLE};
  var dialog = new util.dialog(content, opt);
  util.dialog.instances.push(dialog);
  return dialog;
};

util.dialog.btnCb = function(e) {
  var el = e.target;
  util.dialog.instances.pop();
  util.dialog.btnHandler(el);
};

util.dialog.btnHandler = function(el) {
  var ctx = el.ctx;
  var data;
  if (ctx.opt) data = ctx.opt.data;
  var f = true;
  if (el.cb) {
    f = el.cb(data);
    if (f !== false) f = true;
  }
  if (f) ctx.close(ctx);
};

util.dialog.close = function(btnIdx) {
  var d = util.dialog.instances.pop();
  if (!d) return;
  if (btnIdx == undefined) {
    d.close(d);
  } else {
    var b = d.btnEls[btnIdx];
    if (b) {
      util.dialog.btnHandler(b);
    } else {
      d.close(d);
    }
  }
};
util.dialog.closeAll = function() {
  var n = util.dialog.count();
  for (var i = 0; i < n; i++) {
    util.dialog.close();
  }
};

util.dialog.get = function(idx) {
  if (idx == undefined) idx = util.dialog.instances.length - 1;
  if (idx >= util.dialog.instances.length) return null;
  return util.dialog.instances[idx];
};

util.dialog.count = function() {
  return util.dialog.instances.length;
};

util.dialog.pressButton = function(n) {
  var d = util.dialog.get();
  if (d) d.pressButton(n);
};

//-----------------------------------------------
/**
 * util.dialog.info('message');
 * util.dialog.info('title', 'message');
 * util.dialog.info('message', cb);
 * util.dialog.info('title', 'message', cb);
 * util.dialog.info('message', opt);
 * util.dialog.info('title', 'message', opt);
 * util.dialog.info('message', cb, opt);
 * util.dialog.info('title', 'message', cb, opt);
 *
 * opt = {
 *   focus: 'yes'|'no',
 *   data: object,
 *   style: {
 *     ...
 *   }
 * };
 *
 * cb = function(data) {}
 *
 * class:
 *  dialog
 *  dialog-title
 *  dialog-content
 *  dialog-button
 */
util.dialog.info = function(a1, a2, a3, a4) {
  var a = [a1, a2, a3, a4];
  var title;
  var msg = a1;
  var cb = (typeof a2 == 'function' ? a2 : null);
  var opt = {};
  var i = 1;
  if (typeof a2 == 'string') {
    title = a1;
    msg = a2;
    i++;
    if (typeof a3 == 'function') {
      cb = a3;
      i++;
    } else {
      cb = null;
    }
  }
  if (a[i]) opt = a[i];
  var dialogOpt = {
    title: title,
    buttons: [{label: 'OK', focus: true, cb: cb}],
    style: opt.style
  };
  msg = util.null2empty(msg);
  msg = util.convertNewLine(msg, '<br>');
  var content = document.createElement('div');
  content.style.display = 'inline-block';
  if (opt.style && opt.style) {
    for (var key in opt.style) {
      util.setStyle(content, key, opt.style[key]);
    }
  }
  content.innerHTML = msg;
  return util.dialog.open(content, dialogOpt);
};

//-----------------------------------------------------------
/**
 * util.dialog.confirm('message', cbYes);
 * util.dialog.confirm('message', cbYes, cbNo);
 * util.dialog.confirm('message', cbYes, opt);
 * util.dialog.confirm('message', cbYes, cbNo, opt);
 * util.dialog.confirm('title', 'message', cbYes);
 * util.dialog.confirm('title', 'message', cbYes, cbNo);
 * util.dialog.confirm('title', 'message', cbYes, opt);
 * util.dialog.confirm('title', 'message', cbYes, cbNo, opt);
 *
 * opt = {
 *   focus: 'yes'|'no',
 *   data: object,
 *   style: {
 *     ...
 *   }
 * };
 *
 * cb = function(data) {}
 *
 * class:
 *  dialog
 *  dialog-title
 *  dialog-content
 *  dialog-button
 */
util.dialog.confirm = function(a1, a2, a3, a4, a5) {
  var a = [a1, a2, a3, a4, a5];
  var title;
  var msg = a1;
  var cbY = null;
  var cbN = null;
  var opt = {};
  var i = 1;
  var j = 2;
  var k = 3;
  if (typeof a2 == 'string') {
    title = a1;
    msg = a2;
    i++; j++; k++;
  }
  if (typeof a[i] == 'function') {
    cbY = a[i];
  } else {
    j--; k--;
  }
  if (typeof a[j] == 'function') {
    cbN = a[j];
  } else {
    k--;
  }
  if (a[k]) opt = a[k];
  var definition = {
    labelY: (opt.labelY ? opt.labelY : 'Yes'),
    labelN: (opt.labelN ? opt.labelN : 'No'),
    cbY: util.dialog.sysCbY,
    cbN: util.dialog.sysCbN,
  };
  msg = util.null2empty(msg);
  msg = util.convertNewLine(msg + '', '<br>');
  var content = document.createElement('div');
  content.style.display = 'inline-block';
  if (opt.style && opt.style) {
    for (var key in opt.style) {
      util.setStyle(content, key, opt.style[key]);
    }
  }
  content.innerHTML = msg;
  var dialog = new util.dialog.confirmDialog(title, content, definition, opt);
  dialog.cbY = cbY;
  dialog.cbN = cbN;
  return dialog;
};
util.dialog.confirmDialog = function(title, content, definition, opt) {
  var ctx = this;
  ctx.data = opt.data;
  var buttons = [
    {label: definition.labelY, cb: definition.cbY},
    {label: definition.labelN, cb: definition.cbN}
  ];
  var focusIdx = 0;
  if (opt.focus == 'no') focusIdx = 1;
  buttons[focusIdx].focus = true;
  var dialogOpt = {
    title: title,
    buttons: buttons,
    data: ctx,
    focusEl: definition.focusEl, // prior
    style: opt.style,
    onenter: opt.onenter
  };
  ctx.dlg = util.dialog.open(content, dialogOpt);
};
util.dialog.confirmDialog.prototype = {
  pressButton: function(n) {
    this.dlg.pressButton(n);
  }
};
util.dialog.sysCbY = function(ctx) {
  var f;
  if (ctx.cbY) f = ctx.cbY(ctx.data);
  return f;
};
util.dialog.sysCbN = function(ctx) {
  var f;
  if (ctx.cbN) ctx.cbN(ctx.data);
  return f;
};

//-----------------------------------------------------------
/**
 * util.dialog.text('message', cbOK);
 * util.dialog.text('message', cbOK, cbCancel);
 * util.dialog.text('message', cbOK, opt);
 * util.dialog.text('message', cbOK, cbCancel, opt);
 * util.dialog.text('title', 'message', cbOK);
 * util.dialog.text('title', 'message', cbOK, cbCancel);
 * util.dialog.text('title', 'message', cbOK, opt);
 * util.dialog.text('title', 'message', cbOK, cbCancel, opt);
 *
 * opt = {
 *   data: object,
 *   value: 'value',
 *   placeholder: 'placeholder',
 *   secure: true|false,
 *   style: {
 *     message: {
 *       ...
 *     },
 *     textbox: {
 *       ...
 *     }
 *   },
 *   onenter: cb
 * };
 *
 * cb = function(data) {}
 *
 * class:
 *  dialog
 *  dialog-title
 *  dialog-content
 *  dialog-button
 *  dialog-textbox
 *  dialog-textarea
 */
util.dialog.text = function(a1, a2, a3, a4, a5) {
  var a = [a1, a2, a3, a4, a5];
  var title;
  var msg = a1;
  var cbY = null;
  var cbN = null;
  var opt = {};
  var i = 1;
  var j = 2;
  var k = 3;
  if (typeof a2 == 'string') {
    title = a1;
    msg = a2;
    i++; j++; k++;
  }
  if (typeof a[i] == 'function') {
    cbY = a[i];
  } else {
    j--; k--;
  }
  if (typeof a[j] == 'function') {
    cbN = a[j];
  } else {
    k--;
  }
  if (a[k]) opt = a[k];
  var elType = ((opt.type == 'textarea') ? 'textarea' : 'input');
  var txtBox = document.createElement(elType);
  txtBox.type = (opt.secure ? 'password' : 'text');
  txtBox.className = ((opt.type == 'textarea') ? 'dialog-textarea' : 'dialog-textbox');
  if (opt && opt.style && opt.style.textbox) {
    for (var key in opt.style.textbox) {
      util.setStyle(txtBox, key, opt.style.textbox[key]);
    }
  }
  txtBox.spellcheck = false;
  txtBox.value = (opt.value ? opt.value : '');
  txtBox.placeholder = (opt.placeholder ? opt.placeholder : '');
  var body = document.createElement('div');
  var wrp1 = document.createElement('div');
  wrp1.style.display = 'inline-block';
  if (opt.style && opt.style.message) {
    for (key in opt.style.message) {
      util.setStyle(wrp1, key, opt.style.message[key]);
    }
  }
  var wrp2 = document.createElement('div');
  wrp2.style.marginTop = '8px';
  if (typeof msg == 'string') {
    wrp1.innerHTML = msg;
  } else {
    wrp1.appendChild(msg);
  }
  wrp2.appendChild(txtBox);
  body.appendChild(wrp1);
  body.appendChild(wrp2);
  var definition = {
    labelY: 'OK',
    labelN: 'Cancel',
    cbY: util.dialog.text.sysCbOK,
    cbN: util.dialog.text.sysCbCancel,
    focusEl: txtBox
  };
  var dialog = new util.dialog.confirmDialog(title, body, definition, opt);
  txtBox.ctx = dialog;
  if (elType == 'input') {
    txtBox.addEventListener('keydown', util.dialog.text.onEnter);
  }
  dialog.cbY = cbY;
  dialog.cbN = cbN;
  dialog.txtBox = txtBox;
  return dialog;
};
util.dialog.text.sysCbOK = function(ctx) {
  var f;
  var text = ctx.txtBox.value;
  if (ctx.cbY) f = ctx.cbY(text, ctx.data);
  return f;
};
util.dialog.text.sysCbCancel = function(ctx) {
  var f;
  var text = ctx.txtBox.value;
  if (ctx.cbN) f = ctx.cbN(text, ctx.data);
  return f;
};
util.dialog.text.onEnter = function(e) {
  if (e.keyCode != 13) return;
  var dlg = e.target.ctx.dlg;
  var opt = dlg.opt;
  if (opt.onenter) {
    var b = dlg.btnEls[0];
    util.dialog.btnHandler(b);
  }
};

util.alert = function(a1, a2, a3, a4) {
  return util.dialog.info(a1, a2, a3, a4);
};
util.confirm = function(a1, a2, a3, a4, a5) {
  return util.dialog.confirm(a1, a2, a3, a4, a5);
};

//---------------------------------------------------------
// Meter
//---------------------------------------------------------
/**
 * tgt: element / selector
 * opt = {
 *  min
 *  max
 *  low
 *  high
 *  optimum
 *  value
 *  width
 *  height
 *  background
 *  border
 *  borderRadius
 *  transition
 *  color
 *  green
 *  yellow
 *  red,
 *  label: {
 *    text: 'TEXT',
 *    style: {
 *    }
 *  },
 *  scales: [
 *   {
 *     value: 80,
 *     style: '1px solid #00f'
 *   }
 *  ],
 * sleed
 * }
 *
 * <div id="meter1"></div>
 * var m = new util.Meter('#meter1', opt);
 */
util.initMeter = function(tgt, opt) {
  return new util.Meter(tgt, opt);
};
util.Meter = function(tgt, opt) {
  tgt = util.getElement(tgt);
  tgt.innerHTML = '';

  var min = 0;
  var max = 100;
  var low, high, optimum, value;
  var w = '100px';
  var h = '14px';
  var bg = '#888';
  var bd = '1px solid #ccc';
  var bdRd = '0';
  var green = 'linear-gradient(to right, #0d0, #8f8)';
  var yellow = 'linear-gradient(to right, #dd0 , #ff8)';
  var red = 'linear-gradient(to right, #d66 , #fcc)';
  var speed = 250;
  if (opt) {
    if (opt.min != undefined) min = opt.min;
    if (opt.max != undefined) max = opt.max;
    if (opt.low != undefined) low = opt.low;
    if (opt.high != undefined) high = opt.high;
    if (opt.optimum != undefined) optimum = opt.optimum;
    if (opt.value != undefined) value = opt.value;
    if (opt.width != undefined) w = opt.width;
    if (opt.height != undefined) h = opt.height;
    if (opt.background != undefined) bg = opt.background;
    if (opt.border != undefined) bd = opt.border;
    if (opt.borderRadius != undefined) bdRd = opt.borderRadius;
    if (opt.color) {
      green = opt.color;
      yellow = opt.color;
      red = opt.color;
    } else {
      if (opt.green != undefined) green = opt.green;
      if (opt.yellow != undefined) yellow = opt.yellow;
      if (opt.red != undefined) red = opt.red;
    }
    if (opt.speed != undefined) speed = opt.speed;
  }

  if (low == undefined) low = min;
  if (high == undefined) high = max;
  if (value == undefined) value = min;
  if (low < min) low = min;
  if (high > max) high = max;
  if (optimum != undefined) {
    if (optimum < min) optimum = min;
    if (optimum > max) optimum = max;
  }
  var base = tgt;
  base.className = 'meter';
  var style = {
    display: 'inline-block',
    position: 'relative',
    width: w,
    height: h,
    background: bg,
    border: bd,
    'border-radius': bdRd
  };
  util.setStyle(base, style);

  var v = value / max * 100;
  var bar = document.createElement('div');
  bar.className = 'meter-bar';
  style = {
    width: v + '%',
    height: '100%',
    background: green
  };
  if (opt && opt.transition) style.transition = opt.transition;
  util.setStyle(bar, style);
  base.appendChild(bar);

  var scales;
  if (opt) scales = opt.scales;
  if (scales) {
    for (var i = 0; i < scales.length; i++) {
      var scale = scales[i];
      var e = document.createElement('div');
      var sw = scale.value;
      if (!(sw + '').match(/%/)) {
        sw = (sw / max * 100) + '%';
      }
      var ss = scale.style;
      if (!ss) ss = '1px solid #aaa';
      var s = {
        position: 'absolute',
        top: 0,
        left: 0,
        width: sw,
        height: '100%',
        border: 'none',
        'border-right': ss
      };
      util.setStyle(e, s);
      base.appendChild(e);
    }
  }

  var label = null;
  if (opt) label = opt.label;
  if (label) {
    var lblEl = this.createLblEl(label);
    base.appendChild(lblEl);
  }

  this.opt = {
    min: min,
    max: max,
    low: low,
    high: high,
    optimum: optimum,
    width: w,
    height: h,
    background: bg,
    border: bd,
    borderRadius: bdRd,
    green: green,
    yellow: yellow,
    red: red,
    label: label,
    scales: scales,
    speed: speed
  };
  this.value = value;
  this.el = base;
  this.bar = bar;
  this.lblEl = lblEl;
  this.setSpeed(speed);
  this.redraw();
};

util.Meter.prototype = {
  initLblEl: function() {
    this.lblEl = this.createLblEl();
    this.el.appendChild(this.lblEl);
  },
  createLblEl: function(opt) {
    if (!opt) opt = {};
    var lblEl = document.createElement('div');
    var s = {
      position: 'absolute',
      display: 'inline-block',
      height: '1em',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      margin: 'auto 2px',
      color: '#fff',
      'font-size': '12px',
      'font-family': 'Consolas, Monaco, Menlo, monospace, sans-serif',
      'text-align': 'center',
      'vertical-align': 'middle'
    };
    util.setStyle(lblEl, s);
    if (opt.style) util.setStyle(lblEl, opt.style);
    if (opt.text) lblEl.innerHTML = opt.text;
    return lblEl;
  },
  getValue: function() {
    return this.value;
  },
  setValue: function(v, a2, a3) {
    if (v != null) this._setValue(v);
    var txt, speed;
    if (a2 != undefined) {
      if (typeof a2 == 'string') {
        txt = a2;speed = a3;
      } else {
        speed = a2;
      }
    }
    if (txt != undefined) this.setText(txt);
    if (speed != undefined) this.setSpeed(speed);
    this.redraw();
    setTimeout(util._resetMeterSpeed, 5, this);
  },
  _setValue: function(v) {
    v |= 0;
    var opt = this.opt;
    if (v > opt.max) {
      v = opt.max;
    } else if (v < opt.min) {
      v = opt.min;
    }
    this.value = v;
  },
  setText: function(s) {
    if (!this.lblEl) this.initLblEl();
    this.lblEl.innerHTML = s;
    this.redraw();
  },
  setTextStyle: function(s) {
    if (!this.lblEl) this.initLblEl();
    util.setStyle(this.lblEl, s);
    this.redraw();
  },
  increase: function(v) {
    var opt = this.opt;
    if (v == undefined) v = 1;
    v |= 0;
    this.value += v;
    if (this.value > opt.max) this.value = opt.max;
    this.redraw();
  },
  decrease: function(v) {
    var opt = this.opt;
    if (v == undefined) v = 1;
    v |= 0;
    this.value -= v;
    if (this.value < opt.min) this.value = opt.min;
    this.redraw();
  },
  setMin: function(v) {
    this.opt.min = v | 0;
    this.redraw();
  },
  setMax: function(v) {
    this.opt.max = v | 0;
    this.redraw();
  },
  setLow: function(v) {
    this.opt.low = v | 0;
    this.redraw();
  },
  setHigh: function(v) {
    this.opt.high = v | 0;
    this.redraw();
  },
  setOptimum: function(v) {
    this.optimum = v | 0;
    this.redraw();
  },
  setSpeed: function(v) {
    var s = 'all ' + (v / 1000) + 's ease-out';
    util.setStyle(this.bar, 'transition', s);
  },
  redraw: function() {
    var ctx = this;
    var opt = ctx.opt;
    var value = ctx.value;
    var max = opt.max;
    var optimum = opt.optimum;
    var high = opt.high;
    var low = opt.low;
    var mode = 'M';
    if (optimum != undefined) {
      if (optimum <= low) {
        mode = 'L';
      } else if (optimum >= high) {
        mode = 'H';
      }
    }
    var v = value / max * 100;
    var bg = opt.green;
    if (mode == 'M') {
      if ((value < low) || (high < value)) {
        bg = opt.yellow;
      }
    } else if (mode == 'L') {
      if ((low <= value) && (value < high)) {
        bg = opt.yellow;
      } else if (high <= value) {
        bg = opt.red;
      }
    } else if (mode == 'H') {
      if ((low <= value) && (value < high)) {
        bg = opt.yellow;
      } else if (value < low) {
        bg = opt.red;
      }
    }
    util.setStyle(ctx.bar, 'width', v + '%');
    util.setStyle(ctx.bar, 'background', bg);
  }
};

/**
 * val: '70%'
 * opt: {
 *   low: 80,
 *   high: 90,
 *   optimum: 50
 * };
 */
util.Meter.buildHTML = function(val, opt) {
  if (!opt) opt = {};
  if ((val + '').match(/%/)) val = val.replace(/%/, '');
  opt.value = val;
  var d = document.createElement('div');
  var m = new util.Meter(d, opt);
  return m.el.outerHTML;
};

util._resetMeterSpeed = function(c) {
  c.setSpeed(c.opt.speed);
};

//---------------------------------------------------------
// LED
//---------------------------------------------------------
// var led = new util.Led('#led1', opt);
// led.on();
// led.off();
// led.blink();
// led.setLavel('TEXT');
/**
 * opt = {
 *   size: '16px',
 *   color: '#0f0',
 *   offColor: '#888',
 *   shadow: '0 0 5px',
 *   speed: 100,
 *   className: 'xxx',
 *   labelClassName: 'xxx',
 *   active: true
 * };
 */
util.Led = function(tgt, opt) {
  var baseEl = util.getElement(tgt);
  opt = util.copyDefaultProps(util.Led.DFLTOPT, opt);
  var active = opt.active ? true : false;

  var ledEl = document.createElement('span');
  util.addClass(ledEl, 'led');
  if (opt.className) util.addClass(ledEl, opt.className);
  var style = {
    'font-size': opt.size,
    color: (active ? opt.color : opt.offColor),
    cursor: 'default'
  };
  if (opt.shadow) style['text-shadow'] = opt.shadow;
  util.setStyle(ledEl, style);
  baseEl.innerHTML = '';
  baseEl.appendChild(ledEl);

  this.opt = opt;
  this.baseEl = baseEl;
  this.ledEl = ledEl;
  this.labelEl = null;
  this.active = active;
  this.lighted = active;
  this.timerId = 0;
  this.blinkDuration = util.Led.DFLT_BLINK_DURATION;
};
util.Led.DFLTOPT = {
  size: '16px',
  color: '#0f0',
  offColor: '#888',
  shadow: '0 0 5px',
  speed: 0
};
util.Led.DFLT_BLINK_DURATION = 700;
util.Led.prototype = {
  on: function(a1, a2) {
    var c = a1;
    var s = a2;
    if (typeof a1 == 'number') {
      c = '';s = a1;
    }
    var ctx = this;
    ctx._stopBlink();
    if (c) ctx.opt.color = c;
    ctx.active = true;
    ctx._on(ctx, s);
  },
  off: function(a1, a2) {
    var c = a1;
    var s = a2;
    if (typeof a1 == 'number') {
      c = '';s = a1;
    }
    var ctx = this;
    ctx._stopBlink();
    if (c) ctx.opt.offColor = c;
    ctx.active = false;
    ctx._off(ctx, s);
  },
  toggle: function(speed) {
    var ctx = this;
    if (ctx.active) {
      ctx.off(speed);
    } else {
      ctx.on(speed);
    }
  },
  _on: function(ctx, s) {
    ctx.lighted = true;
    if (s == undefined) s = ctx.opt.speed;
    s = (s ? (s / 1000) : 0);
    var style = {
      color: ctx.opt.color,
      transition: 'all ' + s + 's ease-out'
    };
    util.setStyle(ctx.ledEl, style);
  },
  _off: function(ctx, s) {
    ctx.lighted = false;
    if (s == undefined) s = ctx.opt.speed;
    s = (s ? (s / 1000) : 0);
    var style = {
      color: ctx.opt.offColor,
      transition: 'all ' + s + 's ease-in'
    };
    util.setStyle(ctx.ledEl, style);
  },
  blink: function(a1, a2) {
    var ctx = this;
    var d = a1;
    var c;
    if (typeof a1 == 'string') {
      c = a1;d = a2;
    }
    if (c) ctx.opt.color = c;
    ctx._stopBlink();
    if (d === false) return;
    d |= 0;
    if (d <= 0) d = util.Led.DFLT_BLINK_DURATION;
    ctx.blinkDuration = d;
    ctx._blink(ctx);
  },
  _blink: function(ctx) {
    if (ctx.lighted) {
      ctx._off(ctx);
    } else {
      ctx._on(ctx);
    }
    ctx.timerId = setTimeout(ctx._blink, ctx.blinkDuration, ctx);
  },
  blink2: function(a) {
    var ctx = this;
    if (a == undefined) {
      a = true;
    } else if (typeof a == 'string') {
      ctx.opt.color = a;
    }
    ctx._stopBlink();
    if (a) {
      ctx._on(ctx);
      util.addClass(ctx.ledEl, 'blink2');
    }
  },
  _stopBlink: function() {
    var ctx = this;
    util.removeClass(ctx.ledEl, 'blink2');
    if (ctx.timerId > 0) {
      clearTimeout(ctx.timerId);
      ctx.timerId = 0;
    }
    var active = ctx.active;
    if (active) {
      ctx._on(ctx);
    } else {
      ctx._off(ctx);
    }
  },
  isActive: function() {
    return this.active;
  },
  setColor: function(c) {
    var ctx = this;
    ctx.opt.color = c;
    if (ctx.lighted) {
      ctx._on(ctx);
    }
  },
  setOffColor: function(c) {
    var ctx = this;
    ctx.opt.offColor = c;
    if (!ctx.lighted) {
      ctx._off(ctx);
    }
  },
  setLabel: function(txt) {
    var ctx = this;
    var el = ctx.labelEL;
    if (!el) {
      el = document.createElement('span');
      util.addClass(el, 'led-label');
      if (ctx.opt.labelClassName) {
        util.addClass(el, ctx.opt.labelClassName);
      } else {
        el.style.marginLeft = '4px';
      }
      ctx.baseEl.appendChild(el);
    }
    el.innerHTML = txt;
    ctx.labelEL = el;
  },
  getElement: function() {
    return this.baseEl;
  },
  getLedElement: function() {
    return this.ledEl;
  }
};

//---------------------------------------------------------
// Form
//---------------------------------------------------------
util.submit = function(url, method, params, uriEnc) {
  var form = document.createElement('form');
  form.action = url;
  form.method = method;
  for (var key in params) {
    var input = document.createElement('input');
    var val = params[key];
    if (uriEnc) val = encodeURIComponent(val);
    input.type = 'hidden';
    input.name = key;
    input.value = val;
    form.appendChild(input);
  }
  document.body.appendChild(form);
  form.submit();
};

util.postSubmit = function(url, params, uriEnc) {
  util.submit(url, 'POST', params, uriEnc);
};

//---------------------------------------------------------
// URL / Query
//---------------------------------------------------------
util.getProtocol = function() {
  return location.protocol;
};
util.getHost = function() {
  return location.host.split(':')[0];
};
util.getPort = function() {
  return location.port;
};
util.getParentPath = function() {
  return location.href.replace(/(.*\/).*/, '$1');
};
util.getQuery = function(k) {
  var s = window.location.search.substr(1);
  if (!k) return s;
  var q = s.split('&');
  var a = [];
  for (var i = 0; i < q.length; i++) {
    var p = q[i].split('=');
    if (p[0] == k) a.push(p[1]);
  }
  var v = null;
  if (a.length == 1) {
    v = a[0];
  } else if (a.length > 1) {
    v = a;
  }
  return v;
};
util.getUrlHash = function() {
  var s = window.location.hash;
  if (s) s = s.substr(1);
  return s;
};

//---------------------------------------------------------
// Language
//---------------------------------------------------------
util.getClientLanguage = function(f) {
  var v = navigator.language;
  if (f) v = v.replace(/-.*/, '');
  return v;
};

util.getClientLanguages = function(f) {
  var v = navigator.languages;
  var a = [];
  if (!v) v = [navigator.language];
  for (var i = 0; i < v.length; i++) {
    a[i] = v[i];
  }
  if (f) {
    for (i = 0; i < a.length; i++) {
      a[i] = a[i].replace(/-.*/, '');
    }
    a = util.list2set(a);
  }
  return a;
};

//---------------------------------------------------------
// Browser
//---------------------------------------------------------
util.getBrowserInfo = function(ua) {
  if (ua == undefined) ua = navigator.userAgent;
  var ver;
  var brws = {name: '', version: ''};
  if (ua.indexOf('Edge') >= 1) {
    brws.name = 'Edge Legacy';
    ver = ua.match(/Edge\/(.*)/);
    if (ver) brws.version = ver[1];
    return brws;
  }
  if (ua.indexOf('Edg') >= 1) {
    brws.name = 'Edge';
    ver = ua.match(/Edg\/(.*)/);
    if (ver) brws.version = ver[1];
    return brws;
  }
  if (ua.indexOf('OPR/') >= 1) {
    brws.name = 'Opera';
    ver = ua.match(/OPR\/(.*)/);
    if (ver) brws.version = ver[1];
    return brws;
  }
  if (ua.indexOf('Chrome') >= 1) {
    brws.name = 'Chrome';
    ver = ua.match(/Chrome\/(.*)\s/);
    if (ver) brws.version = ver[1];
    return brws;
  }
  if (ua.indexOf('Firefox') >= 1) {
    brws.name = 'Firefox';
    ver = ua.match(/Firefox\/(.*)/);
    if (ver) brws.version = ver[1];
    return brws;
  }
  if (ua.indexOf('Trident/7.') >= 1) {
    brws.name = 'IE11';
    brws.family = 'IE';
    return brws;
  }
  if ((ua.indexOf('Safari/') >= 1) && (ua.indexOf('Version/') >= 1)) {
    brws.name = 'Safari';
    ver = ua.match(/Version\/(.*)\sSafari/);
    if (ver) brws.version = ver[1];
    return brws;
  }
  return brws;
};
util.getColoredBrowserName = function(n, dark) {
  if (!n) {
    var b = util.getBrowserInfo();
    n = b.name;
  }
  var s = n;
  var c;
  switch (n) {
    case 'Chrome':
      if (dark) {
        s = '<span style="color:#f44">Ch</span><span style="color:#ff0">ro</span><span style="color:#4f4">m</span><span style="color:#6cf">e</span>';
      } else {
        s = '<span style="color:#f44">Ch</span><span style="color:#cc0">ro</span><span style="color:#2d2">m</span><span style="color:#0af">e</span>';
      }
      break;
    case 'Edge':
      if (dark) {
        s = '<span style="color:#4e7">E</span><span style="color:#3df">d</span><span style="color:#0af">ge</span>';
      } else {
        s = '<span style="color:#2c4">E</span><span style="color:#0cf">d</span><span style="color:#08d">ge</span>';
      }
      break;
    case 'Edge Legacy':
      c = (dark ? '#0af' : '#08d');
      s = '<span style="color:' + c + '">Edge</span>';
      break;
    case 'Firefox':
      c = (dark ? '#e57f25' : '#e60');
      s = '<span style="color:' + c + '">' + n + '</span>';
      break;
    case 'Opera':
      c = (dark ? '#f44' : '#ff1b2c');
      s = '<span style="color:' + c + '">' + n + '</span>';
      break;
    case 'IE11':
      c = (dark ? '#61d5f8' : '#0af');
      s = '<span style="color:' + c + '">' + n + '</span>';
      break;
    case 'Safari':
      s = '<span style="color:#86c8e8">Safa</span><span style="color:#dd5651">r</span><span style="color:#ececec">i</span>';
      break;
    default:
      b = util.getBrowserInfo(n);
      if (b.name) s = util.getColoredBrowserName(b.name);
  }
  return s;
};

//---------------------------------------------------------
// ROTx
//---------------------------------------------------------
util.rot5 = function(s, n) {
  if (n == null) n = 5;
  n |= 0;
  if ((n < -9) || (n > 9)) n = n % 10;
  var r = '';
  for (var i = 0; i < s.length; i++) {
    var c = s.charAt(i);
    var cc = c.charCodeAt(0);
    if (util.isNumber(c)) {
      cc += n;
      if (cc > 0x39) {
        cc = 0x2F + (cc - 0x39);
      } else if (cc < 0x30) {
        cc = 0x3A - (0x30 - cc);
      }
    }
    r += String.fromCharCode(cc);
  }
  return r;
};
util.rot13 = function(s, n) {
  if (n == null) n = 13;
  n |= 0;
  if ((n < -25) || (n > 25)) n = n % 26;
  var r = '';
  for (var i = 0; i < s.length; i++) {
    var c = s.charAt(i);
    var cc = c.charCodeAt(0);
    if (util.isAlphabet(c)) {
      cc += n;
      if (util.isUpperCase(c)) {
        if (cc > 0x5A) {
          cc = 0x40 + (cc - 0x5A);
        } else if (cc < 0x41) {
          cc = 0x5B - (0x41 - cc);
        }
      } else if (util.isLowerCase(c)) {
        if (cc > 0x7A) {
          cc = 0x60 + (cc - 0x7A);
        } else if (cc < 0x61) {
          cc = 0x7B - (0x61 - cc);
        }
      }
    }
    r += String.fromCharCode(cc);
  }
  return r;
};
util.rot18 = function(s, n) {
  return util.rot5(util.rot13(s, n), n);
};
util.rot47 = function(s, n) {
  if (n == null) n = 47;
  n |= 0;
  if ((n < -93) || (n > 93)) n = n % 94;
  var r = '';
  for (var i = 0; i < s.length; i++) {
    var cc = s.charCodeAt(i);
    if ((cc >= 0x21) && (cc <= 0x7E)) {
      if (n < 0) {
        cc += n;
        if (cc < 0x21) cc = 0x7F - (0x21 - cc);
      } else {
        cc = ((cc - 0x21 + n) % 94) + 0x21;
      }
    }
    r += String.fromCharCode(cc);
  }
  return r;
};

//---------------------------------------------------------
// Base64
//---------------------------------------------------------
util.Base64 = {};
util.Base64.encode = function(arr) {
  var len = arr.length;
  if (len == 0) return '';
  var tbl = {64: 61, 63: 47, 62: 43};
  for (var i = 0; i < 62; i++) {
    tbl[i] = (i < 26 ? i + 65 : (i < 52 ? i + 71 : i - 4));
  }
  var str = '';
  for (i = 0; i < len; i += 3) {
    str += String.fromCharCode(
      tbl[arr[i] >>> 2],
      tbl[(arr[i] & 3) << 4 | arr[i + 1] >>> 4],
      tbl[(i + 1) < len ? (arr[i + 1] & 15) << 2 | arr[i + 2] >>> 6 : 64],
      tbl[(i + 2) < len ? (arr[i + 2] & 63) : 64]
    );
  }
  return str;
};
util.Base64.decode = function(str) {
  var arr = [];
  if (str.length == 0) return arr;
  for (var i = 0; i < str.length; i++) {
    var c = str.charCodeAt(i);
    if (!(((c >= 0x30) && (c <= 0x39)) || ((c >= 0x41) && (c <= 0x5A)) || ((c >= 0x61) && (c <= 0x7A)) || (c == 0x2B) || (c == 0x2F) || (c == 0x3D))) {
      util._log.e('invalid b64 char: 0x' + c.toString(16).toUpperCase() + ' at ' + i);
      return arr;
    }
  }
  var tbl = {61: 64, 47: 63, 43: 62};
  for (i = 0; i < 62; i++) {
    tbl[i < 26 ? i + 65 : (i < 52 ? i + 71 : i - 4)] = i;
  }
  var buf = [];
  for (i = 0; i < str.length; i += 4) {
    for (var j = 0; j < 4; j++) {
      buf[j] = tbl[str.charCodeAt(i + j) || 0];
    }
    arr.push(
      buf[0] << 2 | (buf[1] & 63) >>> 4,
      (buf[1] & 15) << 4 | (buf[2] & 63) >>> 2,
      (buf[2] & 3) << 6 | buf[3] & 63
    );
  }
  if (buf[3] == 64) {
    arr.pop();
    if (buf[2] == 64) {
      arr.pop();
    }
  }
  return arr;
};

util.encodeBase64 = function(s, b) {
  return (b ? util.encodeBase64fmB(s) : util.encodeBase64fmT(s));
};
util.encodeBase64fmT = function(s) {
  if (s == undefined) return '';
  return btoa(encodeURIComponent(s).replace(/%([0-9A-F]{2})/g, function(match, p1) {return String.fromCharCode('0x' + p1);}));
};
util.encodeBase64fmB = function(s) {
  s = util.toBinaryString(s);
  return btoa(s);
};
util.decodeBase64 = function(s, b) {
  if ((s == null) || (!window.atob)) return null;
  return (b ? util.decodeBase64asB(s) : util.decodeBase64asT(s));
};
util.decodeBase64asT = function(s) {
  var r;
  try {
    r = decodeURIComponent(Array.prototype.map.call(atob(s), function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
  } catch (e) {
    r = atob(s);
  }
  return r;
};
util.decodeBase64asB = function(s) {
  var b = atob(s);
  var a = [];
  for (var i = 0; i < b.length; i++) {
    a.push(b[i].charCodeAt(0));
  }
  return new Uint8Array(a);
};

util.toBinaryString = function(a) {
  var s = '';
  var b = new Uint8Array(a);
  for (var i = 0; i < b.byteLength; i++) {
    s += String.fromCharCode(b[i]);
  }
  return s;
};

//---------------------------------------------------------
// Base64s
//---------------------------------------------------------
util.encodeBase64s = function(s, k) {
  var a = ((typeof s == 'string') ? util.UTF8.toByteArray(s) : s);
  var x = util.UTF8.toByteArray(k);
  var b = util._encodeBase64s(a, x);
  return util.Base64.encode(b);
};
util._encodeBase64s = function(a, k) {
  var ln = a.length;
  var kl = k.length;
  if ((ln == 0) || (kl == 0)) return a;
  var d = kl - ln;
  if (d < 0) d = 0;
  var b = [];
  for (var i = 0; i < ln; i++) {
    b.push(a[i] ^ k[i % kl]);
  }
  var n = i;
  for (i = 0; i < d; i++) {
    b.push(255 ^ k[(n + i) % kl]);
  }
  b.push(d);
  return b;
};
util.decodeBase64s = function(s, k, byB) {
  if (s == null) return null;
  s = util.convertNewLine(s, '\n').replace(/\n/g, '');
  var b = util.Base64.decode(s);
  var x = util.UTF8.toByteArray(k);
  var a = util._decodeBase64s(b, x);
  if (!byB) a = util.UTF8.fromByteArray(a);
  return a;
};
util._decodeBase64s = function(a, k) {
  var al = a.length;
  var kl = k.length;
  if ((al == 0) || (kl == 0)) return a;
  var d = a[al - 1];
  var ln = al - d - 1;
  var b = [];
  for (var i = 0; i < ln; i++) {
    b.push(a[i] ^ k[i % kl]);
  }
  return b;
};

//---------------------------------------------------------
// UTF-8
//---------------------------------------------------------
util.UTF8 = {};
util.UTF8.toByteArray = function(s) {
  var a = [];
  if (!s) return a;
  var chs = util.str2chars(s);
  for (var i = 0; i < chs.length; i++) {
    var ch = chs[i];
    var c = ch.charCodeAt(0);
    if (c <= 0x7F) {
      a.push(c);
    } else {
      var e = encodeURIComponent(ch);
      var w = e.split('%');
      for (var j = 1; j < w.length; j++) {
        a.push(('0x' + w[j]) | 0);
      }
    }
  }
  return a;
};
util.UTF8.fromByteArray = function(b) {
  if (!b) return null;
  var e = '';
  for (var i = 0; i < b.length; i++) {
    e += '%' + util.toHex(b[i], true, 2);
  }
  return decodeURIComponent(e);
};

//---------------------------------------------------------
// bit operation
//---------------------------------------------------------
util.bit8 = {};
util.bit8.rotateL = function(v, n) {
  n = n % 8;
  return ((v << n) | (v >> (8 - n))) & 255;
};
util.bit8.rotateR = function(v, n) {
  n = n % 8;
  return ((v >> n) | (v << (8 - n))) & 255;
};
util.bit8.invert = function(v) {
  return (~v) & 255;
};

//---------------------------------------------------------
// BSB64
//---------------------------------------------------------
util.encodeBSB64 = function(s, n) {
  var a = ((typeof s == 'string') ? util.UTF8.toByteArray(s) : s);
  return util.BSB64.encode(a, n);
};
util.decodeBSB64 = function(s, n, byB) {
  if (s == null) return null;
  s = util.convertNewLine(s, '\n').replace(/\n/g, '');
  if (s.match(/\$\d+$/)) {
    var v = s.split('$');
    s = v[0];
    n = v[1];
  }
  var a = util.BSB64.decode(s, n);
  if (!byB) a = util.UTF8.fromByteArray(a);
  return a;
};
util.BSB64 = {};
util.BSB64.encode = function(a, n) {
  var fn = util.bit8.rotateL;
  if (n % 8 == 0) fn = util.bit8.invert;
  var b = [];
  for (var i = 0; i < a.length; i++) {
    b.push(fn(a[i], n));
  }
  return util.Base64.encode(b);
};
util.BSB64.decode = function(s, n) {
  var fn = util.bit8.rotateR;
  if (n % 8 == 0) fn = util.bit8.invert;
  var b = util.Base64.decode(s);
  var a = [];
  for (var i = 0; i < b.length; i++) {
    a.push(fn(b[i], n));
  }
  return a;
};

//---------------------------------------------------------
// Ring Buffer
//---------------------------------------------------------
util.RingBuffer = function(len) {
  this.buffer = new Array(len);
  this.len = len;
  this.cnt = 0;
};
util.RingBuffer.prototype = {
  add: function(data) {
    var i = this.cnt % this.len;
    this.buffer[i] = data;
    this.cnt++;
  },
  set: function(idx, data) {
    var ctx = this;
    var p;
    if (idx < 0) {
      idx *= -1;
      if (((ctx.cnt < ctx.len) && (idx > ctx.cnt)) || ((ctx.cnt >= ctx.len) && (idx > ctx.len))) {
        return;
      }
      p = ctx.cnt - idx;
    } else {
      if (((ctx.cnt < ctx.len) && (idx >= ctx.cnt)) || ((ctx.cnt >= ctx.len) && (idx >= ctx.len))) {
        return;
      }
      p = ctx.cnt - ctx.len;
      if (p < 0) p = 0;
      p += idx;
    }
    var i = p % ctx.len;
    ctx.buffer[i] = data;
  },
  get: function(idx) {
    if (this.len < this.cnt) idx += this.cnt;
    idx %= this.len;
    return this.buffer[idx];
  },
  getAll: function() {
    var buf = [];
    var len = this.len;
    var pos = 0;
    if (this.cnt > len) pos = (this.cnt % len);
    for (var i = 0; i < len; i++) {
      if (pos >= len) pos = 0;
      if (this.buffer[pos] == undefined) {
        break;
      } else {
        buf[i] = this.buffer[pos];
        pos++;
      }
    }
    return buf;
  },
  clear: function() {
    this.buffer = new Array(this.len);
    this.cnt = 0;
  },
  count: function() {
    return this.cnt;
  },
  size: function() {
    return this.len;
  }
};

//---------------------------------------------------------
// Console
//---------------------------------------------------------
/**
 * opt = {
 *   bufsize: 10,
 *   width: '500px',
 *   height: '100px',
 *   color: '#fff',
 *   background: '#000',
 *   border: '1px solid #888',
 *   fontFamily: 'Consolas',
 *   fontSize: '12px',
 *   class: 'console1'
 * }
 */
util.initConsole = function(el, opt) {
  return new util.Console(el, opt);
};
util.Console = function(el, opt) {
  var wrapper = util.getElement(el);
  if (!opt) opt = {};
  var bufsize = (opt.bufsize == undefined ? 1000 : opt.bufsize);
  var fontFamily = (opt.fontFamily == undefined ? 'Consolas, Monaco, Menlo, monospace, sans-serif' : opt.fontFamily);

  var pre = document.createElement('pre');
  pre.width = '100%';
  pre.height = '100%';
  pre.style.margin = 0;
  pre.style.padding = 0;
  pre.style.fontFamily = fontFamily;
  if (opt.color) pre.style.color = opt.color;
  if (opt.fontSize) pre.style.fontSize = opt.fontSize;
  if (opt.class) pre.className = opt.class;

  wrapper.appendChild(pre);
  wrapper.style.overflow = 'auto';
  if (opt.width) wrapper.style.width = opt.width;
  if (opt.height) wrapper.style.height = opt.height;
  if (opt.background) wrapper.style.background = opt.background;
  if (opt.border) wrapper.style.border = opt.border;
  wrapper.addEventListener('scroll', this.onScroll, true);
  wrapper.ctx = this;

  this.wrapper = wrapper;
  this.buf = new util.RingBuffer(bufsize);
  this.pre = pre;
  this.autoScroll = true;
};
util.Console.prototype = {
  print: function(m, idx) {
    var ctx = this;
    idx |= 0;
    if (idx == 0) {
      ctx.buf.add(m);
    } else {
      if (idx > 0) idx--;
      ctx.buf.set(idx, m);
    }
    ctx._print(ctx);
  },
  _print: function(ctx) {
    var b = ctx.buf.getAll();
    var s = '';
    for (var i = 0; i < b.length; i++) {
      s += b[i] + '\n';
    }
    ctx.pre.innerHTML = s;
    if (ctx.autoScroll) ctx.toBottom();
  },
  write: function(m, n) {
    var ctx = this;
    ctx.buf.clear();
    n |= 0;
    var a = util.text2list(m);
    if (n == 0) {
      for (var i = 0; i < a.length; i++) {
        ctx.buf.add(a[i]);
      }
    } else {
      var s = '';
      var st = a.length - n;
      if (st < 0) st = 0;
      var ed = st + n;
      if (ed > a.length) ed = a.length;
      for (i = st; i < ed; i++) {
        s += a[i] + '\n';
      }
      ctx.buf.add(s);
    }
    ctx._print(ctx);
  },
  clear: function() {
    var ctx = this;
    ctx.buf.clear();
    ctx._print(ctx);
  },
  copy: function() {
    var s = this.getText();
    util.copy(s);
  },
  getText: function(idx) {
    var s = this.getHTML(idx);
    return util.html2text(s);
  },
  getHTML: function(idx) {
    idx |= 0;
    var c = this.pre.innerHTML;
    if (idx == 0) return c;
    var a = util.text2list(c);
    if (idx < 0) {
      var i = a.length + idx;
      return (i < 0 ? '' : a[i]);
    }
    idx--;
    return (idx < a.length ? a[idx] : '');
  },
  toTop: function() {
    this.wrapper.scrollTop = 0;
  },
  toRight: function() {
    this.wrapper.scrollLeft = this.wrapper.scrollWidth;
  },
  toBottom: function() {
    this.wrapper.scrollTop = this.wrapper.scrollHeight;
  },
  toLeft: function() {
    this.wrapper.scrollLeft = 0;
  },
  scrollX: function(v) {
    if (v == undefined) return this.wrapper.scrollLeft;
    this.wrapper.scrollLeft = v;
  },
  scrollY: function(v) {
    if (v == undefined) return this.wrapper.scrollTop;
    this.wrapper.scrollTop = v;
  },
  onScroll: function(e) {
    var ctx = e.target.ctx;
    var rect = ctx.wrapper.getBoundingClientRect();
    var h = rect.height;
    var d = ctx.wrapper.scrollHeight - ctx.wrapper.scrollTop;
    if ((d - 20 <= h) && (h <= d + 20)) {
      ctx.autoScroll = true;
    } else {
      ctx.autoScroll = false;
    }
  }
};

//---------------------------------------------------------
// Counter
//---------------------------------------------------------
/**
 * opt: see util.Counter.DFLTOPT
 */
util.initCounter = function(el, opt) {
  return new util.Counter(el, opt);
};
util.Counter = function(el, opt) {
  var ctx = this;
  el = util.getElement(el);
  opt = util.copyDefaultProps(util.Counter.DFLTOPT, opt);
  if (typeof opt.duration == 'number') {
    opt.duration = {min: opt.duration, max: opt.duration};
  }
  var v = opt.value;
  ctx.dMin = opt.duration.min;
  ctx.dMax = opt.duration.max;
  ctx.R = 20;
  ctx.el = el;
  ctx.v = v;
  ctx.v0 = v;
  ctx.step = 0;
  ctx.r = ctx.R;
  ctx.fmt = (opt.format ? true : false);
  ctx.scale = opt.scale;
  ctx.pfx = opt.prefix;
  ctx.sfx = opt.suffix;
  ctx.tmrId = 0;
  ctx.cb = opt.cb;
  if (opt.text == undefined) {
    ctx.print(ctx, v);
  } else {
    ctx._print(ctx, opt.text);
  }
};
util.Counter.DFLTOPT = {
  value: 0,
  duration: {min: 250, max: 250},
  format: true,
  scale: 0,
  prefix: '',
  suffix: '',
  text: undefined,
  cb: null
};
util.Counter.prototype = {
  getValue: function() {
    return this.v;
  },
  setValue: function(v, dur) {
    var ctx = this;
    v = util.floor(parseFloat(v), ctx.scale);
    ctx._stopTmr(ctx);
    ctx.v = v;
    var dMax = (dur == undefined ? ctx.dMax : dur);
    var dMin = (dur == undefined ? ctx.dMin : dur);
    var ngtvD = 0;
    if (dMax == 0) {
      ctx.print(ctx, v);
      ctx.v0 = v;
      return;
    } else if (dMax < 0) {
      ngtvD = 1;
      dMax *= (-1);
    }
    var d = v - ctx.v0;
    var F = dMax / ctx.R;
    ctx.r = ctx.R;
    var a = Math.abs(d);
    if ((a >= dMax) || (a >= F)) {
      ctx.step = util.ceil(a / F, ctx.scale);
      if (ctx.step % 10 == 0) {
        if (ctx.scale > 0) {
          ctx.step += parseFloat('0.' + util.repeatCh('0', ctx.scale - 1) + '1');
        } else {
          ctx.step++;
        }
      } else if (ctx.scale > 0) {
        ctx.step += parseFloat('0.' + util.repeatCh('0', ctx.scale - 1) + '1');
      }
    } else {
      if (ctx.scale > 0) {
        ctx.step = parseFloat('1.' + util.repeatCh('0', ctx.scale - 1) + '1');
      } else {
        ctx.step = 1;
      }
      if ((a < dMin) && (a != 0)) ctx.r = util.ceil(dMin / a, ctx.scale);
    }
    if (Math.floor(ctx.step) % 10 == 0) ctx.step += 1;
    if (d < 0) ctx.step *= (-1);
    if (ngtvD) {
      var t = (a < dMin ? dMin : dMax);
      ctx.v0 = v;
      ctx.tmrId = setTimeout(ctx.update, t, ctx);
    } else {
      ctx.update(ctx);
    }
  },
  setDurationMin: function(d) {
    this.dMin = d;
    if (this.tmrId > 0) this.setValue(this.v);
  },
  setDurationMax: function(d) {
    this.dMax = d;
    if (this.tmrId > 0) this.setValue(this.v);
  },
  setDuration: function(d) {
    this.dMin = d;
    this.dMax = d;
    if (this.tmrId > 0) this.setValue(this.v);
  },
  up: function() {
    var ctx = this;
    var v = ctx.v;
    if (ctx.scale == 0) {
      v++;
    } else {
      v = util.alignDecimal(v, ctx.scale).replace('.', '');
      v = parseInt(v) + 1;
      var s = v + '';
      v = s.substr(0, s.length - ctx.scale) + '.' + s.slice(ctx.scale * (-1));
    }
    ctx.setValue(v);
  },
  down: function() {
    var ctx = this;
    var v = ctx.v;
    if (ctx.scale == 0) {
      v--;
    } else {
      v = util.alignDecimal(v, ctx.scale).replace('.', '');
      v = parseInt(v);
      if (v <= 0) {
        v = v * (-1) + 1;
        var s = '-' + util.lpad(v, '0', ctx.scale);
      } else {
        v--;
        s = v + '';
      }
      v = s.substr(0, s.length - ctx.scale) + '.' + s.slice(ctx.scale * (-1));
    }
    ctx.setValue(v);
  },
  setText: function(s) {
    var ctx = this;
    ctx._stopTmr(ctx);
    ctx._print(ctx, s);
  },
  update: function(ctx) {
    ctx.tmrId = 0;
    var v = ctx.v0;
    v += ctx.step;
    v = util.round(v, ctx.scale);
    var c = true;
    if (((ctx.step >= 0) && (v > ctx.v)) || ((ctx.step < 0) && (v < ctx.v))) {
      v = ctx.v;
      c = false;
    }
    ctx.v0 = v;
    ctx.print(ctx, v);
    if (c) ctx.tmrId = setTimeout(ctx.update, ctx.r, ctx);
  },
  print: function(ctx, v) {
    var s = v;
    if (ctx.scale > 0) s = util.alignDecimalZero(s, ctx.scale);
    if (ctx.fmt && ((v >= 1000) || (v <= 1000))) s = util.formatNumber(s);
    ctx._print(ctx, ctx.pfx + s + ctx.sfx);
    if (ctx.cb) ctx.cb(v);
  },
  _print: function(ctx, v) {
    ctx.el.innerHTML = v;
  },
  _stopTmr: function(ctx) {
    if (ctx.tmrId > 0) {
      clearTimeout(ctx.tmrId);
      ctx.tmrId = 0;
    }
  }
};

//---------------------------------------------------------
// Interval Proc
//---------------------------------------------------------
// Start  : util.IntervalProc.start('<PROC_ID>', fn, 1000, ARG, [async(true|false)]);
// Stop   : util.IntervalProc.stop('<PROC_ID>');
// Restart: util.IntervalProc.start('<PROC_ID>');
//
// Async:
// -> call in fn: util.IntervalProc.next('<PROC_ID>');
//---------------------------------------------------------
util.IntervalProc = {};

// proc = {
//   id: {
//     fn: function(),
//     interval: millis,
//     arg: argument for fn,
//     async: true|false,
//     tmrId: timer-id
//   }
// }
util.IntervalProc.procs = {};

/**
 * Register an interval proc.
 */
util.IntervalProc.register = function(id, fn, interval, arg, async) {
  util.IntervalProc.procs[id] = {
    fn: fn,
    interval: interval,
    arg: arg,
    async: (async ? true : false),
    tmrId: 0
  };
};

/**
 * Remove an interval proc.
 */
util.IntervalProc.remove = function(id) {
  delete util.IntervalProc.procs[id];
};

/**
 * Start an interval proc.
 */
util.IntervalProc.start = function(id, fn, interval, arg, async) {
  if (fn) util.IntervalProc.register(id, fn, interval, arg, async);
  var p = util.IntervalProc.procs[id];
  if (p) {
    util.IntervalProc._stop(p);
    util.IntervalProc.exec(id);
  }
};

/**
 * Stop an interval proc.
 */
util.IntervalProc.stop = function(id) {
  var p = util.IntervalProc.procs[id];
  if (p) util.IntervalProc._stop(p);
};
util.IntervalProc._stop = function(p) {
  if (p.tmrId > 0) {
    clearTimeout(p.tmrId);
    p.tmrId = 0;
  }
};

/**
 * Sets a timer which executes an interval proc function.
 */
util.IntervalProc.next = function(id, interval) {
  var p = util.IntervalProc.procs[id];
  if (p) {
    util.IntervalProc._stop(p);
    if (interval == undefined) interval = p.interval;
    p.tmrId = setTimeout(util.IntervalProc.exec, interval, id);
  }
};

/**
 * Sets interval in milliseconds.
 */
util.IntervalProc.setInterval = function(id, interval) {
  var p = util.IntervalProc.procs[id];
  if (p) p.interval = interval;
};

/**
 * Execute an interval proc.
 */
util.IntervalProc.exec = function(id) {
  var p = util.IntervalProc.procs[id];
  if (p) {
    p.fn(p.arg);
    if (!p.async) util.IntervalProc.next(id);
  }
};

util.IntervalProc.ids = function() {
  return util.objKeys(util.IntervalProc.procs);
};

//---------------------------------------------------------
// Events
//---------------------------------------------------------
// NAMESPACE.cb = function(ev) {
//   log(ev);
// }
//
// util.event.addListener('EVENT_NAME', NAMESPACE.cb);
//
// var data = {msg: 'abc'};
// util.event.send('EVENT_NAME', data);
//---------------------------------------------------------
util.event = {};
util.event.listeners = {};
util.event.events = [];

util.event.addListener = function(name, fn) {
  if (!util.event.listeners[name]) util.event.listeners[name] = [];
  util.addListItem(util.event.listeners[name], fn);
};

util.event.removeListener = function(name, fn) {
  util.removeListItem(util.event.listeners[name], fn);
};

util.event.send = function(name, data) {
  var e = {name: name, data: data};
  util.event.events.push(e);
  setTimeout(util.event._send, 0);
};

util.event._send = function() {
  var ev = util.event.events.shift();
  if (!ev) return;
  var e = {name: ev.name, data: ev.data};
  var listeners = util.event.listeners[ev.name];
  if (listeners) util.event.callListeners(listeners, e);
  listeners = util.event.listeners['*'];
  if (listeners) util.event.callListeners(listeners, e);
};

util.event.callListeners = function(listeners, e) {
  for (var i = 0; i < listeners.length; i++) {
    var f = listeners[i];
    f(e);
  }
};

//---------------------------------------------------------
// Geo Location
//---------------------------------------------------------
util.geo = {};
util.geo.DFLT_OPT = {
  enableHighAccuracy: true,
  timeout: 5000,
  maximumAge: 0
};
util.geo.watchId = null;
util.geo.count = 0;
util.geo.cbOK = null;
util.geo.cbERR = null;

util.geo.getPosition = function(cbOK, cbERR, opt) {
  if (!opt) opt = util.geo.DFLT_OPT;
  util.geo.cbOK = cbOK;
  util.geo.cbERR = cbERR;
  navigator.geolocation.getCurrentPosition(util.geo.onGetPosOK, util.geo.onGetPosERR, opt);
};

util.geo.onGetPosOK = function(pos) {
  var coords = pos.coords;
  var speed = coords.speed;
  var kmh = null;
  if (speed != null) {
    kmh = speed * 60 * 60 / 1000;
    kmh = Math.round(kmh * 10) / 10;
  }

  var data = {
    'timestamp': pos.timestamp,
    'latitude': coords.latitude,
    'longitude': coords.longitude,
    'altitude': coords.altitude,
    'accuracy': coords.accuracy,
    'altitudeAccuracy': coords.altitudeAccuracy,
    'heading': coords.heading,
    'speed': coords.speed, // m/s
    'speed_kmh': kmh
  };

  if (util.geo.cbOK) util.geo.cbOK(data);
};

util.geo.onGetPosERR = function(err) {
  if (util.geo.cbERR) util.geo.cbERR(err);
};

util.geo.startWatchPosition = function(cbOK, cbERR, opt) {
  if (util.geo.watchId != null) return;
  if (!opt) opt = util.geo.DFLT_OPT;
  util.geo.cbOK = cbOK;
  util.geo.cbERR = cbERR;
  util.geo.watchId = navigator.geolocation.watchPosition(util.geo.onGetPosOK, util.geo.onGetPosERR, opt);
  util.geo.count = 0;
};

util.geo.stopWatchPosition = function() {
  if (util.geo.watchId != null) {
    navigator.geolocation.clearWatch(util.geo.watchId);
    util.geo.watchId = null;
    util.geo.count = 0;
  }
};

// '35.681237, 139.766985'
// -> {'latitude': 35.681237, 'longitude': 139.766985}
util.parseCoordinate = function(location) {
  location = location.replace(/ /g, '');
  var loc = location.split(',');
  var lat = parseFloat(loc[0]);
  var lon = parseFloat(loc[1]);
  var coordinate = {'latitude': lat, 'longitude': lon};
  return coordinate;
};

// '35.681237, 139.766985'
// -> 35.681237
util.latitude = function(location) {
  var c = util.parseCoordinate(location);
  return c['latitude'];
};

// '35.681237, 139.766985'
// -> 139.766985
util.longitude = function(location) {
  var c = util.parseCoordinate(location);
  return c['longitude'];
};

// m/s -> km/h
util.ms2kmh = function(speed) {
  var kmh = speed * 60 * 60 / 1000;
  kmh = Math.round(kmh * 10) / 10;
  return kmh;
};

/**
 * 269, 0, 180 -> false
 * 270, 0, 180 -> true
 *   0, 0, 180 -> true
 *  90, 0, 180 -> true
 *  91, 0, 180 -> false
 */
util.isForwardMovement = function(azimuth, heading, range) {
  azimuth = util.roundAngle(azimuth);
  heading = util.roundAngle(heading);
  range = util.roundAngle(range);
  var rangeL = heading - (range / 2);
  if (rangeL < 0) rangeL += 360;
  var rangeR = heading + (range / 2);
  if (rangeR >= 360) rangeR -= 360;
  if (rangeR < rangeL) {
    if ((azimuth >= rangeL) || (azimuth <= rangeR)) return true;
  } else {
    if ((azimuth >= rangeL) && (azimuth <= rangeR)) return true;
  }
  return false;
};

//---------------------------------------------------------
util.calcMod10 = function(s, w) {
  var a = s.split('');
  var v = [0, 0];
  var d = 0;
  for (var i = a.length - 1; i >= 0; i--) {
    v[d % 2] += a[i] | 0;
    d++;
  }
  var c = v[0] * w + v[1];
  return ((10 - (c % 10)) % 10);
};

util.calcMod11 = function(s) {
  var a = s.split('');
  var n = 0;
  var d = 0;
  for (var i = a.length - 1; i >= 0; i--) {
    var v = a[i] | 0;
    n += v * ((d % 6) + 2);
    d++;
  }
  var c = n % 11;
  return (((c == 0) || (c == 1)) ? 0 : 11 - c);
};

util.calcMod43 = function(s) {
  var MOD43TBL = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%';
  if (s.match(/^".+"$/)) {
    try {
      s = eval(s);
    } catch (e) {return '';}
  }
  var a = s.split('');
  var n = 0;
  for (var i = 0; i < a.length; i++) {
    var v = a[i];
    if (v == '*') {
      if ((i == 0) || (i == a.length - 1)) {
        continue;
      } else {
        return '';
      }
    }
    var b = MOD43TBL.indexOf(v);
    if (b == -1) return '';
    n += b;
  }
  return MOD43TBL.charAt(n % 43);
};

//---------------------------------------------------------
util.onMouseMove = function(e) {
  var x = e.clientX;
  var y = e.clientY;
  util.mouseX = x;
  util.mouseY = y;
  util.infotip.onMouseMove(x, y);
  util.tooltip.onMouseMove(x, y);
  util.Window.onMouseMove(x, y);
};

util.onMouseUp = function(e) {
  if (e.button == 0) util.Window.onPointerUp();
};

util.onTouchMove = function(e) {
  var e0 = e.changedTouches[0];
  var x = e0.clientX;
  var y = e0.clientY;
  util.Window.onMouseMove(x, y);
};

util.onTouchEnd = function() {
  util.Window.onPointerUp();
};

//---------------------------------------------------------
util.keyHandlers = {down: [], press: [], up: []};

/**
 * keyCode: code / 'A'
 * type: down / press / up
 * fn: function(e) {};
 * combination: {ctrl: true, shift: false, alt: false, meta: false};
 * addKeyHandler(65, 'down', fn, combination);
 */
util.addKeyHandler = function(keyCode, type, fn, combination) {
  if (typeof keyCode == 'string') keyCode = keyCode.toUpperCase().charCodeAt(0);
  if ((type != 'down') && (type != 'press') && (type != 'up')) return;
  var handler = {keyCode: keyCode, combination: combination, fn: fn};
  util.keyHandlers[type].push(handler);
};

util.onKeyDown = function(e) {
  util.callKeyHandlers(e, 'down');
};

util.onKeyPress = function(e) {
  util.callKeyHandlers(e, 'press');
};

util.onKeyUp = function(e) {
  util.callKeyHandlers(e, 'up');
};

util.callKeyHandlers = function(e, type) {
  var handlers = util.keyHandlers[type];
  for (var i = 0; i < handlers.length; i++) {
    var handler = handlers[i];
    if (util.isTargetKey(e, handler)) {
      if (handler.fn) handler.fn(e);
    }
  }
};

util.isTargetKey = function(e, handler) {
  if (handler.keyCode == e.keyCode) {
    var combination = handler.combination;
    if (!combination ||
        ((combination.shift == undefined) || (e.shiftKey == combination.shift)) &&
        ((combination.ctrl == undefined) || (e.ctrlKey == combination.ctrl)) &&
        ((combination.alt == undefined) || (e.altKey == combination.alt)) &&
        ((combination.meta == undefined) || (e.metaKey == combination.meta))) {
      return true;
    }
  }
  return false;
};

util.addEnterKeyHandler = function(fn) {
  util.addKeyHandler(13, 'down', fn);
};

util.addEscKeyHandler = function(fn) {
  util.addKeyHandler(27, 'down', fn);
};

// k: keyCode or 'A-Za-z0-9'
util.addCtrlKeyHandler = function(k, fn) {
  util.addKeyHandler(k, 'down', fn, {ctrl: true, shift: false, alt: false, meta: false});
};

//---------------------------------------------------------
/**
 * cb = function(data, file)
 * opt = {
 *   mode: 'txt'|'b64'|'data'(Data URL)|'bin'(Uint8Array)|'blob'([object ArrayBuffer])|'evt'([object DragEvent])
 *   onloadstart: function(file),
 *   onprogress: function(e, loaded, total, pct),
 *   onload: function(data, file),
 *   onabort: function(),
 *   onerror: function(e) {
 *     e.target.error.code:
 *       e.target.error.NOT_FOUND_ERR
 *       e.target.error.SECURITY_ERR
 *       e.target.error.NOT_READABLE_ERR
 *       e.target.error.ABORT_ERR
 *   }
 * };
 */
util.addDndHandler = function(el, cb, opt) {
  el = util.getElement(el);
  if (!el) return;
  el.addEventListener('dragover', util.dnd.onDragOver, false);
  el.addEventListener('drop', util.dnd.onDrop, false);
  var o = new util.DndHandler(el, cb, opt);
  util.dnd.handlers.push(o);
  return o;
};
util.DndHandler = function(el, cb, opt) {
  if (!opt) opt = {};
  this.el = el;
  this.cb = cb;
  this.mode = (opt.mode ? opt.mode : 'txt');
  this.onloadstart = opt.onloadstart;
  this.onprogress = opt.onprogress;
  this.onload = opt.onload;
  this.onabort = opt.onabort;
  this.onerror = opt.onerror;
};
util.DndHandler.prototype = {
  setMode: function(mode) {
    this.mode = mode;
  }
};

util.dnd = {};
util.dnd.handlers = [];
util.fileLoader = null;
util.dnd.onDragOver = function(e) {
  e.stopPropagation();
  e.preventDefault();
  e.dataTransfer.dropEffect = 'copy';
};
util.dnd.onDrop = function(e) {
  e.stopPropagation();
  e.preventDefault();
  var handlers = util.dnd.handlers;
  var handler = null;
  for (var i = 0; i < handlers.length; i++) {
    handler = handlers[i];
    if (util.isTargetEl(e.target, handler.el)) break;
  }
  if (i == handlers) return;
  var cb = handler.cb;
  if (handler.mode == 'evt') {
    if (cb) cb(e);
  } else {
    var d = e.dataTransfer.getData('text');
    if (d) {
      if (cb) cb(d);
    } else {
      util.dnd.handleDroppedFile(e, handler);
    }
  }
};

util.dnd.handleDroppedFile = function(e, handler) {
  try {
    if (!e.dataTransfer.files) return;
    if (e.dataTransfer.files.length > 0) {
      var f = e.dataTransfer.files[0];
      if (f) util.loadFile(f, handler);
    } else {
      if (handler.cb) handler.cb('');
    }
  } catch (x) {}
};

util.loadFile = function(file, handler) {
  var fr = new FileReader();
  fr.onload = util.onFileLoaded;
  fr.onloadstart = util.onFileLoadStart;
  fr.onprogress = util.onFileLoadProg;
  fr.onabort = util.onFileLoadAbort;
  fr.onerror = util.onFileLoadError;
  util.fileLoader = {file: file, reader: fr, handler: handler};
  var mode = handler.mode;
  if ((mode == 'bin') || (mode == 'blob')) {
    fr.readAsArrayBuffer(file);
  } else if ((mode == 'b64') || (mode == 'data')) {
    fr.readAsDataURL(file);
  } else {
    fr.readAsText(file);
  }
};

util.onFileLoadStart = function() {
  var loader = util.fileLoader;
  var file = loader.file;
  var cb = util.fileLoader.handler.onloadstart;
  if (cb) cb(file);
};

util.onFileLoadProg = function(e) {
  if (e.lengthComputable) {
    var total = e.total;
    var loaded = e.loaded;
    var pct = (total == 0) ? 100 : Math.round((loaded / total) * 100);
  }
  var cb = util.fileLoader.handler.onprogress;
  if (cb) cb(e, loaded, total, pct);
};

util.onFileLoaded = function() {
  var loader = util.fileLoader;
  var fr = loader.reader;
  var file = loader.file;
  var content = '';
  try {
    if (fr.result != null) content = fr.result;
  } catch (e) {}
  var handler = loader.handler;

  var mode = handler.mode;
  if (mode == 'b64') {
    content = util.getDataUrlBody(content);
  } else if (mode == 'bin') {
    content = new Uint8Array(content);
  }

  var cb = util.fileLoader.handler.onload;
  if (!cb) cb = handler.cb;
  if (cb) cb(content, file);
  util.finalizeFileLoad();
};

util.onFileLoadAbort = function() {
  var cb = util.fileLoader.handler.onabort;
  if (cb) cb();
  util.finalizeFileLoad();
};

util.onFileLoadError = function(e) {
  var cb = util.fileLoader.handler.onerror;
  if (cb) cb(e);
  util.finalizeFileLoad();
};

util.finalizeFileLoad = function() {
  util.fileLoader = null;
};

util.getDataUrlBody = function(d) {
  return d.split(',')[1];
};

util.decodeDataUrl = function(d) {
  return util.decodeBase64(util.getDataUrlBody(d));
};

util.isTargetEl = function(el, tgt) {
  do {
    if (el == tgt) return true;
    el = el.parentNode;
  } while (el != null);
  return false;
};

//---------------------------------------------------------
util._log = function(o) {
  if (window.log) window.log(o);
};
util._log.v = function(o) {
  if (window.log) window.log.v(o);
};
util._log.d = function(o) {
  if (window.log) window.log.d(o);
};
util._log.i = function(o) {
  if (window.log) window.log.i(o);
};
util._log.w = function(o) {
  if (window.log) window.log.w(o);
};
util._log.e = function(o) {
  if (window.log) window.log.e(o);
};

//---------------------------------------------------------
util.onResize = function(e) {
  util.infotip.adjust();
  util.dialog.adjust();
  util.Window.onResize(e);
};

util.$onReady = function(e) {
  util.setupStyle();
  var fn = window.$onReady;
  if (fn) fn(e);
};
util.$onLoad = function(e) {
  var fn = window.$onLoad;
  if (fn) fn(e);
};
util.$onBeforeUnload = function(e) {
  var fn = window.$onBeforeUnload;
  if (fn) fn(e);
};
util.$onUnload = function(e) {
  var fn = window.$onUnload;
  if (fn) fn(e);
};
util.$onKeyDown = function(e) {
  var p = 0;
  if (e.keyCode == 13) {
    if (window.$onEnterKey) window.$onEnterKey(e);
  } else if (e.keyCode == 27) {
    if (window.$onEscKey) window.$onEscKey(e);
  } else if ((e.keyCode == 83) && e.ctrlKey) {
    if (window.$onCtrlS) {p = 1;window.$onCtrlS(e);}
  }
  if (p) e.preventDefault();
  var fn = window.$onKeyDown;
  if (fn) fn(e);
};
util.$onKeyPress = function(e) {
  var fn = window.$onKeyPress;
  if (fn) fn(e);
};
util.$onKeyUp = function(e) {
  var fn = window.$onKeyUp;
  if (fn) fn(e);
};
util.$onMouseDown = function(e) {
  var fn = window.$onMouseDown;
  if (fn) fn(e);
};
util.$onClick = function(e) {
  var fn = window.$onClick;
  if (fn) fn(e);
};
util.$onMouseUp = function(e) {
  var fn = window.$onMouseUp;
  if (fn) fn(e);
};
util.$onMouseMove = function(e) {
  var fn = window.$onMouseMove;
  if (fn) fn(e);
};
util.$onResize = function(e) {
  var fn = window.$onResize;
  if (fn) fn(e);
};
util.$onScroll = function(e) {
  var fn = window.$onScroll;
  if (fn) fn(e);
};

//---------------------------------------------------------
var $dbg = {};
util.debug = 0;

util.objKeys = function(o) {
  var a = [];
  for (var k in o) {
    a.push(k);
  }
  return a;
};

util.callFn = function(f, a1, a2, a3) {
  if (f) return f(a1, a2, a3);
};

util.getElRelObj = function(objs, el) {
  if (!el) return null;
  for (var k in objs) {
    var o = objs[k];
    if (o.el == el) return o;
  }
  return null;
};

util.nop = function() {};

//---------------------------------------------------------
util.init = function() {
  try {
    if (typeof window.localStorage != 'undefined') util.LS_AVAILABLE = true;
  } catch (e) {}
  window.addEventListener('mousemove', util.onMouseMove, true);
  window.addEventListener('mouseup', util.onMouseUp, true);
  window.addEventListener('keydown', util.onKeyDown, true);
  window.addEventListener('keypress', util.onKeyPress, true);
  window.addEventListener('keyup', util.onKeyUp, true);
  window.addEventListener('resize', util.onResize, true);
  window.addEventListener('touchmove', util.onTouchMove, true);
  window.addEventListener('touchend', util.onTouchEnd, true);

  window.addEventListener('DOMContentLoaded', util.$onReady, true);
  window.addEventListener('load', util.$onLoad, true);
  window.addEventListener('beforeunload', util.$onBeforeUnload, true);
  window.addEventListener('unload', util.$onUnload, true);
  window.addEventListener('keydown', util.$onKeyDown, true);
  window.addEventListener('keypress', util.$onKeyPress, true);
  window.addEventListener('keyup', util.$onKeyUp, true);
  window.addEventListener('mousedown', util.$onMouseDown, true);
  window.addEventListener('click', util.$onClick, true);
  window.addEventListener('mouseup', util.$onMouseUp, true);
  window.addEventListener('mousemove', util.$onMouseMove, true);
  window.addEventListener('resize', util.$onResize, true);
  window.addEventListener('scroll', util.$onScroll, true);
};
util.init();
