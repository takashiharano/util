/*!
 * Disk Usage Viewer
 * Copyright (c) 2024 Takashi Harano
 */
var disk = {};
disk.dataList = [];
disk.n = 0;

disk.init = function() {
  var opt = {
    bufsize: 100
  };
  disk.dfTextConsole = util.initConsole('#df-list', opt);
  disk.getData();
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

disk.getDataCb = function(xhr, res, req) {
  util.loader.hide('#perf-chart-area');

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
  var logtext = data.logtext;
  logtext = util.decodeBase64(logtext);
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
      one_k_blocks: v[1],
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
  html += usageData.time;
  html += '<table>';
  html += '<tr>';
  html += '<th>Filesystem</th>';
  html += '<th>1K-blocks</th>';
  html += '<th>Used</th>';
  html += '<th>Available</th>';
  html += '<th colspan="2">Use%</th>';
  html += '<th>Mounted on</th>';
  html += '</tr>';

  var usages = usageData.usages;
  for (var i = 0; i < usages.length; i++) {
    var usage = usages[i];
    var oneKblkB = util.convByte(usage.one_k_blocks);
    var usedB = util.convByte(usage.used * 1000);
    var availB = util.convByte(usage.available * 1000);
    html += '<tr>';
    html += '<td style="padding-right:10px;">';
    html += usage.filesystem;
    html += '</td>';
    html += '<td style="text-align:right;padding-right:10px;">';
    html += oneKblkB;
    html += '</td>';
    html += '<td style="text-align:right;padding-right:10px;">';
    html += usedB;
    html += '</td>';
    html += '<td style="text-align:right;padding-right:10px;">';
    html += availB;
    html += '</td>';
    html += '<td style="padding-right:10px;">';
    html += util.Meter.buildHTML(usage.use_percent, opt);
    html += '</td>';
    html += '<td style="text-align:right;padding-right:10px;">';
    html += usage.use_percent;
    html += '</td>';
    html += '<td style="padding-right:10px;">';
    html += usage.mounted_on;
    html += '</td>';
    html += '<td>';
    html += '</tr>';
  }
  html += '</table>';
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
