#!/bin/bash -eu
############################################################################
# SFTP File Receiver
# Copyright 2023 Takashi Harano
# Released under the MIT license
# https://libutil.com/
############################################################################

#---------------------------------------------------------------------------
HOST="localhost"
USER="user1"
PVTKEY="~/.ssh/id_ed25519"
PORT=22
TARGET_DIR="/path/to"
#---------------------------------------------------------------------------

LF="
"
sftp_ret=""

###########################################
#
# Execute command via SFTP
#
###########################################
function exec_sftp_cmd() {
  local sftp_cmd
  local cmd

  sftp_cmd=$1

  cmd="spawn sftp"
  if [ -n "${PVTKEY}" ]; then
    cmd+=" -i ${PVTKEY}"
  fi
  cmd+=" -P ${PORT} ${USER}@${HOST}"

  sftp_ret=$(expect -c "
  set timeout 1
  ${cmd}
  expect \"sftp&gt;\"
  ${sftp_cmd}
  send \"bye\r\"
  expect eof
  exit
  ")
}

###########################################
#
# Execute ls command via SFTP
#
###########################################
function exec_sftp_ls_cmd() {
  sftp_cmd_ls="send \"cd ${TARGET_DIR}\r\"
  expect \"sftp&gt;\"
  send \"ls -1\r\"
  expect \"sftp&gt;\"
  "
}

###########################################
#
# Get the files via SFTP
#
###########################################
function get_files() {
  local cmd
  local cmd_get

  cmd_get=$1
  cmd="  send \"cd ${TARGET_DIR}\r\"
  expect \"sftp&gt;\"
  ${cmd_get}"

  exec_sftp_cmd "${cmd}"
}

###########################################
#
# Lists the file names with timestamp
#
###########################################
function list_target_files() {
  target_files=()
  shopt -s extglob lastpipe
  echo "${sftp_ret}" | while read line; do
    filename=${line}
    # data-2023-08-20_12.34.56.csv -> (2023-08-20_12.34.56)
    if [[ ${filename} =~ ${BASE_FILENAME}([0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}\.[0-9]{2}\.[0-9]{2})${EXTENSION} ]]; then
      target_files+=("${filename}")
    fi
  done
}

###########################################
#
# Prepares the SFTP command for get
#
###########################################
function build_ftp_get_command() {
  sftp_get_cmd=""
  if [ ${#target_files[*]} -gt 0 ]; then
    echo "Receiving files..."
  fi
  for i in "${!target_files[@]}"; do
    filename=${target_files[$i]}
    echo ${filename}
    sftp_get_cmd+=" send \"get ${filename} ${DEST_DIR}\r\"${LF}"
    sftp_get_cmd+=" expect \"sftp&gt;\"${LF}"
  done
}

###########################################
#
# Removes the files via SFTP
#
###########################################
function delete_files() {
  sftp_rm_cmd=""
  for i in "${!target_files[@]}"; do
    sftp_rm_cmd+=" send \"rm ${target_files[$i]}\r\"${LF}"
    sftp_rm_cmd+=" expect \"sftp&gt;\"${LF}"
  done

  cmd="  send \"cd ${TARGET_DIR}\r\"
  expect \"sftp&gt;\"
  ${sftp_rm_cmd}"

  exec_sftp_cmd "${cmd}"
}

###########################################################################
#
# Main
#
###########################################################################
if [ $# -lt 3 ]; then
  echo "Usage: sftp.sh BASE_FILENAME EXTENSION DESTDIR [delete]"
  exit 1
fi

BASE_FILENAME=$1
EXTENSION=$2
DEST_DIR=$3
DELETE=false
if [ $# -ge 4 ] && [ $4 == "delete" ]; then
  DELETE=true
fi

exec_sftp_ls_cmd
exec_sftp_cmd "${sftp_cmd_ls}"
list_target_files
build_ftp_get_command

if [ -z "${sftp_get_cmd}" ]; then
  echo "No files to receive"
  exit 0
fi

# sftp> get
get_files "${sftp_get_cmd}"

# sftp> rm
if ${DELETE}; then
  echo "Deleting the received files on the server..."
  delete_files
fi

echo "OK"
