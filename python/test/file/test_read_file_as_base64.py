#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test1():
  s = '\n'
  s += util.read_file_as_base64('./dir1/a.txt') + '\n'
  s += '\n'
  s += util.read_file_as_base64('C:/tmp/img.jpg') + '\n'
  return s

def main():
  s = test1()
  util.send_response('text', s)

main()
