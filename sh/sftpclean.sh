#!/bin/bash -eu
############################################################################
# SFTP File Cleaner
# Copyright 2022 Takashi Harano
# Released under the MIT License
# https://libutil.com/
#
# Created: 2022-11-13
# Updated: 2025-08-02
#
# Usage: ./sftpclean.sh
# (no arguments)
#
# Example:
# now = 2025-08-01T13:00:00
# Files older than the retention period (24h) will be removed:
#   file-20250723-101520.csv  << Remove
#   file-20250731-125959.csv  << Remove
#   file-20250801-120000.csv  << Keep
############################################################################

#--- Basic Connection Settings ---
HOST="localhost"
PORT=22
USER="user1"
PVTKEY="~/.ssh/id_ed25519"

#--- Target Directory ---
TARGET_DIR="/tmp1"

#--- Timestamp Pattern and Format ---
# e.g., 20240801-123456 -> 2024-08-01 12:34:56
TIMESTAMP_PATTERN='[0-9]{8}-[0-9]{6}'
DATE_CLEANUP_CMD='s/\([0-9]\{4\}\)\([0-9]\{2\}\)\([0-9]\{2\}\)-\([0-9]\{2\}\)\([0-9]\{2\}\)\([0-9]\{2\}\)/\1-\2-\3 \4:\5:\6/'
DATE_FORMAT='%Y-%m-%d %H:%M:%S'

# e.g., 2024-08-01_12.34.56 -> 2024-08-01 12:34:56
#TIMESTAMP_PATTERN='[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}\.[0-9]{2}\.[0-9]{2}'
#DATE_CLEANUP_CMD='s/_/ /;s/\./:/g'

#--- Retention Policy ---
RETENTION_SEC=86400

#---------------------------------------------------------------------------
NOW=$(date +%s)
EXPIRES_AT=$(echo "${NOW} - ${RETENTION_SEC}" | bc)

###########################################
#
# Converts timestamp string to Unix time using user-defined pattern
#
###########################################
to_unixtime() {
  local raw="$1"
  local dt="${raw}"

  if [ -n "${DATE_CLEANUP_CMD}" ]; then
    dt=$(echo "${dt}" | sed "${DATE_CLEANUP_CMD}")
  fi

  if date --version >/dev/null 2>&1; then
    # GNU date (Linux)
    date -d "${dt}" +%s 2>/dev/null || echo -1
  else
    # BSD date (macOS)
    date -j -f "${DATE_FORMAT}" "${dt}" +%s 2>/dev/null || echo -1
  fi
}

###########################################
#
# Execute command via SFTP
#
###########################################
exec_sftp_cmd() {
  local sftp_cmd
  local cmd

  sftp_cmd="$1"

  cmd="spawn sftp"
  if [ -n "${PVTKEY}" ]; then
    cmd+=" -i ${PVTKEY}"
  fi
  cmd+=" -P ${PORT} ${USER}@${HOST}"

  sftp_ret=$(expect -c "
    set timeout 5
    if {[catch {
      ${cmd}
      expect \"sftp>\"
      ${sftp_cmd}
      send \"bye\r\"
      expect eof
    } result]} {
      puts \"SFTP failed: \${result}\"
      exit 1
    }
  ")
}

###########################################
#
# Removes the files via SFTP
#
###########################################
delete_files() {
  local cmd
  local cmd_rm

  cmd_rm=$1
  cmd="send \"cd ${TARGET_DIR}\r\"
  expect \"sftp>\"
  ${cmd_rm}"

  exec_sftp_cmd "${cmd}"
}

echo "Connecting to ${USER}@${HOST}"

###########################################
#
# Lists file names on the SFTP server
#
###########################################
sftp_cmd_ls="send \"cd ${TARGET_DIR}\r\"
expect \"sftp>\"
send \"ls -1\r\"
expect \"sftp>\"
"

exec_sftp_cmd "${sftp_cmd_ls}"

echo "Target directory  = ${TARGET_DIR}"
echo "Timestamp pattern = ${TIMESTAMP_PATTERN}"
echo "Retention period  = ${RETENTION_SEC} sec"

###########################################
#
# Lists the expired file names
#
###########################################
target_file_names=()

mapfile -t file_lines <<< "${sftp_ret}"

# Inspect file names and extract expired ones
for line in "${file_lines[@]}"; do
  filename=${line}
  if [[ ${filename} =~ (${TIMESTAMP_PATTERN}) ]]; then
    timestamp="${BASH_REMATCH[1]}"
    unixtime=$(to_unixtime "${timestamp}")

    if [ "${unixtime}" -eq -1 ]; then
      echo "Warning: Invalid timestamp for file: ${filename}" >&2
      continue
    fi

    if [ "${unixtime}" -lt "${EXPIRES_AT}" ]; then
      target_file_names+=("${filename}")
    fi
  fi
done

file_count="${#target_file_names[@]}"

if [ "${file_count}" -eq 0 ]; then
  echo "No files to be removed"
else
  echo "Target files to be removed:"
  for filename in "${target_file_names[@]}"; do
    echo "${filename}"
  done
  echo "Total: ${file_count} file(s)"
fi

###########################################
#
# Prepares the SFTP command for remove
#
###########################################
LF=$'\n'

sftp_rm_cmd=""
for i in "${!target_file_names[@]}"; do
  sftp_rm_cmd+=" send \"rm ${target_file_names[$i]}\r\"${LF}"
  sftp_rm_cmd+=" expect \"sftp>\"${LF}"
done

###########################################
#
# Execute delete files
#
###########################################
if [ -n "${sftp_rm_cmd}" ]; then
  delete_files "${sftp_rm_cmd}"
  echo "Cleanup complete"
fi
