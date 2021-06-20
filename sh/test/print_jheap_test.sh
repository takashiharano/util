#!/bin/bash -u

# shellcheck disable=SC1091
. ../util.sh

trap error_handler ERR

echo "----------------------------------------"
echo "print_jheap test"
echo "----------------------------------------"
print_jheap "jboss-modules.jar"
print_jheap "jboss-modules.jar" "tsv"
echo ""
