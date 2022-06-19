#!python

import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_send_response():
    str = '{"aaa": "bbb"}'
    #util.send_response('text', str)
    #util.send_response('html', str)
    #util.send_response('binary', str)
  
    obj = {
        'key1': 'abc',
        'key2': 123
    }
    util.send_response('json', obj)

def main():
    test_send_response()

main()
