#==============================================================================
# System Performance Monitor
# Copyright (c) 2023 Takashi Harano
#==============================================================================
import os
import sys

ROOT_DIR = '../../'
sys.path.append(os.path.join(os.path.dirname(__file__), ROOT_DIR + 'libs'))
import util

import conf
import screen

PERF_LOG_DIR = conf.PERF_LOG_DIR
PERF_LOG_PATH = conf.PERF_LOG_PATH

DISK_LOG_DIR = conf.DISK_LOG_DIR
DISK_LOG_PATH = conf.DISK_LOG_PATH

#------------------------------------------------------------------------------
# 2022-08-17T23:28:01.800+09:00  cpu: usage=1% us=0% sy=1% wa=0% st=0%  mem: usage=23%
def get_perf_log(n):
    path = PERF_LOG_PATH

    if n != '' and n != '0':
        path += '.' + n

    if util.path_exists(path):
        status = 'OK'
        text = util.read_text_file(path)
        text = util.encode_base64(text)
    else:
        status = 'NOT_FOUND'
        text = None

    max_n = get_max_log_n(PERF_LOG_DIR)

    ret = {
        'status': status,
        'body': {
            'n': n,
            'max_n': max_n,
            'logtext': text
        }
    }
    return ret

#------------------------------------------------------------------------------
# 2024-02-04T16:01:01.728+09:00
# Filesystem     1K-blocks    Used Available Use% Mounted on
# /dev/root       30298176 6200044  24081748  21% /
# tmpfs             426988       0    426988   0% /dev/shm
# tmpfs             170796     952    169844   1% /run
# tmpfs               5120       0      5120   0% /run/lock
# /dev/sda15        106858    6186    100673   6% /boot/efi
# /dev/sdb1        4044512      28   3818488   1% /mnt
# tmpfs              85396       4     85392   1% /run/user/1000
def get_disk_log(n):
    path = DISK_LOG_PATH

    if n != '' and n != '0':
        path += '.' + n

    if util.path_exists(path):
        status = 'OK'
        text = util.read_text_file(path)
        text = util.encode_base64(text)
    else:
        status = 'NOT_FOUND'
        text = None

    max_n = get_max_log_n(DISK_LOG_DIR)

    ret = {
        'status': status,
        'body': {
            'n': n,
            'max_n': max_n,
            'logtext': text
        }
    }
    return ret

#------------------------------------------------------------------------------
def get_max_log_n(log_dir):
    files = util.list_files(log_dir)
    max = -1
    for i in range(len(files)):
        filename = files[i]
        parts = filename.split('.')
        ext = parts[-1]
        n = -1
        try:
            n = int(ext)
        except:
            pass
        if n > max:
            max = n
    return max

#------------------------------------------------------------------------------
def proc_get_perf_log():
    n = util.get_request_param('n', '')
    ret = get_perf_log(n)
    status = ret['status']
    result = ret['body']
    util.send_result_json(status, result)

#------------------------------------------------------------------------------
def proc_get_disk_log():
    n = util.get_request_param('n', '')
    ret = get_disk_log(n)
    status = ret['status']
    result = ret['body']
    util.send_result_json(status, result)

#------------------------------------------------------------------------------
def main():
    action = util.get_request_param('action', '')

    func_name = 'proc_' + action
    g = globals()
    if func_name in g:
        result = g[func_name]()
    else:
        screen.print_screen()
