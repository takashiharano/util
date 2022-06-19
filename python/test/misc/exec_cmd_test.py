#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    cmd = 'dir'
    ret = ''
    ret += '\n' + util.convert_newline(util.exec_cmd(cmd), '\n')
    ret += '\n'
    ret += '\n' + util.convert_newline(util.exec_cmd(cmd, encoding='shift_jis'), '\n')
    return ret

def main():
    ret = test()
    util.send_response('text', ret)

main()
