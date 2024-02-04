#!/bin/bash -eu
############################################################################
# disk_logging.sh
# Copyright 2024 Takashi Harano
# Released under the MIT license
# https://libutil.com/
#
# Usage:
# [cron]
# 1 */3 * * * /path/to/sh/disk/disk_logging.sh append
# 0 0 * * * /path/to/sh/disk/disk_logging.sh reset
############################################################################

DATE_TIME_FORMAT="%Y-%m-%dT%H:%M:%S.%3N%:z"

#---------------------------------------------------------------------------
LOG_DIR="/path/to/logs/disk"
FILE_NAME="diskusage.log"
MAX_N=90
#---------------------------------------------------------------------------

function append_log() {
  datetime=$(date +"${DATE_TIME_FORMAT}")
  LOG_PATH="${LOG_DIR}/${FILE_NAME}"
  cd $(dirname ${0})

  dusk_usage=$(df)
  res="$datetime\n${dusk_usage}\n"

  echo -e "${res}" >> ${LOG_PATH}
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
  echo "Usage: ./disk_logging.sh append|reset"
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
