#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test1():
  data = 'YWJj'
  util.write_file_from_base64('C:/tmp/test.txt', data)

def main():
  test1()
  util.send_response('text', 'OK')

main()
