/*!
 * Disk Usage Viewer
 * Copyright (c) 2024 Takashi Harano
 */
var disk = {};
disk.dataList = [];
disk.n = 0;

disk.init = function() {
  var opt = {
    bufsize: 100,
    background: '#333'
  };
  disk.dfTextConsole = util.initConsole('#df-list', opt);
  disk.getData();
};

disk.showData = function() {
  $el('#reset-disk-button').disabled = false;
  var n = $el('#disk-hist-n').value;
  if (n.length >= 4) {
    n = sysmon.convDateToN(n);
  }
  disk.getData(n);
};

disk.getData = function(n) {
  if (!n) {
    n = '';
  }
  var param = {
    n: n
  };
  sysmon.callApi('get_disk_log', param, disk.getDataCb, disk.getDataErrCb);
};

disk.showPrev = function() {
  $el('#reset-disk-button').disabled = false;
  disk.n++;
  disk.getData(disk.n);
};

disk.showNext = function() {
  $el('#reset-disk-button').disabled = false;
  disk.n--;
  if (disk.n < 0) {
    disk.n = 0;
  }
  disk.getData(disk.n);
};

disk.reset = function() {
  disk.n = 0;
  disk.getData();
  $el('#reset-disk-button').disabled = true;
};

disk.getDataCb = function(xhr, res, req) {
  if (res.status == 'NOT_FOUND') {
    if (disk.n == 0) {
      disk.dfTextConsole.write('No disk usage logs');
      return;
    }
    disk.n--;
    if (disk.n < 0) {
      disk.n = 0;
    }
    return;
  }

  var data = res.body;

  var n = +data.n;
  disk.n = n;
  $el('#disk-hist-n').value = '';
  if (n > 0) {
    $el('#disk-hist-n').value = n;
  }
  var maxN = data.max_n;
  var logtext = data.logtext;
  logtext = util.decodeBase64(logtext);

  var info = '';
  if (disk.n != 0) {
    info = '[' + disk.n + '/' + maxN + ']';
  }
  $el('#disk-hist-info').innerHTML = info;

  disk.dataList = disk.parseData(logtext);
  disk.drawDiskUsageMeters(disk.dataList.length - 1);
  var html = disk.buildShowDiskUsageLink(disk.dataList);
  disk.drawLog(html);
};

disk.getDataErrCb = function(xhr, res, req) {
  log.w('getDataErrCb(): HTTP ERROR: ' + xhr.status);
};

disk.drawLog = function(logtext) {
  disk.dfTextConsole.write(logtext);
};

disk.parseData = function(logtext) {
  var txtList = util.text2list(logtext);
  var dataList = [];
  var skip = true;
  for (var i = 0; i < txtList.length; i++) {
    var txt = txtList[i];
    if (!txt) {
      dataList.push(data);
      skip = true;
      continue;
    }
    if (txt.match(/^\d{4}-\d{2}-\d{2}/)) {
      var data = {
        time: txt,
        usages: []
      };
      continue;
    }
    if (skip) {
      skip = false;
      continue;
    }
    txt = txt.replace(/\s{2,}/g, ' ');
    var v = txt.split(' ');
    var usage = {
      filesystem: v[0],
      total: v[1],
      used: v[2],
      available: v[3],
      use_percent: v[4],
      mounted_on: v[5]
    }
    data.usages.push(usage);
  }
  return dataList;
};

disk.drawDiskUsageMeters = function(index) {
  var usageData = disk.dataList[index];
  if (!usageData) return;
  var opt = {
    low: 80,
    high: 90,
    optimum: 50
  };
  var html = '';
  html += '<table>';
  html += '<tr>';
  html += '<th>Filesystem</th>';
  html += '<th style="text-align:right;min-width:50px;padding-right:20px;">Total</th>';
  html += '<th style="text-align:right;min-width:50px;padding-right:10px;">Used</th>';
  html += '<th colspan="2">&nbsp;</th>';
  html += '<th style="text-align:right;min-width:50px;padding-right:20px;">Free</th>';
  html += '<th>Mounted on</th>';
  html += '</tr>';

  var usages = usageData.usages;
  usages = util.sortObjectList(usages, 'mounted_on');
  for (var i = 0; i < usages.length; i++) {
    var usage = usages[i];
    var totalB = util.convByte(usage.total * 1000);
    var usedB = util.convByte(usage.used * 1000);
    var availB = util.convByte(usage.available * 1000);
    html += '<tr>';
    html += '<td style="padding-right:20px;">' + usage.filesystem + '</td>';
    html += '<td style="text-align:right;padding-right:20px;">' + totalB + '</td>';
    html += '<td style="text-align:right;padding-right:10px;">' + usedB + '</td>';
    html += '<td>' + util.Meter.buildHTML(usage.use_percent, opt) + '</td>';
    html += '<td style="text-align:right;padding-right:20px;">' + usage.use_percent + '</td>';
    html += '<td style="text-align:right;padding-right:20px;">' + availB + '</td>';
    html += '<td style="padding-right:10px;">' + usage.mounted_on + '</td>';
    html += '</tr>';
  }
  html += '</table>';

  $el('#disk-time').innerHTML = usageData.time;
  $el('#df-meters').innerHTML = html;
};

disk.buildShowDiskUsageLink = function(dataList) {
  var html = '';
  for (var i = 0; i < dataList.length; i++) {
    var data = dataList[i];
    var time = data.time;
    html += '<span class="pseudo-link" onmouseover="disk.drawDiskUsageMeters(' + i + ');">' + time + '</span>\n';
  }
  return html;
};
