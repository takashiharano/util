#!/bin/bash
############################################################################
# util.sh
# Copyright 2021 Takashi Harano
# Released under the MIT license
# https://libutil.com/
# v.202211130007
############################################################################
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
#   Date string
# Outputs:
#   Writes current unixtime to stdout
#######################################
function unixtime() {
  if [ $# -lt 1 ]; then
    date +%s.%3N
  else
    date +%s.%3N --date "$1"
  fi
}

#######################
function unixmillis() {
  if [ $# -lt 1 ]; then
    date +%s%3N
  else
    date +%s%3N --date "$1"
  fi
}

#######################################
# Convert unixtime tp string
# Arguments:
#   Unixtime
# Outputs:
#   A string corresponding to the given unixtime
#######################################
function datestring() {
  local t
  local fmt
  if [ $# -ge 1 ]; then
    t=$1
  else
    t=$(date +%s.%3N)
  fi
  if [ $# -ge 2 ]; then
    fmt="$2"
  else
    fmt="${DATE_TIME_FORMAT}"
  fi
  date --date @"${t}" +"${fmt}"
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
