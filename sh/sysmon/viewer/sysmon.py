#==============================================================================
# System Performance Monitor
# Copyright (c) 2023 Takashi Harano
#==============================================================================
import os
import sys

ROOT_DIR = '../../'
sys.path.append(os.path.join(os.path.dirname(__file__), ROOT_DIR + 'libs'))
import util

import conf

PERF_LOG_DIR = conf.PERF_LOG_DIR
PERF_LOG_PATH = conf.PERF_LOG_PATH

DISK_LOG_DIR = conf.DISK_LOG_DIR
DISK_LOG_PATH = conf.DISK_LOG_PATH

#------------------------------------------------------------------------------
# 2022-08-17T23:28:01.800+09:00  cpu: usage=1% us=0% sy=1% wa=0% st=0%  mem: usage=23%
def get_perf_log(n):
    path = PERF_LOG_PATH

    if n != '' and n != '0':
        path += '.' + n

    if util.path_exists(path):
        status = 'OK'
        text = util.read_text_file(path)
        text = util.encode_base64(text)
    else:
        status = 'NOT_FOUND'
        text = None

    max_n = get_max_log_n(PERF_LOG_DIR)

    ret = {
        'status': status,
        'body': {
            'n': n,
            'max_n': max_n,
            'logtext': text
        }
    }
    return ret

#------------------------------------------------------------------------------
# 2024-02-04T16:01:01.728+09:00
# Filesystem     1K-blocks    Used Available Use% Mounted on
# /dev/root       30298176 6200044  24081748  21% /
# tmpfs             426988       0    426988   0% /dev/shm
# tmpfs             170796     952    169844   1% /run
# tmpfs               5120       0      5120   0% /run/lock
# /dev/sda15        106858    6186    100673   6% /boot/efi
# /dev/sdb1        4044512      28   3818488   1% /mnt
# tmpfs              85396       4     85392   1% /run/user/1000
def get_disk_log(n):
    path = DISK_LOG_PATH

    if n != '' and n != '0':
        path += '.' + n

    if util.path_exists(path):
        status = 'OK'
        text = util.read_text_file(path)
        text = util.encode_base64(text)
    else:
        status = 'NOT_FOUND'
        text = None

    max_n = get_max_log_n(PERF_LOG_DIR)

    ret = {
        'status': status,
        'body': {
            'n': n,
            'max_n': max_n,
            'logtext': text
        }
    }
    return ret

#------------------------------------------------------------------------------
def get_max_log_n(log_dir):
    files = util.list_files(log_dir)
    max = -1
    for i in range(len(files)):
        filename = files[i]
        parts = filename.split('.')
        ext = parts[-1]
        n = -1
        try:
            n = int(ext)
        except:
            pass
        if n > max:
            max = n
    return max

#------------------------------------------------------------------------------
def print_screen():
    html = '''
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>System Monitor</title>
<script src="../libs/debug.js"></script>
<script src="../libs/util.js"></script>
<script src="../libs/chart.min.js"></script>
<script src="./sysmon.js"></script>
<script src="./perf.js"></script>
<script src="./disk.js"></script>
<style>
body {
  background: #333;
  color: #fff;
  font-size: 14px;
  font-family: Consolas;
}

#body1 {
  potision: relative;
}

button {
  border: 1px solid #888;
  border-radius: 3px;
  outline: none;
  background: #ddd;
  color: #111;
  font-size: 14px;
  font-family: Consolas;
}

button:hover {
  background: #ccc;
  cursor: pointer;
}

button:disabled {
  color: #888;
}

input {
  font-size: 13px;
  border: none;
  border-bottom: solid 1px #888;
  padding: 2px;
  color: #fff;
  background: transparent;
  font-family: Consolas, Monaco, Menlo, monospace, sans-serif;
  outline: none;
}

input:-webkit-autofill {
  -webkit-transition: all 86400s;
  transition: all 86400s;
}

pre {
  margin: 0;
  padding: 0;
  font-size: 12px;
  font-family: Consolas, Monaco, Menlo, monospace, sans-serif;
}

th {
  text-align: left;
}

.small-button {
  min-width: 30px;
  height: 16px;
  font-size: 8px;
}

.console {
  potision: relative;
  padding: 4px;
  background: #555;
  color: #fff;
  font-size: 13px;
  font-family: Consolas;
}

.log-line:hover {
  background: #777;
}

.meter-block {
  display: inline-block;
  min-width: 80px;
}

#perflog {
  min-width: 1520px;
  width: 100%;
  height: 150px;
  font-size: 12px;
}

#perf-chart-area {
  position: relative;
}

#perf-chart {
  position: relative;
  min-height:296px;
  max-height:296px;
}
#perf-hist-info {
  margin-right: 8px;
  color: #aaa;
  font-size: 12px;
}

#df-list {
  width: 300px;
  height: 120px;
  font-size: 12px;
}
</style>
</head>
<body>
<div id="body1">
<div>
  <div>
    <span id="clock"></span>
  </div>
  <div style="margin-top:10px;">
    <span id="led1"></span>
    <span id="status"></span>
  </div>
</div>

<div style="margin-top:10px;">
  <span>System Performance Log</span>
  <div id="perflog" class="console"></div>

  <div id="perf-history" style="margin-top:16px;">
    <div style="margin-bottom:4px;">
      <span id="log-date" style="color:#ccc;"></span>
    </div>

    <span style="color:#ccc;">
      <span>CPU:</span><span class="meter-block"><span id="cpu-meter"></span><span id="cpu-val" style="margin-left:4px;">-</span></span>
      <span style="margin-left:8px;">MEM:</span><span class="meter-block"><span id="mem-meter"></span><span id="mem-val" style="margin-left:4px;">-</span> / <span id="mem-total-val">-</span></span>

      <span style="margin-left:32px;">Java Heap:</span><span class="meter-block"><span id="java-heap-meter"></span><span id="heap-usage-val" style="margin-left:4px;">-</span></span>
      <span style="margin-left:8px;">Eden:</span><span class="meter-block"><span id="java-eden-heap-meter"></span><span id="heap-eden-usage-val" style="margin-left:4px;">-</span></span>
      <span style="margin-left:4px;">S0:</span><span class="meter-block"><span id="java-s0-heap-meter"></span><span id="heap-s0-usage-val" style="margin-left:4px;">-</span></span>
      <span style="margin-left:4px;">S1:</span><span class="meter-block"><span id="java-s1-heap-meter"></span><span id="heap-s1-usage-val" style="margin-left:4px;">-</span></span>
      <span style="margin-left:8px;">Old:</span><span class="meter-block"><span id="java-old-heap-meter"></span><span id="heap-old-usage-val" style="margin-left:4px;">-</span></span>
    </span>

    <span style="position:absolute;right:8px;">
      <span id="perf-hist-info"></span>
      <button class="small-button" style="min-width:12px;" onclick="perf.showPrev();">&lt;</button><button class="small-button" style="min-width:12px;margin-left:4px;" onclick="perf.showNext();">&gt;</button><input type="text" id="perf-hist-n" style="margin-left:4px;margin-right:4px;width:36px;text-align:right;"><button id="show-perf-button" class="small-button" onclick="perf.showData();">Show</button>
      <div style="display:inline-block;position:relative;top:3px;">
        <input type="checkbox" id="perf-auto-update"><label for="perf-auto-update">Auto Update</label>
      </div>
      <button id="reset-perf-button" class="small-button" onclick="perf.reset();" disabled>Reset</button>
    </span>

    <div id="perf-chart-area" style="min-width:1521px;height:300px;">
      <canvas id="perf-chart"></canvas>
    </div>
  </div>

  <div style="margin-top:16px;">
    <div>Disk Usage</div>
    <pre id="df-meters" style="margin-top:4px;"></pre>
    <div style="margin-top:8px;">
      <div id="df-list" class="console"></div>
    </div>
  </div>
</div>

</div>
</body>
</html>
'''
    util.send_html(html)

#------------------------------------------------------------------------------
def proc_get_perf_log():
    n = util.get_request_param('n', '')
    ret = get_perf_log(n)
    status = ret['status']
    result = ret['body']
    util.send_result_json(status, result)

#------------------------------------------------------------------------------
def proc_get_disk_log():
    n = util.get_request_param('n', '')
    ret = get_disk_log(n)
    status = ret['status']
    result = ret['body']
    util.send_result_json(status, result)

#------------------------------------------------------------------------------
def main():
    action = util.get_request_param('action', '')

    func_name = 'proc_' + action
    g = globals()
    if func_name in g:
        result = g[func_name]()
    else:
        print_screen()
