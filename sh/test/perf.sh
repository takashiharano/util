#!/bin/bash -u
# shellcheck disable=SC1091
. ../util.sh
trap error_handler ERR

interval=0
if [ $# -ge 1 ]; then
  interval=$1
fi

fmt=""
if [ $# -ge 2 ]; then
  fmt=$2
fi

sep="  "
if [ "${fmt}" == "tsv" ]; then
  sep="\\t"
fi

while true
do
  if [ "${fmt}" == "tsv" ]; then
    time=$(unix_millis)
  else
    time=$(current_time)
  fi
  cpumem=$(cpu_mem ${fmt})
  jheap=$(java_heap "jboss-modules.jar" ${fmt})
  echo -e "${time}${sep}cpumem${sep}${cpumem}${sep}jheap${sep}${jheap}"
  if [ ${interval} -eq 0 ]; then
    exit 0
  fi
  sleep ${interval}
done
