#!/bin/bash -eu
############################################################################
# SFTP File Receiver
# Copyright 2023 Takashi Harano
# Released under the MIT License
# https://libutil.com/
#
# Usage:
#   $ sftpget.sh [-d]
############################################################################

#---------------------------------------------------------------------------
HOST="localhost"
USER="user1"
PVTKEY="~/.ssh/id_ed25519"
PORT=22
SFTP_DIR="/tmp1"
DEST_DIR="."
BASE_FILENAME="file-"
EXTENSION="csv"
#TIMESTAMP_PATTERN='[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}\.[0-9]{2}\.[0-9]{2}'
TIMESTAMP_PATTERN='[0-9]{8}-[0-9]{6}'
#---------------------------------------------------------------------------

LF=$'\n'
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
  expect \"sftp>\"
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
  sftp_cmd_ls="send \"cd ${SFTP_DIR}\r\"
  expect \"sftp>\"
  send \"ls -1\r\"
  expect \"sftp>\"
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
  cmd="  send \"cd ${SFTP_DIR}\r\"
  expect \"sftp>\"
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
  local pattern="^${BASE_FILENAME}${TIMESTAMP_PATTERN}\\.${EXTENSION}$"
  mapfile -t target_files < <(
    echo "${sftp_ret}" | tr -d '\r' | grep -E "${pattern}"
  )
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
    sftp_get_cmd+=" expect \"sftp>\"${LF}"
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
    sftp_rm_cmd+=" expect \"sftp>\"${LF}"
  done

  cmd="  send \"cd ${SFTP_DIR}\r\"
  expect \"sftp>\"
  ${sftp_rm_cmd}"

  exec_sftp_cmd "${cmd}"
}

###########################################################################
#
# Main
#
###########################################################################
DELETE=false
if [ "${1:-}" = "-d" ]; then
  DELETE=true
fi

echo "Connecting to ${USER}@${HOST}"

exec_sftp_ls_cmd
exec_sftp_cmd "${sftp_cmd_ls}"
list_target_files

echo "dir = ${SFTP_DIR}"
echo "file = ${BASE_FILENAME}${TIMESTAMP_PATTERN}.${EXTENSION}"

build_ftp_get_command

if [ -z "${sftp_get_cmd}" ]; then
  echo "No files to download"
  exit 0
fi

# sftp> get
get_files "${sftp_get_cmd}"

# sftp> rm
if ${DELETE}; then
  echo "Deleting downloaded files from the server..."
  delete_files
fi

echo "OK"
