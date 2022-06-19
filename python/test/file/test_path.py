#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util


def test_get_relative_path():
    s = '\n'
    s += '\'./dir1/a.txt\', \'./dir2\' = '
    s += util.get_relative_path('./dir1/a.txt', './dir2')
    return s

def test():
    ret = ''
    ret += 'test_get_relative_path() = ' + test_get_relative_path() + '\n'
    return ret

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)
    util.send_response('text', ret)

main()
