#!/bin/bash -eu

LOG_DIR="/home/user1/logs/perf"
FILE_NAME="perf.log"
MAX_N=90

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
