#!/bin/bash -u
# shellcheck disable=SC1091
# shellcheck disable=SC2034
. ../util.sh
trap error_handler ERR

current_time

DATE_TIME_FORMAT="%Y-%m-%d %H:%M:%S.%3N %:z"
current_time
