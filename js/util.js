/*!
 * util.js
 * Copyright 2019 Takashi Harano
 * Released under the MIT license
 * https://github.com/takashiharano/util
 */
var util = util || {};
util.v = '202009160006';

util.DFLT_FADE_SPEED = 500;
util.LS_AVAILABLE = false;
util.mouseX = 0;
util.mouseY = 0;

/*\
|*| Polyfill which enables the passage of arbitrary arguments to the
|*| callback functions of JavaScript timers (HTML5 standard syntax).
|*|
|*| https://developer.mozilla.org/en-US/docs/DOM/window.setInterval
|*|
|*| Syntax:
|*| var timeoutID = window.setTimeout(func, delay[, param1, param2, ...]);
|*| var timeoutID = window.setTimeout(code, delay);
|*| var intervalID = window.setInterval(func, delay[, param1, param2, ...]);
|*| var intervalID = window.setInterval(code, delay);
\*/
(function() {
  setTimeout(function(arg1) {
    if (arg1 === 'test') {
      // feature test is passed, no need for polyfill
      return;
    }
    var __nativeST__ = window.setTimeout;
    window.setTimeout = function(vCallback, nDelay /*, argumentToPass1, argumentToPass2, etc. */) {
      var aArgs = Array.prototype.slice.call(arguments, 2);
      return __nativeST__(vCallback instanceof Function ? function() {
        vCallback.apply(null, aArgs);
      } : vCallback, nDelay);
    };
  }, 0, 'test');

  var interval = setInterval(function(arg1) {
    clearInterval(interval);
    if (arg1 === 'test') {
      // feature test is passed, no need for polyfill
      return;
    }
    var __nativeSI__ = window.setInterval;
    window.setInterval = function(vCallback, nDelay /*, argumentToPass1, argumentToPass2, etc. */) {
      var aArgs = Array.prototype.slice.call(arguments, 2);
      return __nativeSI__(vCallback instanceof Function ? function() {
        vCallback.apply(null, aArgs);
      } : vCallback, nDelay);
    };
  }, 0, 'test');
}());

//-----------------------------------------------------------------------------
// Date & Time
//-----------------------------------------------------------------------------
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

/**
 * DateTime class
 * dt
 *  Date / millis from 1970-01-01T00:00:00Z / date-string
 * offset
 *  minutes (-480=-0800 / 0=+0000 / 540=+0900)
 */
util.DateTime = function(dt, offset) {
  if ((dt !== 0) && !dt) {
    dt = new Date();
  } else if (!(dt instanceof Date)) {
    dt = new Date(dt);
  }
  this.timestamp = dt.getTime();
  var os = dt.getTimezoneOffset();
  if (offset == undefined) {
    this.offset = os * (-1);
  } else {
    this.offset = offset;
    var ts = this.timestamp + (os + offset) * 60000;
    dt = new Date(ts);
  }
  var year = dt.getFullYear();
  var month = dt.getMonth() + 1;
  var day = dt.getDate();
  var hours = dt.getHours();
  var minutes = dt.getMinutes();
  var seconds = dt.getSeconds();
  var milliseconds = dt.getMilliseconds();

  this.year = year;
  this.month = month;
  this.day = day;
  this.hours = hours;
  this.minutes = minutes;
  this.seconds = seconds;
  this.milliseconds = milliseconds;

  this.yyyy = year + '';
  this.mm = ('0' + month).slice(-2);
  this.dd = ('0' + day).slice(-2);
  this.hh = ('0' + hours).slice(-2);
  this.mi = ('0' + minutes).slice(-2);
  this.ss = ('0' + seconds).slice(-2);
  this.sss = ('00' + milliseconds).slice(-3);
  this.wday = dt.getDay(); // Sunday - Saturday : 0 - 6
  this.WDAYS = util.WDAYS;
};
util.DateTime.prototype = {
  setWdays: function(wdays) {
    this.WDAYS = wdays;
  },
  // -> '+0900' : ext=true -> '+09:00'
  getTZ: function(ext) {
    return util.formatTZ(this.offset, ext);
  },
  toString: function(fmt) {
    if (!fmt) fmt = '%Y-%M-%D %H:%m:%S.%s %Z';
    var s = fmt;
    s = s.replace(/%Y/, this.yyyy);
    s = s.replace(/%M/, this.mm);
    s = s.replace(/%D/, this.dd);
    s = s.replace(/%W/, this.WDAYS[this.wday]);
    s = s.replace(/%H/, this.hh);
    s = s.replace(/%m/, this.mi);
    s = s.replace(/%S/, this.ss);
    s = s.replace(/%s/, this.sss);
    s = s.replace(/%z/, this.getTZ());
    s = s.replace(/%Z/, this.getTZ(true));
    s = s.replace(/%N/, util.getTzName());
    return s;
  }
};

/**
 * Returns DateTime object
 * dt: timestamp / Date object
 */
util.getDateTime = function(dt, ofst) {
  return new util.DateTime(dt, ofst);
};

/**
 * Returns current timestamp
 */
util.now = function() {
  return new Date().getTime();
};

/**
 * Returns Date-Time string
 * t: timestamp / Date object
 * fmt: '%Y-%M-%D %H:%m:%S.%s'
 */
util.getDateTimeString = function(t, fmt) {
  var dt = new util.DateTime(t);
  return dt.toString(fmt);
};

/**
 * '12:34:56.987' -> DateTime object
 * offset: -1 -> Yesterday
 *          0 -> Today (default)
 *          1 -> Tomorrow
 */
util.getDateTimeFromTime = function(timeString, offset) {
  var ts = util.getTimeStampOfDay(timeString, offset);
  return util.getDateTime(ts);
};

/**
 * '12:34:56.987' -> timestamp (millis from 1970-01-01T00:00:00Z)
 * offset: -1 -> Yesterday
 *          0 -> Today (default)
 *          1 -> Tomorrow
 */
util.getTimeStampOfDay = function(timeString, offset) {
  var tm = timeString.replace(/:/g, '').replace(/\./, '');
  var hh = tm.substr(0, 2);
  var mi = tm.substr(2, 2);
  var ss = tm.substr(4, 2);
  var sss = tm.substr(6, 3);
  var d = util.getDateTime();
  var d1 = new Date(d.yyyy, (d.mm | 0) - 1, d.dd, hh, mi, ss, sss);
  var ts = d1.getTime();
  if (offset != undefined) {
    ts += (offset * util.DAY);
  }
  return ts;
};

/**
 * millis to struct
 * -> {'sign': false, 'days': 1, 'hrs': 0, 'hours': 24, 'minutes': 34, 'seconds': 56, 'milliseconds': 123}
 */
util.ms2struct = function(ms) {
  var wk = ms;
  var sign = false;
  if (ms < 0) {
    sign = true;
    wk *= (-1);
  }
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
    sign: sign,
    days: d,
    hrs: hh - d * 24,
    hours: hh,
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
  if (!toString) {
    s = parseFloat(s);
  }
  return s;
};

/**
 * Returns time zone offset string from minutes
 * -480       -> -0800
 *  540       -> +0900
 *  540, true -> +09:00
 */
util.formatTZ = function(v, e) {
  v |= 0;
  var s = '+';
  if (v < 0) {
    s = '-';
    v *= -1;
  }
  var h = (v / 60) | 0;
  var m = v - h * 60;
  var str = s + ('0' + h).slice(-2) + ('0' + m).slice(-2);
  if (e) str = str.substr(0, 3) + ':' + str.substr(3, 2);
  return str;
};

/**
 * Returns local time zone offset string
 * +0900
 * ext=true: +09:00
 */
util.getTZ = function(ext) {
  return util.formatTZ((new Date()).getTimezoneOffset() * (-1), ext);
};

/**
 * Returns TZ database name
 * i.e., America/Los_Angeles
 */
util.getTzName = function() {
  var n = Intl.DateTimeFormat().resolvedOptions().timeZone;
  if (!n) n = '';
  return n;
};

/**
 * Returns TZ offset in minutes
 * +0900 ->  540
 * -0800 -> -480
 */
util.getTzOffset = function() {
  return (new Date()).getTimezoneOffset() * -1;
};

/**
 * baseline, comparisonValue, abs(opt)
 * 1581217200000, 1581066000000 -> -1 (1: abs=true)
 * 1581217200000, 1581217200000 ->  0
 * 1581217200000, 1581318000000 ->  1
 */
util.diffDays = function(ms1, ms2, abs) {
  var sign = 1;
  var d = ms2 - ms1;
  if (d < 0) {
    d *= -1;
    if (!abs) sign = -1;
  }
  return Math.floor(d / 86400000) * sign;
};

//-----------------------------------------------------------------------------
/**
 * Time class
 * t:
 *   millis or '12:34:56.789'
 */
util.Time = function(t) {
  if (typeof t == 'string') {
    // HH:MI:SS.sss
    var wk = t.split('.');
    var sss = wk[1] | 0;
    if (sss) {
      sss = (sss + '000').substr(0, 3) | 0;
    }
    wk = wk[0].split(':');
    var hh = wk[0] | 0;
    var mi = wk[1] | 0;
    var ss = wk[2] | 0;
    t = (hh * 3600 + mi * 60 + ss) * 1000 + sss;
  }
  this.millis = t;
  var tm = util.ms2struct(t);
  this.sign = tm.sign;
  this.days = tm.days;
  this.hrs = tm.hrs;
  this.hours = tm.hours;
  this.minutes = tm.minutes;
  this.seconds = tm.seconds;
  this.milliseconds = tm.milliseconds;
};
util.Time.prototype = {
  /**
   * To clock format
   *
   * fmt
   *  'DHMSs'
   */
  toClock: function(fmt) {
    if (!fmt) fmt = 'HMSs';
    var d = this.days;
    var h = this.hours;
    var m = this.minutes;
    var s = this.seconds;
    var ms = this.milliseconds;

    if (fmt.match(/D/)) h = this.hrs;
    if (!fmt.match(/H/)) m += h * 60;
    if (!fmt.match(/M/)) s += m * 60;
    if (!fmt.match(/S/)) ms += s * 1000;

    d += '';
    h += '';
    m += '';
    s += '';
    ms += '';

    if (h < 10) h = '0' + h;
    m = ('0' + m).slice(-2);
    s = ('0' + s).slice(-2);
    ms = ('00' + ms).slice(-3);

    var r = '';
    if (fmt.match(/D/)) r += d + 'd ';
    if (fmt.match(/H/)) r += h + ':';
    if (fmt.match(/M/)) r += m;
    if (fmt.match(/S/)) r += ':' + s;
    if (fmt.match(/s/)) r += '.' + ms;
    return r;
  },

  /**
   * To string the time.
   *
   * 1d 23h 45m 59s
   * h:
   *   >= 24h instead of days
   *   true: 47h 45m 59s
   * f:
   *   to display millis
   *   true: 1d 23h 45m 59s 123
   */
  toString: function(h, f) {
    var r = '';
    var d = 0;
    if (!h && (this.days > 0)) {
      d = 1;
      r += this.days + 'd ';
    }
    if (h && (this.hours > 0)) {
      d = 1;
      r += this.hours + 'h ';
    } else if (d || (this.hrs > 0)) {
      d = 1;
      r += this.hrs + 'h ';
    }
    if (d || (this.minutes > 0)) {
      d = 1;
      r += this.minutes + 'm ';
    }
    r += this.seconds + 's';
    if (f) r += ' ' + ('00' + this.milliseconds).slice(-3);
    return r;
  }
};

/**
 * Millis to a string. (171959000 -> '1d 23h 45m 59s')
 *
 * h:
 *   >= 24h instead of days
 *   true: 47h 45m 59s
 * f:
 *   to display millis
 *   true: 1d 23h 45m 59s 123
 */
util.getTimeString = function(ms, h, f) {
  return new util.Time(ms).toString(h, f);
};

//------------------------------------------------
// Time calculation
//------------------------------------------------
/**
 * ClockTime Class
 */
util.ClockTime = function(msecs, days, integratedSt, clocklikeSt) {
  this.msecs = msecs;
  this.days = days;
  this.integratedSt = integratedSt;
  this.clocklikeSt = clocklikeSt;
};
util.ClockTime.prototype = {
  // %H:%m            '25:00'
  // %H:%m:%S         '25:00:00'
  // %H:%m:%S.%s      '25:00:00.000'
  // %H:%m:%S.%s (%d) '01:00:00.000 (+1 Day)'
  toString: function(fmt) {
    if (!fmt) fmt = '%H:%m:%S.%s';
    var byTheDay = fmt.match(/%d/) != null;

    var h = this.toHrStr(byTheDay);
    var m = this.toMinStr(byTheDay);
    var s = this.toSecStr(byTheDay);
    var ms = this.toMilliSecStr(byTheDay);

    if ((this.msecs < 0) && !byTheDay) {
      h = '-' + h;
    }

    var r = fmt;
    r = r.replace(/%H/, h);
    r = r.replace(/%m/, m);
    r = r.replace(/%S/, s);
    r = r.replace(/%s/, ms);

    if (byTheDay && (this.days > 0)) {
      var d = this.toDaysStr();
      r = r.replace(/%d/, d);
    }
    return r;
  },

  toDaysStr: function() {
    var days;
    if (this.msecs < 0) {
      days = '-';
    } else {
      days = '+';
    }
    days += this.days + ' ' + util.plural('Day', this.days);
    return days;
  },

  toHrStr: function(byTheDay) {
    var h;
    var hh;
    if (byTheDay === undefined) {
      byTheDay = false;
    }
    if (byTheDay) {
      h = this.clocklikeSt['hrs'];
    } else {
      h = this.integratedSt['hours'];
    }
    if (h < 10) {
      hh = ('0' + h).slice(-2);
    } else {
      hh = h + '';
    }
    return hh;
  },

  toMinStr: function(byTheDay) {
    if (byTheDay === undefined) {
      byTheDay = false;
    }
    var st = (byTheDay ? this.clocklikeSt : this.integratedSt);
    return ('0' + st['minutes']).slice(-2);
  },

  toSecStr: function(byTheDay) {
    if (byTheDay === undefined) {
      byTheDay = false;
    }
    var st = (byTheDay ? this.clocklikeSt : this.integratedSt);
    return ('0' + st['seconds']).slice(-2);
  },

  toMilliSecStr: function(byTheDay) {
    if (byTheDay === undefined) {
      byTheDay = false;
    }
    var st = (byTheDay ? this.clocklikeSt : this.integratedSt);
    return ('00' + (st['milliseconds'] | 0)).slice(-3);
  }
};

/**
 * Add time
 * '12:00' + '01:30' -> '13:30'
 * '12:00' + '13:00' -> '01:00 (+1 Day)' / '25:00'
 * fmt:
 * '10:00:00.000 (+1 Day)'
 *  %H:%m:%S.%s (%d)
 */
util.addTime = function(t1, t2, fmt) {
  if (!fmt) fmt = '%H:%m';
  var ms1 = util.time2msec(t1);
  var ms2 = util.time2msec(t2);
  var t = util._addTime(ms1, ms2);
  return t.toString(fmt);
};
// Returns ClockTime object
util._addTime = function(ms1, ms2) {
  var totalMillis = ms1 + ms2;
  var wkMillis = totalMillis;
  var days = 0;
  if (wkMillis >= util.DAY) {
    days = (wkMillis / util.DAY) | 0;
    wkMillis -= days * util.DAY;
  }
  return util._calcTime(totalMillis, wkMillis, days);
};

/**
 * Sub time
 * '12:00' - '01:30' -> '10:30'
 * '12:00' - '13:00' -> '23:00 (-1 Day)' / '-01:00'
 * fmt:
 * '10:00:00.000 (-1 Day)'
 *  %H:%m:%S.%s (%d)
 */
util.subTime = function(t1, t2, fmt) {
  if (!fmt) fmt = '%H:%m';
  var ms1 = util.time2msec(t1);
  var ms2 = util.time2msec(t2);
  var t = util._subTime(ms1, ms2);
  return t.toString(fmt);
};
// Returns ClockTime object
util._subTime = function(ms1, ms2) {
  var totalMillis = ms1 - ms2;
  var wkMillis = totalMillis;
  var days = 0;
  if (wkMillis < 0) {
    wkMillis *= -1;
    days = (wkMillis / util.DAY) | 0;
    days = days + ((wkMillis % util.DAY == 0) ? 0 : 1);
    if (ms1 != 0) {
      if ((wkMillis % util.DAY == 0) && (wkMillis != util.DAY)) {
        days += 1;
      }
    }
    wkMillis = util.DAY - (wkMillis - days * util.DAY);
  }
  return util._calcTime(totalMillis, wkMillis, days);
};

/**
 * Multiply time
 * '01:30' * 2 -> '03:00'
 * '12:00' * 3 -> '12:00 (+1 Day)' / '36:00'
 * fmt:
 * '10:00:00.000 (+1 Day)'
 *  %H:%m:%S.%s (%d)
 */
util.multiTime = function(t, v, fmt) {
  if (!fmt) fmt = '%H:%m';
  var ms = util.time2msec(t);
  var c = util._multiTime(ms, v);
  return c.toString(fmt);
};
// Returns ClockTime object
util._multiTime = function(ms, v) {
  var totalMillis = ms * v;
  var wkMillis = totalMillis;
  var days = 0;
  if (wkMillis >= util.DAY) {
    days = (wkMillis / util.DAY) | 0;
    wkMillis -= days * util.DAY;
  }
  return util._calcTime(totalMillis, wkMillis, days);
};

/**
 * Divide time
 * '03:00' / 2 -> '01:30'
 * '72:00' / 3 -> '00:00 (+1 Day)' / '24:00'
 * fmt:
 * '10:00:00.000 (+1 Day)'
 *  %H:%m:%S.%s (%d)
 */
util.divTime = function(t, v, fmt) {
  if (!fmt) fmt = '%H:%m';
  var ms = util.time2msec(t);
  var c = util._divTime(ms, v);
  return c.toString(fmt);
};
// Returns ClockTime object
util._divTime = function(ms, v) {
  var totalMillis = ms / v;
  var wkMillis = totalMillis;
  var days = 0;
  if (wkMillis >= util.DAY) {
    days = (wkMillis / util.DAY) | 0;
    wkMillis -= days * util.DAY;
  }
  return util._calcTime(totalMillis, wkMillis, days);
};

// Calc time (convert to struct)
util._calcTime = function(totalMillis, wkMillis, days) {
  var integratedSt = util.ms2struct(totalMillis);
  var clocklikeSt = util.ms2struct(wkMillis);
  var ret = new util.ClockTime(totalMillis, days, integratedSt, clocklikeSt);
  return ret;
};

// '09:00', '10:00' -> -1
// '10:00', '10:00' -> 0
// '10:00', '09:00' -> 1
util.timecmp = function(t1, t2) {
  var ms1 = util.time2msec(t1);
  var ms2 = util.time2msec(t2);
  var d = ms1 - ms2;
  if (d == 0) {
    return 0;
  } else if (d < 0) {
    return -1;
  }
  return 1;
};

// timeStr: 'HH:MI:SS.sss'
// '01:00'        -> 3600000
// '01:00:30'     -> 3630000
// '01:00:30.123' -> 3630123
// '0100'         -> 3600000
// '010030'       -> 3630000
// '010030.123'   -> 3630123
util.time2msec = function(timeStr) {
  var hour = 0;
  var min = 0;
  var sec = 0;
  var msec = 0;
  var wkSs = '0';
  var wkTimes;
  var sPrt;
  var tm;

  if (timeStr.match(/:/)) {
    wkTimes = timeStr.split(':');
    if (wkTimes.length == 3) {
      hour = wkTimes[0] | 0;
      min = wkTimes[1] | 0;
      wkSs = wkTimes[2];
    } else if (wkTimes.length == 2) {
      hour = wkTimes[0] | 0;
      min = wkTimes[1] | 0;
    } else {
      return -1;
    }
    sPrt = wkSs.split('.');
    sec = sPrt[0] | 0;
    if (sPrt.length >= 2) {
      msec = sPrt[1] | 0;
    }
  } else {
    tm = timeStr.split('.');
    wkTimes = tm[0];
    if (tm.length >= 2) {
      msec = tm[1] | 0;
    }

    if (wkTimes.length == 6) {
      hour = wkTimes.substr(0, 2) | 0;
      min = wkTimes.substr(2, 2) | 0;
      sec = wkTimes.substr(4, 2) | 0;
    } else if (wkTimes.length == 4) {
      hour = wkTimes.substr(0, 2) | 0;
      min = wkTimes.substr(2, 2) | 0;
    } else {
      return -1;
    }
  }

  var time = (hour * util.HOUR) + (min * util.MINUTE) + sec * 1000 + msec;
  return time;
};

//-----------------------------------------------------------------------------
// ['0000', '12:00', '1530'] ->
// {
//   time: '12:00',
//   datetime: DateTime object
// }
util.calcNextTime = function(times) {
  var now = util.getDateTime();
  times.sort();
  var ret = {
    time: null,
    datetime: null
  };
  for (var i = 0; i < times.length; i++) {
    var t = times[i];
    t = t.replace(/T/, '').replace(/:/g, '');
    var tmstr = t.substr(0, 2) + t.substr(2, 2) + '5959.999';
    var tgt = util.getDateTimeFromTime(tmstr);
    if (now.timestamp <= tgt.timestamp) {
      ret.time = times[i];
      ret.datetime = tgt;
      return ret;
    }
  }
  ret.time = times[0];
  ret.datetime = util.getDateTimeFromTime(times[0], 1);
  return ret;
};

//-----------------------------------------------------------------------------
/**
 * 0.12345, 3 -> 0.123
 * 0.12345, 4 -> 0.1235
 * 12345, -1  -> 12350
 * 12345, -2-  > 12300
 */
util.round = function(number, precision) {
  precision |= 0;
  return util._shift(Math.round(util._shift(number, precision, false)), precision, true);
};

util._shift = function(number, precision, reverseShift) {
  if (reverseShift) {
    precision = -precision;
  }
  var numArray = ('' + number).split('e');
  return +(numArray[0] + 'e' + (numArray[1] ? (+numArray[1] + precision) : precision));
};

// 123   , 1 -> '123.0'
// 123.4 , 1 -> '123.4'
// 123.45, 1 -> '123.5'
util.decimalAlignment = function(v, scale, zero) {
  v = util.round(v, scale);
  if (zero && v == 0) return 0;
  v = util.decimalPadding(v, scale);
  return v;
};

// 123  , 1 -> '123.0'
// 123  , 2 -> '123.00'
// 123.4, 1 -> '123.4'
// 123.4, 2 -> '123.40'
util.decimalPadding = function(v, scale) {
  var r = v + '';
  if (scale == undefined) scale = 1;
  if (scale <= 0) return r;
  var w = r.split('.');
  var i = w[0];
  var d = (w[1] == undefined ? '' : w[1]);
  d = util.strPadding(d, '0', scale, 'R');
  r = i + '.' + d;
  return r;
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

//-----------------------------------------------------------------------------
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
 * getRandomString(len)
 * getRandomString(tbl)
 * getRandomString(tbl, len)
 * getRandomString(tbl, minLen, maxLen)
 */
util.getRandomString = function(a1, a2, a3) {
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
  if (typeof a3 == 'number') {
    max = a3;
  }
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

//-----------------------------------------------------------------------------
util.fromJSON = function(j, r) {
  if (!j) return j;
  return JSON.parse(j, r);
};

util.toJSON = function(o, r, s) {
  return JSON.stringify(o, r, s);
};

util.copyProps = function(src, dst) {
  for (var k in src) {
    dst[k] = src[k];
  }
};

util.loadObject = function(key) {
  if (util.LS_AVAILABLE) {
    return JSON.parse(localStorage.getItem(key));
  }
  return null;
};

util.saveObject = function(key, obj) {
  if (util.LS_AVAILABLE) {
    localStorage.setItem(key, JSON.stringify(obj));
  }
};

util.clearObject = function(key) {
  if (util.LS_AVAILABLE) {
    localStorage.removeItem(key);
  }
};

util.str2arr = function(s) {
  return s.match(/[\uD800-\uDBFF][\uDC00-\uDFFF]|[\s\S]/g) || [];
};

/**
 * startsWith(string, pattern, position)
 * startsWith(string, pattern, case-insensitive)
 * startsWith(string, pattern, position, case-insensitive)
 */
util.startsWith = function(s, p, a3, a4) {
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

util.strPadding = function(str, ch, len, pos) {
  var t = str + '';
  var d = len - t.length;
  if (d <= 0) return t;
  var pd = util.repeatCh(ch, d);
  if (pos == 'R') {
    t += pd;
  } else {
    t = pd + t;
  }
  return t;
};

util.null2empty = function(s) {
  if ((s == null) || (s == undefined)) s = '';
  return s;
};

util.countStr = function(s, p) {
  var i = 0;
  var t = Object.prototype.toString.call(p);
  if (t == '[object RegExp]') {
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

util.convertNewLine = function(s, nl) {
  return s.replace(/\r\n/g, '\n').replace(/\r/g, '\n').replace(/\n/g, nl);
};

util.toHalfWidth = function(s) {
  var h = s.replace(/　/g, ' ').replace(/”/g, '"').replace(/’/g, '\'').replace(/‘/g, '`').replace(/￥/g, '\\');
  h = h.replace(/[！-～]/g, util.shift2half);
  return h;
};
util.shift2half = function(w) {
  return String.fromCharCode(w.charCodeAt(0) - 65248);
};

util.toFullWidth = function(s) {
  var f = s.replace(/ /g, '　').replace(/"/g, '”').replace(/'/g, '’').replace(/`/g, '‘').replace(/\\/g, '￥');
  f = f.replace(/[!-~]/g, util.shift2full);
  return f;
};
util.shift2full = function(w) {
  return String.fromCharCode(w.charCodeAt(0) + 65248);
};

util.getUnicodePoints = function(str) {
  var cd = '';
  var chs = util.str2arr(str);
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
 * -1234.98765
 * -> '-1,234.98765'
 */
util.formatNumber = function(v) {
  var v0 = v + '';
  var v1 = '';
  if (v0.match(/\./)) {
    var a = v0.split('.');
    v0 = a[0];
    v1 = '.' + a[1];
  }
  var len = v0.length;
  var r = '';
  for (var i = 0; i < len; i++) {
    if ((i != 0) && ((len - i) % 3 == 0)) {
      if (!((i == 1) && (v0.charAt(0) == '-'))) {
        r += ',';
      }
    }
    r += v0.charAt(i);
  }
  r += v1;
  return r;
};

/**
 * '-0102.3040'
 * -> '-102.304'
 */
util.trimZeros = function(v) {
  v += '';
  v = v.trim();
  var s = '';
  if (v.charAt(0) == '-') {
    s = '-';
    v = v.substr(1);
  }
  var p = v.split('.');
  var i = p[0];
  var d = '';
  if (p.length > 1) d = p[1];
  i = i.replace(/^0+/, '');
  d = d.replace(/0+$/, '');
  if (i == '') i = '0';
  var r = i;
  if (d != '') r += '.' + d;
  if (r != '0') r = s + r;
  return r;
};

util.plural = function(s, n) {
  return (n >= 2 ? (s + 's') : s);
};

util.copy2clpbd = function(s) {
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
 *  27  -> 'AA'
 * 'A'  ->  1
 * 'B'  ->  2
 * 'Z'  -> 26
 * 'AA' -> 27
 */
util.xlsCol = function(c) {
  var f = (isNaN(c) ? util.xlsColA2N : util.xlsColN2A);
  return f(c);
};
util.xlsColA2N = function(c) {
  var t = util.A2Z;
  return util.strpIndex(t, c.trim().toUpperCase());
};
util.xlsColN2A = function(n) {
  var t = util.A2Z;
  var a = util.strp(t, n);
  if (n <= 0) a = '';
  return a;
};

/**
 * String permutation.
 * strp('ABC', 1)  -> 'A'
 * strp('ABC', 2)  -> 'B'
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
          if (a.length <= j + 1) {
            a[j + 1] = -1;
          }
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

//-----------------------------------------------------------------------------
// Array
//-----------------------------------------------------------------------------
util.arr = {};
/**
 * ['A', 'B', 'C', '1', 1], 'A'
 * -> 0
 *
 * ['A', 'B', 'C', '1', 1], 1, false
 * -> 3
 *
 * ['A', 'B', 'C', '1', 1], 1, true
 * -> 4
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
 * ['A', 'B', 'A'], 'A'
 * -> 2
 *
 * ['A', 'B', 'A'], 'Z'
 * -> 0
 */
util.arr.count = function(a, v, f) {
  var c = 0;
  for (var i = 0; i < a.length; i++) {
    if ((!f && (a[i] == v)) || (f && (a[i] === v))) c++;
  }
  return c;
};

/**
 * ['A', 'B', 'C', 'B', 'A', 'A']
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
 * ['A', 'B', 'C'], 'B'
 * -> ['A', 'C']
 */
util.arr.del = function(arr, v) {
  for (var i = 0; i < arr.length; i++) {
    if (arr[i] == v) {
      arr.splice(i--, 1);
    }
  }
};

/**
 * ['A', 'B', 'C'], 'A'
 * -> true
 *
 * ['A', 'B', 'C'], 'Z'
 * -> false
 */
util.arr.hasValue = function(a, v, f) {
  return (util.arr.pos(a, v, f) >= 0);
};

/**
 * ['A', 'B', 'C'], 'A'
 * -> 'B'
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
 * ['A', 'B', 'C'], 'A'
 * -> 'C'
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
 * ['A', 'B', 'C', 'B', 'A', 'A']
 * -> ['A', 'B', 'C']
 */
util.arr.toUniqueValues = function(arr, srt) {
  var o = util.arr.countByValue(arr, srt);
  var v = [];
  for (var k in o) {
    v.push({key: k, cnt: o[k]});
  }
  if (srt == 'asc|count') {
    v.sort(function(a, b) {
      return a.cnt - b.cnt;
    });
  } else if (srt == 'desc|count') {
    v.sort(function(a, b) {
      return b.cnt - a.cnt;
    });
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

//-----------------------------------------------------------------------------
util.addListener = function(listeners, fn) {
  if (listeners && !util.arr.hasValue(listeners, fn)) listeners.push(fn);
};

util.removeListener = function(listeners, fn) {
  if (listeners) util.arr.del(listeners, fn);
};

//-----------------------------------------------------------------------------
// HTTP
//-----------------------------------------------------------------------------
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
  var trcid = util.getRandomString(util.http.TRC_ID_CHARS, util.http.TRC_ID_LEN);
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
  var data = null;
  if ((req.data != undefined) && (req.data != '')) {
    data = req.data;
  }
  if (trc) {
    if (!data) data = {};
    if (typeof data == 'string') {
      data += '&_trcid=' + trcid;
    } else {
      data._trcid = trcid;
    }
  }
  if (data instanceof Object) {
    data = util.http.buildQueryString(data);
  }
  var url = req.url;
  if (data && (req.method == 'GET')) {
    url += '?' + data;
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
    var m = '[' + trcid + '] => ' + req.url;
    if (data) m += ' : ' + data.substr(0, util.http.MAX_LOG_LEN);
    util._log.v(m);
  }
  if (util.debug.mode) $dbg[trcid] = {req: req};
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
  if (util.debug.mode) $dbg[req.trcid].res = res;
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
      m = '<= [' + st + '] ' + util.escHTML(m);
    }
    util._log.v('[' + req.trcid + '] ' + m);
  }
  if (util.http.onReceive(xhr, res, req)) {
    if (st == 200) {
      if (util.http.isJSONable(xhr, req)) {
        res = util.fromJSON(res);
        if (util.debug.mode) $dbg[req.trcid].res = res;
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
    if (cnt > 0) {
      s += '&';
    }
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
  util.addListener(util.http.listeners[type], fn);
};
util.http.onStart = function() {
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
util.http.countConnections = function() {
  return util.http.conn;
};
util.http.TRC_ID_CHARS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
util.http.TRC_ID_LEN = 4;
util.http.LOG_LIMIT = 3145728;
util.http.MAX_LOG_LEN = 4096;
util.http.online = true;
util.http.logging = false;
util.http.trace = false;
util.http.conn = 0;
util.http.listeners = {
  start: [],
  send: [],
  sent: [],
  receive: [],
  stop: [],
  error: []
};

//-----------------------------------------------------------------------------
// Element
//-----------------------------------------------------------------------------
var $el = function(target, idx) {
  var el = util.getElement(target, idx);
  if (el) {
    for (var k in $el.fn) {
      if (el[k] == undefined) el[k] = $el.fn[k];
    }
  }
  return el;
};
$el.fn = {
  html: function(html, speed) {
    if (html == undefined) return this.innerHTML;
    if (speed == undefined) speed = 0;
    util.writeHTML(this, html, speed);
  },
  text: function(text, speed) {
    if (text == undefined) return this.innerText;
    var html = util.escHTML(text);
    if (speed == undefined) speed = 0;
    util.writeHTML(this, html, speed);
  },
  clear: function(speed) { // Not available for <pre> on IE11
    var el = this;
    if (util.isTextInput(el)) {
      el.value = '';
    } else {
      if (speed == undefined) speed = 0;
      util.clearHTML(el, speed);
    }
  },
  textseq: function(text, speed, step) {
    return util.textseq(this, text, speed, step);
  },
  startTextSeq: function() {
    util.textseq.start(this);
  },
  stopTextSeq: function() {
    util.textseq.stop(this);
  },
  setStyle: function(n, v) {
    util.setStyle(this, n, v);
  },
  setStyles: function(n, v) {
    util.setStyles(this, n, v);
  },
  addClass: function(n) {
    util.addClass(this, n);
  },
  removeClass: function(n) {
    util.removeClass(this, n);
  },
  hasClass: function(n) {
    return util.hasClass(this, n);
  },
  isActive: function() {
    return util.isActiveElement(this);
  },
  center: function() {
    util.center(this);
  },
  position: function(x, y) {
    util.setPosition(this, x, y);
  },
  blink: function(a) {
    if (a == undefined) a = true;
    var f = a ? util.addClass : util.removeClass;
    f(this, 'blink');
  },
  blink2: function(a) {
    if (a == undefined) a = true;
    var f = a ? util.addClass : util.removeClass;
    f(this, 'blink2');
  },
  hide: function() {
    var el = this;
    el.displayBak = el.style.display;
    el.style.display = 'none';
  },
  show: function() {
    var el = this;
    if (el.style.display != 'none') return;
    var v = el.displayBak;
    if (v == undefined) v = '';
    el.style.display = v;
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
  prev: function() {
    return util.prevElement(this);
  },
  next: function() {
    return util.nextElement(this);
  }
};

util.getElement = function(target, idx) {
  var el = target;
  if (typeof target == 'string') {
    el = document.querySelectorAll(target);
    if (target.charAt(0) == '#') idx = 0;
    if (idx != undefined) el = el.item(idx);
  }
  return el;
};

util.addClass = function(el, n) {
  el = $el(el);
  if (util.hasClass(el, n)) return;
  if (el.className == '') {
    el.className = n;
  } else {
    el.className += ' ' + n;
  }
};

util.removeClass = function(el, n) {
  el = $el(el);
  var names = el.className.split(' ');
  var nm = '';
  for (var i = 0; i < names.length; i++) {
    if (names[i] != n) {
      if (i > 0) nm += ' ';
      nm += names[i];
    }
  }
  el.className = nm;
};

util.hasClass = function(el, n) {
  el = $el(el);
  var names = el.className.split(' ');
  for (var i = 0; i < names.length; i++) {
    if (names[i] == n) return true;
  }
  return false;
};

util.isActiveElement = function(el, idx) {
  return $el(el, idx) == document.activeElement;
};

util.hasParent = function(el, parent) {
  el = $el(el);
  parent = $el(parent);
  if (!el || !parent) return false;
  do {
    if (parent.toString() == '[object NodeList]') {
      for (var i = 0; i < parent.length; i++) {
        var p = parent[i];
        if (el == p) {
          return true;
        }
      }
    } else {
      if (el == parent) {
        return true;
      }
    }
    el = el.parentNode;
  } while (el);
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
  if (x < 0) {
    x = 0;
  }
  if (y < 0) {
    y = 0;
  }
  util.setPosition(el, x, y);
};

util.setPosition = function(el, x, y) {
  var style = {
    left: x + 'px',
    top: y + 'px'
  };
  util.setStyles(el, style);
};

util.isTextInput = function(el) {
  if (el.tagName == 'TEXTAREA') return true;
  if (el.tagName == 'INPUT') {
    if ((el.type == 'text') || (el.type == 'password')) return true;
  }
  return false;
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

util.escHTML = function(s) {
  return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
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

//-----------------------------------------------------------------------------
util.textarea = {};
/**
 * addStatusInfo('#textarea-id', '#infoarea-id')
 */
util.textarea.addStatusInfo = function(textarea, infoarea) {
  textarea = $el(textarea);
  if (!textarea) return;
  infoarea = $el(infoarea);
  if (!infoarea) return;
  textarea.infoarea = infoarea;
  util.textarea._adqdLIstener(textarea);
};
/**
 * updateTextAreaInfo('#textarea-id')
 */
util.updateTextAreaInfo = function(textarea) {
  textarea = $el(textarea);
  if (!textarea) return;
  var txt = textarea.value;
  var len = txt.length;
  var lenB = util.lenB(txt);
  var lfCnt = (txt.match(/\n/g) || []).length;
  var lenWoLf = len - lfCnt;
  var st = textarea.selectionStart;
  var ed = textarea.selectionEnd;
  var sl = ed - st;
  var ch = util.str2arr(txt)[st] || '';
  var cd = util.getCodePoint(ch);
  var cd16 = util.getUnicodePoints(ch, true);
  var cp = '';
  if (cd) cp = (cd == 10 ? 'LF' : ch) + ':' + cd16 + '(' + cd + ')';
  var slct = (sl ? 'Selected=' + sl : '');
  if (textarea.infoarea) {
    textarea.infoarea.innerText = 'LEN=' + lenWoLf + ' (w/RET=' + len + ') ' + lenB + ' bytes ' + cp + ' ' + slct;
  }
  var listener = textarea.listener;
  if (listener) {
    var data = {
      codePoint: cd,
      chr: ch,
      len: len,
      lenB: lenB,
      start: st,
      end: ed,
      selected: sl
    };
    listener(data);
  }
};
util.textarea._adqdLIstener = function(target) {
  target.addEventListener('input', util.textarea.onInput);
  target.addEventListener('change', util.textarea.onInput);
  target.addEventListener('keydown', util.textarea.onInput);
  target.addEventListener('keyup', util.textarea.onInput);
  target.addEventListener('click', util.textarea.onInput);
};
util.textarea.onInput = function(e) {
  util.updateTextAreaInfo(e.target);
};
util.textarea.addListener = function(target, f) {
  var el = $el(target);
  if (el) {
    el.listener = f;
    util.textarea._adqdLIstener(el);
  }
};

//-----------------------------------------------------------------------------
// Write HTML
//-----------------------------------------------------------------------------
/**
 * Write HTML and Fade in
 * util.writeHTML('#id', 'text');
 * util.writeHTML('#id', 'text', 300);
 *
 * Clear and Fade out
 * util.writeHTML('#id', '');
 * util.writeHTML('#id', '', 200);
 */
util.writeHTML = function(target, html, speed) {
  var el = target;
  if (typeof target == 'string') {
    el = document.querySelector(target);
  }
  if (!el) return;
  if (speed == 0) {
    el.innerHTML = html;
    return;
  }
  if (html == '') {
    util.clearHTML(el, speed);
  } else {
    el.innerHTML = '';
    var cbData = {html: html, speed: speed};
    util.fadeOut(el, 0, util._writeHTML, cbData);
  }
};
util._writeHTML = function(target, cbData) {
  var DFLT_SPEED = 250;
  var speed = cbData.speed;
  if ((speed == undefined) || (speed < 0)) {
    speed = DFLT_SPEED;
  }
  target.innerHTML = cbData.html;
  setTimeout(util.__writeHTML, 10, target, speed);
};
util.__writeHTML = function(target, speed) {
  util.fadeIn(target, speed);
};

/**
 * Fade out and Clear
 */
util.clearHTML = function(target, speed) {
  var DFLT_SPEED = 200;
  if ((speed == undefined) || (speed < 0)) {
    speed = DFLT_SPEED;
  }
  util.fadeOut(target, speed, util._clearHTML);
};
util._clearHTML = function(el) {
  el.innerHTML = '';
  util.removeClass(el, 'fadeout');
};

//-----------------------------------------------------------------------------
// Text Sequencer
//-----------------------------------------------------------------------------
/**
 * var ctx = util.textseq(el, text, speed, step, start, len);
 * ctx.onprogress = <callback-function(ctx, chunk)>;
 * ctx.oncomplete = <callback-function(ctx)>;
 */
util.textseq = function(el, text, speed, step, start, len) {
  if (speed == undefined) speed = util.textseq.DFLT_SPEED;
  if (step == undefined) step = 1;
  if (start == undefined) start = 0;
  if ((len == undefined) || (len < 0)) len = -1;
  var ctx = util.textseq.getCtx(el);
  if (ctx) util.textseq._stop(ctx);
  ctx = util.textseq.createCtx(el, text, speed, step, start, len);
  var i = util.textseq.idx(el);
  if (i < 0) {
    util.textseq.ctxs.push(ctx);
  } else {
    util.textseq.ctxs[i] = ctx;
  }
  ctx.tmrId = setTimeout(util._textseq, 0, ctx);
  return ctx;
};
util._textseq = function(ctx) {
  var el = ctx.el;
  var speed = ctx.speed;
  if (speed < 0) speed = util.textseq.DFLT_SPEED;
  var step = ctx.step;
  var prevPos = ctx.pos;
  var cutLen = step;
  if (ctx.pos < ctx.start) {
    ctx.pos = ctx.start - 1;
    if (ctx.pos < 0) ctx.pos = 0;
    cutLen = ctx.start;
  }
  if ((speed == 0) || (step == 0)) {
    ctx.pos = ctx.text.length;
    cutLen = ctx.pos;
  } else {
    ctx.pos += step;
  }
  if (ctx.pos > ctx.end) {
    ctx.pos = ctx.text.length;
    cutLen = ctx.text.length - prevPos;
  }
  ctx.tmrId = 0;
  var text = ctx.text.substr(0, ctx.pos);
  if (ctx.isInp) {
    el.value = text;
  } else {
    el.innerText = text;
  }
  util.textseq.onprogress(ctx, prevPos, cutLen);
  if (ctx.pos < ctx.text.length) {
    speed = ctx.speed;
    ctx.tmrId = setTimeout(util._textseq, speed, ctx);
  } else {
    util.textseq.oncomplete(ctx);
  }
};
util.textseq.start = function(el) {
  var ctx = util.textseq.getCtx(el);
  if (!ctx) return;
  util.textseq._stop(ctx);
  util._textseq(ctx);
};
util.textseq.stop = function(el) {
  var i = util.textseq.idx(el);
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
util.textseq.onprogress = function(ctx, prevPos, cutLen) {
  if (!ctx.onprogress) return;
  var chunk = ctx.text.substr(prevPos, cutLen);
  ctx.onprogress(ctx, chunk);
};
util.textseq.oncomplete = function(ctx) {
  var i = util.textseq.idx(ctx.el);
  util.textseq.ctxs.splice(i, 1);
  if (ctx.oncomplete) ctx.oncomplete(ctx);
};
util.textseq.createCtx = function(el, text, speed, step, start, len) {
  var end = text.length;
  if (len > 0) end = start + len;
  var pos = start - 1;
  if (pos < 0) pos = 0;
  var ctx = {
    el: el,
    text: text,
    speed: speed,
    step: step,
    start: start,
    end: end,
    isInp: util.isTextInput(el),
    tmrId: 0,
    pos: 0,
    onprogress: null,
    oncomplete: null
  };
  return ctx;
};
util.textseq.idx = function(el) {
  var ctxs = util.textseq.ctxs;
  for (var i = 0; i < ctxs.length; i++) {
    var ctx = ctxs[i];
    if (ctx.el == el) return i;
  }
  return -1;
};
util.textseq.getCtx = function(el) {
  var ctx = null;
  var i = util.textseq.idx(el);
  if (i >= 0) ctx = util.textseq.ctxs[i];
  return ctx;
};
util.textseq.DFLT_SPEED = 20;
util.textseq.ctxs = [];

//-----------------------------------------------------------------------------
// Styles
//-----------------------------------------------------------------------------
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
.blink {animation: blinker 1.5s step-end infinite;}
@keyframes blinker {
  50% {opacity: 0;}
  100% {opacity: 0;}
}
.blink2 {animation: blinker2 1s ease-in-out infinite alternate;}
@keyframes blinker2 {
  0% {opacity: 0;}
  70% {opacity: 1;}
}
.progdot:after {content:"..."; animation: progdot1 1.2s linear infinite;}
@keyframes progdot1 {
  10% {content: "";}
  20% {content: ".";}
  30% {content: "..";}
  40% {content: "...";}
}
.dialog {
  background: #fff;
  color: #000;
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

util.setStyle = function(el, n, v) {
  el = $el(el);
  el.style.setProperty(n, v, 'important');
};
util.setStyles = function(el, s) {
  for (var k in s) {
    util.setStyle(el, k, s[k]);
  }
};

//-----------------------------------------------------------------------------
// Infotip
//-----------------------------------------------------------------------------
util.infotip = {};
util.infotip.ST_HIDE = 0;
util.infotip.ST_FADEIN = 1;
util.infotip.ST_OPEN = 2;
util.infotip.ST_SHOW = 3;
util.infotip.ST_FADEOUT = 4;
util.DFLT_DURATION = 1500;
util.infotip.FADE_SPEED = 250;
util.infotip.obj = {
  id: 'infotip',
  st: util.infotip.ST_HIDE,
  el: {
    body: null,
    pre: null
  },
  duration: 0
};
util.infotip.opt = null;
util.infotip.timerId = 0;

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

/**
 * show("message");
 * show("message", 3000);
 * show("message", 0, {pos: {x: 100, y: 200});
 * show("message", 0, {pos: 'pointer', offset: {x: 5, y: -8}});
 * show("message", 0, {pos: 'active'});
 * show("message", 0, {style: {'font-size': '18px'}});
 */
util.infotip.show = function(msg, duration, opt) {
  var x;
  var y;
  var style;
  var offset;

  if (opt) {
    if (opt.pos) {
      if (opt.pos == 'pointer') {
        x = util.mouseX;
        y = util.mouseY;
        if (!opt.offset) {
          opt.offset = {
            x: 5,
            y: -8
          };
        }
        offset = opt.offset;
      } else if (opt.pos == 'active') {
        var el = document.activeElement;
        var rect = el.getBoundingClientRect();
        x = rect.left;
        y = rect.top;
      } else if (opt.pos.x != undefined) {
        x = opt.pos.x;
      }

      if (opt.pos.y != undefined) {
        y = opt.pos.y;
      }
    }

    if (opt.style) {
      style = opt.style;
    }
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
  if (obj.st != util.infotip.ST_SHOW) {
    obj.st = util.infotip.ST_FADEIN;
     setTimeout(util.infotip.fadeIn, 10, obj);
  }
};

util.infotip.fadeIn = function(obj) {
  var cb = util.infotip.onFadeInCompleted;
  util.fadeIn(obj.el.body, util.infotip.FADE_SPEED, cb, obj);
  var duration = obj.duration;
  if (duration > 0) {
    if (util[obj.id].timerId) {
      clearTimeout(util[obj.id].timerId);
    }
    util[obj.id].timerId = setTimeout(util.infotip.hide, duration, obj);
  }
};
util.infotip.onFadeInCompleted = function(el, obj) {
  obj.st = util.infotip.ST_SHOW;
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
    if (offset.y < 0) {
      y -= rect.height;
    }
  }
  if (y < 0) {
    y = 0;
  }
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
util.infotip.hide = function(obj) {
  var delay = util.infotip.FADE_SPEED;
  obj.st = util.infotip.ST_FADEOUT;
  util.fadeOut(obj.el.body, delay);
  util[obj.id].timerId = setTimeout(util.infotip.onFadeOutCompleted, delay, null, obj);
};
util.infotip.onFadeOutCompleted = function(el, obj) {
  util.infotip._hide(obj);
};
util.infotip._hide = function(obj) {
  var div = obj.el.body;
  if ((div != null) && (div.parentNode)) {
    document.body.removeChild(div);
  }
  obj.el.pre = null;
  obj.el.body = null;
  if (obj.id == 'infotip') {
    util.infotip.opt = null;
  }
  obj.st = util.infotip.ST_HIDE;
};

util.infotip.isVisible = function() {
  return util.infotip.obj.el != null;
};

util.infotip.adjust = function() {
  if (util.infotip.isVisible()) {
    util.infotip.center();
  }
};

util.infotip.onMouseMove = function(x, y) {
  if (util.infotip.opt && util.infotip.opt.pos == 'pointer') {
    util.infotip.move(x, y, util.infotip.opt.offset);
  }
};

//-----------------------------------------------------------------------------
// Tooltip
//-----------------------------------------------------------------------------
util.tooltip = {};
util.tooltip.DELAY = 500;
util.tooltip.offset = {
  x: 5,
  y: -8
};
util.tooltip.targetEl = null;
util.tooltip.timerId = 0;
util.tooltip.obj = {
  id: 'tooltip',
  st: util.infotip.ST_HIDE,
  el: {
    body: null,
    pre: null
  }
};
util.tooltip.disabled = false;
util.tooltip.show = function(el, msg, x, y) {
  if (util.tooltip.obj.st == util.infotip.ST_FADEOUT) {
    util.tooltip.cancel();
  }
  if ((el == util.tooltip.targetEl) && (util.tooltip.obj.st >= util.infotip.ST_OPEN)) {
    util.infotip._move(util.tooltip.obj, x, y, util.tooltip.offset);
  } else {
    if (util.tooltip.obj.st != util.infotip.ST_SHOW) {
      util.tooltip.obj.st = util.infotip.ST_OPEN;
    }
    util.tooltip.targetEl = el;
    if (util.tooltip.obj.el.body) {
      util.tooltip._show(msg);
    } else {
      if (util.tooltip.timerId) {
        clearTimeout(util.tooltip.timerId);
      }
      util.tooltip.timerId = setTimeout(util.tooltip._show, util.tooltip.DELAY, msg);
    }
  }
};
util.tooltip._show = function(msg) {
  var st = util.tooltip.obj.st;
  if ((st == util.infotip.ST_FADEOUT) || (st == util.infotip.ST_HIDE)) {
    util.tooltip.cancel();
    return;
  }
  var x = util.mouseX;
  var y = util.mouseY;
  var el = document.elementFromPoint(x, y);
  if (!el || (el != util.tooltip.targetEl)) {
    return;
  }
  util.infotip._show(util.tooltip.obj, msg);
  util.infotip._move(util.tooltip.obj, x, y, util.tooltip.offset);
};

util.tooltip.hide = function() {
  util.infotip.hide(util.tooltip.obj);
  util.tooltip.targetEl = null;
};

util.tooltip.cancel = function() {
  util.tooltip.targetEl = null;
  if (util.tooltip.timerId) {
    clearTimeout(util.tooltip.timerId);
    util.tooltip.timerId = 0;
  }
  util.infotip.onFadeOutCompleted(null, util.tooltip.obj);
};

util.tooltip.onMouseMove = function(x, y) {
  if (util.tooltip.disabled) return;
  var el = document.elementFromPoint(x, y);
  var tooltip = ((el && el.dataset) ? el.dataset.tooltip : null);
  if (tooltip) {
    util.tooltip.show(el, tooltip, x, y);
  } else if (util.tooltip.obj.el.body) {
    util.tooltip.hide();
  }
};

util.tooltip.setDelay = function(ms) {
  util.tooltip.DELAY = ms;
};

//-----------------------------------------------------------------------------
// Fade in / out
//-----------------------------------------------------------------------------
util.registerFadeStyle = function() {
  var style = '.fadein {';
  style += '  opacity: 1 !important;';
  style += '}';
  style += '.fadeout {';
  style += '  opacity: 0 !important;';
  style += '}';
  util.registerStyle(style);
};

util.fadeIn = function(el, speed, cb, arg) {
  el = $el(el);
  if (!el) return;
  if ((speed == undefined) || (speed < 0)) {
    speed = util.DFLT_FADE_SPEED;
  }
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
  if (dat.cb) {
    dat.cb(dat.el, dat.arg);
  }
};

util.fadeOut = function(el, speed, cb, arg) {
  el = $el(el);
  if (!el) return;
  if ((speed == undefined) || (speed < 0)) {
    speed = util.DFLT_FADE_SPEED;
  }
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
  if (dat.cb) {
    dat.cb(dat.el, dat.arg);
  }
};

//-----------------------------------------------------------------------------
// Screen Fader
//-----------------------------------------------------------------------------
util.SCREEN_FADER_ZINDEX = 99999999;
util.fadeScreenEl = null;
/**
 * onReady()
 *   initScreenFader('#id')
 * onLoad()
 *   fadeScreenIn()
 */
util.initScreenFader = function(a) {
  var el = util.fadeScreenEl;
  if (!el) el = $el(a);
  if (!el) el = util.createFadeScreenEl();
  util.fadeScreenEl = el;
  document.body.appendChild(el);
  return el;
};

util.fadeScreenIn = function(speed, cb) {
  if (speed == undefined) {
    speed = util.DFLT_FADE_SPEED;
  }
  var el = util.initScreenFader();
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

util.fadeScreenOut = function(speed, cb) {
  if (speed == undefined) {
    speed = util.DFLT_FADE_SPEED;
  }
  var el = util.initScreenFader();
  util.fadeIn(el, speed, cb);
};

util.createFadeScreenEl = function() {
  var el = document.createElement('div');
  var style = {
    'position': 'fixed',
    'width': '100vw',
    'height': '100vh',
    'top': '0',
    'left': '0',
    'background': '#fff',
    'z-index': util.SCREEN_FADER_ZINDEX
  };
  util.setStyles(el, style);
  return el;
};

//-----------------------------------------------------------------------------
// Loader Indication
//-----------------------------------------------------------------------------
util.loader = {};
util.loader.timerId = 0;
util.loader.count = 0;
util.loader.el = null;

util.loader.registerStyle = function() {
  var style = '@keyframes loader-rotate {';
  style += '  0% {';
  style += '    transform: rotate(0);';
  style += '  }';
  style += '  100% {';
  style += '    transform: rotate(360deg);';
  style += '  }';
  style += '}';
  style += '.loader {';
  style += '  display: block;';
  style += '  width: 46px;';
  style += '  height: 46px;';
  style += '  border: 4px solid rgba(204, 204, 204, 0.25);';
  style += '  border-top-color: #ccc;';
  style += '  border-radius: 50%;';
  style += '  position: fixed;';
  style += '  top: 0;';
  style += '  left: 0;';
  style += '  right: 0;';
  style += '  bottom: 0;';
  style += '  margin: auto;';
  style += '  animation: loader-rotate 1s linear infinite;';
  style += '}';
  style += '.loading {';
  style += '  cursor: progress !important;';
  style += '}';
  util.registerStyle(style);
};

util.loader.show = function(delay) {
  if (delay == undefined) {
    delay = 500;
  }
  util.loader.count++;
  if (util.loader.count > 1) {
    return;
  }
  util.loader.timerId = setTimeout(util.loader._show, delay);
};
util.loader._show = function() {
  util.loader.timerId = 0;
  var el = util.loader.el;
  if (!el) {
    el = util.loader.create();
    util.loader.el = el;
  }
  util.addClass(document.body, 'loading');
  document.body.appendChild(el);
  util.fadeIn(el);
};

util.loader.create = function() {
  var el = document.createElement('div');
  el.className = 'loader';
  return el;
};

util.loader.hide = function(force) {
  if (force) {
    util.loader.count = 0;
  } else if (util.loader.count > 0) {
    util.loader.count--;
  }
  if (util.loader.count == 0) {
    if (util.loader.timerId > 0) {
      clearTimeout(util.loader.timerId);
      util.loader.timerId = 0;
    }
    util.removeClass(document.body, 'loading');
    util.fadeOut(util.loader.el, 0, util.loader._hide);
  }
};

util.loader._hide = function() {
  if (util.loader.el) {
    document.body.removeChild(util.loader.el);
    util.loader.el = null;
  }
};

//-----------------------------------------------------------------------------
// Modal
//-----------------------------------------------------------------------------
util.MODAL_ZINDEX = 1000;
util.modal = function(child, addCloseHandler) {
  this.sig = 'modal';
  var el = document.createElement('div');
  var style = {};
  util.copyProps(util.modal.DFLT_STYLE, style);
  if (util.modal.style) {
    util.copyProps(util.modal.style, style);
  }
  util.setStyles(el, style);
  el.style.opacity = '0';
  if (addCloseHandler) {
    el.addEventListener('click', this.onClick);
  }
  if (child) {
    el.appendChild(child);
  }
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
      util.fadeOut(el, 200, ctx._hide);
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
    if (el.ctx && (el.ctx.sig == 'modal')) {
      el.ctx.hide();
    }
  }
};
util.modal.show = function(el, closeAnywhere) {
  var m = new util.modal(el, closeAnywhere).show();
  return m;
};
util.modal.setStyle = function(s) {
  util.modal.style = s;
};
util.modal.DFLT_STYLE = {
  'position': 'fixed',
  'top': '0',
  'left': '0',
  'min-width': '100vw',
  'min-height': '100vh',
  'width': '100%',
  'height': '100%',
  'background': 'rgba(0,0,0,0.6)',
  'z-index': util.MODAL_ZINDEX
};
util.modal.style = null;

//-----------------------------------------------------------------------------
// Dialog
//-----------------------------------------------------------------------------
/**
 * content: HTML|DOM
 *
 * opt = {
 *   title: 'Title',
 *   buttons = [
 *     {
 *       label: 'Yes',
 *       cb: function1
 *     },
 *     {
 *       label: 'No',
 *       cb: function2
 *     }
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

  var closeAnywhere = false;
  if (opt) {
    if (opt.closeAnywhere) {
      closeAnywhere = true;
    }
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
      'z-index': '1100'
    };
    util.setStyles(base, style);

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
    util.setStyles(body, style);
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
      util.setStyles(titleArea, style);
      if (opt && opt.style && opt.style.title) {
        for (var key in opt.style.title) {
          util.setStyle(titleArea, key, opt.style.title[key]);
        }
      }
      body.appendChild(titleArea);
    }

    var contentArea = document.createElement('pre');
    contentArea.className = 'dialog-content';
    if (title) {
      style = {'margin': '0'};
    } else {
      style = {'margin': '10px 0'};
    }
    util.setStyles(contentArea, style);

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
        style = {
          'margin-top': '1em',
          'margin-bottom': '0',
        };
        if (i > 0) {
          style['margin-left'] = '0.5em';
        }
        util.setStyles(btnEl, style);
        if (opt && opt.style && opt.style.button) {
          for (key in opt.style.button) {
            util.setStyle(btnEl, key, opt.style.button[key]);
          }
        }
        btnEl.className = 'dialog-button';
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
    var ret = {
      boby: body,
      btnEls: btnEls
    };
    return ret;
  },

  close: function(ctx) {
    ctx.modal.removeChild(ctx.el);
    ctx.modal.hide();
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
util.dialog.getTopDialog = function() {
  var dialog = null;
  var instances = util.dialog.instances;
  if (instances.length > 0) {
    dialog = instances[instances.length - 1];
  }
  return dialog;
};
util.dialog.adjust = function() {
  var d = util.dialog.getTopDialog();
  if (d) d.center(d);
};
util.dialog.show = function() {
  var d = util.dialog.getTopDialog();
  d.el.style.opacity = 1;
  util.dialog.adjust();
};

// opt = {
//   title: 'Title',
//   buttons = [
//     {
//       label: 'Yes',
//       focus: true,
//       cb: cbYes
//     },
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
  if (!opt.style) {
    opt.style = {
      body: DEFAULT_STYLE
    };
  }
  var dialog = new util.dialog(content, opt);
  util.dialog.instances.push(dialog);
  return dialog;
};

util.dialog.btnCb = function(e) {
  util.dialog.instances.pop();
  var el = e.target;
  util.dialog.btnHandler(el);
};

util.dialog.btnHandler = function(el) {
  var ctx = el.ctx;
  ctx.close(ctx);
  var data;
  if (ctx.opt) data = ctx.opt.data;
  if (el.cb) el.cb(data);
};

util.dialog.close = function(btnIdx) {
  var dialog = util.dialog.instances.pop();
  if (dialog) {
    if (btnIdx == undefined) {
      dialog.close(dialog);
    } else {
      var b = dialog.btnEls[btnIdx];
      if (b) {
        util.dialog.btnHandler(b);
      } else {
        dialog.close(dialog);
      }
    }
  }
};

util.dialog.count = function() {
  return util.dialog.instances.length;
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
 *     message: {
 *       ...
 *     }
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
  if (a[i]) {
    opt = a[i];
  }
  var dialogOpt = {
    title: title,
    buttons: [
      {
        label: 'OK',
        focus: true,
        cb: cb
      }
    ],
    style: opt.style
  };
  msg = util.null2empty(msg);
  msg = util.convertNewLine(msg, '<br>');
  var content = document.createElement('div');
  content.style.display = 'inline-block';
  if (opt.style && opt.style.message) {
    for (var key in opt.style.message) {
      util.setStyle(content, key, opt.style.message[key]);
    }
  }
  content.innerHTML = msg;
  util.dialog.open(content, dialogOpt);
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
 *     message: {
 *       ...
 *     }
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
  if (a[k]) {
    opt = a[k];
  }
  var definition = {
    labelY: 'Yes',
    labelN: 'No',
    cbY: util.dialog.sysCbY,
    cbN: util.dialog.sysCbN,
  };
  msg = util.null2empty(msg);
  msg = util.convertNewLine(msg + '', '<br>');
  var content = document.createElement('div');
  content.style.display = 'inline-block';
  if (opt.style && opt.style.message) {
    for (var key in opt.style.message) {
      util.setStyle(content, key, opt.style.message[key]);
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
    {
      label: definition.labelY,
      cb: definition.cbY
    },
    {
      label: definition.labelN,
      cb: definition.cbN
    }
  ];
  var focusIdx = 0;
  if (opt.focus == 'no') {
    focusIdx = 1;
  }
  buttons[focusIdx].focus = true;
  var dialogOpt = {
    title: title,
    buttons: buttons,
    data: ctx,
    focusEl: definition.focusEl, // prior
    style: opt.style
  };
  util.dialog.open(content, dialogOpt);
};
util.dialog.sysCbY = function(ctx) {
  if (ctx.cbY) ctx.cbY(ctx.data);
};
util.dialog.sysCbN = function(ctx) {
  if (ctx.cbN) ctx.cbN(ctx.data);
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
 *   secure: true|false,
 *   style: {
 *     message: {
 *       ...
 *     },
 *     textbox: {
 *       ...
 *     }
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
 *  dialog-textbox
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
  if (a[k]) {
    opt = a[k];
  }
  var txtBox = document.createElement('input');
  if (opt.secure) {
    txtBox.type = 'password';
  } else {
    txtBox.type = 'text';
  }
  txtBox.className = 'dialog-textbox';
  if (opt && opt.style && opt.style.textbox) {
    for (var key in opt.style.textbox) {
      util.setStyle(txtBox, key, opt.style.textbox[key]);
    }
  }
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
  dialog.cbY = cbY;
  dialog.cbN = cbN;
  dialog.txtBox = txtBox;
  return dialog;
};
util.dialog.text.sysCbOK = function(ctx) {
  var text = ctx.txtBox.value;
  if (ctx.cbY) ctx.cbY(text, ctx.data);
};
util.dialog.text.sysCbCancel = function(ctx) {
  var text = ctx.txtBox.value;
  if (ctx.cbN) ctx.cbN(text, ctx.data);
};

util.alert = function(a1, a2, a3, a4) {
  util.dialog.info(a1, a2, a3, a4);
};
util.confirm = function(a1, a2, a3, a4, a5) {
  util.dialog.confirm(a1, a2, a3, a4, a5);
};

//-----------------------------------------------------------------------------
// Meter
//-----------------------------------------------------------------------------
/**
 * target: element / selector
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
 *  ]
 * }
 *
 * <div id="meter1"></div>
 * var m = new util.Meter('#meter1', opt);
 */
util.Meter = function(target, opt) {
  target = $el(target);
  target.innerHTML = '';

  var min = 0;
  var max = 100;
  var low;
  var high;
  var optimum;
  var value;
  var w = '100px';
  var h = '14px';
  var bg = '#888';
  var bd = '1px solid #ccc';
  var bdRd = '0';
  var green = 'linear-gradient(to right, #0d0, #8f8)';
  var yellow = 'linear-gradient(to right, #dd0 , #ff8)';
  var red = 'linear-gradient(to right, #d66 , #fcc)';

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
    if (opt.green != undefined) green = opt.green;
    if (opt.yellow != undefined) yellow = opt.yellow;
    if (opt.red != undefined) red = opt.red;
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

  var base = target;
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
  util.setStyles(base, style);

  var v = value / max * 100;
  var bar = document.createElement('div');
  bar.className = 'meter-bar';
  style = {
    width: v + '%',
    height: '100%',
    background: green
  };
  util.setStyles(bar, style);
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
      util.setStyles(e, s);
      base.appendChild(e);
    }
  }

  var label = null;
  if (opt) label = opt.label;
  if (label) {
    var lblEl = document.createElement('div');
    s = {
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
    util.setStyles(lblEl, s);
    if (label.style) {
      util.setStyles(lblEl, label.style);
    }
    lblEl.innerHTML = label.text;
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
    scales: scales
  };
  this.value = value;
  this.el = base;
  this.bar = bar;
  this.lblEl = lblEl;
  this.redraw();
};

util.Meter.prototype = {
  getValue: function() {
    return this.value;
  },
  setValue: function(v, txt) {
    if (v != null) {
      this._setValue(v);
    }
    if (txt != undefined) {
      this.setText(txt);
    }
    this.redraw();
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
    this.lblEl.innerHTML = s;
    this.redraw();
  },
  setTextStyle: function(s) {
    util.setStyles(this.lblEl, s);
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

//-----------------------------------------------------------------------------
// LED
//-----------------------------------------------------------------------------
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
 *   className: 'xxx',
 *   labelClassName: 'xxx',
 *   active: true
 * };
 */
util.Led = function(target, opt) {
  var baseEl = $el(target);
  if (!opt) opt = {};
  if (opt.size == undefined) opt.size = '16px';
  if (opt.color == undefined) opt.color = util.Led.DFLT_COLOR;
  if (opt.offColor == undefined) opt.offColor = util.Led.DFLT_INACTV_COLOR;
  if (opt.shadow == undefined) opt.shadow = '0 0 5px';
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
  util.setStyles(ledEl, style);
  ledEl.innerHTML = '&#x25CF;';
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
util.Led.DFLT_COLOR = '#0f0';
util.Led.DFLT_INACTV_COLOR = '#888';
util.Led.DFLT_BLINK_DURATION = 700;
util.Led.prototype = {
  on: function(color) {
    var ctx = this;
    ctx._stopBlink();
    if (color) ctx.opt.color = color;
    ctx.active = true;
    ctx._on(ctx);
  },
  off: function(color) {
    var ctx = this;
    ctx._stopBlink();
    if (color) ctx.opt.offColor = color;
    ctx.active = false;
    ctx._off(ctx);
  },
  toggle: function() {
    var ctx = this;
    if (ctx.active) {
      ctx.off();
    } else {
      ctx.on();
    }
  },
  _on: function(ctx) {
    ctx.lighted = true;
    util.setStyle(ctx.ledEl, 'color', ctx.opt.color);
  },
  _off: function(ctx) {
    ctx.lighted = false;
    util.setStyle(ctx.ledEl, 'color', ctx.opt.offColor);
  },
  blink: function(d) {
    var ctx = this;
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
    if (a == undefined) a = true;
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

//-----------------------------------------------------------------------------
// Form
//-----------------------------------------------------------------------------
util.submit = function(url, method, params, enc) {
  var form = document.createElement('form');
  form.action = url;
  form.method = method;
  for (var key in params) {
    var input = document.createElement('input');
    var val = params[key];
    if (enc) val = encodeURIComponent(val);
    input.type = 'hidden';
    input.name = key;
    input.value = val;
    form.appendChild(input);
  }
  document.body.appendChild(form);
  form.submit();
};

//-----------------------------------------------------------------------------
// URL / Query
//-----------------------------------------------------------------------------
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

//-----------------------------------------------------------------------------
// Browser
//-----------------------------------------------------------------------------
util.getBrowserType = function(ua) {
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
  if (ua.indexOf('Trident/6.') >= 1) {
    brws.name = 'IE10';
    brws.family = 'IE';
    return brws;
  }
  if (ua.indexOf('Trident/5.') >= 1) {
    brws.name = 'IE9';
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
    var b = util.getBrowserType();
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
      if (dark) {
        c = '#0af';
      } else {
        c = '#08d';
      }
      s = '<span style="color:' + c + '">Edge</span>';
      break;
    case 'Firefox':
      if (dark) {
        c = '#e57f25';
      } else {
        c = '#e60';
      }
      s = '<span style="color:' + c + '">' + n + '</span>';
      break;
    case 'Opera':
      if (dark) {
        c = '#f44';
      } else {
        c = '#ff1b2c';
      }
      s = '<span style="color:' + c + '">' + n + '</span>';
      break;
    case 'IE11':
    case 'IE10':
    case 'IE9':
      if (dark) {
        c = '#61d5f8';
      } else {
        c = '#0af';
      }
      s = '<span style="color:' + c + '">' + n + '</span>';
      break;
    case 'Safari':
      s = '<span style="color:#86c8e8">Safa</span><span style="color:#dd5651">r</span><span style="color:#ececec">i</span>';
  }
  return s;
};

//-----------------------------------------------------------------------------
// Base64
//-----------------------------------------------------------------------------
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

util.encodeBase64 = function(s) {
  var r;
  try {
    r = btoa(s);
  } catch (e) {
    r = btoa(encodeURIComponent(s).replace(/%([0-9A-F]{2})/g, function(match, p1) {return String.fromCharCode('0x' + p1);}));
  }
  return r;
};
util.decodeBase64 = function(s) {
  var r = '';
  if (!window.atob) return r;
  try {
    r = decodeURIComponent(Array.prototype.map.call(atob(s), function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
  } catch (e) {
    r = atob(s);
  }
  return r;
};

//-----------------------------------------------------------------------------
// UTF-8
//-----------------------------------------------------------------------------
util.UTF8 = {};
util.UTF8.toByteArray = function(s) {
  var a = [];
  if (!s) return a;
  var chs = util.str2arr(s);
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

//-----------------------------------------------------------------------------
// bit operation
//-----------------------------------------------------------------------------
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

//-----------------------------------------------------------------------------
// BSB64
//-----------------------------------------------------------------------------
util.encodeBSB64 = function(s, n) {
  var a = util.UTF8.toByteArray(s);
  return util.BSB64.encode(a, n);
};
util.decodeBSB64 = function(s, n) {
  if (s.match(/\$\d+$/)) {
    var v = s.split('$');
    s = v[0];
    n = v[1];
  }
  var a = util.BSB64.decode(s, n);
  return util.UTF8.fromByteArray(a);
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

//-----------------------------------------------------------------------------
// Ring Buffer
//-----------------------------------------------------------------------------
util.RingBuffer = function(len) {
  this.buffer = new Array(len);
  this.len = len;
  this.cnt = 0;
};
util.RingBuffer.prototype = {
  add: function(data) {
    var idx = this.cnt % this.len;
    this.buffer[idx] = data;
    this.cnt++;
  },
  set: function(idx, data) {
    this.buffer[idx] = data;
  },
  get: function(idx) {
    if (this.len < this.cnt) {
      idx += this.cnt;
    }
    idx %= this.len;
    return this.buffer[idx];
  },
  getAll: function() {
    var buf = [];
    var len = this.len;
    var pos = 0;
    if (this.cnt > len) {
      pos = (this.cnt % len);
    }
    for (var i = 0; i < len; i++) {
      if (pos >= len) {
        pos = 0;
      }
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

//-----------------------------------------------------------------------------
// Interval Proc
//-----------------------------------------------------------------------------
// Start  : startIntervalProc('proc-id', fn, 1000, [async(true|false)]);
// Stop   : stopIntervalProc('proc-id');
// Restart: startIntervalProc('proc-id');
//
// Async:
// -> call in fn: nextIntervalProc('proc-id');
//-----------------------------------------------------------------------------
// {
//   id: {
//     fn: function(),
//     interval: millis,
//     async: true|false,
//     tmrId: timer-id
//   }
// }
util.intervalProcs = {};

/**
 * Register an interval proc.
 */
util.registerIntervalProc = function(id, fn, interval, async) {
  util.intervalProcs[id] = {
    fn: fn,
    interval: interval,
    async: (async ? true : false),
    tmrId: 0
  };
};

/**
 * Remove an interval proc.
 */
util.removeIntervalProc = function(id) {
  delete util.intervalProcs[id];
};

/**
 * Start an interval proc.
 */
util.startIntervalProc = function(id, fn, interval, async) {
  if (fn) util.registerIntervalProc(id, fn, interval, async);
  var p = util.intervalProcs[id];
  if (p) {
    util._stopIntervalProc(p);
    util._execIntervalProc(id);
  }
};

/**
 * Stop an interval proc.
 */
util.stopIntervalProc = function(id) {
  var p = util.intervalProcs[id];
  if (p) util._stopIntervalProc(p);
};
util._stopIntervalProc = function(p) {
  if (p.tmrId > 0) {
    clearTimeout(p.tmrId);
    p.tmrId = 0;
  }
};

/**
 * Sets a timer which executes an intefval proc function.
 */
util.nextIntervalProc = function(id, interval) {
  var p = util.intervalProcs[id];
  if (p) {
    util._stopIntervalProc(p);
    if (interval == undefined) interval = p.interval;
    p.tmrId = setTimeout(util._execIntervalProc, interval, id);
  }
};

/**
 * Sets interval in milliseconds.
 */
util.setInterval = function(id, interval) {
  var p = util.intervalProcs[id];
  if (p) p.interval = interval;
};

/**
 * Execute an interval proc.
 */
util._execIntervalProc = function(id) {
  var p = util.intervalProcs[id];
  if (p) {
    p.fn();
    if (!p.async) util.nextIntervalProc(id);
  }
};

//-----------------------------------------------------------------------------
// Events
//-----------------------------------------------------------------------------
// NAMESPACE.cb = function(ev) {
//   log(ev);
// }
//
// util.event.addListener('EVENT_NAME', NAMESPACE.cb);
//
// var data = {
//   msg: 'abc'
// };
// util.event.send('EVENT_NAME', data);
//-----------------------------------------------------------------------------
util.event = {};
util.event.listeners = {};
util.event.events = [];

util.event.addListener = function(name, fn) {
  if (!util.event.listeners[name]) {
    util.event.listeners[name] = [];
  }
  util.addListener(util.event.listeners[name], fn);
};

util.event.removeListener = function(name, fn) {
  util.removeListener(util.event.listeners[name], fn);
};

util.event.send = function(name, data) {
  var e = {
    name: name,
    data: data
  };
  util.event.events.push(e);
  setTimeout(util.event._send, 0);
};

util.event._send = function() {
  var ev = util.event.events.shift();
  if (!ev) {
    return;
  }

  var e = {
    name: ev.name,
    data: ev.data
  };

  var listeners = util.event.listeners[ev.name];
  if (listeners) {
    util.event.callListeners(listeners, e);
  }

  listeners = util.event.listeners['*'];
  if (listeners) {
    util.event.callListeners(listeners, e);
  }
};

util.event.callListeners = function(listeners, e) {
  for (var i = 0; i < listeners.length; i++) {
    var f = listeners[i];
    f(e);
  }
};

//-----------------------------------------------------------------------------
// Geo Location
//-----------------------------------------------------------------------------
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
  if (!opt) {
    opt = util.geo.DFLT_OPT;
  }
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

  if (util.geo.cbOK) {
    util.geo.cbOK(data);
  }
};

util.geo.onGetPosERR = function(err) {
  if (util.geo.cbERR) {
    util.geo.cbERR(err);
  }
};

util.geo.startWatchPosition = function(cbOK, cbERR, opt) {
  if (util.geo.watchId != null) return;
  if (!opt) {
    opt = util.geo.DFLT_OPT;
  }
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
// ->
// {
//   'latitude': 35.681237,
//   'longitude': 139.766985
// }
util.parseCoordinate = function(location) {
  location = location.replace(/ /g, '');
  var loc = location.split(',');
  var lat = parseFloat(loc[0]);
  var lon = parseFloat(loc[1]);
  var coordinate = {
    'latitude': lat,
    'longitude': lon
  };
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
  if (rangeL < 0) {
    rangeL += 360;
  }
  var rangeR = heading + (range / 2);
  if (rangeR >= 360) {
    rangeR -= 360;
  }

  if (rangeR < rangeL) {
    if ((azimuth >= rangeL) || (azimuth <= rangeR)) {
      return true;
    }
  } else {
    if ((azimuth >= rangeL) && (azimuth <= rangeR)) {
      return true;
    }
  }
  return false;
};

//-----------------------------------------------------------------------------
util.onMouseMove = function(e) {
  var x = e.clientX;
  var y = e.clientY;
  util.mouseX = x;
  util.mouseY = y;
  util.infotip.onMouseMove(x, y);
  util.tooltip.onMouseMove(x, y);
};

//-----------------------------------------------------------------------------
util.keyHandlers = {
  down: [],
  press: [],
  up: []
};

// combination = {
//   ctrl: true
// }
// 'down', 83, fn, combination
// fn(e)
util.addKeyHandler = function(type, keyCode, fn, combination) {
  if ((type != 'down') && (type != 'press') && (type != 'up')) {
    return;
  }
  var handler = {
    keyCode: keyCode,
    combination: combination,
    fn: fn
  };
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

//-----------------------------------------------------------------------------
util.loadScript = function(path) {
  var s = document.createElement('script');
  s.src = path;
  document.body.appendChild(s);
};

//-----------------------------------------------------------------------------
util.setupLogs = function() {
  if (!window.log) {
    window.log = function(o) {
      console.log(o);
    };
    window.log.v = function(o) {
      console.log(o);
    };
    window.log.d = function(o) {
      console.log(o);
    };
    window.log.i = function(o) {
      console.info(o);
    };
    window.log.w = function(o) {
      console.warn(o);
    };
    window.log.e = function(o) {
      console.error(o);
    };
  }
  util._log = function(o) {
    window.log(o);
  };
  util._log.v = function(o) {
    window.log.v(o);
  };
  util._log.d = function(o) {
    window.log.d(o);
  };
  util._log.i = function(o) {
    window.log.i(o);
  };
  util._log.w = function(o) {
    window.log.w(o);
  };
  util._log.e = function(o) {
    window.log.e(o);
  };
};

//-----------------------------------------------------------------------------
util.onReady = function() {
  util.setupStyle();
};

util.onResize = function() {
  util.infotip.adjust();
  util.dialog.adjust();
};

util.$onReady = function() {
  var fn = window.$onReady;
  if (fn) fn();
};
util.$onLoad = function() {
  var fn = window.$onLoad;
  if (fn) fn();
};
util.$onBeforeUnload = function() {
  var fn = window.$onBeforeUnload;
  if (fn) fn();
};
util.$onUnload = function() {
  var fn = window.$onUnload;
  if (fn) fn();
};
util.$onKeyDown = function(e) {
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

//-----------------------------------------------------------------------------
var $dbg = {};
util.debug = {};
util.debug.mode = 0;
util.debug.on = function() {
  util.debug.mode = 1;
};
util.debug.off = function() {
  util.debug.mode = 0;
};
util.debug.reset = function() {
  $dbg = {};
};

//-----------------------------------------------------------------------------
util.init = function() {
  util.setupLogs();
  try {
    if (typeof window.localStorage != 'undefined') {
      util.LS_AVAILABLE = true;
    }
  } catch (e) {}
  window.addEventListener('DOMContentLoaded', util.onReady, true);
  window.addEventListener('mousemove', util.onMouseMove, true);
  window.addEventListener('keydown', util.onKeyDown, true);
  window.addEventListener('keypress', util.onKeyPress, true);
  window.addEventListener('keyup', util.onKeyUp, true);
  window.addEventListener('resize', util.onResize, true);

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
