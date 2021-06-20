#!/bin/bash -u

# shellcheck disable=SC1091
. ../util.sh

trap error_handler ERR

echo "----------------------------------------"
echo "print_cpu_usage test"
echo "----------------------------------------"
print_cpu_usage
print_cpu_usage "tsv"
echo ""
