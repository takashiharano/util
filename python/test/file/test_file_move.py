#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_move():
  ret = util.move('d1/a.txt', 'd2', force=True)
  return 'move OK: ' + str(ret)

def main():
  s = test_move()
  util.send_response('text', s)

main()
