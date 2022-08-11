# util.py
# Copyright 2018 Takashi Harano
# Released under the MIT license
# https://libutil.com/
# Python 3.4+
v = 202208112234

import sys
import os
import io
import math
import codecs
import subprocess
import re
import datetime
import time
import shutil
import base64
import json
import random
import hashlib
import socket
import cgi
from http import cookies
import urllib.request
import urllib.parse
import ssl
import csv
import logging
import zipfile

DEFAULT_ENCODING = 'utf-8'
DEFAULT_HTTP_TIMEOUT = 15
#LINE_SEP = os.linesep
LINE_SEP = '\n'

MINUTE = 60
HOUR = 3600
DAY = 86400

SUNDAY = 0
MONDAY = 1
TUESDAY = 2
WEDNESDAY = 3
THURSDAY = 4
FRIDAY = 5
SATURDAY = 6
WDAYS = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT']
LOCAL_TZ_OFFSET = datetime.datetime.now(datetime.timezone.utc).astimezone().tzinfo.utcoffset(None).seconds

sys.dont_write_bytecode = True
stdin_data = None
form_data = None
logger = None
start_time = 0
res_debug = False

#------------------------------------------------------------------------------
# system
#------------------------------------------------------------------------------
def set_stdout_encoding(encoding):
    v = get_python_version()
    if v['major'] >= 3 and v['minor'] >= 7:
        sys.stdout.reconfigure(encoding=encoding)
    else:
        sys.stdout = codecs.getwriter(encoding)(sys.stdout.detach())

# util.append_system_path(__file__, '../')
def append_system_path(_file_, path):
    sys.path.append(get_relative_path(_file_, path))

def read_stdin():
    global stdin_data
    if stdin_data is None:
        stdin_data = sys.stdin.read()
    return stdin_data

def free_stdin_data():
    global stdin_data
    stdin_data = None

# python foo.py a b c
# -> ['foo.py', 'a', 'b', 'c']
def get_args():
    return sys.argv

def get_args_len():
    return len(sys.argv)

def get_arg(n, default=''):
    if n >= len(sys.argv):
        return default
    return sys.argv[n]

#------------------------------------------------------------------------------
# lang
#------------------------------------------------------------------------------
# {'major': 3, 'minor': 7, 'micro': 0, 'releaselevel': 'final', 'serial': 0}
def get_python_version():
    v = sys.version_info
    ver = {
        'major': v[0],
        'minor': v[1],
        'micro': v[2],
        'releaselevel': v[3],
        'serial': v[4]
    }
    return ver

# 'abc'     = str
# 123       = int
# 123.0     = float
# True      = bool
# {}        = dict
# []        = list
# ()        = tuple
# None      = NoneType
# b'\x00'   = bytes
# r'\d'     = str
# u'\u3042' = str
# func()    = function
# Exception = type
# util      = module
# datetime.datetime.today() = datetime
def typename(obj):
    return type(obj).__name__

#------------------------------------------------------------------------------
# string
#------------------------------------------------------------------------------
# flags:
#  re.IGNORECASE
#  re.MULTILINE
#  re.DOTALL
#  (re.MULTILINE | re.DOTALL)
def match(string, pattern, flags=0):
    return re.search(pattern, string, flags) is not None

# r'pattern', 'repl'
# r'(?P<g1>pattern)', r'\g<g1>'
def replace(s, pattern, repl, count=0, flags=0):
    return re.sub(pattern, repl, s, count, flags)

# 'abc123abc', 'abc' -> 2
def count_str_pattern(string, pattern, flags=0):
    m = re.findall(pattern, string, flags)
    return len(m)

# 'abc123xyz', 'abc(\d+).*'
# -> '123'
# 'abc123xyz', 'ab(\d+).*'
# -> ''
# 'aaa of bbb of ccc', '.+? of (.*)'
# -> 'bbb of ccc'
def extract_str(s, pattern, count=0, flags=0):
    ptn = re.sub(r'(?P<g1>[^(]*)\((?P<g2>.+)[^\\]?\)(?P<g3>.*)', '\g<g1>(?P<g1>\g<g2>)\g<g3>', pattern)
    ret = ''
    if re.search('\(?P<g1>', ptn):
        if re.search(ptn, s, flags):
            ret = re.sub(ptn, r'\g<g1>', s, count, flags)
    else:
        if re.search(ptn, s, flags):
            ret = ptn
    return ret

# <span>abc</span>
# -> abc
def get_tag_text(s, tag_name, greedy=True):
    if greedy:
        ret = re.sub(r'.*?<' + tag_name + '.*?>(?P<g1>.*)</' + tag_name + '>.*', r'\g<g1>', s)
    else:
        ret = re.sub(r'.*?<' + tag_name + '.*?>(?P<g1>[^>]*)</' + tag_name + '>.*', r'\g<g1>', s)
    return ret

# ('abc123', r'\d+')
#   <re.Match object; span=(3, 6), match='123'>
#   m.group(0)=123
def str_find(s, pattern, flags=0):
    return re.search(pattern, s, flags)

# ('abc123def456', '\d+')
# -> ['123', '456']
def str_findall(s, pattern, flags=0):
    return re.findall(pattern, s, flags)

# ('ABC', '0', 5)
# -> '00ABC'
# ('ABCDE', '0', 5)
# -> 'ABCDE'
# (1, '0', 5)
# -> '00001'
def lpad(s, ch, len):
    return str(s).rjust(len, ch)

# ('ABC', '0', 5)
# -> 'ABC00'
# ('ABCDE', '0', 5)
# -> 'ABCDE'
# (1, '0', 5)
# -> '10000'
def rpad(s, ch, len):
    return str(s).ljust(len, ch)

# Convert newline
def convert_newline(s, nl):
    return s.replace('\r\n', '\n').replace('\r', '\n').replace('\n', nl)

# ' abc\n123 456 ' -> 'abc123456'
def remove_space_newline(s):
    return re.sub('\s', '', s).replace('\r\n', '\n').replace('\r', '\n').replace('\n', '')

# ('abc123aabb', ['^a', '\d'])
# -> bcaabb
def delete_patterns(s, patterns):
    for i in range(len(patterns)):
        s = re.sub(patterns[i], '', s)
    return s

# '', ' ', None
def is_empty(s):
    return s is None or s.strip() == ''

# '1' -> True
# '1.2' -> False
# 'a' -> False
def is_int(s):
    if s is None:
        return False
    return match(s.strip(), '^-?\\d+$')

# '1.2' -> True
# '1' -> False
# 'a' -> False
def is_float(s):
    if s is None:
        return False
    return match(s.strip(), '^-?\\d+\\.\\d+$')

# '1' -> True
# '1.2' -> True
# 'a' -> False
def is_num(s):
    if s is None:
        return False
    return is_int(s) or is_float(s)

# '-0102.3040'
# -> '-102.304'
def trim_zeros(v):
    v = str(v)
    v = v.strip()
    if len(v) == 0:
        return ''

    s = ''
    if v[0] == '-':
        s = '-'
        v = v[1:]

    prts = v.split('.')
    val_i = prts[0]
    val_d = ''
    if len(prts) >= 2:
        val_d = prts[1]

    val_i = replace(val_i, '^0+', '')
    val_d = replace(val_d, '0+$', '')
    if val_i == '':
        val_i = '0'
    r = val_i
    if val_d != '':
        r += '.' + val_d

    if r != '0':
        r = s + r
    return r

# !1Aa(Full-Width) -> !1Aa(Half-Width)
def to_half_width(text, alphabet=True, number=True, symbol=True):
    tbl_upper = dict((0xFF21 + ch, 0x0041 + ch) for ch in range(26))
    tbl_lower = dict((0xFF41 + ch, 0x0061 + ch) for ch in range(26))
    tbl_number = dict((0xFF10 + ch, 0x0030 + ch) for ch in range(10))

    tbl_symbol1 = dict((0xFF00 + ch, 0x0020 + ch) for ch in range(16))
    tbl_symbol2 = dict((0xFF1A + ch, 0x003A + ch) for ch in range(7))
    tbl_symbol3 = dict((0xFF3B + ch, 0x005B + ch) for ch in range(6))
    tbl_symbol4 = dict((0xFF5B + ch, 0x007B + ch) for ch in range(4))
    tbl_symbol0 = {
        0x3000: 0x20, #  
        0x201D: 0x22, # "
        0x2019: 0x27, # '
        0xFFE5: 0x5C, # \
        0x2018: 0x60  # `
    }

    tbl_symbol = dict(tbl_symbol1)
    tbl_symbol.update(tbl_symbol2)
    tbl_symbol.update(tbl_symbol3)
    tbl_symbol.update(tbl_symbol4)
    tbl_symbol.update(tbl_symbol0)

    table = dict()

    if alphabet:
        table.update(tbl_upper)
        table.update(tbl_lower)
    if number:
        table.update(tbl_number)
    if symbol:
        table.update(tbl_symbol)

    half = text.translate(table)
    return half

# !1Aa(Half-Width) -> !1Aa(Full-Width)
def to_full_width(text, alphabet=True, number=True, symbol=True):
    tbl_upper = dict((0x0041 + ch, 0xFF21 + ch) for ch in range(26))
    tbl_lower = dict((0x0061 + ch, 0xFF41 + ch) for ch in range(26))
    tbl_number = dict((0x0030 + ch, 0xFF10 + ch) for ch in range(10))

    tbl_symbol1 = dict((0x0020 + ch, 0xFF00 + ch) for ch in range(16))
    tbl_symbol2 = dict((0x003A + ch, 0xFF1A + ch) for ch in range(7))
    tbl_symbol3 = dict((0x005B + ch, 0xFF3B + ch) for ch in range(6))
    tbl_symbol4 = dict((0x007B + ch, 0xFF5B + ch) for ch in range(4))
    tbl_symbol0 = {
        0x20: 0x3000, #  
        0x22: 0x201D, # "
        0x27: 0x2019, # '
        0x5C: 0xFFE5, # \
        0x60: 0x2018  # `
    }

    tbl_symbol = dict(tbl_symbol1)
    tbl_symbol.update(tbl_symbol2)
    tbl_symbol.update(tbl_symbol3)
    tbl_symbol.update(tbl_symbol4)
    tbl_symbol.update(tbl_symbol0)

    table = dict()
    if alphabet:
        table.update(tbl_upper)
        table.update(tbl_lower)
    if number:
        table.update(tbl_number)
    if symbol:
        table.update(tbl_symbol)

    full = text.translate(table)
    return full

# line1
# line2
# line3
# -> ['line1', 'line2', 'line3']
def text2list(text):
    text = convert_newline(text, '\n')
    a = text.split('\n')
    if len(a) >= 2 and a[-1] == '':
        del a[-1]
    return a

# ['item1', 'item2', 'item3']
# ->
# item1
# item2
# item3
def list2text(arr, line_sep='\n'):
    text = ''
    for i in range(len(arr)):
        text += arr[i] + line_sep
    return text

# AAAAAAAAAA
# BBB<start>BBBBBBB
# CCCCCCCCCC
# DDDDDDD<end>DDD
# EEEEEEEEEE
# ->
# BBB<start>BBBBBBB
# CCCCCCCCCC
# DDDDDDD<end>DDD
def extract_text(src, start=None, end=None, include_start=True, include_end=True,
                 greedy=False, count=1, flags_s=0, flags_e=0):
    text_list = text2list(src)
    rows = len(text_list)
    start_line = -1
    end_line = -1
    target = False
    cnt = 0

    if start is None:
        target = True
        start_line = 0

    for i in range(rows):
        line = text_list[i]
        if target:
            if start is not None and not greedy and match(line, start, flags_s):
                start_line = i
            if end is not None and match(line, end, flags_e):
                end_line = i
                if not greedy:
                    break
        elif match(line, start, flags_s):
            cnt += 1
            if cnt == count:
                target = True
                if start_line == -1 or not greedy:
                    start_line = i
                if end is not None and match(line, start + '.*' + end):
                    end_line = i
                    if not greedy:
                        break

    if end_line == -1 and end is None:
        end_line = rows - 1

    ret = ''
    if start_line >= 0 and end_line >= 0:
        if start_line == end_line:
            ret = text_list[start_line] + LINE_SEP
        else:
            if not include_start:
                start_line += 1
            if not include_end:
                end_line -= 1
            if not include_start and not include_end and start_line > end_line:
                return ''

            for i in range(start_line, end_line + 1):
                ret += text_list[i] + LINE_SEP

    return ret

# AAAAAAAAAA
# BB<pattern>BBB
# CC<pattern>CCC
# DDDDDDDDDD
# ->
# BB<pattern>BBB
#
# count=2
# CC<pattern>CCC
def extract_line(src, pattern, count=1, flags=0):
    text_list = text2list(src)
    cnt = 0
    for i in range(len(text_list)):
        line = text_list[i]
        if re.search(pattern, line, flags) is not None:
            cnt += 1
            if cnt == count:
                return line
    return ''

# Delete tags
def delete_tags(src):
    return replace(src, r'<.*?>', '')

# ('Day', 1) -> Day
# ('Day', 2) -> Days
def plural(s, n):
    return s if n == 1 else s + 's'

# '01AB' or '01 AB' or '0x12 0xAB'
# -> bytes
def hex2bytes(h):
    h = replace(h, '\s', '')
    h = replace(h, '0x', '')
    return bytes.fromhex(h)

# bytes -> '01 AB ...'
def bytes2hex(b, upper=True, line_break=16):
    byte_len = len(b)
    s = ''
    for i in range(0, byte_len):
        if i > 0:
            if i % line_break == 0:
                s += '\n'
            else:
                s += ' '
        v = b[i]
        sb = hex(v)[2:]
        s += sb.zfill(2)

    if upper:
        s = s.upper()
    return s

# '00000001 00000010 ...' -> bytes
def bin2bytes(s):
    s = replace(s, '\s', '')
    s = convert_newline(s, '')
    a = bytearray()
    for i in range(0, len(s), 8):
        b = s[i:i + 8]
        v = int(b, 2)
        a.append(v)
    return bytes(a)

# bytes -> '00000001 00000010 ...'
def bytes2bin(b, line_break=16):
    byte_len = len(b)
    s = ''
    for i in range(0, byte_len):
        if i > 0:
            if i % line_break == 0:
                s += '\n'
            else:
                s += ' '
        v = b[i]
        sb = bin(v)[2:]
        s += sb.zfill(8)
    return s

#------------------------------------------------------------------------------
# String permutation
#------------------------------------------------------------------------------
# Count the number of total patterns of the table
def strp_total(chars, digit):
    tbl = list(chars)
    c = len(tbl)
    n = 0
    for i in range(1, digit + 1):
        n += c ** i
    return n

# Given pattern's index
def strp_index(chars, pattern):
    ptn_len = len(pattern)
    rdx = len(chars)
    idx = 0
    for i in range(ptn_len):
        d = ptn_len - i - 1
        c = pattern[d:d + 1]
        v = chars.find(c) + 1
        n = v * (rdx ** i)
        idx += n
    return idx

# Returns a String pattern at the specified position
def strp(chars, idx):
    return _strp(chars, idx)['s']

def _strp(chars, idx, a=None):
    if idx <= 0:
        r = {
            's': '',
            'a': None
        }
        return r

    tbl = list(chars)
    tbl_len = len(tbl)
    if a is None:
        a = [-1]
        st = 0
    else:
        st = idx - 1

    for i in range(st, idx):
        j = 0
        carry_flag = True
        while j < len(a):
            if carry_flag:
                a[j] += 1
                if a[j] > tbl_len - 1:
                    a[j] = 0
                    if len(a) <= j + 1:
                        a.append(-1)
                else:
                    carry_flag = False
            j += 1
    s = ''
    len_a = len(a)
    last_idx = len_a - 1
    for i in range(len_a):
        s += tbl[a[last_idx - i]]

    r = {
        's': s,
        'a': a
    }
    return r

#------------------------------------------------------------------------------
# Dict/JSON
#------------------------------------------------------------------------------
# Object -> JSON
def to_json(obj, skipkeys=False, ensure_ascii=True, check_circular=True,
            allow_nan=True, cls=None, indent=None, separators=None,
            default=None, sort_keys=False):
    return json.dumps(obj, skipkeys=skipkeys, ensure_ascii=ensure_ascii,
                      check_circular=check_circular, allow_nan=allow_nan,
                      cls=cls, indent=indent, separators=separators,
                      default=default, sort_keys=sort_keys)

# JSON -> Object
def from_json(s, encoding=None, cls=None, object_hook=None, parse_float=None,
              parse_int=None, parse_constant=None, object_pairs_hook=None, default=None):
    if s is None or s == '':
        return default
    return json.loads(s, cls=cls, object_hook=object_hook,
                      parse_float=parse_float, parse_int=parse_int,
                      parse_constant=parse_constant, object_pairs_hook=object_pairs_hook)

# Load Dict
def load_dict(path, default=None, encoding=None, cls=None, object_hook=None,
              parse_float=None, parse_int=None, parse_constant=None, object_pairs_hook=None):
    if path_exists(path):
        s = read_text_file(path)
        if s != '':
            return json.loads(s, cls=cls, object_hook=object_hook,
                              parse_float=parse_float, parse_int=parse_int,
                              parse_constant=parse_constant, object_pairs_hook=object_pairs_hook)
    return default

# Save Dict
def save_dict(path, obj, skipkeys=False, ensure_ascii=True, check_circular=True,
              allow_nan=True, cls=None, indent=1, separators=None,
              default=None, sort_keys=False):
    s = json.dumps(obj, skipkeys=skipkeys, ensure_ascii=ensure_ascii,
                   check_circular=check_circular, allow_nan=allow_nan,
                   cls=cls, indent=indent, separators=separators,
                   default=default, sort_keys=sort_keys)
    write_text_file(path, s)

# Make Parent Dir
def make_parent_dir(path):
    parent_path = get_parent_path(path)
    if not path_exists(parent_path):
        mkdir(parent_path)

# Append Dict
def append_dict(path, obj, skipkeys=False, ensure_ascii=True, check_circular=True,
                allow_nan=True, cls=None, indent=1, separators=None,
                default=None, sort_keys=False, max=0, reverse=False):
    d = load_dict(path, default=[])

    if reverse:
        d.insert(0, obj)
    else:
        d.append(obj)

    new_data = d

    list_len = len(new_data)
    if max > 0 and list_len > max:
        if reverse:
            start = 0
            end = max
        else:
            start = list_len - max
            end = list_len

        new_data = new_data[start:end]

    save_dict(path, new_data, skipkeys=skipkeys, ensure_ascii=ensure_ascii, check_circular=check_circular,
              allow_nan=allow_nan, cls=cls, indent=indent, separators=separators,
              default=default, sort_keys=sort_keys)

# {
#  "a": {
#   "b": "c"
#  }
# }
# -> {"b": "c"}
def shell_dict(o):
    if typename(o) != 'dict' or len(o) >= 2:
        return o
    for k in o:
        d = o[k]
        return d

# [
#   {'key1', 1, 'key2', 7},
#   {'key1', 3, 'key2', 5},
#   {'key1', 2, 'key2', 8},
# ]
# -> key1
# [
#   {'key1', 1, 'key2', 7},
#   {'key1', 2, 'key2', 8},
#   {'key1', 3, 'key2', 5},
# ]
def sort_by_value(o, key):
    d = sorted(o, key=lambda x:x[key])
    return d

#------------------------------------------------------------------------------
# List
#------------------------------------------------------------------------------
# ['a', 'b', 'c']
# -> ['c', 'b', 'a']
def reverse(src):
    return list(reversed(src))

# [1, 2, 3]
# 1 -> 2 / 3 -> 1
def next_list_val(arr, val, offset=1):
    r = arr[0]
    last_index = len(arr) - 1
    for i in range(len(arr)):
        if arr[i] == val:
            n = (i + offset) % len(arr)
            if n <= last_index:
                r = arr[n]
            else:
                r = arr[n - last_index]
            break
    return r

#------------------------------------------------------------------------------
# Date Time
#------------------------------------------------------------------------------
#-----------------
# DateTime Class
#-----------------
# src:
#   timestamp
#   date-time-string
#   datetime
# fmt:
#   %Y-%m-%d %H:%M:%S.%f = 2020-01-02 12:34:56.123456
# tz :
#   datetime.timezone(offset, name=None)
#     offset: datetime.timedelta(days=0, seconds=0, microseconds=0, milliseconds=0, minutes=0, hours=0, weeks=0)
class DateTime:
    def __init__(self, src=None, fmt=None, tz=None):
        if typename(src) == 'datetime':
            dt = src
        else:
            if typename(tz) == 'int':
                tz = datetime.timezone(datetime.timedelta(seconds=tz))
            elif typename(tz) == 'float':
                os = int(tz * 3600)
                tz = datetime.timezone(datetime.timedelta(seconds=os))
            dt = get_datetime(src, fmt, tz)
        self.timestamp = dt.timestamp()
        self.datetime = dt
        self.year = dt.year
        self.month = dt.month
        self.day = dt.day
        self.hour = dt.hour
        self.minute = dt.minute
        self.second = dt.second
        self.microsecond = dt.microsecond
        self.weekday = dt.isoweekday()
        if self.weekday == 7:
            self.weekday = 0 # 0=SUN, 1=MON, 2=TUE, 3=WED, 4=THU, 5=FRI, 6=SAT
        self.yyyy = str(dt.year)
        self.mm = ('0' + str(dt.month))[-2:]
        self.dd = ('0' + str(dt.day))[-2:]
        self.hh = ('0' + str(dt.hour))[-2:]
        self.mi = ('0' + str(dt.minute))[-2:]
        self.ss = ('0' + str(dt.second))[-2:]
        self.us = ('00000' + str(dt.microsecond))[-6:]
        if tz is None:
            self.offset = LOCAL_TZ_OFFSET
        else:
            offset = tz.utcoffset(None).seconds
            if offset > 43200:
                offset = (86400 - offset) * (-1)
            self.offset = offset

    # 2020-01-02 12:34:56.123456 +09:00
    def to_str(self, fmt='%Y-%m-%d %H:%M:%S.%f %Z'):
        s = fmt
        s = replace(s, '%Y', self.yyyy)
        s = replace(s, '%m', self.mm)
        s = replace(s, '%d', self.dd)
        s = replace(s, '%H', self.hh)
        s = replace(s, '%M', self.mi)
        s = replace(s, '%S', self.ss)
        s = replace(s, '%F', self.us[:-3])
        s = replace(s, '%f', self.us)
        s = replace(s, '%W', WDAYS[self.weekday])
        s = replace(s, '%z', format_tz(self.offset))
        s = replace(s, '%Z', format_tz(self.offset, True))
        return s

def now():
    return get_timestamp()

def get_timestamp_in_millis(dt=None, fmt=None):
    ts = get_timestamp(dt, fmt)
    return micro_to_milli(ts)

# 1546400096.123456 (float)    -> datetime
# '2019-01-02 12:34:56.123456' -> datetime
# fmt='%Y-%m-%d %H:%M:%S.%f'
def get_datetime(src=None, fmt=None, tz=None):
    if src is None:
        dt = datetime.datetime.today()
        if tz is not None:
            ts = dt.timestamp()
            dt = datetime.datetime.fromtimestamp(ts, tz)
    elif typename(src) == 'str':
        if fmt is None:
            src = serialize_datetime(src)
            fmt = '%Y%m%d%H%M%S%f'
        dt = datetime.datetime.strptime(src, fmt)
    else:
        dt = datetime.datetime.fromtimestamp(src, tz)
    return dt

# datetime                  -> '2019-01-02 12:34:56.123456'
# 1546400096.123456 (float) -> '2019-01-02 12:34:56.123456'
def get_datetime_str(dt=None, fmt='%Y-%m-%d %H:%M:%S.%f', tz=None):
    s = None
    if dt is None:
        dt = datetime.datetime.today()
    elif typename(dt) == 'float' or typename(dt) == 'int':
        dt = datetime.datetime.fromtimestamp(dt, tz)
    elif typename(dt) == 'str':
        fmt = dt
        dt = datetime.datetime.today()
    s = dt.strftime(fmt)
    return s

# POSIX timestamp (float)
# datetime                     -> 1546400096.123456
# '2019-01-02 12:34:56.123456' -> 1546400096.123456
# fmt='%Y-%m-%d %H:%M:%S.%f'
def get_timestamp(dt=None, fmt=None):
    if dt is None:
        dt = datetime.datetime.today()
    elif typename(dt) == 'str':
        if fmt is None:
            dt = serialize_datetime(dt)
            fmt = '%Y%m%d%H%M%S%f'
        dt = datetime.datetime.strptime(dt, fmt)
    ts = dt.timestamp()
    return ts

# '12:34' -> <theDay> 12:34 (datetime)
# timestr='12:34',fmt='%H:%M'
def get_datetime_of_day(timestr='00:00:00.000000', fmt='%H:%M:%S.%f', offset=0):
    theday = get_timestamp() + offset * 86400
    theday_str = get_datetime_str(theday)
    tm = get_datetime_str(get_datetime(timestr, fmt), fmt='%Y-%m-%d %H:%M:%S.%f')
    src = theday_str[:11] + tm[11:]
    return get_datetime(src)

# '12:34' -> timestamp of the day (float)
# timestr='12:34',fmt='%H:%M'
def get_timestamp_of_day(timestr='00:00:00.000000', fmt='%H:%M:%S.%f', offset=0):
    dt = get_datetime_of_day(timestr, fmt, offset)
    ts = dt.timestamp()
    return ts

# Returns the midnight timestamp for the given moment.
def get_timestamp_of_midnight(dt=None):
    dt1 = DateTime(dt)
    s = dt1.to_str('%Y%m%d')
    dt0 = DateTime(s)
    midnight = dt0.timestamp
    return midnight

# Last day of month
# 2019, 1 -> 31
# 2019, 2 -> 28
# 2020, 2 -> 29
def get_last_day_of_month(year, month):
    month += 1
    if month > 12:
        month = month % 12
        year += int(month / 12)
    date = datetime.date(year, month, 1) - datetime.timedelta(days=1)
    return date.day

# is leap year
def is_leap_year(year):
    if (year % 4 == 0) and (year % 100 != 0 or year %400 == 0):
        return True
    return False

# Normalize the date-time string in YYYYMMDDHHMISSffffff format
# 20200920                   -> 20200920000000000000
# 20200920T1234              -> 20200920123400000000
# 20200920T123456.789        -> 20200920123456789000
# 20200920T123456.789123     -> 20200920123456789123
# 2020-09-20 12:34:56.789    -> 20200920123456789000
# 2020-09-20 12:34:56.789123 -> 20200920123456789123
# 2020/9/3 12:34:56.789      -> 20200903123456789000
# 2020/9/3 12:34:56.789123   -> 20200903123456789123
# 20200920T123456.789123+0900-> 20200920123456789123+0900
def serialize_datetime(s):
    w = s
    w = w.strip()
    w = re.sub('\s{2,}', ' ', w)
    w = re.sub('T', ' ', w)

    wk = _split_datetime_and_timezone(w)
    w = wk[0]
    tz = wk[1]

    if re.search('[-/:]', w) is None:
        return _serialize_datetime(w, tz)

    prt = w.split(' ')
    date = prt[0]
    time = prt[1] if len(prt) >= 2 else ''
    date = re.sub('/', '-', date)

    prt = date.split('-')
    yyyy = prt[0]
    mm = ('0' + prt[1])[-2:]
    dd = ('0' + prt[2])[-2:]
    date = yyyy + mm + dd

    prt = time.split('.')
    f = ''
    if len(prt) >= 2 and prt[1]:
        f = prt[1]
        time = prt[0]

    prt = time.split(':')
    hh = ('0' + prt[0])[-2:]

    mi = '00'
    if (len(prt) >= 2):
        mi = ('0' + prt[1])[-2:]

    ss = '00'
    if (len(prt) >= 3):
        ss = ('0' + prt[2])[-2:]

    time = hh + mi + ss + f
    return _serialize_datetime(date + time, tz)

def _serialize_datetime(s, tz):
    s = re.sub('[-\s:\.]', '', s)
    s = (s + '000000000000')[0:20]
    s += tz
    return s

# '20220123T123456+0900'
# -> ['20220123T123456', '+0900']
#
# '20220123T123456'
# -> ['20220123T123456', None]
def _split_datetime_and_timezone(s):
    w = s
    tz = ''
    if len(w) > 10:
        tz_pos = _get_tz_pos(w)
        if tz_pos != -1:
            tz = w[tz_pos:]
            w = w[0:tz_pos]
    ret = [w, tz]
    return ret

def _get_tz_pos(s):
    p = s.find('Z')
    if p != -1:
        return p
    tz_sn_cnt = count_str_pattern(s, '\\+')
    if (tz_sn_cnt == 1):
        return s.find('+')
    tz_sn_cnt = count_str_pattern(s, '-')
    if (tz_sn_cnt > 0):
        return s.find('-', len(s) - 6)
    return -1

# ('2021-01-01', 1, '%Y-%m-%d') -> '2021-01-02'
# (datetime, 1, '%Y-%m-%d') -> '2021-01-02'
def add_date(dt, offset, fmt='%Y-%m-%d'):
    if typename(dt) == 'str':
        v = serialize_datetime(dt)
        dt0 = number_to_datetime(v)
    else:
        dt0 = dt
    dt1 = dt0 + datetime.timedelta(days=offset)
    return format_datetime(dt1, fmt)

# '20210102123456789123' -> datetime
def number_to_datetime(v):
    yyyy = v[0:4]
    mm = v[4:6]
    dd = v[6:8]
    hh = v[8:10]
    mi = v[10:12]
    ss = v[12:14]
    f6 = v[14:20]
    y = int(yyyy)
    m = int(mm)
    d = int(dd)
    h = int(hh)
    mn = int(mi)
    s = int(ss)
    f = int(f6)
    return datetime.datetime(y, m, d, h, mn, s, f)

# fmt = '%Y-%m-%d %H:%M:%S.%f'
def format_datetime(dt, fmt):
    if typename(dt) == 'str':
        dt = number_to_datetime(dt)

    yyyy = str(dt.year)
    mm = ('0' + str(dt.month))[-2:]
    dd = ('0' + str(dt.day))[-2:]
    hh = ('0' + str(dt.hour))[-2:]
    mi = ('0' + str(dt.minute))[-2:]
    ss = ('0' + str(dt.second))[-2:]
    f6 = ('00000' + str(dt.microsecond))[-6:]

    s = fmt
    s = re.sub('%Y', yyyy, s)
    s = re.sub('%m', mm, s)
    s = re.sub('%d', dd, s)
    s = re.sub('%H', hh, s)
    s = re.sub('%M', mi, s)
    s = re.sub('%S', ss, s)
    s = re.sub('%f', f6, s)
    return s

# ['0300', '0900', '1200', '1800']
# moment='2020-07-01 00:00:00.0000' -> '2020-07-01 03:00:00.0000'
# moment='2020-07-01 06:00:00.0000' -> '2020-07-01 09:00:00.0000'
# moment='2020-07-01 19:00:00.0000' -> '2020-07-02 03:00:00.0000'
# moment='2020-07-01 00:00:00.0000', offset=-1 -> '2020-06-30 18:00:00.0000'
def next_date_time(time_list, offset=1, moment=None, tz=None):
    if moment is None:
        moment = datetime.datetime.today()
    dt = next_datetime(time_list=time_list, offset=offset, moment=moment, tz=tz)
    return DateTime(dt)

def next_datetime(time_list, offset=1, moment=None, tz=None):
    if moment is None:
        moment = datetime.datetime.today()
    elif typename(moment) == 'float' or typename(moment) == 'int':
        moment = datetime.datetime.fromtimestamp(moment, tz)

    time_list = sorted(time_list)
    last_index = len(time_list) - 1
    year = moment.year
    month = moment.month
    day = moment.day
    timestamp = moment.timestamp()

    found = False
    for i in range(len(time_list)):
        date_time = _get_next_datetime(year, month, day, time_list, i)
        if timestamp <= date_time.timestamp():
            found = True
            break

    if found:
        n = i
    else:
        n = 0

    if offset > 0:
        index = (n + offset - 1) % len(time_list)
    else:
        index = (n + offset) % len(time_list)
    if n > last_index:
        index = n - (last_index - 1)

    date_time = _get_next_datetime(year, month, day, time_list, index)

    if n + offset < 0:
        date_time = date_time + datetime.timedelta(days=-1)
    elif not found:
        date_time = date_time + datetime.timedelta(days=1)

    return date_time

def _get_next_datetime(year, month, day, time_list, index):
    hhmm = time_list[index]
    hour = int(hhmm[0:2])
    min = int(hhmm[2:4])
    sec = 0
    usec = 0
    return datetime.datetime(year, month, day, hour, min, sec, usec)

# ['20190627T090000', '20190627T123000', '20190628T010000']
# '20190627T150000' -> '20190627T123000'
# '20190627T150000', offset=1 -> '20190627T090000'
# '20190627T150000', offset=2 -> None
# '20190627T150000', offset=-1 -> '20190628T010000'
def get_latest_datetime(time_list, moment=None, fmt='%Y%m%dT%H%M%S.%f', offset=0):
    if moment is None:
        moment = get_datetime_str(fmt=fmt)
    moment_ts = get_timestamp(moment, fmt)
    latest = None
    idx = -1
    for i in range(len(time_list)):
        t = time_list[i]
        ts = get_timestamp(t, fmt)
        if ts < moment_ts:
            idx = i
        else:
            break
    if idx >= 0:
        idx += (offset * -1)
        if idx >= 0 and idx < len(time_list):
            latest = time_list[idx]
    return latest

# Returns time zone offset string from seconds / hours
#  32400       -> '+0900'
#  32400, True -> '+09:00'
# -28800       -> '-0800'
# -28800, True -> '-08:00'
#  5.5         -> '+0530'
# -8.0         -> '-0800'
def format_tz(v, ext=False):
    if typename(v) == 'float':
        sec = v * 3600
    else:
        sec = v
    s = '+'
    if sec < 0:
        sec *= -1
        s = '-'
    h = int(sec / 3600)
    m = int((sec - h * 3600) / 60)
    ret = s + ('0' + str(h))[-2:] + ('0' + str(m))[-2:]
    if ext:
        ret = ret[:3] + ':' + ret[3:5]
    return ret

# Local time zone offset string
def get_tz(ext=None):
    return format_tz(LOCAL_TZ_OFFSET, ext)

def micro_to_milli(v):
    return math.floor(v * 1000)

def milli_to_micro(v):
    return v / 1000

#------------------------------------------------
# Time calculation
#------------------------------------------------
# Addition
# '01:00' + '02:00' -> 10800
def add_time(t1, t2):
    s1 = clock2sec(t1)
    s2 = clock2sec(t2)
    return s1 + s2

# Addition
# '12:00' + '01:30' -> '13:30'
# '12:00' + '13:00' -> '01:00 (+1 Day)' / '25:00'
# * Returns ClockTime object
def time_add(t1, t2):
    total_secs = add_time(t1, t2)
    wk_secs = total_secs
    days = 0
    if wk_secs >= DAY:
        days = int(wk_secs / DAY)
        wk_secs -= days * DAY
    return _calc_time(total_secs, wk_secs, days)

# Subtraction
# '01:30' + '00:30' -> 3600
def sub_time(t1, t2):
    s1 = clock2sec(t1)
    s2 = clock2sec(t2)
    return s1 - s2

# Subtraction
# '12:00' - '01:30' -> '10:30'
# '12:00' - '13:00' -> '23:00 (-1 Day)' / '-01:00'
# * Returns ClockTime object
def time_sub(t1, t2):
    s1 = clock2sec(t1)
    s2 = clock2sec(t2)
    total_secs = s1 - s2
    wk_secs = total_secs
    days = 0

    if wk_secs < 0:
        wk_secs *= -1
        days = int(wk_secs / DAY)
        days = days + (0 if (wk_secs % DAY == 0) else 1)
        if s1 != 0:
            if wk_secs % DAY == 0 and wk_secs != DAY:
                days += 1
        wk_secs = DAY - (wk_secs - days * DAY)

    return _calc_time(total_secs, wk_secs, days)

#-----------------
# ClockTime Class
#-----------------
class ClockTime:
    def __init__(self, secs, days, integrated_st, clocklike_st):
        self.secs = secs
        self.days = days
        self.integrated_st = integrated_st
        self.clocklike_st = clocklike_st

    # by_the_day=True
    #         '01:00:00.000000 (+1 Day)'
    # (HM   ) '01:00'
    # (HMS  ) '01:00:00'
    # (HMSs ) '01:00:00.000000'
    # (HMSsD) '01:00:00.000000 (+1 Day)'
    #
    # by_the_day=False
    #         '25:00:00.000000'
    # (HM   ) '25:00'
    # (HMS  ) '25:00:00'
    # (HMSs ) '25:00:00.000000'
    # (HMSsD) '25:00:00.000000'
    def to_str(self, fmt='HMSsD', by_the_day=False):
        hh = self.str_hours(by_the_day)
        mi = self.str_minutes(by_the_day)
        ss = self.str_seconds(by_the_day)
        ms = self.str_microseconds(by_the_day)
        s = ''
        if self.secs < 0 and not by_the_day:
            s += '-'
        if match(fmt, 'H'):
            s += hh
        if match(fmt, 'M'):
            s += ':' + mi
        if match(fmt, 'S'):
            s += ':' + ss
        if match(fmt, 's'):
            s += '.' + ms
        if match(fmt, 'D') and by_the_day and self.days > 0:
            s += ' (' + self.str_days() + ')'
        return s

    def str_days(self):
        if self.secs < 0:
            days = '-'
        else:
            days = '+'
        days += str(self.days) + ' ' + plural('Day', self.days)
        return days

    def str_hours(self, by_the_day=False):
        if by_the_day:
            h = self.clocklike_st['hrs']
        else:
            h = self.integrated_st['hours']
        if h < 10:
            hh = ('0' + str(h))[-2:]
        else:
            hh = str(h)
        return hh

    def str_minutes(self, by_the_day=False):
        st = self.clocklike_st if by_the_day else self.integrated_st
        return ('0' + str(st['minutes']))[-2:]

    def str_seconds(self, by_the_day=False):
        st = self.clocklike_st if by_the_day else self.integrated_st
        return ('0' + str(st['seconds']))[-2:]

    def str_microseconds(self, by_the_day=False):
        st = self.clocklike_st if by_the_day else self.integrated_st
        return ('00000' + str(int(st['microseconds'] * 1000000)))[-6:]

    def is_negative(self):
        return True if self.secs < 0 else False

# Calc time (convert to struct)
def _calc_time(total_secs, wk_secs, days):
    integrated_st = sec2struct(total_secs)
    clocklike_st = sec2struct(wk_secs)
    ret = ClockTime(total_secs, days, integrated_st, clocklike_st)
    return ret

# 86567.123456
# -> {'s': seconds, 'days': 1, 'hours': 24, 'hrs': 0, 'minutes': 2, 'seconds': 47, 'microseconds': 0.123456}
def sec2struct(seconds):
    wk = seconds
    if seconds < 0:
        wk *= (-1)

    days = int(wk / DAY)
    hh = 0
    if wk >= HOUR:
        hh = int(wk / HOUR)
        wk -= (hh * HOUR)

    mi = 0
    if wk >= MINUTE:
        mi = int(wk / MINUTE)
        wk -= (mi * MINUTE)

    ss = int(wk)
    us = round(wk - ss, 6)
    tm = {
        's': seconds,
        'days': days,
        'hrs': hh - days * 24,
        'hours': hh,
        'minutes': mi,
        'seconds': ss,
        'microseconds': us
    }
    return tm

# timestr: '[+|-]HH:MI:SS.ssssss'
# '01:00'           -> 3600.0
# '01:00:30'        -> 3630.0
# '01:00:30.123456' -> 3630.123456
# '0100'            -> 3600.0
# '010030'          -> 3630.0
# '010030.123456'   -> 3630.123456
# '-01:00'          -> -3600.0
# '-010030.123456'  -> -3630.123456
# '+01:00'          -> 3600.0
# '+010030.123456'  -> 3630.123456
def clock2sec(timestr):
    wk = timestr
    hours = 0
    minutes = 0
    seconds = 0
    usecs = 0
    sign = False

    if wk[:1] == '-':
        sign = True
        wk = wk[1:]
    elif wk[:1] == '+':
        wk = wk[1:]

    prt = wk.split('.')
    if len(prt) >= 2:
        wk = prt[0]
        usecs = float('0.' + prt[1])

    if match(wk, ':'):
        pos = wk.index(':')
        h = wk[:pos]
        hours = int(h)
        wk = wk[pos + 1:]
        wk = replace(wk, ':', '')
    else:
        h = wk[:2]
        hours = int(h)
        wk = wk[2:]

    wk = (wk + '00')[:4]
    minutes = int(wk[0:2])
    seconds = int(wk[2:4])

    s = (hours * HOUR) + (minutes * MINUTE) + seconds + usecs
    if sign:
        s *= (-1)
    return s

#     60 ->  '00:01'
#   3600 ->  '01:00'
#  86399 ->  '23:59'
# 360000 -> '100:00'
#    -60 -> '-00:01'
def sec2clock(v):
    st = sec2struct(v)
    h = str(st['hours'])
    if len(h) == 1:
        h = '0' + h
    s = h + ':' + ('0' + str(st['minutes']))[-2:]
    if v < 0:
        s = '-' + s
    return s

# 171959.123456 -> '1d 23h 45m 59s 123456'
# h=True: 47h / h=False: 1d 23h
# f=True: 59.123456s / f=False: 59s
def sec2str(sec, h=False, f=False):
    st = sec2struct(sec)
    p = False
    s = ''
    if sec < 0:
        s = '-'
    if st['days'] > 0 and not h:
        p = True
        s += str(st['days']) + 'd '
    if st['hours'] > 0:
        p = True
        if h:
            s += str(st['hours'])
        else:
            s += str(st['hrs'])
        s += 'h '
    if st['minutes'] > 0 or p:
        p = True
        s += str(st['minutes']) + 'm '
    s += str(st['seconds'])
    if f:
        us = replace(f'{st["microseconds"]:,.6f}'[2:], '0+$', '')
        if st['microseconds'] > 0:
            s += '.' + us
    s += 's'
    return s

# '07:00' -> 7.0
# '07:30' -> 7.5
# '07:45' -> 7.75
def clock2float(s, ndigits=None):
    a = s.split(':')
    hh = a[0]
    mm = a[1]
    h = int(hh)
    m = int(mm)
    sign = 1
    if h < 0:
        h *= -1
        sign = -1
    f = (h + m / 60) * sign
    r = f
    if ndigits is not None:
        r = round(f, ndigits)
    return r

#  7    -> '07:00'
#  7.5  -> '07:30'
#  7.75 -> '07:45'
# -7.75 -> '-07:45'
def float2clock(v):
    sign = ''
    if v < 0:
        v *= -1
        sign = '-'
    i = int(v)
    f = v - int(v)
    m = int(60 * f)
    hh = str(i)
    if i < 10:
        hh = '0' + hh
    mm = str(m)
    if m < 10:
        mm = '0' + mm
    s = sign + hh + ':' + mm
    return s

# '09:00', '10:00' -> -1
# '10:00', '10:00' -> 0
# '10:00', '09:00' -> 1
def timecmp(t1, t2):
    s1 = clock2sec(t1)
    s2 = clock2sec(t2)
    d = s1 - s2
    if d == 0:
        return 0
    elif d < 0:
        return -1
    return 1

# Sleep
def sleep(seconds):
    try:
        seconds = float(seconds)
    except:
        seconds = 0
    if seconds:
        time.sleep(seconds)

#------------------------------------------------------------------------------
# Base64
#------------------------------------------------------------------------------
def decode_base64(s, encoding=DEFAULT_ENCODING, tostring=True, altchars=None, validate=False):
    b = base64.b64decode(s, altchars=altchars, validate=validate)
    if tostring:
        decoded = b.decode(encoding)
    else:
        decoded = b
    return decoded

def encode_base64(s, encoding=DEFAULT_ENCODING, tostring=True, altchars=None):
    b = s
    if typename(s) == 'str':
        b = s.encode(encoding)
    encoded = base64.b64encode(b, altchars=altchars)
    if tostring:
        encoded = encoded.decode(encoding)
    return encoded

#------------------------------------------------------------------------------
# Path
#------------------------------------------------------------------------------
# File Name
# './aaa/abc.txt' -> 'abc.txt'
# './aaa/abc' -> 'abc'
# './aaa/' -> 'aaa'
def get_filename(file_path):
    file_path = file_path.replace('\\', '/')
    if file_path.endswith('/'):
        file_path = file_path[:-1]
    return file_path.split('/')[-1]

# File Name (name only)
# './aaa/abc.txt' -> 'abc'
def get_file_name(file_path):
    filename = get_filename(file_path)
    parts = filename.split('.')
    if len(parts) == 1:
        return filename
    ext = '.' + parts[-1]
    return re.sub(ext, '', filename)

# Extension
# './aaa/abc.txt' -> 'txt'
def get_file_ext(file_path):
    filename = get_filename(file_path)
    parts = filename.split('.')
    if len(parts) == 1:
        return ''
    return parts[-1]

# Parent Path
# a.txt -> ./
# ./dir1/a.txt -> ./dir1
# ./dir1/abc   -> ./dir1
# ./dir1/dir2/ -> ./dir1/dir2
def get_parent_path(path):
    path = path.replace('\\', '/')
    if path.endswith('/'):
        path = path[:-1]
    parent_path = os.path.dirname(path)
    if parent_path == '':
        parent_path = './'
    return parent_path

# aaa/bbb, abc.txt  -> "aaa/bbb/abc.txt"
# aaa/bbb/, abc.txt -> "aaa/bbb/abc.txt"
# aaa/abc, xyz.txt  -> "aaa/xyz.txt"
def get_relative_path(base, path):
    base = base.replace('\\', '/')
    if not base.endswith('/'):
        base_name = get_filename(base)
        if re.search('\.', base_name) is not None:
            base = get_parent_path(base)
    relative_path = os.path.join(base, path)
    return relative_path

# 'dir1', 'a.txt'   -> 'dir1/a.txt'
# 'dir1/', 'a.txt'  -> 'dir1/a.txt'
# 'dir1', '/a.txt'  -> 'dir1/a.txt'
# 'dir1/', '/a.txt' -> 'dir1/a.txt'
def join_path(base, path):
    base = replace(base, r'/$', '')
    base = replace(base, r'\\$', '')
    path = replace(path, r'^/', '')
    path = replace(path, r'^\\', '')
    new_path = base + '/' + path
    return new_path

# prefix20190102T123456suffix.ext
#
#  filename='abc.txt'
#
#  suffix='_' + filename
#  -> 20190102T123456_abc.txt
#
#  prefix=filename + '_'
#  -> abc.txt_20190102T123456
def get_datetime_filename(suffix='', fmt='%Y%m%dT%H%M%S', prefix=''):
    strtime = get_datetime_str(fmt=fmt)
    filename = prefix + strtime + suffix
    return filename

#------------------------------------------------------------------------------
# File
#------------------------------------------------------------------------------
# Read File
# mode:
#  't' = text
#  'b' = bin
def read_file(path, mode='b', default=None, encoding=DEFAULT_ENCODING):
    if not path_exists(path):
        return default
    if mode == 't':
        return read_text_file(path, encoding)
    else:
        return read_binary_file(path)

# Read file as text
def read_text_file(path, encoding=DEFAULT_ENCODING):
    # f = TextIOWrapper
    f = open(path, 'r', encoding=encoding)
    text = f.read()
    f.close()
    return text

# Read text file as list
def read_text_file_as_list(path, default=[], encoding=DEFAULT_ENCODING):
    text_list = default
    if path_exists(path):
        text = read_text_file(path, encoding=encoding)
        text_list = text2list(text)
        if len(text_list) == 1 and text_list[0] == '':
            text_list = default
    return text_list

# Read file as binary
# return type: 'bytes'
def read_binary_file(path):
    # f = BufferedReader
    f = open(path, 'rb')
    b = f.read()
    f.close()
    return b

# Read file as Base64
def read_file_as_base64(path):
    b = read_binary_file(path)
    return base64.b64encode(b).decode()

# Write File
def write_file(path, data, encoding=DEFAULT_ENCODING, make_dir=True):
    if typename(data) == 'str':
        write_text_file(path, data, encoding, make_dir)
    else:
        write_binary_file(path, data, make_dir)

# Write text file
def write_text_file(path, text, encoding=DEFAULT_ENCODING, make_dir=True):
    if typename(text) == 'list':
        text = list2text(text)
    if make_dir:
        make_parent_dir(path)
    b = text.encode(encoding=encoding)
    f = open(path, 'wb')
    f.write(b)
    f.close()

# Write text file from list
def write_text_file_from_list(path, text_list, encoding=DEFAULT_ENCODING, make_dir=True, line_sep='\n'):
    text = list2text(text_list, line_sep)
    write_text_file(path, text, encoding, make_dir)

# Write binary file
def write_binary_file(path, data, make_dir=True):
    if make_dir:
        make_parent_dir(path)
    f = open(path, 'wb')
    data_type = typename(data)
    if data_type == 'BufferedWriter' or data_type == 'BytesIO' or data_type == '_TemporaryFileWrapper':
        for chunk in _read_chunk(data):
            f.write(chunk)
    else:
        f.write(data)
    f.close()

def _read_chunk(file_object, chunk_size=102400):
    while True:
        data = file_object.read(chunk_size)
        if not data:
            break
        yield data

# Write file from Base64
def write_file_from_base64(path, data):
    b = base64.b64decode(data)
    write_binary_file(path, b)

# Base64 file to file decoder
def decode_base64_file_to_file(src_path, dest_path):
    d = read_text_file(src_path)
    write_file_from_base64(dest_path, d)

# Base64 file to file encoder
def encode_base64_file_to_file(src_path, dest_path, newline_position=76):
    s = read_file_as_base64(src_path)
    if newline_position > 0:
        s = insert_newline(s, newline_position)
    write_text_file(dest_path, s)

def insert_newline(s, pos):
    a = list(s)
    p = 0
    while p < len(a):
        p = p + pos
        if p >= len(a):
            break
        a.insert(p, '\r\n')
        p = p + 2;
    return ''.join(a)

# Append a line to text file
def append_line_to_text_file(path, text, encoding=DEFAULT_ENCODING, max=0):
    text_list = read_text_file_as_list(path, default=[], encoding=encoding)
    new_data = ''

    start = 0
    if max > 0 and len(text_list) >= max:
        start = len(text_list) - (max - 1)

    for i in range(start, len(text_list)):
        new_data += text_list[i] + LINE_SEP

    new_data = new_data + text + LINE_SEP
    write_text_file(path, new_data)

def path_exists(path):
    return os.path.exists(path)

def is_file(path):
    return os.path.isfile(path)

def is_dir(path):
    return os.path.isdir(path)

def file_lock(lock_file_path):
    if os.path.exists(lock_file_path):
        return False
    try:
        mkdir(lock_file_path)
    except:
        return False
    return True

def file_unlock(lock_file_path):
    rmdir(lock_file_path)

#------------------------------------------------------------------------------
# ['aaa.txt', 'bbb.txt', 'dir1', ...]
def list_dir(path, pattern=None):
    if not os.path.exists(path):
        return []
    dir_list = os.listdir(path)
    if pattern is not None:
        dir_list = _filter_list(dir_list, pattern)
    dir_list.sort()
    return dir_list

# ['aaa.txt', 'bbb.txt', ...]
def list_files(path, pattern=None):
    if not os.path.exists(path):
        return []
    files = os.listdir(path)
    file_list = [f for f in files if os.path.isfile(os.path.join(path, f))]
    if pattern is not None:
        file_list = _filter_list(file_list, pattern)
    file_list.sort()
    return file_list

# ['dir1', ...]
def list_dirs(path, pattern=None):
    if not os.path.exists(path):
        return []
    files = os.listdir(path)
    dir_list = [f for f in files if os.path.isdir(os.path.join(path, f))]
    if pattern is not None:
        dir_list = _filter_list(dir_list, pattern)
    dir_list.sort()
    return dir_list

def _filter_list(src_list, pattern):
    filtered_list = []
    for item in src_list:
        if match(item, pattern):
            filtered_list.append(item)
    return filtered_list

# File Info
def get_file_info(path):
    info = {
        'filename': get_filename(path),
        'name': get_file_name(path),
        'ext': get_file_ext(path),
        'parent': get_parent_path(path),
        'size': os.path.getsize(path),
        'ctime': os.path.getctime(path),
        'mtime': os.path.getmtime(path),
        'atime': os.path.getatime(path),
        'isdir': os.path.isdir(path),
        'islink': os.path.islink(path)
    }
    return info

# DIR/File Info
# recursive: search level (0 = no limit)
# depth: max level of the result tree (0 = no limit)
def get_dir_info(path, pattern=None, recursive=0, depth=0):
    info = _get_dir_info(path, pattern, recursive, 0, depth)
    if 'found' in info:
        del info['found']
    return info

def _get_dir_info(path, pattern, recursive, lv, depth):
    lv += 1
    dir_info = get_file_info(path)
    dirsize = 0
    dirs = 0
    files = 0
    filename = get_filename(path)
    islink = os.path.islink(path)

    if os.path.isdir(path):
        dir_info['dirsize'] = 0
        dir_info['dirs'] = 0
        dir_info['files'] = 0
        dir_info['children'] = {}
        dir_info['found'] = False

        if pattern is None or match(filename, pattern):
            dir_info['found'] = True

        try:
            for p in os.listdir(path):
                full_path = os.path.join(path, p)
                if os.path.isdir(full_path):
                    if recursive == 0 or lv < recursive:
                        info = _get_dir_info(full_path, pattern, recursive, lv, depth)
                        if pattern is None or info['found'] or (pattern is not None and info['dirsize'] > 0):
                            dir_info['found'] = True
                            del info['found']
                            dirsize += info['dirsize']
                            dirs += info['dirs'] + 1
                            files += info['files']
                            dir_info['children'][p] =info
                    else:
                        if pattern is None or match(p, pattern):
                            dir_info['found'] = True
                            info = get_file_info(full_path)
                            info['dirsize'] = 0
                            info['dirs'] = 0
                            info['files'] = 0
                            info['children'] = {}
                            dir_info['children'][p] = info
                            dirs += 1
                else:
                    if pattern is None or match(p, pattern):
                        dir_info['found'] = True
                        if depth == 0:
                            dir_info['children'][p] = get_file_info(full_path)
                        if not islink:
                            dirsize += os.path.getsize(full_path)
                            files += 1
        except Exception as e:
            dir_info['info'] = str(e)

        dir_info['dirsize'] = dirsize
        dir_info['dirs'] = dirs
        dir_info['files'] = files

        if depth > 0 and lv + 1 > depth:
            del dir_info['children']

    return dir_info

# DIR/File Size
def get_size(path, recursive=False, slink=True):
    total = 0
    islink = os.path.islink(path)
    if not islink or slink:
        if os.path.isdir(path):
            for p in os.listdir(path):
                full_path = os.path.join(path, p)
                islink = os.path.islink(full_path)
                if not islink or slink:
                    if recursive and os.path.isdir(full_path):
                        total += get_size(full_path, recursive, slink)
                    else:
                        total += os.path.getsize(full_path)
        else:
            total += os.path.getsize(path)
    return total

# Make DIR
def mkdir(path):
    if not is_dir(path):
        os.makedirs(path)

# Delete DIR
# force=True: delete recursively
def rmdir(path, force=False):
    if os.path.exists(path):
        if force:
            shutil.rmtree(path)
        else:
            os.rmdir(path)

# Delete a file
def delete_file(path):
    if os.path.exists(path):
        os.remove(path)

# Delete a file or DIR
# force=True: delete recursively
def delete(path, force=False):
    if is_dir(path):
        rmdir(path, force)
    else:
        delete_file(path)

# Copy a file or DIR
def copy(src, dst, overwrite=True):
    if is_dir(src):
        copy_dir(src, dst, overwrite)
    else:
        copy_file(src, dst)

# copy_file('a.txt', 'b.txt')
# copy_file('a.txt', 'd1')
# copy_file('d1/a.txt', 'd2/a.txt')
# dst will be overwritten if exists
def copy_file(src, dst):
    shutil.copy2(src, dst)

# Copy DIR recursively
# FileExistsError will be thrown if dst exists
def copy_dir(src, dst):
    shutil.copytree(src, dst)

# Move a file or DIR
def move(src, dst, force=False):
    move_dst = dst
    if is_file(src) and is_dir(dst):
        src_filename = get_filename(src)
        move_dst = join_path(dst, src_filename)
    if is_file(move_dst) and path_exists(move_dst):
        if force:
            delete_file(move_dst)
        else:
            return False
    shutil.move(src, dst)
    return True

# Rename file/dir
def rename(src, dst, force=False):
    if path_exists(dst):
        if force:
            if is_file(src) == is_file(dst):
                delete_file(dst)
            else:
                return False
        else:
            return False
    os.rename(src, dst)
    return True

# replace_file('a.txt', r"(?P<p1>.*\.v\s+=\s+').*(?P<p2>';)", r'\g<p1>20190101-1234\g<p2>')
def replace_file(path, pattern, repl, count=0, flags=0):
    text = read_text_file(path)
    if re.search(pattern, text, re.MULTILINE) is None:
        return False
    else:
        new_text = re.sub(pattern, repl, text, count, flags)
        write_text_file(path, new_text)
        return True

# grep
def grep(path, pattern, flags=0, filename=None, filename_flags=0, recursive=True, output='dict'):
    results = {}
    _grep(path, pattern, flags, filename, filename_flags, recursive, results)
    if output == 'text':
        ret = ''
        for k in results:
            result = results[k]
            for i in range(len(result)):
                ret += k + '(' + str(result[i]['line']) + '): ' + result[i]['matched'] + LINE_SEP
    else:
        if len(results) == 0:
            ret = None
        else:
            ret = results
    return ret

def _grep(path, pattern, flags, filename, filename_flags, recursive, ret):
    if os.path.isdir(path):
        try:
            for p in os.listdir(path):
                full_path = os.path.join(path, p)
                if os.path.isdir(full_path):
                    if recursive:
                        _grep(full_path, pattern, flags, filename, filename_flags, recursive, ret)
                else:
                    fname = get_filename(full_path)
                    if filename is None or re.search(filename, fname, filename_flags) is not None:
                        results = _search_text_pattern(full_path, pattern, flags)
                        if results is not None:
                            ret[full_path] = results
        except:
            return
    else:
        fname = get_filename(path)
        if filename is None or re.search(filename, fname, filename_flags) is not None:
            results = _search_text_pattern(path, pattern, flags)
            if results is not None:
                ret[path] = results

def _search_text_pattern(path, pattern, flags):
    results = []
    try:
        txt = read_text_file(path, encoding=DEFAULT_ENCODING)
    except:
        return None
    lines = text2list(txt)
    for i in range(len(lines)):
        line = lines[i]
        if match(line, pattern, flags):
            info = {
                'line': i + 1,
                'matched': line
            }
            results.append(info)
    if len(results) == 0:
        results = None
    return results

#------------------------------------------------------------------------------
# command
#------------------------------------------------------------------------------
def exec_cmd(cmd, timeout=None, encoding=None):
    if encoding is not None:
        set_stdout_encoding(encoding)
    cp = subprocess.run(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, timeout=timeout)
    ret = cp.stdout.decode(sys.stdout.encoding)
    return ret

#------------------------------------------------------------------------------
# Random
#------------------------------------------------------------------------------
# int
def random_int(start, stop=None, step=1):
    if stop is None:
        start += 1
    else:
        stop += 1
    return random.randrange(start, stop, step)

# float
def random_float(a, b):
    return random.uniform(a, b)

# boolean
def random_bool(rate=0.5):
    if rate == 0:
        return False
    a = 1 / rate
    r = random_float(0, a)
    return r <= 1.0

# ASCII Char
def random_ascii():
    c = random_int(0x20, 0x7e)
    return chr(c)

# String
def random_str(min, max=None, tbl=[*'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789']):
    strlen = min
    if max is not None:
        strlen = random_int(min, max)

    if len(tbl) == 0:
        return ''

    s = ''
    max_tbl_idx = len(tbl) - 1
    for i in range(strlen):
        n = random_int(max_tbl_idx)
        s += tbl[n]

    return s

#------------------------------------------------------------------------------
# Hash
#------------------------------------------------------------------------------
def hash(src, algorithm='SHA-256'):
    m = None

    if algorithm == 'SHA-256':
        m = hashlib.sha256()
    elif algorithm == 'SHA-512':
        m = hashlib.sha512()
    elif algorithm == 'SHA-224':
        m = hashlib.sha224()
    elif algorithm == 'SHA-384':
        m = hashlib.sha384()
    elif algorithm == 'SHA-1':
        m = hashlib.sha1()
    elif algorithm == 'MD5':
        m = hashlib.md5()
    # Python 3.6+
    elif algorithm == 'SHA3-256':
        m = hashlib.sha3_256()
    elif algorithm == 'SHA3-512':
        m = hashlib.sha3_512()
    elif algorithm == 'SHA3-224':
        m = hashlib.sha3_224()
    elif algorithm == 'SHA3-384':
        m = hashlib.sha3_384()

    if m is None:
        return ''

    if typename(src) == 'str':
        src = src.encode()

    m.update(src)
    return m.hexdigest()

# Returns a hash string of the file content
def get_file_hash(path, algorithm='SHA-256'):
    b = read_binary_file(path)
    return hash(b, algorithm)

#------------------------------------------------------------------------------
# HTTP Request
#------------------------------------------------------------------------------
# proxies:
#  None = auto-detection
#  {} = disable auto-detection
#  {'http': 'http://localhost:8080', 'https': 'https://localhost:8080', 'ftp': 'ftp://localhost:8080'}
#
def http(url, method='GET', params=None, data=None, user=None, password=None,
         headers=None, proxies=None, encoding=DEFAULT_ENCODING,
         timeout=DEFAULT_HTTP_TIMEOUT, cafile=None, capath=None,
         cadefault=False, context=None, verifycert=True):
    try:
        res = http_request(url, method, params, user, password, headers, proxies, encoding, timeout, cafile=cafile, capath=capath, cadefault=cadefault, context=context, verifycert=verifycert)
    except socket.timeout:
        raise Exception('TIMEOUT')
    except urllib.error.HTTPError as e:
        raise Exception(e.code)
    except urllib.error.URLError as e:
        raise Exception(e.reason)

    chrset = res.headers.get_content_charset()
    if chrset is None:
        chrset = encoding

    text = res.read().decode(chrset)
    return text

# returns an object which can work as a context manager and has methods such as
#  geturl()  - return the URL of the resource retrieved,
#              commonly used to determine if a redirect was followed
#  info()    - return the meta-information of the page, such as headers,
#              in the form of an email.message_from_string()
#              instance (see Quick Reference to HTTP Headers)
#  getcode() - int: return the HTTP status code of the response.
#
# https://docs.python.org/3.6/library/http.client.html#httpresponse-objects
# http.client.HTTPResponse
#  read([amt]) - Reads and returns the response body, or up to the next amt bytes.
#  readinto(b) - Reads up to the next len(b) bytes of the response body
#                into the buffer b. Returns the number of bytes read.
#  getheader(name, default=None)
#  getheaders() - Return a list of (header, value) tuples.
#  fileno()     - Return the fileno of the underlying socket.
#  msg          - A http.client.HTTPMessage instance containing the response headers.
#                 http.client.HTTPMessage is a subclass of email.message.Message.
#  version      - HTTP protocol version used by server. 10 for HTTP/1.0, 11 for HTTP/1.1.
#  status       - Status code returned by server.
#  reason       - Reason phrase returned by server.
#  debuglevel   - A debugging hook. If debuglevel is greater than zero,
#                 messages will be printed to stdout as the response is read and parsed.
#  closed       - Is True if the stream is closed.
def http_request(url, method='GET', params=None, data=None, user=None, password=None,
                 headers=None, proxies=None, encoding=DEFAULT_ENCODING,
                 timeout=DEFAULT_HTTP_TIMEOUT, cafile=None,
                 capath=None, cadefault=False, context=None, verifycert=True):

    if data is None:
        if params is not None:
            q = urllib.parse.urlencode(params, doseq=True)
            if method == 'POST':
                data = q.encode(encoding)
            else:
                url += '?' + q
    else:
        if typename(data) == 'str':
            data = data.encode()

    if headers is None:
        headers = {}

    if (not user is None) and (not password is None):
        auth = encode_base64(user + ':' + password)
        headers['Authorization'] = 'Basic ' + auth

    req = urllib.request.Request(url, headers=headers, method=method)
    if headers is None or 'User-Agent' not in headers:
        req.add_header('User-Agent', 'Mozilla/5.0')

    proxy = urllib.request.ProxyHandler(proxies)
    opener = urllib.request.build_opener(proxy)
    urllib.request.install_opener(opener)

    if not verifycert:
        ssl._create_default_https_context = ssl._create_unverified_context

    res = urllib.request.urlopen(req, data, timeout=timeout, cafile=cafile, capath=capath, cadefault=cadefault, context=context)
    return res

def get_response_body(response, encoding=DEFAULT_ENCODING):
    chrset = response.headers.get_content_charset()
    if chrset is None:
        chrset = encoding
    return response.read().decode(chrset)

# {'http': 'http://localhost:1234', 'https': 'https://localhost:1234', 'ftp': 'ftp://localhost:1234'} or {}
def get_proxies():
    return urllib.request.getproxies()

# 'a b' -> 'a%20b'
def encode_uri(s):
    return urllib.parse.quote(s)

# 'a%20b' -> 'a b'
def decode_uri(s):
    return urllib.parse.unquote(s)

#------------------------------------------------------------------------------
# CGI
#------------------------------------------------------------------------------
def get_field_storage():
    global form_data
    if form_data is None:
        form_data = cgi.FieldStorage()
    return form_data

def free_form_data():
    global form_data
    form_data = None

def get_request_param(key, default=None):
    form = get_field_storage()
    return form.getvalue(key, default)

# Query String
def get_query(key=None, q=None):
    if q is None:
        if os.environ.get('REQUEST_METHOD') == 'POST':
            q = read_stdin()
        else:
            q = os.environ.get('QUERY_STRING')

    if q is not None and key is not None:
        q = _get_query(q, key)
        if q is not None:
            if typename(q) == 'list':
                for i in range(len(q)):
                    q[i] = decode_uri(q[i])
            else:
                q = decode_uri(q)

    return q

def _get_query(s, key):
    q = s.split('&')
    a = []
    for i in range(len(q)):
        p = q[i].split('=')
        if p[0] == key:
            a.append(p[1])
    v = None
    if len(a) == 1:
        v = a[0]
    elif len(a) > 1:
        v = a
    return v

# POST data
def get_post_data():
    return read_stdin()

# Remote IP Address
def get_ip_addr(default=''):
    return os.environ.get('REMOTE_ADDR', default)

# Remote Host Name
# Returns '' if not available
def get_host_name(default=''):
    addr = get_ip_addr()
    return get_host_by_addr(addr, default)

# gethostbyaddr
def get_host_by_addr(addr, default=''):
    try:
        host_name = socket.gethostbyaddr(addr)[0]
    except Exception:
        host_name = default
    return host_name

# Request URI
def get_request_uri():
    return os.environ.get('REQUEST_URI', '')

# User-Agent
def get_user_agent(default=''):
    return os.environ.get('HTTP_USER_AGENT', default)

# Cookie
def get_cookie():
    return os.environ.get('HTTP_COOKIE', '')

# http.cookies.Morsel
def get_cookie_morsel():
    c = None
    if 'HTTP_COOKIE' in os.environ:
        c = cookies.SimpleCookie()
        rawdata = get_cookie()
        c.load(rawdata)
    return c

# Returns cookie value
def get_cookie_val(key, default=None):
    c = get_cookie_morsel()
    if c is not None and key in c:
        return c[key].value
    return default

# 'key=value; max-age=3600; expires=Thu, 01 Jan 1970 00:00:00 GMT; Path=/; Secure; HttpOnly'
def build_cookie(key, value, max_age=None, expires=None, path=None, secure=False, http_only=False):
    s = key + '=' + value

    if expires is not None:
        s += '; expires=' + expires

    if max_age is not None:
        s += '; max-age=' + max_age

    if path is not None:
        s += '; path=' + path

    if secure:
        s += '; Secure'

    if http_only:
        s += '; HttpOnly'

    return s

# 'key=; expires=Thu, 01 Jan 1970 00:00:00 GMT; Path=/; Secure; HttpOnly'
def build_cookie_clear(key, path=None, secure=False, http_only=False):
    s = key + '=; expires=Thu, 01 Jan 1970 00:00:00 GMT'

    if path is not None:
        s += '; path=' + path

    if secure:
        s += '; Secure'

    if http_only:
        s += '; HttpOnly'

    return s

#------------------------------------------------------------------------------
# HTTP Response
#------------------------------------------------------------------------------
# https://tools.ietf.org/html/rfc2616#section-6.1.1
HTTP_STATUS = {
    '100': 'Continue',
    '101': 'Switching Protocols',
    '200': 'OK',
    '201': 'Created',
    '202': 'Accepted',
    '203': 'Non-Authoritative Information',
    '204': 'No Content',
    '205': 'Reset Content',
    '206': 'Partial Content',
    '300': 'Multiple Choices',
    '301': 'Moved Permanently',
    '302': 'Found',
    '303': 'See Other',
    '304': 'Not Modified',
    '305': 'Use Proxy',
    '307': 'Temporary Redirect',
    '400': 'Bad Request',
    '401': 'Unauthorized',
    '402': 'Payment Required',
    '403': 'Forbidden',
    '404': 'Not Found',
    '405': 'Method Not Allowed',
    '406': 'Not Acceptable',
    '407': 'Proxy Authentication Required',
    '408': 'Request Time-out',
    '409': 'Conflict',
    '410': 'Gone',
    '411': 'Length Required',
    '412': 'Precondition Failed',
    '413': 'Request Entity Too Large',
    '414': 'Request-URI Too Large',
    '415': 'Unsupported Media Type',
    '416': 'Requested range not satisfiable',
    '417': 'Expectation Failed',
    '500': 'Internal Server Error',
    '501': 'Not Implemented',
    '502': 'Bad Gateway',
    '503': 'Service Unavailable',
    '504': 'Gateway Time-out',
    '505': 'HTTP Version not supported'
}
def get_status_message(status):
    st = str(status)
    if st in HTTP_STATUS:
        msg = st + ' ' + HTTP_STATUS[st]
    else:
        msg = st
    return msg

# headers = [
#  {'Set-Cookie': 'key1=val1'},
#  {'Set-Cookie': 'key2=val2'}
#  {'Location': '/foo.txt'},
# ]
def send_response(type, content, status=200, headers=None, encoding=DEFAULT_ENCODING):
    global res_debug

    # Prevent the following error:
    #  ap_content_length_filter: apr_bucket_read() failed
    #  Failed to flush CGI output to client
    global stdin_data
    global form_data
    if stdin_data is None and form_data is None:
        stdin_data = sys.stdin.read()

    if type == 'text':
        type = 'text/plain'
    elif type == 'json':
        type = 'application/json'
        if typename(content) != 'str':
            content = to_json(content)
    elif type == 'html':
        type = 'text/html'
    elif type == 'binary':
        type = 'application/octet-stream'

    content_type = 'Content-Type: ' + type
    if encoding is not None:
        content_type += '; charset=' + encoding
        set_stdout_encoding(encoding)

    if res_debug:
        log(content)

    st = 'Status: ' + get_status_message(status)
    print(st)
    print(content_type)
    if headers is not None:
        for i in range(len(headers)):
            header = headers[i]
            for name in header:
                print(name + ': ' + header[name])
    print()
    print(content)

def send_result_json(status, body=None, headers=None, encoding=None):
    result = build_result_object(status, body)
    content = to_json(result)
    send_response('json', content, headers=headers, encoding=encoding)

def build_result_object(status, body=None, headers=None):
    result = {
        'status': status
    }
    if headers is not None:
        for key in headers:
            result[key] = headers[key]
    result['body'] = body
    return result

def send_response_debug(enable=True):
    global res_debug
    res_debug = enable

# Send binary response
# content: bytes or hex string
#        '89 50 4E 47 0D 0A 1A 0A ...'
# content_type: MIME Type
# filename: default filename to download
# etag: "xyzzy"
# headers: {
#   'Field-Name': 'Field-Value',
#   ...
# }
# status: HTTP status code for response
def send_binary(content, filename='', content_type='application/octet-stream', etag='', headers=None, status=200):
    # Prevent the following error:
    #  ap_content_length_filter: apr_bucket_read() failed
    #  Failed to flush CGI output to client
    global stdin_data
    global form_data
    if stdin_data is None and form_data is None:
        stdin_data = sys.stdin.read()

    if typename(content) == 'str':
        b = hex2bytes(content)
    else:
        b = content

    st = 'Status: ' + get_status_message(status)
    print(st)

    if content_type != '':
        print('Content-Type: ' + content_type)

    if headers is None or 'Content-Length' not in headers:
        content_len = len(b)
        print('Content-Length: ' + str(content_len))

    if filename != '':
        print('Content-Disposition: attachment;filename="' + filename + '"')

    if etag != '':
        print('ETag: "' + etag + '"')

    if headers is not None:
        for i in range(len(headers)):
            header = headers[i]
            for name in header:
                print(name + ': ' + header[name])

    print()
    sys.stdout.flush()
    sys.stdout.buffer.write(b)

#------------------------------------------------------------------------------
# Location
#------------------------------------------------------------------------------
# '35.681237, 139.766985'
# ->
# {
#   'latitude': 35.681237,
#   'longitude': 139.766985
# }
def parse_coordinate(location):
    location = replace(location, ' ', '')
    loc = location.split(',')
    lat = float(loc[0])
    lon = float(loc[1])
    coordinate = {
        'latitude': lat,
        'longitude': lon
    }
    return coordinate

# '35.681237, 139.766985'
# -> 35.681237
def latitude(location):
    coordinate = parse_coordinate(location)
    return coordinate['latitude']

# '35.681237, 139.766985'
# -> 139.766985
def longitude(location):
    coordinate = parse_coordinate(location)
    return coordinate['longitude']

# 269, 0, 180 -> false
# 270, 0, 180 -> true
#   0, 0, 180 -> true
#  90, 0, 180 -> true
#  91, 0, 180 -> false
def is_forward_movement(azimuth, heading, range):
    azimuth = round_angle(azimuth)
    heading = round_angle(heading)
    range = round_angle(range)

    rangeL = heading - (range / 2)
    if rangeL < 0:
        rangeL += 360

    rangeR = heading + (range / 2)
    if rangeR >= 360:
        rangeR -= 360

    if rangeR < rangeL:
        if azimuth >= rangeL or azimuth <= rangeR:
            return True
    else:
        if azimuth >= rangeL and azimuth <= rangeR:
            return True

    return False

# 360 -> 0
# 361 -> 1
# -1  -> 359
def round_angle(v):
    if v < 0:
        v = 360 + (v % 360)
    if v >= 360:
        v = v % 360
    return v

#------------------------------------------------------------------------------
# CSV
#------------------------------------------------------------------------------
# return:
# [
#  [COL1, COL2, ..., COLn],
#  [COL1, COL2, ..., COLn],
#  ...
# ]
def read_csv(file_path, newline='', encoding='utf-8', delimiter=',', quotechar='"'):
    csv_data = []
    with open(file_path, newline=newline, encoding=encoding) as csvfile:
        spamreader = csv.reader(csvfile, delimiter=delimiter, quotechar=quotechar)
        for row in spamreader:
            csv_data.append(row)
    return csv_data

# data:
# [
#  [COL1, COL2, ..., COLn],
#  [COL1, COL2, ..., COLn],
#  ...
# ]
#
# quoting:
# csv.QUOTE_ALL
# csv.QUOTE_MINIMAL
# csv.QUOTE_NONNUMERIC
# csv.QUOTE_NON
def write_csv(file_path, data, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL):
    with open(file_path, 'w', newline='') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=delimiter, quotechar=quotechar, quoting=quoting)
        for row in data:
            spamwriter.writerow(row)

def quote_csv_field(value, quotechar='"', esc=None):
    if esc is None:
        esc = quotechar
    s = replace(value, quotechar, esc + quotechar);
    s = quotechar + s + quotechar
    return s

#------------------------------------------------------------------------------
# Record Data
#------------------------------------------------------------------------------
class Record:
    def __init__(self, fields=None, sep='\t'):
        if fields is None:
            self.fields = []
        else:
            self.fields = fields.split(sep)

    def add(self, val):
        self.fields.append(val)

    def get(self, index, default=None):
        v = default
        if index < len(self.fields):
            v = self.fields[index]
        return v

    def to_str(self, sep='\t', csvmode=True, quot=False):
        r = ''
        for i in range(len(self.fields)):
            if i > 0:
                r += sep
            v = str(self.fields[i])
            if csvmode:
                v = replace(v, '"', '""')
                if quot or match(v, sep) or match(v, '"') or match(v, '\r\n|\n', flags=re.MULTILINE):
                    v = '"' + v + '"'
            r += v
        return r

#------------------------------------------------------------------------------
# for Test Data
#------------------------------------------------------------------------------
# [s]234567890123...[e]
# s: -1 -> '1'
# e: -1 -> [0-9] of the end position
def get_seq_bytes(size, s=-1, e=-1):
    buf = []
    for i in range(1, size + 1):
        if i == 1:
            if s == -1:
                v = 0x31
            else:
                v = s
        elif i == size:
            if e == -1:
                v = i % 10 + 0x30
            else:
                v = e
        else:
            v = i % 10 + 0x30
        buf.append(v)
    b = bytearray(buf)
    return b

#------------------------------------------------------------------------------
# HEX Dump
#------------------------------------------------------------------------------
# src:
#  b'\x01\xA0'
def hexdump(src, limit=0, last_rows=16, header=True, address=True, ascii=True):
    byte_len = len(src)
    if limit == 0:
        limit = byte_len

    dump_len = limit if byte_len > limit else byte_len
    if dump_len % 0x10 != 0:
        dump_len = (int(dump_len / 0x10) + 1) * 0x10

    last_part_len = 0x10 * last_rows

    sb = ''
    if header:
        if address:
            sb += 'Address    '
        sb += '+0 +1 +2 +3 +4 +5 +6 +7  +8 +9 +A +B +C +D +E +F'
        if ascii:
            sb += '  ASCII'
        sb += LINE_SEP

        if address:
            sb += '----------'
        sb += '------------------------------------------------'
        if ascii:
            sb += '------------------'
        sb += LINE_SEP

    for addr in range(0, dump_len, 16):
        if address:
            sb += dump_addr(addr)
        sb += dump_16bytes(src, addr)
        if ascii:
            sb += '  '
            sb += dump_ascii(src, addr)
        sb += LINE_SEP

    if byte_len > limit:
        if byte_len - limit > 0x10 * last_rows:
            sb += '...\n'

        if last_rows > 0:
            rem = byte_len % 0x10
            start_addr = byte_len - last_part_len if rem == 0 else (byte_len - rem) - (0x10 * (last_rows - 1))

            if start_addr < dump_len:
                rem = (dump_len - start_addr) % 0x10
                start_addr = dump_len + rem

            end_addr = byte_len
            if rem != 0:
                end_addr += (0x10 - rem)

            for addr in range(start_addr, end_addr, 16):
                if address:
                    sb += dump_addr(addr)
                sb += dump_16bytes(src, addr)
                if ascii:
                    sb += '  '
                    sb += dump_ascii(src, addr)
                sb += LINE_SEP

    return sb

# Dump Address
def dump_addr(addr):
    hex_addr = format(addr, 'X')
    base_addr = ('0000000' + hex_addr)[-8:]
    return base_addr + ' : '

# Dump 16 bytes
def dump_16bytes(buf, start_addr):
    addr = start_addr
    sb = ''
    for i in range(16):
        if i == 8:
            sb += '  '
        elif i > 0:
            sb += ' '

        if addr < len(buf):
            b = buf[addr]
            sb += ('0' + format(b, 'X'))[-2:]
        else:
            sb += '  '

        addr += 1

    return sb

# Dump ASCII
def dump_ascii(buf, start_addr):
    sb = ''
    addr = start_addr

    for i in range(16):
        if addr < len(buf):
            b = buf[addr]
            if b >= 0x20 and b <= 0x7E:
                sb += chr(b)
            else:
                sb += '.'
        else:
            sb += ' '

        addr += 1

    return sb

# HEX dump from file
def file_dump(path, limit=0, last_rows=16):
    b = read_binary_file(path)
    return hexdump(b, limit, last_rows)

#------------------------------------------------------------------------------
# Console Print
#------------------------------------------------------------------------------
def printlog(msg=''):
    t = DateTime().to_str('%Y-%m-%d %H:%M:%S.%F %Z')
    print(t + '  ' + msg)

def print_elapsed_time(prefix='', suffix=''):
    t = elapsed_time()
    m = prefix + t + suffix
    printlog(m)

#------------------------------------------------------------------------------
# ZIP
#------------------------------------------------------------------------------
# to file
# zip('C:/tmp/zip1.zip', 'C:/path/a.txt')
# -> a.txt
#
# zip('C:/tmp/zip1.zip', 'C:/path/dir1')
# -> dir1/*
#
# zip('C:/tmp/zip1.zip', 'C:/path/dir1', excl_root_path=True)
# -> *
#
# zip('C:/tmp/zip1.zip', ['C:/path/a.txt', 'C:/path/b.txt'])
# -> a.txt, b.txt
#
# on memory
# zip(None, 'C:/path/dir1')
# -> returns bytes
#
# * filepath must be absolute path
def zip(zip_path, filepath, excl_root_path=False, arcname=None, slink=True):
    if zip_path is None:
        zip_out = io.BytesIO()
    else:
        zip_out = zip_path

    with zipfile.ZipFile(zip_out, 'w', compression=zipfile.ZIP_DEFLATED) as zipf:
        if typename(filepath) == 'list':
            for path in filepath:
                if os.path.exists(path):
                    root_path = _get_root_path(path, excl_root_path)
                    _zip(zipf, path, root_path, slink=slink)
        else:
            if os.path.exists(filepath):
                root_path = _get_root_path(filepath, excl_root_path)
                _zip(zipf, filepath, root_path, arcname=arcname, slink=slink)

    if zip_path == None:
        return zip_out.getvalue()

def _zip(zipf, target_path, root_path, arcname=None, slink=True):
    islink = os.path.islink(target_path)
    if islink and not slink:
        return
    # DIR
    if os.path.isdir(target_path):
        for path in os.listdir(target_path):
            full_path = os.path.join(target_path, path)
            full_path = re.sub('\\\\', '/', full_path)
            islink = os.path.islink(full_path)
            if not islink or slink:
                if os.path.isdir(full_path):
                    _zip(zipf, full_path, root_path, slink=slink)
                else:
                    arcname = full_path
                    arcname = re.sub('\\\\', '/', arcname)
                    arcname = re.sub(root_path, '', arcname)
                    arcname = re.sub('^/', '', arcname)
                    zipf.write(full_path, arcname=arcname)
    # File
    else:
        if arcname is None:
            parent_path = get_parent_path(target_path)
            arcname = re.sub(parent_path, '', target_path)
        zipf.write(target_path, arcname=arcname)

# unzip('C:/tmp/zip1.zip', 'C:/path/dir1')
# unzip('C:/tmp/zip1.zip', 'C:/path/dir1', 'pwd='pass123')
def unzip(zip_path, out_path, pwd=None):
    if pwd is not None:
        pwd = pwd.encode(encoding='utf-8')
    with zipfile.ZipFile(zip_path) as zipf:
        zipf.extractall(out_path, pwd=pwd)

# 'C:/path/dir1', excl_root_path=True  -> C:/path/dir1
# 'C:/path/dir1', excl_root_path=False -> C:/path
def _get_root_path(path, excl_root_path):
    if excl_root_path:
        root_path = path
    else:
        root_path = get_parent_path(path)
    root_path = re.sub('\\\\', '/', root_path)
    return root_path

# ['a.txt', 'b.txt', 'dir1/c.txt']
def list_zip(zip_path):
    if not path_exists(zip_path):
        return None
    zip_f = zipfile.ZipFile(zip_path)
    namelist = zip_f.namelist()
    zip_f.close()
    return namelist

#------------------------------------------------------------------------------
# Debugging
#------------------------------------------------------------------------------
# Start debugging
def debug():
    setup_logging(reset=True)

# Setup Logging
def setup_logging(name=__name__, filename='log.txt', level=logging.DEBUG,
                  format='%(asctime)s [%(name)s] [%(levelname)s] %(message)s',
                  reset=False):
    global logger
    if reset:
        _delete_logfile(filename)
    logger = logging.getLogger(name)
    logging.basicConfig(filename=filename, format=format, level=level)

def _delete_logfile(filename):
    try:
        delete_file(filename)
    except:
        return

# Log output
def log(msg=''):
    if logger is None:
        setup_logging()
    logger.debug(str(msg))

# Start time test
def start_timetest():
    global time_start
    time_start = datetime.datetime.today()

# End time test
def end_timetest():
    global time_start
    time_end = datetime.datetime.today()
    delta = time_end - time_start
    return delta.total_seconds()

# Returns elapsed time
def elapsed_time():
    if not 'time_start' in globals():
        return ''
    t = end_timetest()
    return sec2str(t)

if __name__ == '__main__':
    print('util.py')
