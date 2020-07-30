#!python

import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_send_response():
  #s = 'abcあいう你好' #UnicodeEncodeError: 'shift_jis' codec can't encode character '\\u4f60' in position 6: illegal multibyte sequence
  s = 'abcあいう'
  util.send_response('text', s, encoding='shift_jis')

def main():
  test_send_response()

main()
