#!/bin/bash -u

# shellcheck disable=SC1091
. ../util.sh

trap error_handler ERR

echo "----------------------------------------"
echo "log test"
echo "----------------------------------------"
log "aaa"
log "bbb"
log "a b c 123"
log ""
log "あ你한"
echo ""
