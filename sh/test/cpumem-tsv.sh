#!/bin/bash -u
# shellcheck disable=SC1091
. ../util.sh
trap error_handler ERR

interval=0
if [ $# -ge 1 ]; then
  interval=$1
fi

while true
do
  cpu_mem "tsv"
  if [ ${interval} -eq 0 ]; then
    exit 0
  fi
  sleep ${interval}
done
