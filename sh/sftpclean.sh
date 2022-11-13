#!/bin/bash -eu
############################################################################
# SFTP File Cleaner
# Copyright 2022 Takashi Harano
# Released under the MIT license
# https://libutil.com/
############################################################################

HOST="localhost"
USER="user1"
PORT=22
TARGET_DIR="/home/user1/data"
RETENTION_SEC=86400

NOW=$(date +%s)
EXPIRES_AT=$(echo "${NOW} - ${RETENTION_SEC}" | bc)

sftp_ret=""

###########################################
#
# Converts YYYY-MM-DD_hh.mm.ss to unixtime
#
###########################################
function to_unixtime() {
  local datepart
  local datetime
  local unixtime
  local ret

  # 2022-11-13_12.34.56
  datepart=$1

  # 20221113T12:34:56
  datetime=$(echo "${datepart}" | sed -E "s/[:_/T\.\-]//g")
  if [[ ${datetime} =~ ([0-9]{8})([0-9]{2})([0-9]{2})([0-9]{2}) ]]; then
    datetime="${BASH_REMATCH[1]} ${BASH_REMATCH[2]}:${BASH_REMATCH[3]}:${BASH_REMATCH[4]}"
  fi

  ret=$(date +%s --date "${datetime}")
  echo ${ret}
}

###########################################
#
# Execute command via SFTP
#
###########################################
function exec_sftp_cmd() {
  local sftp_cmd
  sftp_cmd=$1

  sftp_ret=$(expect -c "
  set timeout 1
  spawn sftp -P ${PORT} ${USER}@${HOST}
  expect \"sftp&gt;\"
  ${sftp_cmd}
  send \"bye\r\"
  expect eof
  exit
  ")
}

###########################################
#
# Removes the files via SFTP
#
###########################################
function delete_files() {
  local cmd
  local cmd_rm

  cmd_rm=$1
  cmd="  send \"cd ${TARGET_DIR}\r\"
  expect \"sftp&gt;\"
  ${cmd_rm}"

  exec_sftp_cmd "${cmd}"
}

###########################################
#
# Lists file names on the SFTP server
#
###########################################
sftp_cmd_ls="send \"cd ${TARGET_DIR}\r\"
expect \"sftp&gt;\"
send \"ls -1\r\"
expect \"sftp&gt;\"
"

exec_sftp_cmd "${sftp_cmd_ls}"

###########################################
#
# Lists the expired file names
#
###########################################
target_files=()
shopt -s extglob lastpipe
echo "${sftp_ret}" | while read line; do
  filename=${line}

  # data-2022-11-13_12.34.56.csv -> (2022-11-13_12.34.56)
  if [[ ${filename} =~ ([0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}\.[0-9]{2}\.[0-9]{2}) ]]; then
    # 2022-11-13_12.34.56 -> unixtime (1668310496)
    unixtime=$(to_unixtime "${BASH_REMATCH[1]}")

    if [ ${unixtime} -lt ${EXPIRES_AT} ]; then
      target_files+=("${filename}")
    fi
  fi
done

###########################################
#
# Prepares the SFTP command for remove
#
###########################################
LF="
"

sftp_rm_cmd=""
for i in "${!target_files[@]}"; do
  sftp_rm_cmd+=" send \"rm ${target_files[$i]}\r\"${LF}"
  sftp_rm_cmd+=" expect \"sftp&gt;\"${LF}"
done

###########################################
#
# Execute delete files
#
###########################################
if [ -n "${sftp_rm_cmd}" ]; then
  delete_files "${sftp_rm_cmd}"
fi
