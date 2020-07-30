#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_copy_dir():
  ret = util.copy_dir('d1', 'd3', False)
  return 'copy OK: ' + str(ret)

def main():
  s = test_copy_dir()
  util.send_response('text', s)

main()
