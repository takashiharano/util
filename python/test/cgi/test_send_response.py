#!python

import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_send_response():
    s = 'abc'
    util.send_response('text', s)

def main():
    test_send_response()

main()
