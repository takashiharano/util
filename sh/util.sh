#!/bin/bash
#
# util.sh
# Copyright 2021 Takashi Harano
# Released under the MIT license
# https://libutil.com/
# v.202108102200
#
DATE_TIME_FORMAT="%Y-%m-%dT%H:%M:%S.%3N%:z"

#######################################
# Error Handler
# Arguments:
#   None
# To enable this function, put the following code.
# trap error_handler ERR
#######################################
function error_handler() {
  EXIT_STATUS=$?
  echo ""
  echo "Error! (${EXIT_STATUS})" >&2
  exit 1
}

#######################################
# Print the current unixtime
# Arguments:
#   None
# Outputs:
#   Writes current unixtime to stdout
#######################################
function unixtime() {
  date +%s.%3N
}

function unix_millis() {
  date +%s%3N
}

#######################################
# Print the current time
# Globals:
#   DATE_TIME_FORMAT
# Arguments:
#   None
# Outputs:
#   Writes current time to stdout
#######################################
function current_time() {
  local t
  t=$(date +"${DATE_TIME_FORMAT}")
  echo "${t}"
}

#######################################
# Logging
# Globals:
#   DATE_TIME_FORMAT
# Arguments:
#   A string to output
# Outputs:
#   Log string
#######################################
function log() {
  local t
  t=$(date +"${DATE_TIME_FORMAT}")
  echo "[${t}] $1"
}

#######################################
# Print CPU/Memory Usage
# Arguments:
#   "tsv" or None
# Outputs:
#   CPU/Memory usage percentage or details in TSV format.
#######################################
function cpu_mem() {
  local fmt
  fmt=""
  if [ $# -ge 1 ]; then
    fmt=$1
  fi

  local vmstat_res
  ###############################
  # 2 times every 1 sec
  # The first one always shows 100%, so use the second one
  vmstat_res=$(vmstat 1 2)
  ###############################

  # procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
  #  r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
  #  0  0 357632 209012   2892 2934188    0    2    33   227  132    8  9  4 86  1  0
  #  0  0 357632 209056   2892 2934188    0    0     0     0 2416  337  0  2 98  0  0
  local cpu_info
  cpu_info=${vmstat_res}
  cpu_info=$(echo "${cpu_info}" | sed -E "s/\s+/ /g")
  cpu_info=$(echo ${cpu_info} | sed -E "s/.+\sst\s//g")
  cpu_info=$(echo "${cpu_info}" | sed -E "s/[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s[0-9]+\s//")

  local vals
  local v_r
  local v_b
  local v_swpd
  local v_free
  #local v_buff
  #local v_cache
  #local v_si
  #local v_so
  local v_bi
  local v_bo
  #local v_in
  #local v_cs
  local v_us
  local v_sy
  local v_id
  local v_wa
  local v_st

  vals=(${cpu_info// / })
  v_r=${vals[0]} # The number of runnable processes (running or waiting for run time)
  v_b=${vals[1]} # The number of processes blocked waiting for I/O to complete
  v_swpd=${vals[2]} # the amount of swap memory used.
  v_free=${vals[3]} # the amount of idle memory
  #v_buff=${vals[4]} # the amount of memory used as buffers
  #v_cache=${vals[5]} # the amount of memory used as cache
  #v_si=${vals[6]} # Amount of memory swapped in from disk (/s)
  #v_so=${vals[7]} # Amount of memory swapped to disk (/s)
  v_bi=${vals[8]} # Blocks received from a block device (blocks/s)
  v_bo=${vals[9]} # Blocks sent to a block device (blocks/s)
  #v_in=${vals[10]} # number of interrupts per second, including the clock
  #v_cs=${vals[11]} # number of context switches per second
  v_us=${vals[12]} # user time
  v_sy=${vals[13]} # system (kernel) time
  v_id=${vals[14]} # idle
  v_wa=${vals[15]} # I/O wait
  v_st=${vals[16]} # time stolen from a vm

  local usage
  usage=$(echo "scale=2; (100 - ${v_id})" | bc)
  usage=$(echo "${usage}" | sed -E "s/\.00$//")

  if [[ ${fmt} = "tsv" ]]; then
    local info_tsv
    info_tsv=$(echo -e "${usage}\\t${v_us}\\t${v_sy}\\t${v_wa}\\t${v_st}\\t${v_r}\\t${v_b}\\t${v_swpd}\\t${v_free}\\t${v_bi}\\t${v_bo}")
    echo -e "${info_tsv}"
  else
    echo "cpu:${usage}%  user:${v_us}%  kernel:${v_sy}%  wa:${v_wa}  st:${v_st}  proc_r:${v_r}  proc_b:${v_b}  swpd:${v_swpd}  free:${v_free}  bi:${v_bi}  bo:${v_bo}"
  fi
}

#######################################
# Print Java heap status
# Arguments:
#   Target name or PID, output format (option)
# Outputs:
#   Java Heap Usage.
#   in TSV format if "tsv" is specified for the second argument.
#
# Usage: print_jheap TARGET_NAME|PID
# e.g.,
# print_jheap "helloworld.jar"
# print_jheap 1234
#######################################
function java_heap() {
  if [ $# -lt 1 ]; then
    echo "Error: no target name or pid"
    return 1
  fi
  local target
  target=$1

  local fmt
  fmt=""
  if [ $# -ge 2 ]; then
    fmt=$2
  fi

  local pid
  pid=""
  if [[ ${target} =~ ^[0-9]+$ ]]; then
    pid=${target}
  else
    # find the PID corresponding to the target name
    local res_array
    res_array=($(jps | grep "${target}"))

    if [ ${#res_array[@]} -eq 0 ]; then
      echo "process_not_found"
      return
    fi
    pid=${res_array[0]}
  fi

  local jstat_res
  ###############################
  jstat_res=$(jstat -gc "${pid}")
  ###############################
  #echo "${pid}"
  #echo "${jstat_res}"

  # 0   1   2   3   4  5  6  7  8  9  10   11   12  13   14  15   16
  # S0C S1C S0U S1U EC EU OC OU MC MU CCSC CCSU YGC YGCT FGC FGCT GCT
  #
  # New = Survivor0(From) + Survivor1(To) + Eden
  # Old = OC
  # -Xmx = New + Old
  #
  # Allocated
  # (S0C + S1C + EC + OC) / 1024 = MB
  #
  # Used
  # (S0U + S1U + EU + OU) / 1024 = MB
  #
  # https://docs.oracle.com/javase/jp/8/docs/technotes/tools/unix/jstat.html
  local heap_info
  heap_info=$(echo "${jstat_res}" | sed -E "s/\r//g")
  heap_info=$(echo "${heap_info}" | sed -E "s/\s+/ /g")
  heap_info=$(echo "${heap_info}" | sed -E "s/(.+[A-Z]) ([0-9]*)/\2/g")
  #echo "${heap_info}"

  local vals
  vals=(${heap_info//,/ })

  local s0c
  local s1c
  local s0u
  local s1u
  local ec
  local eu
  local oc
  local ou
  #local mc
  #local mu
  #local ccsc
  #local ccsu
  #local ygc
  #local ygct
  local fgc
  local fgct
  #local gct

  s0c=${vals[0]} # Survisor0: capacity (KB)
  s1c=${vals[1]} # Survisor1: capacity (KB)
  s0u=${vals[2]} # Survisor0: utilization (KB)
  s1u=${vals[3]} # Survisor1: utilization (KB)
  ec=${vals[4]} # Eden: capacity (KB)
  eu=${vals[5]} # Eden: utilization (KB)
  oc=${vals[6]} # Old: capacity (KB)
  ou=${vals[7]} # Old: utilization: (KB)
  #mc=${vals[8]} # Meta space: capacity (KB)
  #mu=${vals[9]} # Meta space: utilization (KB)
  #ccsc=${vals[10]} # Compressed class space capacity (KB)
  #ccsu=${vals[11]} # Compressed class space used (KB)
  #ygc=${vals[12]} # Number of young generation garbage collection events
  #ygct=${vals[13]} # Young generation garbage collection time
  fgc=${vals[14]} # Number of full GC events
  fgct=${vals[15]} # Full garbage collection time
  #gct=${vals[16]} # Total garbage collection time

  local new
  local old
  local max
  local used
  local free
  local usage

  new=$(echo "scale=2; (${s0c} + ${s1c} + ${ec}) / 1024" | bc)
  old=$(echo "scale=2; ${oc} / 1024" | bc)
  max=$(echo "scale=2; ${new} + ${old}" | bc)
  used=$(echo "scale=2; (${s0u} + ${s1u} + ${eu} + ${ou}) / 1024" | bc)
  free=$(echo "scale=2; (${max} - ${used})" | bc)
  usage=$(echo "scale=2; (${used} / ${max}) * 100" | bc)
  usage=$(echo "${usage}" | sed -E "s/\.00$//")

  local ep
  local s0p
  local s1p
  local op
  ep="0"
  s0p="0"
  s1p="0"
  op="0"

  if [ "${ec}" != "0.0" ]; then
    ep=$(echo "scale=2; (${eu} / ${ec}) * 100" | bc)
    ep=$(echo "${ep}" | sed -E "s/\.00$//")
  fi
  if [ "${s0c}" != "0.0" ]; then
    s0p=$(echo "scale=2; (${s0u} / ${s0c}) * 100" | bc)
    s0p=$(echo "${s0p}" | sed -E "s/\.00$//")
  fi
  if [ "${s1c}" != "0.0" ]; then
    s1p=$(echo "scale=2; (${s1u} / ${s1c}) * 100" | bc)
    s1p=$(echo "${s1p}" | sed -E "s/\.00$//")
  fi
  if [ "${oc}" != "0.0" ]; then
    op=$(echo "scale=2; (${ou} / ${oc}) * 100" | bc)
    op=$(echo "${op}" | sed -E "s/\.00$//")
  fi

  if [[ ${fmt} = "tsv" ]]; then
    echo -e "${usage}\\t${used}\\t${free}\\t${max}\\t${ep}\\t${s0p}\\t${s1p}\\t${op}\\t${fgc}\\t${fgct}"
  else
    echo "Used:${used}MB (${usage}%)  Free:${free}MB  Max:${max}MB  Eden:${ep}%  S0:${s0p}%  S1:${s1p}%  Old:${op}%  FullGC:${fgc} (${fgct}s)"
  fi
}
