#!/bin/bash -eu
############################################################################
# SFTP File Receiver
# Copyright 2023 Takashi Harano
# Released under the MIT License
# https://libutil.com/
#
# Created: 2023-08-20
# Updated: 2025-08-02
#
# Usage:
#   ./sftpget.sh [-d] [REMOTE_FILE LOCAL_PATH]
############################################################################

#---------------------------------------------------------------------------
HOST="localhost"
PORT=22
USER="user1"
PRIVATE_KEY="${HOME}/.ssh/id_ed25519"

REMOTE_DIR="/tmp1/d1"

BASE_FILENAME="file"
EXTENSION=".csv"

#----------------------------------------
# SUFFIX_PATTERN:
#   Regular expression pattern for the suffix part of the file name,
#   appended after BASE_FILENAME and before EXTENSION.
#
#   Examples:
#     SUFFIX_PATTERN='-[0-9]{8}-[0-9]{6}'
#       -> Matches: file-20250802-130000.csv
#
#     SUFFIX_PATTERN='-[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}\.[0-9]{2}\.[0-9]{2}'
#       -> Matches: file-2025-08-02_13.00.00.csv
#
#   Note:
#     Set SUFFIX_PATTERN to an empty string ('') to match all files
#     that start with BASE_FILENAME and end with EXTENSION.
#     e.g., matches file.csv, file-abc.csv, file-anything.csv, etc.
#----------------------------------------
SUFFIX_PATTERN='-[0-9]{8}-[0-9]{6}'
#SUFFIX_PATTERN='-[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}\.[0-9]{2}\.[0-9]{2}'

LOCAL_DIR="."

TIMEOUT=15
#---------------------------------------------------------------------------

LF=$'\n'
sftp_ret=""

###########################################
#
# Execute command via SFTP
#
###########################################
exec_sftp_cmd() {
  local sftp_cmd=$1
  local cmd

  cmd="spawn sftp"
  if [ -n "${PRIVATE_KEY}" ]; then
    cmd+=" -i ${PRIVATE_KEY}"
  fi
  cmd+=" -P ${PORT} ${USER}@${HOST}"

  sftp_ret=$(LANG=C expect -c "
    log_user 1
    set timeout ${TIMEOUT}
    ${cmd}
    expect \"sftp>\"
    ${sftp_cmd}
    send \"bye\r\"
    expect eof
  ")

  sftp_ret=$(echo "${sftp_ret}" | tr -d '\r')

  # Comment out for debugging
  #echo "${sftp_ret}"

  if [ -z "${sftp_ret}" ]; then
    echo "Error: SFTP command failed or produced no output." >&2
    exit 9
  elif echo "${sftp_ret}" | grep -q "'s password:"; then
    echo "Error: Authentication failed for user '${USER}'." >&2
    exit 3
  elif echo "${sftp_ret}" | grep -q "Permission denied"; then
    echo "Error: Authentication failed for user '${USER}'." >&2
    exit 3
  elif echo "${sftp_ret}" | grep -q "Connection refused"; then
    echo "Error: Connection to '${HOST}:${PORT}' was refused." >&2
    exit 3
  elif echo "${sftp_ret}" | grep -q "Could not resolve hostname"; then
    echo "Error: Unknown host '${HOST}'." >&2
    exit 3
  elif echo "${sftp_ret}" | grep -q " not found."; then
    echo "Error: Remote path or file not found." >&2
    exit 4
  fi
}

###########################################
#
# Execute ls command via SFTP
#
###########################################
build_sftp_ls_cmd() {
  sftp_ls_cmd="send \"cd ${REMOTE_DIR}\r\"
  expect \"sftp>\"
  send \"ls -1\r\"
  expect \"sftp>\"
  "
}

###########################################
#
# Lists the file names with timestamp
#
###########################################
list_target_files() {
  target_file_names=()

  local effective_suffix="${SUFFIX_PATTERN}"
  if [ -z "${SUFFIX_PATTERN}" ]; then
    # Match all files when SUFFIX_PATTERN is empty
    # Matches: file.csv, file-*.csv, etc.
    effective_suffix=".*"
  fi

  local pattern
  pattern="^${BASE_FILENAME}${effective_suffix}${EXTENSION}$"

  mapfile -t target_file_names < <(
    extract_ls_output | grep -E "${pattern}" | sed "s|^|${REMOTE_DIR}/|"
  )
}

extract_ls_output() {
  # Extract the file list from the SFTP session output.
  # This removes all interactive prompt echoes and isolates only the
  # actual result lines between 'ls -1' and the next 'sftp>' prompt.
  # 
  # For example, from this:
  #   sftp> ls -1  << Skipped
  #   ls -1        << Skipped
  #   file1.csv    << Retained
  #   file2.csv    << Retained
  #   sftp>        << Skipped
  echo "${sftp_ret}" \
    | tr -d '\r' \
    | awk '/ls -1/{f=1;next} /sftp>/{f=0} f' \
    | grep -v '^ls -1$'
}

###########################################
#
# Prepares the SFTP command for get
#
###########################################
build_sftp_get_command() {
  sftp_get_cmd=""
  local filename
  for i in "${!target_file_names[@]}"; do
    filename=${target_file_names[$i]}
    sftp_get_cmd+=" send \"get ${filename} ${LOCAL_DIR}\r\"${LF}"
    sftp_get_cmd+=" expect \"sftp>\"${LF}"
  done
}

###########################################
#
# Removes the files via SFTP
#
###########################################
delete_files() {
  sftp_rm_cmd=""
  for i in "${!target_file_names[@]}"; do
    sftp_rm_cmd+=" send \"rm ${target_file_names[$i]}\r\"${LF}"
    sftp_rm_cmd+=" expect \"sftp>\"${LF}"
  done

  cmd="${sftp_rm_cmd}"

  exec_sftp_cmd "${cmd}"

  if echo "${sftp_ret}" | grep -q "Couldn't delete file"; then
    echo "Warning: Some files could not be deleted." >&2
  fi
}

###########################################################################
#
# perform_file_transfer
#
###########################################################################
perform_file_transfer() {
  local errmsg="$1"
  local exitcode="$2"

  build_sftp_get_command
  log_target_files
  exec_sftp_cmd "${sftp_get_cmd}"

  if echo "${sftp_ret}" | grep -q "Couldn't stat"; then
    echo "Error: ${errmsg}" >&2
    exit "${exitcode}"
  fi

  if [ "${DELETE_REMOTE_FILES}" = "true" ]; then
    echo "Deleting downloaded files from the server..."
    delete_files
  fi
}

###########################################
#
# Prepares the SFTP command for get
#
###########################################
log_target_files() {
  if [ ${#target_file_names[*]} -gt 0 ]; then
    echo "Receiving files..."
  fi
  local filename
  for i in "${!target_file_names[@]}"; do
    filename=${target_file_names[$i]}
    echo "${filename}"
  done
}


###########################################################################
#
# proc_get_files
#
###########################################################################
proc_get_files() {
  build_sftp_ls_cmd
  exec_sftp_cmd "${sftp_ls_cmd}"
  list_target_files

  local display_suffix_pattern="${SUFFIX_PATTERN}"
  if [ -z "${display_suffix_pattern}" ]; then
    display_suffix_pattern="*"
  fi
  local file="${BASE_FILENAME}${display_suffix_pattern}${EXTENSION}"

  echo "dir = ${REMOTE_DIR}"
  echo "file = ${file}"

  if [ ${#target_file_names[@]} -eq 0 ]; then
    echo "No files to download"
    exit 0
  fi

  perform_file_transfer "Some files could not be retrieved." 3
}

###########################################################################
#
# proc_get_single_file
#
###########################################################################
proc_get_single_file() {
  # LOCAL_DIR is used as a general destination path (file or directory)
  # In single file mode, it holds the full local path
  LOCAL_DIR=${LOCAL_PATH}

  if [[ "${REMOTE_FILE}" != /* ]]; then
    REMOTE_FILE="${REMOTE_DIR}/${REMOTE_FILE}"
  fi

  local remote_dir
  local remote_file
  local filepath

  remote_dir=$(dirname -- "${REMOTE_FILE}")
  remote_file=$(basename -- "${REMOTE_FILE}")
  filepath="${remote_dir}/${remote_file}"

  sftp_get_cmd=""
  sftp_get_cmd+=" send \"cd ${remote_dir}\r\"${LF}"
  sftp_get_cmd+=" expect \"sftp>\"${LF}"
  sftp_get_cmd+=" send \"get ${remote_file} ${LOCAL_DIR}\r\"${LF}"
  sftp_get_cmd+=" expect \"sftp>\"${LF}"

  echo "get ${filepath}"
  exec_sftp_cmd "${sftp_get_cmd}"

  if echo "${sftp_ret}" | grep -q "Couldn't canonicalize"; then
    echo "Error: Directory '${remote_dir}' not found on SFTP server." >&2
    exit 2
  elif echo "${sftp_ret}" | grep -q "No such file"; then
    echo "Error: Remote file '${REMOTE_FILE}' not found on SFTP server." >&2
    exit 2
  fi

  if [ "${DELETE_REMOTE_FILES}" = "true" ]; then
    echo "Deleting downloaded file from the server..."
    sftp_rm_cmd=""
    sftp_rm_cmd+=" send \"cd ${remote_dir}\r\"${LF}"
    sftp_rm_cmd+=" expect \"sftp>\"${LF}"
    sftp_rm_cmd+=" send \"rm ${remote_file}\r\"${LF}"
    sftp_rm_cmd+=" expect \"sftp>\"${LF}"
    exec_sftp_cmd "${sftp_rm_cmd}"
  fi
}

###########################################################################
#
# Main
#
###########################################################################
: ${BASE_FILENAME:=""}
: ${EXTENSION:=""}
: ${SUFFIX_PATTERN:=""}
: ${LOCAL_DIR:=""}
: ${TIMEOUT:=-1}

if [ ! -f "$PRIVATE_KEY" ]; then
  echo "Error: Private key '${PRIVATE_KEY}' does not exist." >&2
  exit 1
fi

args=()
REMOTE_FILE=""
LOCAL_PATH=""
DELETE_REMOTE_FILES="false"

for arg in "$@"; do
  if [ "$arg" = "-d" ]; then
    DELETE_REMOTE_FILES="true"
  else
    args+=("$arg")
  fi
done

case ${#args[@]} in
  0)
    # Multi-file mode: auto-detect files from REMOTE_DIR and download
    echo "Connecting to ${USER}@${HOST}"
    proc_get_files
    ;;
  2)
    # Single file mode
    REMOTE_FILE="${args[0]}"
    LOCAL_PATH="${args[1]}"
    echo "Connecting to ${USER}@${HOST}"
    proc_get_single_file
    ;;
  *)
    echo "Usage: $0 [-d] [REMOTE_FILE LOCAL_PATH]" >&2
    exit 1
    ;;
esac

echo "OK"
