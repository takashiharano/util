#!/bin/bash -eu
############################################################################
# perf_logging.sh
# Copyright 2023 Takashi Harano
# Released under the MIT license
# https://libutil.com/
#
# Usage:
# [cron]
# */1 * * * * ~/sh/perf/perf_logging.sh append
# 0 0 * * * ~/sh/perf/perf_logging.sh reset
############################################################################

#---------------------------------------------------------------------------
LOG_DIR="/path/to/logs/perf"
FILE_NAME="perf.log"
JVM=""
MAX_N=90
#---------------------------------------------------------------------------

function append_log() {
  LOG_PATH="${LOG_DIR}/${FILE_NAME}"
  cd $(dirname ${0})
  ./perf.sh 1 1 ${JVM} >> ${LOG_PATH}
}

function reset_log() {
  cd ${LOG_DIR}
  file_path="${LOG_DIR}/${FILE_NAME}"

  for i in $(seq ${MAX_N} -1 1); do
    n=$((i - 1))
    if [ ${n} -eq 0 ]; then
      if [ -e ${file_path} ]; then
        cp -a ${file_path} ${file_path}.1
        rm -f ${file_path}
        touch ${FILE_NAME}
      fi
    else
      if [ -e ${file_path}.${n} ]; then
        cp -a ${file_path}.${n} ${file_path}."${i}"
      fi
    fi
  done
}

function print_usage() {
  echo "Usage: ./logging.sh append|reset"
}

if [ $# -lt 1 ]; then
  print_usage
  exit 1
fi

op=$1
if [ "${op}" = "append" ]; then
  append_log
elif [ "${op}" = "reset" ]; then
  reset_log
else
  print_usage
  exit 1
fi
