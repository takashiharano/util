/*!
 * System Performance Monitor
 * Copyright (c) 2023 Takashi Harano
 */
var perf = {};
perf.perfLogConsole = null;
perf.timerId = 0;
perf.logList = [];
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

  var opt = {
    bufsize: 1440
  };
  perf.perfLogConsole = util.initConsole('#perflog', opt);

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
  perf.drawData(data);
};

perf.drawData = function(data) {
  var logList = util.text2list(data);
  perf.logList = logList;
  perf.drawLog(logList);
  var obj = perf.convertRawToJsonObject(logList);
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
  var updateInterval = sysmon.INTERVAL;
  util.IntervalProc.start('perf', perf.procInterval, updateInterval, null, true);
  sysmon.led1.blink2();
  sysmon.drawStatus('Watching');
};

perf.stopAutoReload = function() {
  $el('#reset-perf-button').disabled = false;
  $el('#perf-auto-update').checked = false;
  perf.autoReload = false;
  sysmon.led1.off();
  sysmon.drawStatus('Stopped');
};

perf.showData = function() {
  perf.stopAutoReload();
  var n = $el('#perf-hist-n').value;
  if (n.length >= 4) {
    n = perf.convDateToN(n);
  }
  perf.getData(n);
};

perf.convDateToN = function(d1) {
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
      $el('#perf-hist-n').value = '';
    }
    return;
  }

  var data = res.body;
  var n = +data.n;
  perf.n = n;
  $el('#perf-hist-n').value = '';
  if (n > 0) {
    $el('#perf-hist-n').value = n;
  }
  var maxN = data.max_n;
  var logtext = data.logtext;
  logtext = util.decodeBase64(logtext);

  var info = '';
  if (perf.n != 0) {
    info = '[' + perf.n + '/' + maxN + ']';
  }
  $el('#perf-hist-info').innerHTML = info;

  perf.drawData(logtext);
  perf.animation = false;
  util.IntervalProc.next('perf');
};

perf.getDataErrCb = function(xhr, res, req) {
  log.w('getDataErrCb(): HTTP ERROR: ' + xhr.status);
  util.IntervalProc.next('perf');
};

perf.draw = function(dataList) {
  if (dataList.length == 0) {
    sysmon.showInfotip('Performance data is empty');
  }

  var oldestTimestamp = 0;
  if (dataList.length > 0) {
    oldestTimestamp = dataList[0].timestamp;
  }
  var periodFrom = util.getMidnightTimestamp(oldestTimestamp);

  var data = null;
  var ts;
  var firstData = {};
  var lastData = {};
  var tsS = 0;
  var tsE = 0;

  if (dataList.length > 0) {
    firstData = dataList[0];
    lastData = dataList[dataList.length - 1];
    tsS = firstData.timestamp;
    tsE = lastData.timestamp;
  }

  var logDate = '---------- ---';
  if (tsS > 0) {
    logDate = util.getDateTimeString(tsS, '%YYYY-%MM-%DD %W');
  }

  var totalMem = 0;
  if (dataList.length > 0) {
    totalMem = parseInt(lastData.mem.total);
  }
  totalMem = util.convByte(totalMem * 1024);
  $el('#mem-total-val').innerHTML = totalMem;

  var tsMn1 = util.getMidnightTimestamp(tsS);
  var tsMn2 = tsMn1 + util.DAY;
  var preMin = ((tsS - tsMn1) / util.MINUTE) | 0;
  var postMin = ((tsMn2 - tsE) / util.MINUTE) | 0;

  var xLabels = [];
  var chartData = {
    cpu: [],
    mem: [],
    jheap: [],
    jheapEden: [],
    jheapOld: []
  };

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

  perf.drawMeters(data);
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
    plugins: {
      legend: {
        display: true,
        align: 'start',
        labels: {
          color: '#ccc'
        }
      }
    },
    responsive: true,
    scales: {
      xAxis: {
        grid: {
          color: '#444'
        },
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
perf.convertRawToJsonObject = function(logList) {
  var obj = [];
  for (var i = 0; i < logList.length; i++) {
    var record = logList[i];
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

perf.drawLog = function(logList) {
  var txt = '';
  for (var i = 0; i < logList.length; i++) {
    var record = logList[i];
    var data = perf.parseData(record);
    var s = '<span class="log-line" onmouseover="perf.onLogHover(' + i + ');" onmouseout="perf.onLogHover(-1);">' + record + '</span>\n';
    txt += s;
  }
  perf.perfLogConsole.write(txt);
};

perf.onLogHover = function(i) {
  var idx = i;
  if(i < 0) {
    idx = perf.logList.length - 1;
  }
  var record = perf.logList[idx];
  if (!record) return;
  var data = perf.parseData(record);
  perf.drawMeters(data);
};

perf.drawMeters = function(data) {
  var cpuPercent = 0;
  var memPercent = 0;
  var jheapUsage = 0;
  var edenPercent = 0;
  var s0Percent = 0;
  var s1Percent = 0;
  var oldPercent = 0;

  if (data) {
    cpuPercent = data.cpu.usage;
    memPercent = data.mem.usage;
    var javaHeap = data.java_heap;
    if (javaHeap) {
      jheapUsage = javaHeap.usage;
      edenPercent = javaHeap.eden;
      s0Percent = javaHeap.s0;
      s1Percent = javaHeap.s1;
      oldPercent = javaHeap.old;
    }
  }

  perf.cpuMeter.setValue(cpuPercent);
  perf.cpuCounter.setValue(cpuPercent);

  perf.memMeter.setValue(memPercent);
  perf.memCounter.setValue(memPercent);

  perf.heapMeter.setValue(jheapUsage);
  perf.heapCounter.setValue(jheapUsage);

  perf.heapEdenMeter.setValue(edenPercent);
  perf.heapEdenCounter.setValue(edenPercent);

  perf.heapS0Meter.setValue(s0Percent);
  perf.heapS0Counter.setValue(s0Percent);

  perf.heapS1Meter.setValue(s1Percent);
  perf.heapS1Counter.setValue(s1Percent);

  perf.heapOldMeter.setValue(oldPercent);
  perf.heapOldCounter.setValue(oldPercent);
};

$onEnterKey = function(e) {
  if ($el('#perf-hist-n').hasFocus()) {
    perf.showData();
  }
};
