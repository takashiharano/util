#!/bin/bash -eu
############################################################################
# Serialize Date & Time
# Copyright 2022 Takashi Harano
# Released under the MIT License
# https://libutil.com/
#
# Created: 2022-11-12
# Updated: 2025-08-01
#
# Usage:
#   ./serialize_datetime.sh [DATETIME]
#
# Example:
#   ./serialize_datetime.sh 2025-01-02T12:34:56.789
#   ./serialize_datetime.sh 2025-01-02_12:34:56.789
#   ./serialize_datetime.sh "2025-01-02 12:34:56.789"
#
# Outputs:
#   -> 20250102123456789
#
# Acceptable formats:
#   DATE:
#     20250102
#     2025-01-02
#     2025/01/02
#     2025_01_02
#     2025.01.02
#
#  [separator: T | _ | space]
#
#  TIME:
#     123456789
#     12:34:56.789
#     12_34_56_789
#     12.34.56.789
############################################################################

serialize_datetime() {
  local w
  local d
  local t
  local prt
  local yyyy
  local mm
  local dd
  local hh
  local mi
  local ss
  local f
  local dt

  w=$1
  w=$(echo "${w}" | sed -E -e 's/\s{2,}/ /g' -e 's/T/ /' -e 's/_/ /')

  if [[ ! ${w} =~ [-/] ]]; then
    echo "$(_serialize_datetime ${w})"
    return
  fi

  IFS=' ' read -r -a prt <<< "$w"
  d=${prt[0]}
  t=""
  if [ ${#prt[@]} -ge 2 ]; then
    t=${prt[1]}
  fi
  d=$(echo "${d}" | sed -E "s/\//-/g")
  prt=(${d//-/ })
  yyyy="${prt[0]}"

  mm="01"
  if [ ${#prt[@]} -ge 2 ]; then
    mm=$(pad_zero "${prt[1]}")
  fi

  dd="01"
  if [ ${#prt[@]} -ge 3 ]; then
    dd=$(pad_zero "${prt[2]}")
  fi

  if [ "${mm}"  = "00" ]; then
    mm="01"
  fi
  if [ "${dd}"  = "00" ]; then
    dd="01"
  fi
  d="${yyyy}${mm}${dd}"

  t=$(echo "${t}" | sed -E "s/\./:/g")

  prt=(${t//:/ })
  hh="00"
  if [ ${#prt[@]} -ge 1 ]; then
    hh=$(pad_zero "${prt[0]}")
  fi

  mi="00"
  if [ ${#prt[@]} -ge 2 ]; then
    mi=$(pad_zero "${prt[1]}")
  fi

  ss="00"
  if [ ${#prt[@]} -ge 3 ]; then
    ss=$(pad_zero "${prt[2]}")
  fi

  f=""
  if [ ${#prt[@]} -ge 4 ]; then
    f="${prt[3]}"
  fi

  t="${hh}${mi}${ss}${f}"
  dt="${d}${t}"
  echo "$(_serialize_datetime ${dt})"
}

_serialize_datetime() {
  local s
  local w
  local w1
  local w2
  local w3

  s=$1
  if [ $# -ge 2 ]; then
    s="${s}$2"
  fi

  s=$(echo "${s}" | sed -E "s/\//-/g; s/[-\s:\._]//g")
  s="${s}0000000000000"
  s="${s:0:17}"

  w="${s:4:2}"
  if [ "${w}"  = "00" ]; then
    w1="${s:0:4}"
    w2="01"
    w3="${s:6}"
    s="${w1}${w2}${w3}"
  fi

  w="${s:6:2}"
  if [ "${w}"  = "00" ]; then
    w1="${s:0:6}"
    w2="01"
    w3="${s:8}"
    s="${w1}${w2}${w3}"
  fi

  echo "${s}";
}

#  1 -> 01
# 12 -> 12
pad_zero() {
  printf "%02d" "$1"
}

#---------------------------------------------------------------------------
if [ $# -lt 1 ]; then
  echo "Usage: ./serialize_datetime.sh [DATETIME]"
  echo "e.g., ./serialize_datetime.sh 2025-01-02T12:34:56.789"
  exit 1
fi

s=$(serialize_datetime "$1")
echo "${s}"
