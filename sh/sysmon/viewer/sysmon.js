/*!
 * System Performance Monitor
 * Copyright (c) 2023 Takashi Harano
 */
var sysmon = {};

sysmon.INTERVAL = 30000;

sysmon.led1 = null;

sysmon.onReady = function() {
  util.clock('#clock', '%YYYY-%MM-%DD %W %HH:%mm:%SS');
};

sysmon.onLoad = function() {
  sysmon.led1 = new util.Led('#led1');
  perf.init();
  disk.init();
};

sysmon.drawStatus = function(s) {
  $el('#status').innerHTML = s;
};

sysmon.callApi = function(action, params, cb) {
  var data = {
    action: action
  };
  if (params) {
    for (var key in params) {
      data[key] = params[key];
    }
  }

  var req = {
    url: 'index.cgi',
    method: 'POST',
    data: data,
    cb: cb
  };

  util.http(req);
};

sysmon.showInfotip = function(s) {
  var opt = {
    style: {
      'font-size': '18px'
    }
  };
  util.infotip.show(s, opt);
};

sysmon.convDateToN = function(d1) {
  var dt = util.getDateTime();
  if (d1.length == 4) {
    var mmdd = dt.toString('%MM%DD');
    var y = dt.year;
    if (d1 > mmdd) y -= 1;
    d1 = y + d1;
  }
  var d0 = dt.toString('%YYYY%MM%DD');
  var n = util.diffDays(d1, d0);
  return n;
};

window.addEventListener('DOMContentLoaded', sysmon.onReady, true);
window.addEventListener('load', sysmon.onLoad, true);

$onEnterKey = function(e) {
  if ($el('#perf-hist-n').hasFocus()) {
    perf.showData();
  } else if ($el('#disk-hist-n').hasFocus()) {
    disk.showData();
  }
};
