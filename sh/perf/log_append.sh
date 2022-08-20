#!/bin/bash -eu

LOG_DIR="/home/user1/logs"
FILE_NAME="perf.log"
JVM=""

LOG_PATH="${LOG_DIR}/${FILE_NAME}"
cd $(dirname ${0})
./perf.sh 1 1 ${JVM} >> ${LOG_PATH}
