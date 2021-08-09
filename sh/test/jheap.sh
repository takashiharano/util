#!/bin/bash -u
# shellcheck disable=SC1091
. ../util.sh
trap error_handler ERR

java_heap "jboss-modules.jar"
