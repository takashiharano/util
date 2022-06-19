#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_grep():
    ret = ''
    ret += util.to_json(util.grep('dir2', '2019')) + '\n\n'
    ret += util.to_json(util.grep('dir2', '\d')) + '\n\n'
    ret += util.to_json(util.grep('dir2/file1.txt', 'b')) + '\n\n'
    ret += util.to_json(util.grep('dir2/file1.txt', 'a|b')) + '\n\n'
    ret += util.to_json(util.grep('dir2/file1.txt', 'z')) + '\n\n'
    #ret += util.to_json(util.grep('C:/Program Files', '\d')) + '\n\n'
    ret += util.grep('dir2', '\d', output='text') + '\n\n'

    ret += '\\.log$\n'
    ret += util.grep('dir2', '\d', filename=r'\.log$', output='text') + '\n\n'

    return ret

def main():
    s = test_grep()
    util.send_response('text', s)

main()
