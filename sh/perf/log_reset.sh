#!/bin/bash -eu

LOG_DIR="/home/user1/logs"
FILE_NAME="perf.log"

cd ${LOG_DIR}
file_path="${LOG_DIR}/${FILE_NAME}"

if [ -e ${file_path}.8 ]; then
  cp -a ${file_path}.8 ${file_path}.9
fi
if [ -e ${file_path}.7 ]; then
  cp -a ${file_path}.7 ${file_path}.8
fi
if [ -e ${file_path}.6 ]; then
  cp -a ${file_path}.6 ${file_path}.7
fi
if [ -e ${file_path}.5 ]; then
  cp -a ${file_path}.5 ${file_path}.6
fi
if [ -e ${file_path}.4 ]; then
  cp -a ${file_path}.4 ${file_path}.5
fi
if [ -e ${file_path}.3 ]; then
  cp -a ${file_path}.3 ${file_path}.4
fi
if [ -e ${file_path}.2 ]; then
  cp -a ${file_path}.2 ${file_path}.3
fi
if [ -e ${file_path}.1 ]; then
  cp -a ${file_path}.1 ${file_path}.2
fi
if [ -e ${file_path} ]; then
  cp -a ${file_path} ${file_path}.1
  rm -f ${file_path}
  touch ${FILE_NAME}
fi
