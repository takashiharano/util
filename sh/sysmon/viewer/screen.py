#==============================================================================
# System Performance Monitor
# Copyright (c) 2023 Takashi Harano
#==============================================================================
import os
import sys

ROOT_DIR = '../../'
sys.path.append(os.path.join(os.path.dirname(__file__), ROOT_DIR + 'libs'))
import util

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
  height: 112px;
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

  <div style="position:relative;margin-top:16px;">
    <div> Disk Usage</div>
    <div>
      <span id="disk-time" style="position:relative;top:3px;"></span>
      <span style="margin-left:16px;">
        <button class="small-button" style="min-width:12px;" onclick="disk.showPrev();">&lt;</button><button class="small-button" style="min-width:12px;margin-left:4px;" onclick="disk.showNext();">&gt;</button><input type="text" id="disk-hist-n" style="margin-left:4px;margin-right:4px;width:36px;text-align:right;"><button id="show-disk-button" class="small-button" onclick="disk.showData();">Show</button>
        <button id="reset-disk-button" class="small-button" onclick="disk.reset();" disabled>Reset</button>
        <span id="disk-hist-info"></span>
      </span>
    </div>
    <div>
      <div style="display:inline-block;">
        <pre id="df-meters" style="margin-top:4px;font-size:14px;"></pre>
      </div>

      <div style="display:inline-block;vertical-align:top;margin-left:40px;font-size:12px;">
        History:
        <div id="df-list" class="console"></div>
      </div>
    </div>
  </div>
</div>

</div>
</body>
</html>
'''
    util.send_html(html)
