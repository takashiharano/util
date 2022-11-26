#!/bin/bash -eu
############################################################################
# File Rotator
# Copyright 2022 Takashi Harano
# Released under the MIT license
# https://libutil.com/
############################################################################

if [ $# -lt 2 ]; then
  echo "Usage: ./filerotate.sh DIR_PATH FILE_NAME [N]"
  exit 1
fi

DIR=$1
FILE_NAME=$2
MAX_N=5

if [ $# -ge 3 ]; then
  MAX_N=$3
fi

cd ${DIR}
file_path="${DIR}/${FILE_NAME}"

for i in $(seq ${MAX_N} -1 1); do
  n=$((i - 1))
  if [ ${n} -eq 0 ]; then
    if [ -e ${file_path} ]; then
      cp -a ${file_path} ${file_path}.1
      rm -f ${file_path}
    fi
  else
    if [ -e ${file_path}.${n} ]; then
      cp -a ${file_path}.${n} ${file_path}."${i}"
    fi
  fi
done
