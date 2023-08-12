/*!
 * System Performance Monitor
 * Copyright (c) 2023 Takashi Harano
 */
var sysmon = {};

sysmon.INTERVAL = 30000;

sysmon.led1 = null;

sysmon.onReady = function() {
  util.clock('#clock', '%YYYY-%MM-%DD %HH:%mm:%SS');
};

sysmon.onLoad = function() {
  sysmon.led1 = new util.Led('#led1');
  sysmon.led1.blink2();
  sysmon.drawStatus('Watching');

  perf.init();
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
  util.infotip.show(s);
};

window.addEventListener('DOMContentLoaded', sysmon.onReady, true);
window.addEventListener('load', sysmon.onLoad, true);

//-----------------------------------------------------------------------------
var perf = {};
perf.perfLogConsole = null;
perf.timerId = 0;

perf.dndHandler = null;
perf.autoReload = true;
perf.xLabels = [];
perf.n = 0;

perf.animation = {
  duration: 500,
  easing: 'easeOutQuart'
};

perf.init = function() {
  perf.setupDnDHandler();

  perf.perfLogConsole = util.initConsole('#perflog');
  perf.startAutoReload();

  var meterOption = {
    width: '50px',
    height: '10px',
    low: 80,
    high: 90,
    optimum: 50
  };
  perf.cpuMeter = util.initMeter('#cpu-meter', meterOption);
  perf.memMeter = util.initMeter('#mem-meter', meterOption);
  perf.heapMeter = util.initMeter('#java-heap-meter', meterOption);
  perf.heapS0Meter = util.initMeter('#java-s0-heap-meter', meterOption);
  perf.heapS1Meter = util.initMeter('#java-s1-heap-meter', meterOption);
  perf.heapEdenMeter = util.initMeter('#java-eden-heap-meter', meterOption);
  perf.heapOldMeter = util.initMeter('#java-old-heap-meter', meterOption);

  var counterOption = {
    suffix: '%',
    text: '-'
  };
  perf.cpuCounter = util.initCounter('#cpu-val', counterOption);
  perf.memCounter = util.initCounter('#mem-val', counterOption);
  perf.heapCounter = util.initCounter('#heap-usage-val', counterOption);
  perf.heapEdenCounter = util.initCounter('#heap-eden-usage-val', counterOption);
  perf.heapS0Counter = util.initCounter('#heap-s0-usage-val', counterOption);
  perf.heapS1Counter = util.initCounter('#heap-s1-usage-val', counterOption);
  perf.heapOldCounter = util.initCounter('#heap-old-usage-val', counterOption);

  var updateInterval = perf.INTERVAL;
  if (perf.autoReload) {
    util.IntervalProc.start('perf', perf.procInterval, updateInterval, null, true);
  }

  $el('#perf-auto-update').checked = true;
  $el('#perf-auto-update').addEventListener('change', perf.onPerfAutpUpdateChanged);

  util.loader.show('#perf-chart-area');
};

perf.onPerfAutpUpdateChanged = function() {
  if ($el('#perf-auto-update').checked) {
    perf.startAutoReload();
  } else {
    perf.stopAutoReload();
  }
};

/**
 * Drag and Drop Handler
 */
perf.setupDnDHandler = function() {
  var opt = {
    onerror: perf.onFileLoadError
  };
  perf.dndHandler = util.addDndHandler('#perf-chart-area', perf.onDropFile, opt);
};

perf.onDropFile = function(data) {
  perf.stopAutoReload();
  var obj = perf.convertRawToJsonObject(data);
  perf.draw(obj);
};

perf.onFileLoadError = function() {
  sysmon.showInfotip('Load error');
};

/**
 * Interval Process
 */
perf.procInterval = function() {
  if (perf.autoReload) {
    perf.getData();
  }
};

perf.startAutoReload = function() {
  $el('#perf-auto-update').checked = true;
  perf.autoReload = true;
  perf.procInterval();
};

perf.stopAutoReload = function() {
  $el('#reset-perf-button').disabled = false;
  $el('#perf-auto-update').checked = false;
  perf.autoReload = false;
};

perf.getData = function(n) {
  if (!n) {
    n = '';
  }
  var param = {
    n: n
  };
  sysmon.callApi('get_perf_log', param, perf.getDataCb, perf.getDataErrCb);
};

perf.showPrev = function() {
  perf.stopAutoReload();
  perf.n++;
  perf.getData(perf.n);
};

perf.showNext = function() {
  perf.stopAutoReload();
  perf.n--;
  if (perf.n < 0) {
    perf.n = 0;
  }
  perf.getData(perf.n);
};

perf.getDataCb = function(xhr, res, req) {
  util.loader.hide('#perf-chart-area');

  if (res.status == 'NOT_FOUND') {
    if (perf.n == 0) {
      sysmon.showInfotip('Log Not Found');
      return;
    }
    perf.n--;
    if (perf.n < 0) {
      perf.n = 0;
    }
    return;
  }

  var data = util.decodeBase64(res.body);
  perf.drawLog(data);

  var n = '';
  if (perf.n != 0) {
    n = '[' + perf.n + ']';
  }
  $el('#perf-hist-n').innerHTML = n;

  var obj = perf.convertRawToJsonObject(data);
  perf.draw(obj);
  perf.animation = false;
  util.IntervalProc.next('perf');
};

perf.getDataErrCb = function(xhr, res, req) {
  log.w('getDataErrCb(): HTTP ERROR: ' + xhr.status);
  util.IntervalProc.next('perf');
};

perf.draw = function(dataList) {
  if (dataList.length == 0) {
    sysmon.showInfotip('No performance log data');
    return;
  }

  var xLabels = [];

  var chartData = {
    cpu: [],
    mem: [],
    jheap: [],
    jheapEden: [],
    jheapOld: []
  };

  var oldestTimestamp = dataList[0].timestamp;
  var latestTimestamp = dataList[dataList.length - 1].timestamp;
  var periodFrom = util.getTimestampOfMidnight(latestTimestamp);

  var data;
  var ts;
  // >= midnight of the latest data
  if (oldestTimestamp < periodFrom) {
    var tmpDataList = [];
    for (var i = 0; i < dataList.length; i++) {
      data = dataList[i];
      ts = data.timestamp;
      if (ts >= periodFrom) {
        tmpDataList.push(data);
      }
    }
    dataList = tmpDataList;
  }

  if (dataList.length == 0) {
    return;
  }

  var firstData = dataList[0];
  var lastData = dataList[dataList.length - 1];
  var tsS = firstData.timestamp;
  var tsE = lastData.timestamp;
  var logDate = util.getDateTimeString(tsS, '%YYYY-%MM-%DD');

  var totalMem = parseInt(lastData.mem.total);
  totalMem = util.convByte(totalMem * 1024);
  $el('#mem-total-val').innerHTML = totalMem;

  var tsMn1 = util.getTimestampOfMidnight(tsS);
  var tsMn2 = tsMn1 + util.DAY;
  var preMin = ((tsS - tsMn1) / util.MINUTE) | 0;
  var postMin = ((tsMn2 - tsE) / util.MINUTE) | 0;

  for (i = 0; i < preMin; i++) {
    dtStr = perf.getDateTimeString(tsMn1 + i * util.MINUTE);
    xLabels.push(dtStr);
    chartData.cpu.push(null);
    chartData.mem.push(null);
    chartData.jheap.push(null);
    chartData.jheapEden.push(null);
    chartData.jheapOld.push(null);
  }

  var prevTimestamp = -1;
  var interval = 1 * util.MINUTE;
  for (i = 0; i < dataList.length; i++) {
    data = dataList[i];
    ts = data.timestamp;

    var dtStr;
    var jheapUsage = null;
    var s0Percent = null;
    var s1Percent = null;
    var edenPercent = null;
    var oldPercent = null;
    var cpuPercent = null;
    var memPercent = null;
    var diff = ts - prevTimestamp;
    if ((prevTimestamp != -1) && (diff > (interval * 2))) {
      // Intervals with no data for more than 2 minutes are treated as missing
      for (var j = interval; j < diff; j += interval) {
        dtStr = perf.getDateTimeString(prevTimestamp + j);
        xLabels.push(dtStr);
        chartData.cpu.push(null);
        chartData.mem.push(null);
        chartData.jheap.push(null);
        chartData.jheapEden.push(null);
        chartData.jheapOld.push(null);
      }
    }

    jheapUsage = (data.java_heap ? data.java_heap.usage : 0);
    s0Percent = (data.java_heap ? data.java_heap.s0 : 0);
    s1Percent = (data.java_heap ? data.java_heap.s1 : 0);
    edenPercent = (data.java_heap ? data.java_heap.eden : 0);
    oldPercent = (data.java_heap ? data.java_heap.old : 0);
    cpuPercent = data.cpu.usage;
    memPercent = data.mem.usage;
    dtStr = perf.getDateTimeString(ts);
    xLabels.push(dtStr);
    chartData.cpu.push(cpuPercent);
    chartData.mem.push(memPercent);
    chartData.jheap.push(jheapUsage);
    chartData.jheapEden.push(edenPercent);
    chartData.jheapOld.push(oldPercent);
    prevTimestamp = ts;
  }

  for (i = 0; i < postMin; i++) {
    dtStr = perf.getDateTimeString(tsE + i * util.MINUTE);
    xLabels.push(dtStr);
    chartData.cpu.push(null);
    chartData.mem.push(null);
    chartData.jheap.push(null);
    chartData.jheapEden.push(null);
    chartData.jheapOld.push(null);
  }

  var latestCpuUsage = cpuPercent;
  var latestMemUsage = memPercent;
  var latestUsage = jheapUsage;
  var latestS0Usage = s0Percent;
  var latestS1Usage = s1Percent;
  var latestEdenUsage = edenPercent;
  var latestOldUsage = oldPercent;

  if (latestCpuUsage != undefined) {
    perf.cpuMeter.setValue(latestCpuUsage);
    perf.cpuCounter.setValue(latestCpuUsage);
  }

  if (latestMemUsage != undefined) {
    perf.memMeter.setValue(latestMemUsage);
    perf.memCounter.setValue(latestMemUsage);
  }

  if (latestUsage != undefined) {
    perf.heapMeter.setValue(latestUsage);
    perf.heapCounter.setValue(latestUsage);
  }
  if (latestEdenUsage != undefined) {
    perf.heapEdenMeter.setValue(latestEdenUsage);
    perf.heapEdenCounter.setValue(latestEdenUsage);
  }
  if (latestS0Usage != undefined) {
    perf.heapS0Meter.setValue(latestS0Usage);
    perf.heapS0Counter.setValue(latestS0Usage);
  }
  if (latestS1Usage != undefined) {
    perf.heapS1Meter.setValue(latestS1Usage);
    perf.heapS1Counter.setValue(latestS1Usage);
  }
  if (latestOldUsage != undefined) {
    perf.heapOldMeter.setValue(latestOldUsage);
    perf.heapOldCounter.setValue(latestOldUsage);
  }

  $el('#log-date').innerHTML = logDate;

  perf.drawChart(xLabels, chartData);
};

perf.getDateTimeString = function(t) {
  return util.getDateTime(t).toString('%HH:%mm');
};

perf.drawChart = function(xLabels, chartData) {
  perf.xLabels = xLabels;
  var data = {
    labels: xLabels,
    datasets: [
      {
        label: 'CPU usage',
        data: chartData.cpu,
        fill: false,
        borderColor: '#cff',
        lineTension: 0.1,
        borderWidth: 1,
        pointRadius: 0
      },
      {
        label: 'MEM usage',
        data: chartData.mem,
        fill: false,
        borderColor: '#cfc',
        lineTension: 0.1,
        borderWidth: 1,
        pointRadius: 0
      },
      {
        label: 'Heap usage (Survivor 0 + Survivor 1 + Eden + Old)',
        data: chartData.jheap,
        fill: false,
        borderColor: '#bbb',
        lineTension: 0.1,
        borderWidth: 1,
        pointRadius: 0
      },
      {
        label: 'Eden usage',
        data: chartData.jheapEden,
        fill: false,
        borderColor: '#354',
        lineTension: 0.1,
        borderWidth: 1,
        pointRadius: 0,
      },
      {
        label: 'Old space usage',
        data: chartData.jheapOld,
        fill: false,
        borderColor: '#567',
        lineTension: 0.1,
        borderWidth: 1,
        pointRadius: 0,
      }
    ]
  };

  var animation = perf.animation;

  var options = {
    animation: animation,
    tooltips: {
      mode: 'nearest',
      intersect: false,
    },
    legend: {
      display: true,
    },
    responsive: true,
    scales: {
      xAxis: {
        ticks: {
          color: '#ccc',
          callback: function(value, index, ticks) {
            var ret;
            if ((index % 4) == 0) {
              ret = perf.xLabels[index];
            } else {
              ret = '';
            }
            return ret;
          },
        }
      },
      yAxis: {
        type: 'linear',
        position: 'left',
        min: 0,
        max: 100,
        ticks: {
          color: '#ccc',
          stepSize: 10,
        },
        grid: {
          drawBorder: false,
          color: ['#555', '#555', '#555', '#555', '#555', '#555', '#555', '#660', '#840', '#a22', '#888']
        }
      },
    }
  };

  var ctx = document.getElementById('perf-chart');
  var config = {
    type: 'line',
    data: data,
    options: options
  };

  if (perf.chart1) {
    perf.chart1.destroy();
  }
  perf.chart1 = new Chart(ctx, config);
};

/**
 * Converts performance data from its original text format to a JSON object.
 */
perf.convertRawToJsonObject = function(historyTsv) {
  var obj = [];
  if (!historyTsv) {
    return obj;
  }

  var historyList = util.text2list(historyTsv);
  for (var i = 0; i < historyList.length; i++) {
    var record = historyList[i];
    var data = perf.parseData(record);
    obj.push(data);
  }

  return obj;
};

perf.parseData = function(record) {
  // 2022-09-02T18:45:01.960+09:00  cpu: usage=1% us=0% sy=0% wa=0% st=0%  mem: usage=25% total=4019580 used=732056 free=1368864 shared=4824 buff_cache=1918660 available=2995488  java_heap: usage=76% eden=90% s0=0% s1=100% old=51%
  var sections = record.split('  ');

  var time = sections[0];
  var timestamp = util.getDateTime(time).timestamp;

  var data = {
    timestamp: timestamp
  };

  for (var i = 1; i < sections.length; i++) {
    var section = sections[i];
    var fields = section.split(' ');
    var key = fields[0].replace(/:/, '');
    var values = perf.parsePairs(fields);
    data[key] = values;
  }

  data.cpu.usage = perf.parseInt(data.cpu.usage);
  data.mem.usage = perf.parseInt(data.mem.usage);

  if (data.java_heap) {
    var strJheapUsage = data.java_heap.usage; // usage or "no_process"
    data.java_heap.usage = perf.parseInt(strJheapUsage);
    data.java_heap.eden = perf.parseInt(data.java_heap.eden);
    data.java_heap.s0 = perf.parseInt(data.java_heap.s0);
    data.java_heap.s1 = perf.parseInt(data.java_heap.s1);
    data.java_heap.old = perf.parseInt(data.java_heap.old);
  }

  return data;
};

perf.parsePairs = function(fields) {
  var data = {};
  for (var i = 0; i < fields.length; i++) {
    var field = fields[i];
    var wk = field.split('=');
    if (wk.length == 1) {
      continue;
    }
    var name = wk[0];
    var value = wk[1];
    value = value.replace(/%/, '');
    data[name] = value;
  }
  return data;
};

perf.reset = function() {
  perf.n = 0;
  perf.startAutoReload();
  $el('#reset-perf-button').disabled = true;
};

perf.parseInt = function(v, d) {
  if (d == undefined) d = 0;
  var r = parseInt(v);
  if (isNaN(r)) {
    r = d;
  }
  return r;
};

perf.drawLog = function(s) {
  perf.perfLogConsole.write(s);
};
