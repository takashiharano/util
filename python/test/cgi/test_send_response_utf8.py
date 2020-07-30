#!python

import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_send_response():
  s = 'abcあいう你好'
  util.send_response('text', s, encoding='utf-8')

def main():
  test_send_response()

main()
