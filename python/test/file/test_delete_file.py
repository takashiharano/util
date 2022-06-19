#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_delete_file():
    util.copy_file('a.txt', 'a.txt.bak')
    util.delete_file('a.txt')
    return 'delete OK'

def main():
    s = test_delete_file()
    util.send_response('text', s)

main()
