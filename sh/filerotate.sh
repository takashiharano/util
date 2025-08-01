#!/bin/bash -eu
############################################################################
# File Rotator
# Copyright 2022 Takashi Harano
# Released under the MIT License
# https://libutil.com/
#
# Created: 2022-11-26
# Updated: 2025-08-02
############################################################################

if [ $# -lt 2 ]; then
  echo "Usage: ./filerotate.sh DIR_PATH FILE_NAME [N]" >&2
  exit 1
fi

DIR_PATH="$1"
FILE_NAME="$2"
MAX_N="${3:-9}"

cd "${DIR_PATH}" || exit 1

# Rotate files: FILE.N-1 -> FILE.N ... FILE -> FILE.1
i=$MAX_N
while [ "$i" -ge 1 ]; do
  prev=$((i - 1))
  if [ "${prev}" -eq 0 ]; then
    if [ -f "${FILE_NAME}" ]; then
      mv "${FILE_NAME}" "${FILE_NAME}.1"
    fi
  else
    if [ -f "${FILE_NAME}.${prev}" ]; then
      mv "${FILE_NAME}.${prev}" "${FILE_NAME}.${i}"
    fi
  fi
  i=$((i - 1))
done
