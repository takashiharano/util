#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    ret = 'data=' + util.get_post_data()
    return ret

def main():
    ret = test()
    print('Content-Type: text/plain')
    print()
    print(ret)

main()
