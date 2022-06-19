#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    util.set_stdout_encoding('shift-jis')
    #util.set_stdout_encoding('utf-8')

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)

    print('Content-Type: text/plain')
    print()
    print('abcあいう')

main()
