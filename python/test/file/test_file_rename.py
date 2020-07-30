#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_rename():
  ret = util.rename('d1/a.txt', 'd2/b.txt')
  return 'rename OK: ' + str(ret)

def main():
  s = test_rename()
  util.send_response('text', s)

main()
