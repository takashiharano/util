#!/bin/bash -eu
#
# perf.sh
# Copyright 2022 Takashi Harano
# Released under the MIT license
# https://libutil.com/
#
DATE_TIME_FORMAT="%Y-%m-%dT%H:%M:%S.%3N%:z"

#######################################
# Get CPU Usage
#
# Outputs:
#   CPU usage percentage.
#######################################
function get_cpu_usage() {
  local cmd_res
  ###############################
  # 2 times every 1 sec
  # The first one always shows 100%, so use the second one
  cmd_res=$(vmstat 1 2)
  ###############################

  # procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
  #  r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
  #  0  0    268 425888  89220 2141776    0    0    76    74  491  247  2  0 97  1  0
  #  0  0    268 425888  89220 2141776    0    0     0     0  627  230  0  0 100  0  0
  cmd_res=$(echo "${cmd_res}" | head -4 | tail -1)
  cmd_res=$(echo "${cmd_res}" | sed -E "s/\s+/ /g")

  local vals
  #local v_r
  #local v_b
  #local v_swpd
  #local v_free
  #local v_buff
  #local v_cache
  #local v_si
  #local v_so
  #local v_bi
  #local v_bo
  #local v_in
  #local v_cs
  local v_us
  local v_sy
  local v_id
  local v_wa
  local v_st

  vals=(${cmd_res// / })
  #v_r=${vals[0]} # The number of runnable processes (running or waiting for run time)
  #v_b=${vals[1]} # The number of processes blocked waiting for I/O to complete
  #v_swpd=${vals[2]} # the amount of swap memory used.
  #v_free=${vals[3]} # the amount of idle memory
  #v_buff=${vals[4]} # the amount of memory used as buffers
  #v_cache=${vals[5]} # the amount of memory used as cache
  #v_si=${vals[6]} # Amount of memory swapped in from disk (/s)
  #v_so=${vals[7]} # Amount of memory swapped to disk (/s)
  #v_bi=${vals[8]} # Blocks received from a block device (blocks/s)
  #v_bo=${vals[9]} # Blocks sent to a block device (blocks/s)
  #v_in=${vals[10]} # number of interrupts per second, including the clock
  #v_cs=${vals[11]} # number of context switches per second
  v_us=${vals[12]} # user time
  v_sy=${vals[13]} # system (kernel) time
  v_id=${vals[14]} # idle
  v_wa=${vals[15]} # I/O wait
  v_st=${vals[16]} # time stolen from a vm

  # usage = 100 - idle
  local usage
  usage=$(echo "100 - ${v_id}" | bc)

  echo "cpu: usage=${usage}% us=${v_us}% sy=${v_sy}% wa=${v_wa}% st=${v_st}%"
}

#######################################
# Get memory usage
#
# Outputs:
#   Memory usage percentage.
#######################################
function get_mem_usage() {
  # $ free
  #                total        used        free      shared  buff/cache   available
  # Mem:         4018472     1043448     1576308       32648     1398716     2694192
  # Swap:         703928           0      703928
  local cmd_res
  cmd_res=$(free)

  cmd_res=$(echo "${cmd_res}" | head -2 | tail -1)
  cmd_res=$(echo "${cmd_res}" | sed -E "s/\s+/ /g")
  cmd_res=$(echo "${cmd_res}" | sed -E "s/Mem: //")

  local vals
  local total
  local used
  local free
  local shared
  local buff_cache
  local available
  local act_used

  vals=(${cmd_res//,/ })
  total=${vals[0]}
  used=${vals[1]}
  free=${vals[2]}
  shared=${vals[3]}
  buff_cache=${vals[4]}
  available=${vals[5]}

  # actual used = total - available
  act_used=$(echo "${total} - ${available}" | bc)

  local usage
  usage=$(echo "scale=2; (${act_used} / ${total}) * 100" | bc)
  usage=$(echo "${usage}" | sed -E "s/\.00$//")

  local res
  res="mem: usage=${usage}% total=${total} used=${used} free=${free} shared=${shared} buff_cache=${buff_cache} available=${available}"
  echo "${res}"
}

#######################################
# Get Java heap status
# Arguments:
#   Target name or vmid
# Outputs:
#   Java Heap Usage.
#
# Usage: print_jheap TARGET_NAME|VMID
# e.g.,
# print_jheap "helloworld.jar"
# print_jheap 1234
#######################################
function get_java_heap_usage() {
  if [ $# -lt 1 ]; then
    echo "java_heap: [ERROR] no_target_name_or_vmid"
    return 1
  fi
  local target
  target=$1

  local vmid
  vmid=""
  if [[ ${target} =~ ^[0-9]+$ ]]; then
    vmid=${target}
  else
    # find the vmid corresponding to the target name
    local res_array
    res_array=($(jps | grep "${target}"))

    if [ ${#res_array[@]} -eq 0 ]; then
      echo "java_heap: nodata"
      return
    fi
    vmid=${res_array[0]}
  fi

  local cmd_res
  ###############################
  # $ jstat -gc 1234
  #  S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT    CGC    CGCT     GCT   
  # 2112.0 2112.0 1030.8  0.0   16896.0   9013.5   42368.0     7335.3   14848.0 13818.9 1792.0 1446.9      4    0.055   0      0.000   -          -    0.055
  cmd_res=$(jstat -gc "${vmid}")
  ###############################

  if [ $? -ne 0 ]; then
    echo "java_heap: [ERROR] jstat_execution_error"
    return
  fi

  # 0   1   2   3   4  5  6  7  8  9  10   11   12  13   14  15   16
  # S0C S1C S0U S1U EC EU OC OU MC MU CCSC CCSU YGC YGCT FGC FGCT GCT
  #
  # New = Survivor0(From) + Survivor1(To) + Eden
  # Old = OC
  # Total = New + Old
  #
  # Allocated
  # (S0C + S1C + EC + OC) / 1024 = MB
  #
  # Used
  # (S0U + S1U + EU + OU) / 1024 = MB
  #
  # https://docs.oracle.com/javase/jp/8/docs/technotes/tools/unix/jstat.html
  local heap_info
  heap_info=$(echo "${cmd_res}" | sed -E "s/\r//g")
  heap_info=$(echo "${heap_info}" | sed -E "s/\s+/ /g")
  heap_info=$(echo "${heap_info}" | sed -E "s/(.+[A-Z]) ([0-9]*)/\2/g")

  local vals
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
  #local fgc
  #local fgct
  #local gct

  vals=(${heap_info//,/ })
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
  #fgc=${vals[14]} # Number of full GC events
  #fgct=${vals[15]} # Full garbage collection time
  #gct=${vals[16]} # Total garbage collection time

  local new
  local old
  local total
  local used
  #local free
  local usage

  new=$(echo "scale=2; (${s0c} + ${s1c} + ${ec}) / 1024" | bc)
  old=$(echo "scale=2; ${oc} / 1024" | bc)
  total=$(echo "scale=2; ${new} + ${old}" | bc)
  used=$(echo "scale=2; (${s0u} + ${s1u} + ${eu} + ${ou}) / 1024" | bc)
  #free=$(echo "scale=2; (${total} - ${used})" | bc)
  usage=$(echo "scale=2; (${used} / ${total}) * 100" | bc)
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

  echo "java_heap: usage=${usage}% eden=${ep}% s0=${s0p}% s1=${s1p}% old=${op}%"
}

#######################################
# ./perf.sh [delay [count]] [java_process]
#
# delay         Pause wait seconds between each display
# count         Repeat count
# java_process  PID or name
#
# e.g.,
#  ./perf.sh 1 5 1234
#  ./perf.sh 1 5 hello.jar
#
# Outputs:
# 2022-08-31T22:50:35.884+09:00  cpu: usage=0% us=0% sy=0% wa=0% st=0%  mem: usage=33% total=4018456 used=1059428 free=2024060 shared=32640 buff_cache=934968 available=2687824  java_heap: usage=29% eden=62% s0=48% s1=0% old=17%
#######################################
delay=0
count=1
javaproc=""

if [ $# -ge 1 ]; then
  delay=$1
  delay=$(echo "${delay} - 1" | bc)

  if [ "${delay}" -lt 0 ]; then
    delay=0
  fi
fi

if [ $# -ge 2 ]; then
  count=$2
fi

if [ $# -ge 3 ]; then
  javaproc=$3
fi

if [ "${count}" -eq 0 ]; then
  count=1
fi

cnt=0
while [ ${count} -lt 0 ] || [ ${cnt} -lt ${count} ]; do
  datetime=$(date +"${DATE_TIME_FORMAT}")
  cpu_usage=$(get_cpu_usage)
  mem_usage=$(get_mem_usage)

  res="$datetime  ${cpu_usage}  ${mem_usage}"

  if [ -n "${javaproc}" ]; then
    jheap_usage=$(get_java_heap_usage "${javaproc}")
    res="${res}  ${jheap_usage}"
  fi

  echo "${res}"

  cnt=$(echo "${cnt} + 1" | bc)
  sleep ${delay}
done
