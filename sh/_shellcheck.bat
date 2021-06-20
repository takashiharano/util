cd /d %~dp0
shellcheck util.sh

shellcheck test/log_test.sh
shellcheck test/print_cpu_usage_test.sh
shellcheck test/print_jheap_test.sh
shellcheck test/print_time_test.sh

pause
