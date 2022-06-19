#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_decode_base64():
    s = '\n'
    s += util.decode_base64('YWJj')
    s += '\n'
    s += util.decode_base64('YWJj', 'utf-8')
    s += '\n'
    s += util.decode_base64('YWJj', 'shift-jis')
    s += '\n'
    s += util.decode_base64('YWJj44GC44GE44GG', 'utf-8')
    s += '\n'
    s += util.decode_base64('YWJjgqCCooKk', 'shift-jis')
    return s

def test_encode_base64():
    s = '\n'
    s += util.encode_base64('abc')
    s += '\n'
    s += util.encode_base64('abc', 'utf-8')
    s += '\n'
    s += util.encode_base64('abc', 'shift-jis')
    s += '\n'
    s += util.encode_base64('abcあいう', 'utf-8')
    s += '\n'
    s += util.encode_base64('abcあいう', 'shift-jis')
    return s

def test():
    ret = ''
    ret += 'test_decode_base64() = ' + test_decode_base64() + '\n'
    ret += '\n'
    ret += 'test_encode_base64() = ' + test_encode_base64() + '\n'
    return ret

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)
    util.send_response('text', ret)

main()
